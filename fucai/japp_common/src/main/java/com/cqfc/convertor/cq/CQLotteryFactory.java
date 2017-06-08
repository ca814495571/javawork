package com.cqfc.convertor.cq;

import com.cqfc.convertor.IConvertor;
import com.cqfc.convertor.ILotteryFactory;

public class CQLotteryFactory implements ILotteryFactory{
	private IConvertor convertor = new CQConvertor();
	@Override
	public IConvertor getConvertor(String lotteryId) {
		return convertor;
	}

}
