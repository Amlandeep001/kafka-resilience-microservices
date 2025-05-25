package com.kafka.resilience.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig
{
	private final String creditRiskTopic;

	public KafkaConfig(@Value("${credit.risk.topic-name}") String creditRiskTopic)
	{
		this.creditRiskTopic = creditRiskTopic;
	}

	@Bean
	NewTopic createTopic()
	{
		return new NewTopic(creditRiskTopic, 3, (short) 1);
	}
}