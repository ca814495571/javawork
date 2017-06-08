package com.cqfc.management.service;

import java.util.Date;
import java.util.List;

import com.cqfc.management.model.LotteryCount;
import com.cqfc.management.model.StationInfo;

public interface ILotteryCountService {

	public int insertLotteryCount(LotteryCount lotteryCount);

	public List<LotteryCount> getLotteryCountByWhereAnd(LotteryCount lotteryCount);

	/**
	 * 获取前一天的数据
	 * @param lotteryCoun
	 * @param date
	 * @param type
	 * @return
	 */
	public List<LotteryCount> getPreDayLotteryCount(StationInfo stationInfo , Date date ,String type);
	
	
}
