package com.cqfc.common.zookeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.ctc.wstx.util.StringUtil;

/**
 * 服务器信息列表缓存
 * 
 * @author HowKeyond
 * 
 */
public class ServerCache {

	private static Map<String, List<Server>> serverMap = new HashMap<String, List<Server>>();

	public static List<Server> getServerList(String serverKey) {
		return serverMap.get(serverKey);
	}

	public static void putServerList(String serverKey, List<Server> serverList) {
		serverMap.put(serverKey, serverList);
	}

	public static boolean containsKey(String serverKey) {
		return serverMap.containsKey(serverKey);
	}

	public static void setServerMap(Map<String, List<Server>> map) {
		serverMap = map;
	}

	/**
	 * 根据模块名及方法名生成serverKey
	 * 
	 * @param moduleName
	 *            模块名，如businessController、accessBack
	 * @param methodName
	 *            方法名，如isTicketSuccess
	 * @return 返回服务key，如BusinessControllerService_isTicketSuccess
	 */
	public static String getServerKey(String moduleName, String methodName) {
		return moduleName.substring(0, 1).toUpperCase(Locale.ENGLISH)
				+ moduleName.substring(1) + "Service_" + methodName;
	}

	/**
	 * 根据模块名及方法名获取setNo列表
	 * 
	 * @param moduleName
	 *            模块名，如businessController、accessBack
	 * @param methodName
	 *            方法名，如isTicketSuccess
	 * @return setNo列表
	 */
	public static List<String> getSetNos(String moduleName, String methodName) {
		String serverKey = getServerKey(moduleName, methodName);
		List<String> setNos = new ArrayList<String>();
		List<Server> list = serverMap.get(serverKey);
		if (list != null) {
			for (Server server : list) {
				if (StringUtils.isEmpty(server.getSetNo())) {
					continue;
				}
				setNos.add(server.getSetNo());
			}
		}
		return setNos;
	}
}
