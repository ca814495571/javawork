package com.jami.util;

import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;

public class DBSourceUtil {
	// 主库
	public static final String MASTER = "master_";
	// 从库
	public static final String SLAVE = "slave_";
	// 中奖结果库
	public static final String WINNING_RESULT_DBNAME = "fucai_ticket_winning";
	// 中奖结果表
	public static final String WINNING_RESULT_TABLENAME = "t_lottery_winning_result";
	// 订单临时库
	public static final String ORDER_TEMP_DBNAME = "fucai_ticket_winning";
	// 订单临时表
	public final static String ORDER_TEMP_TABLENAME = "t_temp_order";
	// 竞彩订单临时表
	public final static String JC_ORDER_TEMP_TABLENAME = "t_temp_jc_detail";
	/**
	 * 根据索引获取表名
	 * @param index
	 * @return
	 */
	public static String getTableName(String dbName, int index){
		String tableName = TicketWinningConstantsUtil.PARTNER_ORDER_TABLE_NAME_PREFIX;
		
		if(dbName.startsWith(TicketWinningConstantsUtil.USER_ORDER_DB_NAME_PREFIX)){
			tableName = TicketWinningConstantsUtil.USER_ORDER_TABLE_NAME_PREFIX;
		}
		
		if(index < 10){
			tableName += "0" + index;
		}
		else{
			tableName += index;
		}
		
		return tableName;
	}
	
	/**
	 * 根据索引获取数据库名
	 * @param index
	 * @return
	 */
	public static String getDbName(int index){
		String dbName = TicketWinningConstantsUtil.PARTNET_ORDER_DB_NAME_PREFIX;
		
		if(index < 10){
			dbName += "0" + index;
		}
		else{
			index -= 10; 
			dbName = TicketWinningConstantsUtil.USER_ORDER_DB_NAME_PREFIX;
			dbName += "0" + index;
		}
		
		return dbName;
	}
	
	public static void setMasterDataSourceType(String dbName){
		DataSourceContextHolder.setDataSourceType(MASTER + dbName);
	}
	
	public static void setSlaveDataSourceType(String dbName){
		DataSourceContextHolder.setDataSourceType(SLAVE + dbName);
	}
}
