package com.cqfc.order.util;

import com.jami.common.DataSourceContextHolder;

/**
 * @author liwh
 */
public class TaskDbGenerator {

	/**
	 * 获取数据库名
	 * 
	 * @param index
	 * @return
	 */
	public static String getDbName(int index) {
		String dbName = OrderDynamicUtil.PRE_DBNAME + "0" + index;
		return dbName;
	}

	/**
	 * 获取表名
	 * 
	 * @param index
	 * @return
	 */
	public static String getTableName(int index) {
		String indexStr = String.valueOf(index);
		if (indexStr.length() < 2) {
			indexStr = "0" + indexStr;
		}
		return OrderDynamicUtil.PRE_TABLENAME + indexStr;
	}

	/**
	 * 设置数据源
	 * 
	 * @param dbName
	 */
	public static void setDynamicDataSource(String masterOrSlave, String dbName) {
		dbName = masterOrSlave + "_" + dbName;
		DataSourceContextHolder.setDataSourceType(dbName);
	}

	/**
	 * 获取竞技彩主表名
	 * 
	 * @param index
	 * @return
	 */
	public static String getSportMainTableName(int index) {
		String indexStr = String.valueOf(index);
		if (indexStr.length() < 2) {
			indexStr = "0" + indexStr;
		}
		return OrderDynamicUtil.PRE_COMPETITION_TABLENAME_MAIN + indexStr;
	}
	
	/**
	 * 获取竞技彩主表名
	 * 
	 * @param index
	 * @return
	 */
	public static String getSportDetailTableName(int index) {
		String indexStr = String.valueOf(index);
		if (indexStr.length() < 2) {
			indexStr = "0" + indexStr;
		}
		return OrderDynamicUtil.PRE_COMPETITION_TABLENAME_DETAIL + indexStr;
	}

	/**
	 * 竞技彩dbName
	 * 
	 * @param index
	 * @return
	 */
	public static String getSportDbName(int index) {
		String indexStr = String.valueOf(index);
		if (indexStr.length() < 2) {
			indexStr = "0" + indexStr;
		}
		return OrderDynamicUtil.PRE_COMPETITION_DBNAME + indexStr;
	}

}
