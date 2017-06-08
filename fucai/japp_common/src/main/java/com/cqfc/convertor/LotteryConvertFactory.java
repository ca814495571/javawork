package com.cqfc.convertor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cqfc.convertor.cq.CQLotteryFactory;
import com.cqfc.convertor.mock.MockLotteryFactory;

public class LotteryConvertFactory {
	public static final String DEFAULT_KEY = "cq";
	private static final Map<String, ILotteryFactory> factoryMap = new ConcurrentHashMap<String, ILotteryFactory>();
	static{
		factoryMap.put("cq", new CQLotteryFactory());
		factoryMap.put("test", new MockLotteryFactory());
	}
	public static ILotteryFactory getLotteryFactory(String key){
		if (factoryMap.containsKey(key)){
			return factoryMap.get(key);
		}
		return factoryMap.get(DEFAULT_KEY);
	}
}
