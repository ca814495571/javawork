package com.cqfc.management.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.management.dao.LotteryCountDao;
import com.cqfc.management.model.LotteryCount;
import com.cqfc.management.model.StationInfo;
import com.cqfc.management.service.ILotteryCountService;
import com.cqfc.management.util.dateUtils.DateUtils;

@Service
public class LotteryCountServiceImpl implements ILotteryCountService {

	@Autowired
	private LotteryCountDao lotteryCountDao;

	@Override
	public int insertLotteryCount(LotteryCount lotteryCount) {

		// control中的业务逻辑是否可以些在这里？
		return lotteryCountDao.insertLotteryCount(lotteryCount);
	}

	@Override
	public List<LotteryCount> getLotteryCountByWhereAnd(
			LotteryCount lotteryCount) {
		return lotteryCountDao.getLotteryCountByWhereAnd(lotteryCount);
	}

	@Override
	public List<LotteryCount> getPreDayLotteryCount(StationInfo stationInfo,
			Date date, String lotteryType) {

		LotteryCount lc = new LotteryCount();
		lc.setStationId(stationInfo.getId());
		lc.setCountTime(DateUtils.getPreDay(date));
		lc.setLotteryType(lotteryType);
		return getLotteryCountByWhereAnd(lc);
	}

	// public List<CenterLotteryCountByType> getCenterLotteryCounts(StationInfo
	public int getTotalSaleNum(StationInfo stationInfo,String lotteryType,String fromDate ,String toDate){
		
		return lotteryCountDao.getTotalSaleNum(stationInfo, lotteryType, fromDate, toDate);
	}

	@Override
	public int deleteAll(String date) {
		return lotteryCountDao.deleteAll(date);
	}
}
