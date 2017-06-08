package com.jami.util;

import com.jami.common.DataSourceContextHolder;

/**
 * @author liwh
 */
public class DbGenerator {

	public static final String MASTER = "master";

	public static final String SLAVE = "slave";

	/**
	 * 设置数据源
	 * 
	 * @param masterOrSlave
	 *            主从库
	 */
	public static void setDynamicDataSource(String masterOrSlave) {
		String dbName = masterOrSlave + "_fucai";
		DataSourceContextHolder.setDataSourceType(dbName);
	}

}
