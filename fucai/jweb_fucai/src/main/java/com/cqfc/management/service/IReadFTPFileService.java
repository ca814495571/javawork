package com.cqfc.management.service;

import java.util.Date;


public interface IReadFTPFileService {

	
	public static final String[] LOTTERY_TYPE= {"双色球","福彩3D","七乐彩","幸运农场","时时彩"};
	
	
	
	/**
	 * 读取用户信息存到数据库
	 * @param filePath
	 */
	public void insertUserInfo(String filePath);
	
	
	/**
	 * 读取订单方案信息存到数据库
	 * @param filePath
	 */
	public void insertPlanInfo(String filePath);
	
	
	
	/**
	 * 对用户数据数据进行分析，将结果保存到用户统计表中
	 */
	public void analyzeUserInfo(Date date); 
	
	
	
	
	/**
	 * 对彩票方案数据进行分析,将结果保存到彩票销售量统计表中
	 * @param date
	 */
	public void analyzePlanInfo(Date date);
	
}
