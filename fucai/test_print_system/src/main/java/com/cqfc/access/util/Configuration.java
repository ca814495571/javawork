package com.cqfc.access.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;


public class Configuration {

	static String configFile = "parameter_local.properties";
	static Map<String, PropertyResourceBundle> resourceMap = new HashMap<String, PropertyResourceBundle>();
	static {
		InputStream stream = null;
		Reader reader = null;
		try {
			stream = Configuration.class.getClassLoader().getResourceAsStream(configFile);
			if (stream == null){
				configFile = "parameter.properties";
				stream = Configuration.class.getClassLoader().getResourceAsStream(configFile);
			}
			reader = new InputStreamReader(stream);
			PropertyResourceBundle resource = new PropertyResourceBundle(reader);
			resourceMap.put(configFile, resource);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void initResource(String fileName) {
		InputStream stream = null;
		Reader reader = null;
		try {
			stream = Configuration.class.getClassLoader().getResourceAsStream(fileName);
			reader = new InputStreamReader(stream);
			PropertyResourceBundle resource = new PropertyResourceBundle(reader);
			resourceMap.put(fileName, resource);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取相应模块parameter.properties文件定义name的值
	 * 
	 * @param name
	 * @return
	 */
	public static String getConfigValue(String name) {
		String value = null;
		try {
			PropertyResourceBundle resource = resourceMap.get(configFile);
			if (resource.containsKey(name)) {
				value = resource.getString(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static String getConfigValue(String cfgFile, String name) {
		if (!resourceMap.containsKey(cfgFile)) {
			initResource(cfgFile);
		}
		String value = null;
		try {
			PropertyResourceBundle resource = resourceMap.get(cfgFile);
			if (resource.containsKey(name)) {
				value = resource.getString(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
}
