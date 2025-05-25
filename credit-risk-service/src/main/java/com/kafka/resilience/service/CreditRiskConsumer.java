package com.kafka.resilience.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.kafka.resilience.events.CreditDecisionEvent;
import com.kafka.resilience.events.LoanApplicationSubmitEvent;
import com.kafka.resilience.exception.InSufficientCreditScoreException;
import com.kafka.resilience.util.CreditScoreUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CreditRiskConsumer
{
	public final String creditRiskTopic;
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final CreditScoreUtils creditScoreUtils;

	public CreditRiskConsumer(@Value("${credit.risk.topic-name}") String creditRiskTopic, KafkaTemplate<String, Object> kafkaTemplate,
			CreditScoreUtils creditScoreUtils)
	{
		this.creditRiskTopic = creditRiskTopic;
		this.kafkaTemplate = kafkaTemplate;
		this.creditScoreUtils = creditScoreUtils;
	}

	@KafkaListener(topics = "loan-process-topic")
	public void onLoanApplicationReceived(LoanApplicationSubmitEvent event)
	{
		CreditDecisionEvent creditRiskCheckResult = null;
		log.info("Received loan application event: {}", event);

		try
		{
			evaluateCreditRisk(event.getUserId());
			log.info("Credit risk check PASSED for userId: {}", event.getUserId());

			creditRiskCheckResult = buildCreditDecisionEvent(event, true, "Credit risk check PASSED");
			kafkaTemplate.send(creditRiskTopic, creditRiskCheckResult);
			log.info("Credit risk service publish events {}", creditRiskCheckResult);
		}
		catch(InSufficientCreditScoreException ex)
		{
			// update
			log.warn("Credit risk check FAILED for userId: {} | Reason: {}", event.getUserId(), ex.getMessage());
			creditRiskCheckResult = buildCreditDecisionEvent(event, false, ex.getMessage());
			kafkaTemplate.send(creditRiskTopic, creditRiskCheckResult);
			log.warn("Credit risk service publish events {}", creditRiskCheckResult);
		}
	}

	private CreditDecisionEvent buildCreditDecisionEvent(LoanApplicationSubmitEvent event, boolean approved, String Credit_risk_check_PASSED)
	{
		// needs update
		return CreditDecisionEvent.builder()
				.loanId(event.getLoanId())
				.userId(event.getUserId())
				.approved(approved)
				.message(Credit_risk_check_PASSED)
				.build();
	}

	private void evaluateCreditRisk(int userId)
	{
		int creditScore = creditScoreUtils
				.creditScoreResults()
				.getOrDefault(userId, 0);
		if(creditScore < 750)
		{
			throw new InSufficientCreditScoreException("Credit score is too low: " + creditScore);
		}
	}
}
