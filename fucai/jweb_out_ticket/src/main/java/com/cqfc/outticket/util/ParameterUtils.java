package com.cqfc.outticket.util;

import java.util.HashMap;
import java.util.Map;

import com.cqfc.util.Configuration;
import com.jami.util.Log;

/**
 * 加载parameter.properties参数
 */
public class ParameterUtils {

	public static Map<String, String> map = new HashMap<String, String>();

	public static String getParameterValue(String parameterName) {
		String parameterValue = "";
		try {
			parameterValue = map.get(parameterName);
			if (null == parameterValue || "".equals(parameterValue)) {
				parameterValue = Configuration.getConfigValue(parameterName);
				map.put(parameterName, parameterValue);
			}
		} catch (Exception e) {
			Log.run.error("获取parameter.properties参数发生异常,参数名称：" + parameterName, e);
		}
		return parameterValue;
	}
	
}
