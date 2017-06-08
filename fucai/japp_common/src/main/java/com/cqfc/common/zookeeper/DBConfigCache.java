package com.cqfc.common.zookeeper;

import java.util.HashMap;
import java.util.Map;

import com.cqfc.common.zookeeper.data.DBConfig;

/**
 * db服务器信息列表缓存
 * @author HowKeyond
 *
 */
public class DBConfigCache {
	
	private static Map<String, DBConfig> dbConfigMap = new HashMap<String, DBConfig>();
	
	public static DBConfig getDBConfig(String dbKey){
		return dbConfigMap.get(dbKey);
	}
	
	public static void putDBConfig(String dbKey, DBConfig dbConfig){
		dbConfigMap.put(dbKey, dbConfig);
	}
	
	public static boolean containsKey(String dbKey){
		return dbConfigMap.containsKey(dbKey);
	}
	
	public static void setDBConfigMap(Map<String, DBConfig> map) {
		dbConfigMap = map;
	}
}
