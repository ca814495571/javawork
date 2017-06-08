package com.jami.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.cqfc.util.Configuration;


public class DataSourceUtil {

		public static final String PreDbName = "fucai_user_order_";
		
		public static final String TempOrderDbName = "fucai_ticket_winning";

		public static final String TempOrderTableName = "t_temp_order";
		
		public static final String WinningResultTableName = "t_lottery_winning_result";
		
		public static final String RecoveryIndexTable = "t_recovery_index";
		
		public static final String PreTableName = "t_user_order_";

		public static final int DB_NUM = Integer.parseInt(Configuration.getConfigValue("db_num").trim());
		
		public static final int TABLE_NUM = Integer.parseInt(Configuration.getConfigValue("table_num").trim());
		
		public static final String MASTER = "master_";
		
		public static final String SLAVE = "slave_";
		
		
		
		public static final String COUNT_DB = "fucai_count";
		
			
		/**
		 * 根据索引获取数据库名称		
		 * @param num
		 * @return
		 */
		public static String  getDateSourceName(int num){
					
					String numTemp = "";
					String dbName = "";
				
					if(num<10){
						numTemp = "0"+num;
						dbName = PreDbName+numTemp;
					}else{
						dbName = PreDbName+num;
					}
					return dbName;
				}
		
		
		/**
		 * 根据索引获取表名
		 * @param num
		 * @param dbName
		 * @return
		 */
		public static String getDbTableName(int num){
			
			String tableName = "";
			String numTemp ="";
			
//			if(dbName.indexOf(PARTNERORDER_DBNAME) != -1){
				if(num<10){
					numTemp = "0"+num;
					tableName = PreTableName + numTemp;
				}else{
					tableName = PreTableName + num;
				}
				
				return tableName;
		}
		
		public static void main(String[] args) {
			
			System.out.println(getDbName("12"));
			System.out.println(getTableName("12"));
		}
		
		/**
		 * 根据userId生成表名称
		 * 
		 * @param tradeNo
		 * @return
		 */
		public static String getTableName(String userId) {

			String hashCode = String.valueOf(Math.abs(userId.hashCode() % TABLE_NUM));

			while (hashCode.length() < 2) {

				hashCode = "0" + hashCode;
			}

			return DataSourceUtil.PreTableName + hashCode;
		}

		/**
		 * 根据用户id得到数据库名称
		 * 
		 * @param tradeNo
		 * @return
		 */
		public static String getDbName(String userId) {


			String hashCode = String.valueOf(Math.abs(userId.hashCode() / TABLE_NUM % DB_NUM));

			while (hashCode.length() < 2) {

				hashCode = "0" + hashCode;
			}

			return DataSourceUtil.PreDbName + hashCode;
		}
		
		
		public static int getDbCode(String userId) {


			String hashCode = String.valueOf(Math.abs(userId.hashCode() / TABLE_NUM % DB_NUM));

			while (hashCode.length() < 2) {

				hashCode = "0" + hashCode;
			}

			return  Integer.parseInt(hashCode);
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
				CountLog.addUserOrder.error("数据库异常", e3);
				CountLog.run.error("数据库异常", e3);
			}

		}
}
