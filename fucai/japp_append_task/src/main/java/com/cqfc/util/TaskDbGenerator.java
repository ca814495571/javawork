package com.cqfc.util;

import com.jami.common.DataSourceContextHolder;

/**
 * @author liwh
 */
public class TaskDbGenerator {

	private static final String preDbName = "fucai_append_";

	private static final String preTableName = "t_lottery_append_task_detail_";

	/**
	 * 获取数据库名
	 * 
	 * @param index
	 * @return
	 */
	public static String getDbName(int index) {
		String dbName = preDbName + "0" + index;
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
		return preTableName + indexStr;
	}

	/**
	 * 设置数据源
	 * 
	 * @param dbName
	 */
	public static void setDynamicDataSource(String dbName) {
		dbName = DbGenerator.SLAVE + "_" + dbName;
		DataSourceContextHolder.setDataSourceType(dbName);
	}
}
