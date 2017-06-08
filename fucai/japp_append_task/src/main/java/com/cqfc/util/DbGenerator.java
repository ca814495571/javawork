package com.cqfc.util;

import com.jami.common.DataSourceContextHolder;
import com.jami.util.Log;

/**
 * @author liwh
 */
public class DbGenerator {

	public static final String MASTER = "master";

	public static final String SLAVE = "slave";

	private static final String preDbName = "fucai_append_";

	private static final String preTaskTableName = "t_lottery_append_task_";

	private static final String preDetailTableName = "t_lottery_append_task_detail_";

	private static final String preIndexTableName = "t_lottery_append_task_index_";

	/**
	 * 获取数据库名下标
	 * 
	 * @param preDbName
	 *            数据库前缀
	 * @param depots
	 *            分库字符串
	 * @return
	 */
	public static String getDbName(String depots) {
		String dbName = "";
		String hashValue = String.valueOf(Math.abs(depots.hashCode() / AppendTaskConstant.PER_DATASOURCE_TABLE_NUM
				% AppendTaskConstant.DATASOURCE_NUM));
		if (hashValue.length() < 2) {
			hashValue = "0" + hashValue;
		}
		dbName = preDbName + hashValue;
		return dbName;
	}

	/**
	 * 设置数据源
	 * 
	 * @param depots
	 *            分库字符串
	 */
	public static void setDynamicDataSource(String masterSlave, String depots) {
		String dbName = masterSlave + "_" + getDbName(depots);
		Log.run.debug("append set dataSource,dbName=%s,depots=%s", dbName, depots);
		DataSourceContextHolder.setDataSourceType(dbName);
	}

	/**
	 * 获取表名
	 * 
	 * @param preTableName
	 *            表明前缀
	 * @param depots
	 *            分表字符串
	 * @return
	 */
	public static String getTableName(String preTableName, String depots) {
		String tableName = "";
		String hashValue = String.valueOf(Math.abs(depots.hashCode() % AppendTaskConstant.PER_DATASOURCE_TABLE_NUM));
		if (hashValue.length() < 2) {
			hashValue = "0" + hashValue;
		}
		tableName = preTableName + hashValue;
		return tableName;
	}

	/**
	 * 获取追号任务表名
	 * 
	 * @param depots
	 * @return
	 */
	public static String getAppendTaskTableName(String depots) {
		return getTableName(preTaskTableName, depots);
	}

	/**
	 * 获取追号任务明细表名
	 * 
	 * @param depots
	 * @return
	 */
	public static String getAppendTaskDetailTableName(String depots) {
		return getTableName(preDetailTableName, depots);
	}

	/**
	 * 获取追号任务索引表名
	 * 
	 * @param depots
	 * @return
	 */
	public static String getAppendTaskIndexTableName(String depots) {
		return getTableName(preIndexTableName, depots);
	}

}
