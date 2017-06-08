package com.cqfc.management.service.impl;

import org.springframework.stereotype.Service;

import com.cqfc.management.service.IRiskControlService;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.riskcontrol.StatisticDataByGame;
import com.cqfc.protocol.riskcontrol.StatisticDayPageData;
import com.cqfc.protocol.riskcontrol.StatisticPageData;
import com.cqfc.util.ConstantsUtil;
import com.jami.util.Log;

@Service
public class RiskControlServiceImpl implements IRiskControlService {

	@Override
	public StatisticDataByGame getStatisticByGame(String gameId, String issue) {
		ReturnMessage message = TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_RISK_CONTROL, "getStatisticByGame",
				gameId, issue);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			StatisticDataByGame result = (StatisticDataByGame) message.getObj();
			return result;
		} else {
			Log.run.error("get statistic data from risk control failed.");
		}
		return null;
	}

	@Override
	public StatisticPageData queryStatisticByGame(String gameId,
			int currentPage, int pageSize) {
		ReturnMessage message = TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_RISK_CONTROL, "queryStatisticByGame",
				gameId, currentPage, pageSize);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			StatisticPageData result = (StatisticPageData) message.getObj();
			return result;
		} else {
			Log.run.error("get statistic data  page from risk control failed.");
		}
		return null;
	}

	@Override
	public StatisticDayPageData getStatisticByDay(String day, int currentPage, int pageSize){
		ReturnMessage message = TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_RISK_CONTROL, "queryStatisticByDay",
				day, currentPage, pageSize);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			StatisticDayPageData result = (StatisticDayPageData) message.getObj();
			return result;
		} else {
			Log.run.error("get statistic data by day from risk control failed.");
		}
		return null;
	}

}
