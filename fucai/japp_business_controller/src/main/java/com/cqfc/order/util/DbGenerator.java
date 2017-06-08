package com.cqfc.order.util;

import com.cqfc.util.OrderConstant;
import com.jami.common.DataSourceContextHolder;

/**
 * @author liwh
 */
public class DbGenerator {

	/**
	 * 获取数据库名下标
	 * 
	 * @param type
	 *            类型：竞技彩或数字彩
	 * @param depots
	 *            分库字符串
	 * @return
	 */
	public static String getDbName(String type, String depots) {
		String hashValue = String.valueOf(Math.abs(depots.hashCode() / OrderConstant.PER_DATASOURCE_TABLE_NUM
				% OrderConstant.DATASOURCE_NUM));
		if (hashValue.length() < 2) {
			hashValue = "0" + hashValue;
		}
		// return OrderDynamicUtil.PRE_DBNAME + hashValue;
		return type + hashValue;
	}

	/**
	 * 设置数据源
	 * 
	 * @param dbName
	 */
	public static void setOrderDynamicDataSource(String dbName) {
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
		String hashValue = String.valueOf(Math.abs(depots.hashCode() % OrderConstant.PER_DATASOURCE_TABLE_NUM));
		if (hashValue.length() < 2) {
			hashValue = "0" + hashValue;
		}
		return preTableName + hashValue;
	}

	/**
	 * 获取订单表名
	 * 
	 * @param depots
	 * @return
	 */
	public static String getOrderTableName(String depots) {
		return getTableName(OrderDynamicUtil.PRE_TABLENAME, depots);
	}

	/**
	 * 竞技彩获取主表名称
	 * 
	 * @param depots
	 * @return
	 */
	public static String getSportOrderTableNameMain(String depots) {
		return getTableName(OrderDynamicUtil.PRE_COMPETITION_TABLENAME_MAIN, depots);
	}

	/**
	 * 竞技彩获取细表名称
	 * 
	 * @param depots
	 * @return
	 */
	public static String getSportOrderTableNameDetail(String depots) {
		return getTableName(OrderDynamicUtil.PRE_COMPETITION_TABLENAME_DETAIL, depots);
	}

}
