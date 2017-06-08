package com.cqfc.prize.service.Iservice;


public interface ITempPrizeService {

	/**
	 * 日报定时统计
	 * @param currentDay
	 * @return
	 */
	public abstract boolean dailyReportCount(String currentDay);
	
	
	/**
	 * 临时表信息定时清理
	 * @param time
	 * @return
	 */
	public int cleanTemp(String time);
	
	
}
