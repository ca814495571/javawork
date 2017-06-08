package com.cqfc.common.zookeeper;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DBKeyValueCache {
	
	private static Map<String, String> dbConfigMap = new HashMap<String, String>();

	public static String getString(String dbKey){
		return dbConfigMap.get(dbKey);
	}
	
	public static void putString(String dbKey, String dbConfig){
		dbConfigMap.put(dbKey, dbConfig);
	}
	
	public static boolean containsKey(String dbKey){
		return dbConfigMap.containsKey(dbKey);
	}
	
	public static void setKeyValueMap(Map<String, String> map) {
		dbConfigMap = map;
		regist2system();
	}
	
	private static void regist2system() {
		for(Entry<String, String> entry: dbConfigMap.entrySet()){
			System.setProperty(entry.getKey(), entry.getValue());
		}
	}
}
