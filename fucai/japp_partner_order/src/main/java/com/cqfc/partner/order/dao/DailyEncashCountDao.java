package com.cqfc.partner.order.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.partner.order.dao.mapper.DailyEncashCountMapper;
import com.cqfc.protocol.partnerorder.DailyEncashCount;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

@Repository
public class DailyEncashCountDao {

	@Autowired
	DailyEncashCountMapper dailyEncashCountMapper;

	public int insert(DailyEncashCount dailyEncashCount) throws Exception {

		int flag = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;

		try {
			flag = dailyEncashCountMapper.insert(dailyEncashCount);
		} catch (Exception e) {
			Log.run.error("添加日提现统计记录数据库异常",e);
			Log.error("添加日提现统计记录数据库异常", e);
			throw e;
		}
		return flag;
	}

	public long getTotalWithdraw(String day) {
		
		Long money = 0L;
		try {
			
			money = dailyEncashCountMapper.getTotalWithdraw(day);
		} catch (Exception e) {
			Log.run.error("获取日提现总金额数据库异常",e);
			Log.error("获取日提现总金额数据库异常", e);
		}
		if (money == null) {
			return 0;
		}
		return money;
	}

}
