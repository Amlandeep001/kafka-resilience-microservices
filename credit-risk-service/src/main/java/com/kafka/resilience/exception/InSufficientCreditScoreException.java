package com.kafka.resilience.exception;

public class InSufficientCreditScoreException extends RuntimeException
{
	private static final long serialVersionUID = 2237007170306995813L;

	public InSufficientCreditScoreException(String message)
	{
		super(message);
	}
}
