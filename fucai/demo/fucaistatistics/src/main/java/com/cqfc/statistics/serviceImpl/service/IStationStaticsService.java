package com.cqfc.statistics.serviceImpl.service;

import java.util.Date;

public interface IStationStaticsService {
	
	/**
	 * 投注站销量统计(包括人数总增量和总销量)
	 */
	public void stationStatics(Date dateFrom ,Date dateTo);
	/**
	 * 投注站分彩种销量统计(包括人数总增量和总销量)
	 */
	public void stationLotteryStatics(Date dateFrom ,Date dateTo);
	
	/**
	 * 投注站注册用户信息
	 * @param date
	 * @param station
	 */
	public void stationUserStatics(Date dateFrom, Date dateTo);
	
	/**
	 * 投注站用户购彩交易信息
	 * @param date
	 * @param station
	 */
	public void stationDealStatics(Date dateFrom ,Date dateTo);
	
	
	/**
	 * 区县销量统计(包括人数总增量和总销量)
	 */
	public void countryStatics(Date dateFrom ,Date dateTo);
	
	/**
	 * 区县分彩种销量统计(包括人数总增量和总销量)
	 */
	public void countryLotteryStatics(Date dateFrom ,Date dateTo);
	
	
	/**
	 * 分中心销量统计(包括人数总增量和总销量)
	 */
	public void branchStatics(Date dateFrom ,Date dateTo);
	
	/**
	 * 分中心分彩种销量统计(包括人数总增量和总销量)
	 */
	public void branchLotteryStatics(Date dateFrom ,Date dateTo);
	
	
	/**
	 * 中心销量统计(包括人数总增量和总销量)
	 */
	public void centerStatics(Date dateFrom ,Date dateTo);
	
	/**
	 * 中心分彩种销量统计(包括人数总增量和总销量)
	 */
	public void centerLotteryStatics(Date dateFrom ,Date dateTo);
	
}
