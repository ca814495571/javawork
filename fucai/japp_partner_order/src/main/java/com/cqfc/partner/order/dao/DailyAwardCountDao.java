package com.cqfc.partner.order.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.partner.order.dao.mapper.DailyAwardCountMapper;
import com.cqfc.protocol.partnerorder.DailyAwardCount;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

@Repository
public class DailyAwardCountDao {

	@Autowired
	private DailyAwardCountMapper dailyAwardCountMapper;

	public int insert(DailyAwardCount dailyAwardCount) throws Exception {

		int flag = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;

		try {
			flag = dailyAwardCountMapper.insert(dailyAwardCount);
		} catch (Exception e) {
			Log.run.error(e);
			Log.error("添加日中奖统计信息数据库异常",e);
			throw e;
		}
		return flag;

	}

	public List<DailyAwardCount> getDailyAwardByCountTime(String countTime,
			String tableName) {

		List<DailyAwardCount> dailyAwardCounts = new ArrayList<DailyAwardCount>();
		try {
			StringBuffer sb = new StringBuffer();

			sb.append(" createTime between '");
			sb.append(countTime);
			sb.append(" 00:00:00 '");
			sb.append(" and '");
			sb.append(countTime);
			sb.append(" 23:59:59'");
			sb.append(" group by partnerId,lotteryId");
			dailyAwardCounts = dailyAwardCountMapper.getDailyAwardByCountTime(
					sb.toString(), tableName);
		} catch (Exception e) {
			Log.run.error("日兑奖统计出现异常", e);
			Log.error("日兑奖统计数据库出现异常", e);
		}
		return dailyAwardCounts;
	}

	public long getTotalAwardPrize(String day) {
		
		Long money = 0L;
		try {
			
			money = dailyAwardCountMapper.getTotalAwardPrize(day);
		} catch (Exception e) {
			Log.run.error("获取中奖总金额数据库出现异常", e);
			Log.error("获取中奖总金额数据库出现异常", e);
		}
		return money;
	}
}
