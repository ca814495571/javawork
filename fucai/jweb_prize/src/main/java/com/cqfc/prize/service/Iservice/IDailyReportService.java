package com.cqfc.prize.service.Iservice;

import java.util.List;

import com.cqfc.prize.model.DailyReport;

public interface IDailyReportService {

	/**
	 * 兑奖日报信息入库
	 * @param dailyReport
	 * @return
	 */
	public abstract boolean insertDailyReport(DailyReport dailyReport);
	
	/**
	 * 获取某天的日报
	 * @param currentDay
	 * @return
	 */
	public  List<DailyReport> getDailyReport(String currentDay);
	

}
