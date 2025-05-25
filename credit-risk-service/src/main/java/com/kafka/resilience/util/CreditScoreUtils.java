package com.kafka.resilience.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CreditScoreUtils
{
	public Map<Integer, Integer> creditScoreResults()
	{
		Map<Integer, Integer> creditScoreMap = new HashMap<>();
		creditScoreMap.put(101, 760);
		creditScoreMap.put(202, 340);
		creditScoreMap.put(3287, 800);
		creditScoreMap.put(2362, 550);
		return creditScoreMap;
	}
}
