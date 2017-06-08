package com.cqfc.partner.order.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.partner.order.dao.mapper.DailyChargeCountMapper;
import com.cqfc.protocol.partnerorder.DailyChargeCount;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

@Repository
public class DailyChargeCountDao {

	@Autowired
	DailyChargeCountMapper dailyChargeCountMapper;

	public int insert(DailyChargeCount dailyChargeCount) throws Exception {

		int flag = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;

		try {
			flag = dailyChargeCountMapper.insert(dailyChargeCount);
		} catch (Exception e) {
			Log.run.error("添加日充值记录数据库异常",e);
			Log.error("添加日充值记录数据库异常", e);
			throw e;
		}
		return flag;
	}

	public long getTotalRecharge(String day) {
		
		Long money =0L;
		try {
			
			money = dailyChargeCountMapper.getTotalRecharge(day);
			
		} catch (Exception e) {
			Log.run.error("获取充值总金额数据库异常",e);
			Log.error("获取充值总金额数据库异常", e);
		}
		if (money == null) {
			return 0;
		}
		return money;
	}

}
