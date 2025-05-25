package com.kafka.resilience.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.kafka.resilience.constants.LoanStatus;
import com.kafka.resilience.entity.LoanDO;
import com.kafka.resilience.events.CreditDecisionEvent;
import com.kafka.resilience.repository.LoanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditDecisionConsumer
{
	private final LoanRepository repository;

	// consume the CreditDecision event and update the database

	@KafkaListener(topics = "credit-decision-topic", groupId = "credit-loan-group")
	public void consumeCreditDecisionEvent(CreditDecisionEvent event)
	{
		log.info("CreditDecisionConsumer::consumeCreditDecisionEvent received credit decision event {}", event);

		LoanDO loan = repository.findById(event.getLoanId()).orElse(null);

		if(loan != null)
		{
			if(event.isApproved())
			{
				loan.setStatus(LoanStatus.APPROVED);
				log.info("CreditDecisionConsumer - LoanId: {} marked as APPROVED. Proceeding with disbursement logic.", loan.getLoanId());
			}
			else
			{
				loan.setStatus(LoanStatus.REJECTED);
				log.info("CreditDecisionConsumer - LoanId: {} marked as REJECTED. No further action required ", loan.getLoanId());
			}
			repository.save(loan);
		}
	}
}
