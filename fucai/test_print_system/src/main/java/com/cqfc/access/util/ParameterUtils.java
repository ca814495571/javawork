package com.cqfc.access.util;

import java.util.HashMap;
import java.util.Map;


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
			e.printStackTrace();
		}
		return parameterValue;
	}

}
