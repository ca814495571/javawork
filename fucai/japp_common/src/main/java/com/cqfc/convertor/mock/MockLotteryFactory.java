package com.cqfc.convertor.mock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cqfc.convertor.IConvertor;
import com.cqfc.convertor.ILotteryFactory;
import com.cqfc.convertor.cq.CQConvertor;

public class MockLotteryFactory implements ILotteryFactory{
	private static final String DIGIT_LOTTERY = "default";
	private static final Map<String, IConvertor> convertorMap = new ConcurrentHashMap<String, IConvertor>();
	static{
		MockConvertor convertor = new MockConvertor();
		CQConvertor defaultConvertor = new CQConvertor();
		convertorMap.put("BDSF", defaultConvertor);
		convertorMap.put("BDXBC", defaultConvertor);
		convertorMap.put("BDSPF", defaultConvertor);
		convertorMap.put("BDSXDS", defaultConvertor);
		convertorMap.put("BDBQC", defaultConvertor);
		convertorMap.put("BDJQS", defaultConvertor);
		convertorMap.put("BDBF", defaultConvertor);
		convertorMap.put("JZSPF", convertor);
		convertorMap.put("JZBF", convertor);
		convertorMap.put("JZJQS", convertor);
		convertorMap.put("JZBQC", convertor);
		convertorMap.put("JZHHGG", convertor);
		convertorMap.put("JZRQSPF", convertor);
		convertorMap.put("GYJJC", convertor);
		convertorMap.put("JLSF", convertor);
		convertorMap.put("JLRFSF", convertor);
		convertorMap.put("JLSFC", convertor);
		convertorMap.put("JLDXF", convertor);
		convertorMap.put("JLHHGG", convertor);
		convertorMap.put(DIGIT_LOTTERY, defaultConvertor);
		
	}
	@Override
	public IConvertor getConvertor(String lotteryId) {
		if(convertorMap.containsKey(lotteryId)){
			return convertorMap.get(lotteryId);
		}
		return convertorMap.get(DIGIT_LOTTERY);
	}

}
