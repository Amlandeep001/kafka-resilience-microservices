package com.kafka.resilience.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig
{
	private final String topicName;

	public KafkaConfig(@Value("${loan.processing.topic-name}") String topicName)
	{
		this.topicName = topicName;
	}

	@Bean
	NewTopic createTopic()
	{
		return new NewTopic(topicName, 3, (short) 1);
	}

}