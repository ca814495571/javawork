package com.cqfc.ticketissue.util;

import java.util.HashMap;
import java.util.Map;

import com.cqfc.util.Configuration;
import com.jami.util.Log;

/**
 * 加载parameter.properties参数
 */
public class ParameterUtils {

	public static Map<String, String> map = new HashMap<String, String>();
	public static Map<String, String> zsmap = new HashMap<String, String>();
	public static Map<String, String> dynamicMap = new HashMap<String, String>();

	public static String getParameterValue(String parameterName) {
		String parameterValue = "";
		try {
			parameterValue = map.get(parameterName);
			if (null == parameterValue || "".equals(parameterValue)) {
				parameterValue = Configuration.getConfigValue("parameter.properties",parameterName);
				map.put(parameterName, parameterValue);
			}
		} catch (Exception e) {
			Log.run.error("获取parameter.properties参数发生异常,参数名称：" + parameterName, e);
		}
		return parameterValue;
	}
	
	public static String getZSParameterValue(String parameterName) {
		String parameterValue = "";
		try {
			parameterValue = zsmap.get(parameterName);
			if (null == parameterValue || "".equals(parameterValue)) {
				parameterValue = Configuration.getConfigValue("zsparameter.properties", parameterName);
				zsmap.put(parameterName, parameterValue);
			}
		} catch (Exception e) {
			Log.run.error("获取parameter.properties参数发生异常,参数名称：" + parameterName, e);
		}
		return parameterValue;
	}
	
	/**
	 * 根据配置获取参数值
	 * @param parameterName
	 * @return
	 */
	public static String getDynamicParameterValue(String parameterName){
		String parameterValue = "";
		try {
			parameterValue = dynamicMap.get(parameterName);
			if (null == parameterValue || "".equals(parameterValue)) {
				parameterValue = Configuration.getConfigValue(parameterName);
				dynamicMap.put(parameterName, parameterValue);
			}
		} catch (Exception e) {
			Log.run.error("获取parameter.properties参数发生异常,参数名称：" + parameterName, e);
		}
		return parameterValue;
	}
	
}
