package com.cqfc.useraccount.util;

import java.util.ArrayList;
import java.util.List;


/**
 * @author HowKeyond
 */
public class DbGenerator {

	public static final String MASTER = "master_";

	public static final String SLAVE = "slave_";

	public static final String FUCAI = "fucai";

	private static final String preDbName = "fucai_user_account_";

	/**
	 * 是否分表
	 */
	private static boolean separate = true;

	/**
	 * 分表的表数量
	 */
	private static int MAX_TABLE_NUM = 100;

	/**
	 * 分表下标的位数
	 */
	private static int TABLE_INDEX_LENGTH = 2;
	/**
	 * 分库的库数量
	 */
	private static int MAX_DB_NUM = 10;

	/**
	 * 获取数据库名下标
	 * 
	 * @param preDbName
	 *            数据库前缀
	 * @param key
	 *            分库字符串
	 * @return
	 */
	public static String getDbName(String preDbName, Object key) {
		String dbName = "";
		String hashValue = String.valueOf(Math.abs(key.hashCode())
				/ MAX_TABLE_NUM % MAX_DB_NUM);
		if (hashValue.length() < 2) {
			hashValue = "0" + hashValue;
		}
		dbName = preDbName + hashValue;
		return dbName;
	}

	/**
	 * 获取UserId的数据库名，目前只有一个库，可以改成双库以避免该库成为瓶颈
	 * 
	 * @param key
	 * @return
	 */
	public static String getUserIdDbName(Object key) {
		return MASTER + FUCAI;
	}

	public static String getDbNameByIndex(int i) {
		String index = "";
		if (i<10){
			index += "0" + i;
		}else{
			index += i;
		}
		return preDbName + index;
	}
	
	/**
	 * 设置福彩库
	 * @param masterSlave
	 */
	public static void setFucaiDataSource(String masterSlave) {
		String dbName = masterSlave + FUCAI;
		DataSourceContextHolder.setDataSourceType(dbName);
	}
	
	/**
	 * 设置数据源
	 * 
	 * @param key
	 *            分库字符串
	 */
	public static void setDynamicDataSource(String masterSlave, Object key) {
		String dbName = masterSlave + getDbName(preDbName, key);
		DataSourceContextHolder.setDataSourceType(dbName);
	}

	/**
	 * 
	 * @param key
	 *            分表关键字
	 * @return 表名序号
	 */
	public static String getTableIndex(Object key) {
		if (!separate) {
			return "";
		}
		String tableName = "_";
		long hash;
		if (key instanceof Long) {
			hash = (Long) key - 1;
		} else {
			hash = key.hashCode();
		}
		String hashValue = String.valueOf(Math.abs(hash)
				% MAX_TABLE_NUM);
		for (int i = 0; i < (TABLE_INDEX_LENGTH - hashValue.length()); i++) {
			hashValue = "0" + hashValue;
		}
		tableName += hashValue;
		return tableName;
	}

	/**
	 * 获取表名
	 * 
	 * @param preTableName
	 *            表明前缀
	 * @param key
	 *            分表字符串
	 * @return
	 */
	public static String getTableName(String preTableName, Object key) {
		String tableName = "";
		String hashValue = String.valueOf(Math.abs(key.hashCode()) % 100);
		for (int i = 0; i < (TABLE_INDEX_LENGTH - hashValue.length()); i++) {
			hashValue = "0" + hashValue;
		}
		tableName = preTableName + hashValue;
		return tableName;
	}

	public static List<String> getAllTableIndex() {
		List<String> tableIndexs = new ArrayList<String>();
		if (separate) {
			for (int i = 0; i < MAX_TABLE_NUM; i++) {
				String index = "" + i;
				for (int j = 0; j < (TABLE_INDEX_LENGTH - index.length()); j++) {
					index = "0" + index;
				}
				tableIndexs.add("_" + index);
			}
		} else {
			tableIndexs.add("");
		}
		return tableIndexs;
	}

}
