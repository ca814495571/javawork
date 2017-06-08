package com.cqfc.management.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.ResultObj;
import com.cqfc.management.service.IRiskControlService;
import com.cqfc.protocol.riskcontrol.StatisticDataByGame;
import com.cqfc.protocol.riskcontrol.StatisticDayPageData;
import com.cqfc.protocol.riskcontrol.StatisticPageData;

@Controller
@RequestMapping("/risk")
public class RiskController {

	private static final int DEFAUT_PAGE_SIZE = 10;
	private static final int MAX_PAGE_SIZE = 100;
	@Autowired
	IRiskControlService riskControlService;

	@RequestMapping("/getDataByIssue")
	@ResponseBody
	public PcResultObj getStatisticData(String lotteryId, String issue) {
		PcResultObj result = new PcResultObj();
		try {
			StatisticDataByGame dataByGame = riskControlService
					.getStatisticByGame(lotteryId, issue);
			if (dataByGame == null) {
				result.setMsgCode(PcResultObj.FAIL_CODE);
				result.setMsg("查询失败");
			} else {
				result.setEntity(dataByGame);
				result.setMsgCode(PcResultObj.SUCCESS_CODE);
				result.setMsg("查询成功");
			}
		} catch (Exception e) {
			result.setMsgCode(PcResultObj.FAIL_CODE);
			result.setMsg("查询失败," + e.toString());
		}
		return result;
	}

	@RequestMapping("/getDatasByLottery")
	@ResponseBody
	public PcResultObj getStatisticData(String lotteryId, String issue,
			Integer currentPage, Integer pageSize) {

		PcResultObj result = new PcResultObj();
		try {
			if (currentPage == null || currentPage < 1) {
				currentPage = 1;
			}
			if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
				pageSize = DEFAUT_PAGE_SIZE;
			}
			if(StringUtils.isEmpty(issue)){
				StatisticPageData dataByLottery = riskControlService
						.queryStatisticByGame(lotteryId, currentPage, pageSize);
				if (dataByLottery == null) {
					result.setMsgCode(PcResultObj.FAIL_CODE);
					result.setMsg("查询失败");
				} else {
					ResultObj obj = new ResultObj();
					obj.setRecordTotal(dataByLottery.getTotalSize());
					obj.setObjects(dataByLottery.getResultList());
					result.setEntity(obj);
					result.setMsgCode(PcResultObj.SUCCESS_CODE);
					result.setMsg("查询成功");
				}
			}else{

				StatisticDataByGame dataByGame = riskControlService
						.getStatisticByGame(lotteryId, issue);
				List<StatisticDataByGame> dataList = new ArrayList<StatisticDataByGame>();
				ResultObj obj = new ResultObj();
				if (dataByGame == null) {
					obj.setRecordTotal(0);
					obj.setObjects(dataList);
				} else {
					obj.setRecordTotal(1);
					dataList.add(dataByGame);
					obj.setObjects(dataList);
				}
				result.setEntity(obj);
				result.setMsgCode(PcResultObj.SUCCESS_CODE);
				result.setMsg("查询成功");
			}
		} catch (Exception e) {
			result.setMsgCode(PcResultObj.FAIL_CODE);
			result.setMsg("查询失败," + e.toString());
		}
		return result;
	}

	@RequestMapping("/getDataByDay")
	@ResponseBody
	public PcResultObj getStatisticData(String day, Integer currentPage, Integer pageSize) {
		PcResultObj result = new PcResultObj();
		try {
			if (currentPage == null || currentPage < 1) {
				currentPage = 1;
			}
			if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
				pageSize = DEFAUT_PAGE_SIZE;
			}
			StatisticDayPageData dataByDay = riskControlService
					.getStatisticByDay(day, currentPage, pageSize);
			if (dataByDay == null) {
				result.setMsgCode(PcResultObj.FAIL_CODE);
				result.setMsg("查询失败");
			} else {
				ResultObj obj = new ResultObj();
				obj.setRecordTotal(dataByDay.getTotalSize());
				obj.setObjects(dataByDay.getResultList());
				result.setEntity(obj);
				result.setMsgCode(PcResultObj.SUCCESS_CODE);
				result.setMsg("查询成功");
			}
		} catch (Exception e) {
			result.setMsgCode(PcResultObj.FAIL_CODE);
			result.setMsg("查询失败," + e.toString());
		}
		return result;
	}
}
