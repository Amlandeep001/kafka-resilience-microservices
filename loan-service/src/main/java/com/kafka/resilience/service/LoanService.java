package com.kafka.resilience.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.kafka.resilience.constants.LoanStatus;
import com.kafka.resilience.entity.LoanDO;
import com.kafka.resilience.events.LoanApplicationSubmitEvent;
import com.kafka.resilience.repository.LoanRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoanService
{
	private final LoanRepository loanRepository;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	private final String loanProcessingTopic;

	public LoanService(LoanRepository loanRepository, KafkaTemplate<String, Object> kafkaTemplate, @Value("${loan.processing.topic-name}") String loanProcessingTopic)
	{
		this.loanRepository = loanRepository;
		this.kafkaTemplate = kafkaTemplate;
		this.loanProcessingTopic = loanProcessingTopic;
	}

	public String processLoanApplication(LoanDO loanDO)
	{
		// Generate transaction ID
		String loanTransactionId = UUID.randomUUID().toString().split("-")[0];
		loanDO.setLoanTransactionId(loanTransactionId);
		loanDO.setStatus(LoanStatus.PENDING);

		log.info("Starting loan application process | transactionId: {}", loanTransactionId);

		// Save loan with initial status PENDING
		LoanDO savedLoan = loanRepository.save(loanDO);

		// Prepare kafka event for downstream services
		LoanApplicationSubmitEvent event = LoanApplicationSubmitEvent.builder()
				.loanId(savedLoan.getLoanId())
				.userId(savedLoan.getUserId())
				.amount(savedLoan.getAmount())
				.transactionId(savedLoan.getLoanTransactionId())
				.build();

		// Publish to Kafka
		kafkaTemplate.send(loanProcessingTopic, event);
		log.info("Loan application event published to Kafka | topic: {}, loanId: {}", loanProcessingTopic, savedLoan.getLoanId());

		return loanTransactionId;
	}

	public Optional<LoanDO> getLoanStatusById(Long loanId)
	{
		return loanRepository.findById(loanId);
	}

}
