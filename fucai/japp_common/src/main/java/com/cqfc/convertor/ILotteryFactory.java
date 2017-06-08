package com.cqfc.convertor;

public interface ILotteryFactory {
	IConvertor getConvertor(String lotteryId);
}
