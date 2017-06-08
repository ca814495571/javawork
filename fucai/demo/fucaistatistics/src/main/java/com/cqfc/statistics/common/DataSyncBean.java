package com.cqfc.statistics.common;

import java.util.Map;


public class DataSyncBean {


	/**
	 * 源数据执行的sql
	 */
	private String selectSql;
	
	/**
	 * 插入目标数据库的sql
	 */
	private String insertSql;
	
	/**
	 * selectSql 参数配置列表
	 */
	private Map<String,Object> selectSqlParam;
	
	/**
	 * insertSql 参数配置列表
	 */
	private Map<String,Object> insertSqlParam;
	
	/**
	 * 数据来源数据库地址和端口 ip:port
	 */
	private String sourceUrl;
	/**
	 * 数据来源数据库名
	 */
	private String sourceDataBase;
	
	/**
	 * 目标数据库地址和端口 ip:port
	 */
	private String targetUrl;
	
	/**
	 * 目标数据库名
	 */
	private String targetDataBase;
	
	/**
	 * 活动类型
	 */
	private int activity_type;
	
	/**
	 * 异常处理决策 
	 */
	private int exceptionHandler;
	
	/**
	 * 异常处理sql:updateSql 不做处理
	 */
	private String exceptionSql;
	

	public String getSelectSql() {
		return selectSql;
	}

	public void setSelectSql(String selectSql) {
		this.selectSql = selectSql;
	}

	public String getInsertSql() {
		return insertSql;
	}

	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getSourceDataBase() {
		return sourceDataBase;
	}

	public void setSourceDataBase(String sourceDataBase) {
		this.sourceDataBase = sourceDataBase;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getTargetDataBase() {
		return targetDataBase;
	}

	public void setTargetDataBase(String targetDataBase) {
		this.targetDataBase = targetDataBase;
	}

	public int getActivity_type() {
		return activity_type;
	}

	public void setActivity_type(int activity_type) {
		this.activity_type = activity_type;
	}

	public int getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(int exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public String getExceptionSql() {
		return exceptionSql;
	}

	public void setExceptionSql(String exceptionSql) {
		this.exceptionSql = exceptionSql;
	}


	public Map<String, Object> getSelectSqlParam() {
		return selectSqlParam;
	}

	public void setSelectSqlParam(Map<String, Object> selectSqlParam) {
		this.selectSqlParam = selectSqlParam;
	}

	public Map<String, Object> getInsertSqlParam() {
		return insertSqlParam;
	}

	public void setInsertSqlParam(Map<String, Object> insertSqlParam) {
		this.insertSqlParam = insertSqlParam;
	}

	
}
