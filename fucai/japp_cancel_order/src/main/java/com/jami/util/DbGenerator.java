package com.jami.util;

import com.jami.common.DataSourceContextHolder;

/**
 * @author liwh
 */
public class DbGenerator {

	public static final String MASTER = "master";

	public static final String SLAVE = "slave";

	private static final String preCancelDbName = "fucai_cancel_order";

	/**
	 * 设置撤单数据源
	 * 
	 * @param masterSlave
	 */
	public static void setCancelDynamicDataSource(String masterSlave) {
		String dbName = masterSlave + "_" + preCancelDbName;
		DataSourceContextHolder.setDataSourceType(dbName);
	}

}
