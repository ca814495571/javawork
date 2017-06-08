package com.jami.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.util.Configuration;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.OrderConstant;


public class DataSourceUtil {

		public static final String PARTNERORDER_DBNAME = "fucai_partner_order_";

		public static final String PARTNERORDER_TABLENAME = "t_partner_order_";

		public static final String ORDERDETAIL_TABLENAME = "t_jc_order_detail_";
		
		public static final String JCORDER_TABLENAME = "t_jc_order_";
		
		public static final String COUNT_DB = "fucai_count";
		
		public static final String TempOrderDbName = "fucai_ticket_winning";

		public static final String TempOrderTableName = "t_temp_order";
		
		public static final String TempJcDetailTableName = "t_temp_jc_detail";
		
		public static final String WinningResultTableName = "t_lottery_winning_result";
		
		public static final String RecoveryIndexTable = "t_recovery_index";
		
		public static final int DB_NUM = Integer.parseInt(Configuration.getConfigValue("db_num").trim());
		
		public static final int TABLE_NUM = Integer.parseInt(Configuration.getConfigValue("table_num").trim());
		
		public static final String MASTER = "master_";
		
		public static final String SLAVE = "slave_";
		
		public static String  getDateSourceName(int num){
			
			String numTemp = "";
			String dbName = "";
			if(num<10){
				numTemp = "0"+num;
				dbName = PARTNERORDER_DBNAME+numTemp;
			}else{
				dbName = PARTNERORDER_DBNAME+num;
			}
			return dbName;
		}
		
		
		public static String getDbTableName(int num){
			
			String tableName = "";
			String numTemp ="";
			
				if(num<10){
					numTemp = "0"+num;
					tableName = PARTNERORDER_TABLENAME + numTemp;
				}else{
					tableName = PARTNERORDER_TABLENAME + num;
				}
			return tableName;
			
		}
		
		
		/**
		 * 根据合作商订单号生成表名称
		 * 
		 * @param tradeNo
		 * @return
		 */
		public static String getTableName(String tableName,String tradeNo) {

			String hashCode = String.valueOf(Math.abs(tradeNo.hashCode() % 100));

			while (hashCode.length() < 2) {

				hashCode = "0" + hashCode;
			}

			return tableName + hashCode;
		}

		/**
		 * 根据合作商订单号得到数据库名称
		 * 
		 * @param tradeNo
		 * @return
		 */
		public static String getDbName(String tradeNo) {

			String hashCode = String.valueOf(Math.abs(tradeNo.hashCode() / 100 % 10));

			while (hashCode.length() < 2) {

				hashCode = "0" + hashCode;
			}

			return DataSourceUtil.PARTNERORDER_DBNAME + hashCode;
		}
		
		
		public static int getDbCode(String tradeNo) {

			String hashCode = String.valueOf(Math.abs(tradeNo.hashCode() / 100 % 10));

			while (hashCode.length() < 2) {

				hashCode = "0" + hashCode;
			}

			return  Integer.parseInt(hashCode);
		}
		
		//根据订单号得到库名和表名
		public static String getDbNameByOrderNo(Long orderNo){
			return getDateSourceName(Integer.parseInt(orderNo/1000%DB_NUM+""));
		}
		
		public static String getTableNameByOrderNo(Long orderNo){
			return getDbTableName(Integer.parseInt(orderNo%TABLE_NUM+""));
		}
		
		public static void closeConn(Connection conn, Statement statement) {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e3) {
				CountLog.addPartnerOrder.error("数据库异常", e3);
				CountLog.run.error("数据库异常", e3);
			}

		}
		
		
		public static long getOrderNo(String ticketId) {
			try {
				ReturnMessage ret = TransactionProcessor.dynamicInvoke("idGenerate", "idGen", "orderNo");
				if (!ret.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
					Log.fucaibiz.error("获取orderNo发生异常,ticketId=%s,errorMsg=%s", ticketId, ret.getMsg());
				}
				long resp = (Long) ret.getObj();
				return Long.valueOf(resp + getDbTableIndex(ticketId));
			} catch (Exception e) {
				Log.fucaibiz.error("获取orderNo发生异常,ticketId=" + ticketId, e);
				return 0;
			}
		}
		
		private static String getDbTableIndex(String ticketId) {
			int value = ticketId.hashCode();
			String dbIndex = String.valueOf(Math.abs(value / OrderConstant.PER_DATASOURCE_TABLE_NUM
					% OrderConstant.DATASOURCE_NUM));
			dbIndex = dbIndex.length() == 1 ? "0" + dbIndex : dbIndex;
			String tableIndex = String.valueOf(Math.abs(value % OrderConstant.PER_DATASOURCE_TABLE_NUM));
			tableIndex = tableIndex.length() == 2 ? "0" + tableIndex : (tableIndex.length() == 1 ? "00" + tableIndex
					: tableIndex);
			String index = dbIndex + tableIndex;
			Log.run.debug("订单索引,ticketId=%s,value=%s", ticketId, index);
			return index;
		}
		
		
		public static void main(String[] args) {
			
			System.out.println(123/100);
		}
}
