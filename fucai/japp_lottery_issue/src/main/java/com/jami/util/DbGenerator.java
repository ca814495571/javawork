package com.jami.util;

import com.jami.common.DataSourceContextHolder;

/**
 * @author liwh
 */
public class DbGenerator {

	private static final String MASTER = "master";

	private static final String SLAVE = "slave";

	/**
	 * 设置主数据源
	 */
	public static void setDynamicMasterSource() {
		String dbName = MASTER + "_fucai";
		DataSourceContextHolder.setDataSourceType(dbName);
	}

	/**
	 * 设置从数据源
	 */
	public static void setDynamicSlaveSource() {
		String dbName = SLAVE + "_fucai";
		DataSourceContextHolder.setDataSourceType(dbName);
	}

}
