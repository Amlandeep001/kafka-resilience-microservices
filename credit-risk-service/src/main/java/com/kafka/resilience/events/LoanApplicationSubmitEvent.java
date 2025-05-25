package com.kafka.resilience.events;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class LoanApplicationSubmitEvent
{
	long loanId;
	int userId;
	double amount;
	String transactionId;
}
