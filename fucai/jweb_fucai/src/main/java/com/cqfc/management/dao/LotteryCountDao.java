package com.cqfc.management.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.management.dao.mapper.LotteryCountMapper;
import com.cqfc.management.model.LotteryCount;
import com.cqfc.management.model.StationInfo;

@Repository
public class LotteryCountDao {

	@Autowired
	private LotteryCountMapper lotteryCountMapper;

	/**
	 * 插入记录
	 * 
	 * @param LotteryCount
	 * @return
	 */
	public int insertLotteryCount(LotteryCount LotteryCount) {

		filterNullColumn(LotteryCount);
		return lotteryCountMapper.insertLotteryCount(LotteryCount);
	}

	public List<LotteryCount> getLotteryCountByWhereAnd(
			LotteryCount lotteryCount) {

		List<LotteryCount> lotteryCounts = new ArrayList<LotteryCount>();
		filterNullColumn(lotteryCount);

		StringBuffer where = new StringBuffer(" 1=1");

		if (!"".equals(lotteryCount.getLotteryType())) {
			where.append(" and lotteryType = '");
			where.append(lotteryCount.getLotteryType());
			where.append("'");
		}
		if (!"".equals(lotteryCount.getLotteryDailyNum())
				&& lotteryCount.getLotteryDailyNum() != 0) {
			where.append(" and lotteryDailyNum = '");
			where.append(lotteryCount.getLotteryDailyNum());
			where.append("'");
		}
		if (!"".equals(lotteryCount.getCountTime())
				&& !"0000-00-00".equals(lotteryCount.getCountTime())
				&& lotteryCount.getCountTime() != null) {
			where.append(" and countTime = '");
			where.append(lotteryCount.getCountTime());
			where.append("'");
		}
		if (!"".equals(lotteryCount.getYear()) && lotteryCount.getYear() != 0) {
			where.append(" and year = '");
			where.append(lotteryCount.getYear());
			where.append("'");
		}
		if (!"".equals(lotteryCount.getMonth()) && lotteryCount.getMonth() != 0) {
			where.append(" and month = '");
			where.append(lotteryCount.getMonth());
			where.append("'");
		}
		if (!"".equals(lotteryCount.getStationId())
				&& lotteryCount.getStationId() != 0) {
			where.append(" and stationId = '");
			where.append(lotteryCount.getStationId());
			where.append("'");
		}

		System.out.println(where.toString());
		lotteryCounts = lotteryCountMapper.getLotteryCountByWhereAnd(where
				.toString());
		// return where.toString();
		return lotteryCounts;
	}

	/**
	 * 过滤掉null值的字段设置默认值
	 * 
	 * @param LotteryCount
	 */
	public void filterNullColumn(LotteryCount LotteryCount) {

		if (LotteryCount.getLotteryType() == null) {

			LotteryCount.setLotteryType("");
		}

	}

	public List<LotteryCount> getLotteryCountByDate(
			StationInfo stationInfo, String fromDate ,String toDate) {

		return lotteryCountMapper.getLotteryCountByTime(stationInfo, fromDate,toDate);
	}
	
	
	public List<LotteryCount> getLotteryCountByDate(
			StationInfo stationInfo, String fromDate ,String toDate ,int pageNum , int pageSize) {

		return lotteryCountMapper.getLotteryCountByDate(stationInfo, fromDate,toDate,pageNum*(pageSize-1),pageSize);
	}

	public int getRecordTotal(StationInfo stationInfo, String fromDate ,String toDate){
		
		return lotteryCountMapper.getRecordTotal(stationInfo, fromDate, toDate);
	}
	
}
