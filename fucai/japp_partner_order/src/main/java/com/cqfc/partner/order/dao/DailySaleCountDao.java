package com.cqfc.partner.order.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.partner.order.dao.mapper.DailySaleCountMapper;
import com.cqfc.protocol.partnerorder.DailySaleCount;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

@Repository
public class DailySaleCountDao {

	@Autowired
	private DailySaleCountMapper dailySaleCountMapper;

	public int insert(DailySaleCount dailySaleCount) throws Exception {

		int flag = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;

		try {
			flag = dailySaleCountMapper.insert(dailySaleCount);
		} catch (Exception e) {
			Log.run.error("添加日销量统计记录数据库异常",e);
			Log.error("添加日销量统计记录数据库异常", e);
			throw e;
		}
		return flag;

	}

	public List<DailySaleCount> getDailySaleByCountTime(String countTime,
			String tableName) {

		List<DailySaleCount> dailySaleCounts = new ArrayList<DailySaleCount>();
		try {

			StringBuffer sb = new StringBuffer();

			sb.append(" createTime between '");
			sb.append(countTime);
			sb.append(" 00:00:00' and '");
			sb.append(countTime);
			sb.append(" 23:59:59 '");
			sb.append(" group by partnerId,lotteryId");
			dailySaleCounts = dailySaleCountMapper.getDailySaleByCountTime(
					sb.toString(), tableName);
		} catch (Exception e) {
			Log.run.error("获取日销量数据库异常", e);
			Log.error("获取日销量数据库异常", e);
		}
		return dailySaleCounts;
	}

	public long getTotalSale(String day) {
		
		
		Long money = 0L;
		try {
			money = dailySaleCountMapper.getTotalSale(day);
		} catch (Exception e) {
			Log.run.error("获取日销量总金额数据库异常", e);
			Log.error("获取日销量总金额数据库异常", e);
		}
		 
		if (money == null) {
			return 0;
		}
		return money;
	}
}
