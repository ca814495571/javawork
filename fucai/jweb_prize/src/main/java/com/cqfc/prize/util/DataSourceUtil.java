package com.cqfc.prize.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.cqfc.util.Configuration;
import com.jami.util.Log;


public class DataSourceUtil {


	    public static final String TABLE_NAME_PRIZE = "t_prize_";
	    
	    public static final String TABLE_NAME_TEMP_PRIZE = "t_temp_prize";
	    
	    public static final String DB_NAME_TEMP_PRIZE = "fucai_prize";
	    
	    public static final String DB_NAME_PRIZE = "fucai_prize_";
	
		public static final int DB_NUM = Integer.parseInt(Configuration.getConfigValue("db_num").trim());
		
		public static final int TABLE_NUM = Integer.parseInt(Configuration.getConfigValue("table_num").trim());
		
		public static final String MASTER = "master_";
		
		public static final String SLAVE = "slave_";
		
		
		
		/**
		 * 根据用户id,生成表名
		 * @param tableName
		 * @param userId
		 * @return
		 */
		public static String getTableName(String tableName,String userId) {

			String hashCode = String.valueOf(Math.abs(userId.hashCode() % 100));

			while (hashCode.length() < 2) {

				hashCode = "0" + hashCode;
			}

			return tableName + hashCode;
		}

		/**
		 * 根据用户Id生成库名
		 * @param userId
		 * @return
		 */
		public static String getDbName(String userId) {

			String hashCode = String.valueOf(Math.abs(userId.hashCode() / TABLE_NUM % DB_NUM));

			while (hashCode.length() < 2) {

				hashCode = "0" + hashCode;
			}

			return DB_NAME_PRIZE + hashCode;
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
				Log.run.error("数据库异常", e3);
			}

		}
		
		
		public static void main(String[] args) {
			
			System.out.println(123/100);
		}
}
