package com.kafka.resilience.events;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CreditDecisionEvent
{
	Long loanId;
	int userId;
	boolean approved;
	String message;
}