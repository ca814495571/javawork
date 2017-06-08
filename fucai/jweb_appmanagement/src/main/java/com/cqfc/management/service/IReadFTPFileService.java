package com.cqfc.management.service;

import java.io.File;
import java.util.Date;

import com.cqfc.management.util.propertiesUtils.ReadProperties;


public interface IReadFTPFileService {

	
	public static final String[] LOTTERY_TYPE= {"双色球","福彩3D","七乐彩","幸运农场","时时彩"};
	
	
	/**
	 * 读取ftp下载文件中的用户信息存到数据库
	 * @param filePath
	 */
	public void insertUserInfo(File file);
	
	
	/**
	 * 读取ftp下载文件中的订单方案信息存到数据库
	 * @param filePath
	 */
	public void insertPlanInfo(File file);
	
	
	
	/**
	 * 对用户数据数据进行分析，将结果保存到appmanagement用户统计表中
	 */
	public void analyzeUserInfoTest(Date date); 
	
	/**
	 * 对用户数据数据进行分析，将结果保存到appmanagement用户统计表中
	 */
	public void analyzeUserInfo(Date date); 
	
	
	/**
	 * 对彩票方案数据进行分析,将结果保存到appmanagement彩票销售量统计表中
	 * @param date
	 */
	public void analyzePlanInfoTest(Date date);
	
	/**
	 * 对彩票方案数据进行分析,将结果保存到appmanagement彩票销售量统计表中
	 * @param date
	 */
	public void analyzePlanInfo(Date date);
	
	
	/**
	 * FTP数据转换
	 */
	public void ftpDateTransfer();
	
	
	
	
}
