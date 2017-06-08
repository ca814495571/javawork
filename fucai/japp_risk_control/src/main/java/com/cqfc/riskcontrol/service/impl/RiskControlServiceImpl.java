package com.cqfc.riskcontrol.service.impl;

import javax.annotation.Resource;

import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import com.cqfc.protocol.riskcontrol.RiskControlService;
import com.cqfc.protocol.riskcontrol.StatisticDataByDay;
import com.cqfc.protocol.riskcontrol.StatisticDataByGame;
import com.cqfc.protocol.riskcontrol.StatisticDayPageData;
import com.cqfc.protocol.riskcontrol.StatisticPageData;
import com.cqfc.riskcontrol.dao.RiskControlDao;
import com.cqfc.riskcontrol.jms.DayTask;
import com.cqfc.util.ServiceStatusCodeUtil;

@Service
public class RiskControlServiceImpl implements RiskControlService.Iface {
	@Resource
	private RiskControlDao riskControlDao;

	@Override
	public StatisticDataByGame getStatisticByGame(String gameId, String issue)
			throws TException {
		StatisticDataByGame result = riskControlDao.getStatisticByGame(gameId, issue);
		return result;
	}

	@Override
	public StatisticDataByDay getStatisticByDay(String day) throws TException {
		return riskControlDao.getStatisticDataByDay(day);
	}

	@Override
	public StatisticPageData queryStatisticByGame(String gameId,
			int currentPage, int pageSize) throws TException {
		return riskControlDao.queryStatisticByGame(gameId, currentPage, pageSize);
	}

	@Override
	public int restatisticByDay(String day) throws TException {
		DayTask.countData(riskControlDao, day);
		return ServiceStatusCodeUtil.STATUS_CODE_OPERATE_SUCCESS;
	}

	@Override
	public StatisticDayPageData queryStatisticByDay(String day, int currentPage,
			int pageSize) throws TException {
		return riskControlDao.queryStatisticByDay(day, currentPage, pageSize);
	}

}
