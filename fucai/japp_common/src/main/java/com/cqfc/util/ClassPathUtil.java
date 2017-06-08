package com.cqfc.util;

import java.io.InputStream;

public class ClassPathUtil {
	
	/**
	 * 得到当前工程classPath下的资源
	 * @param path
	 * @return
	 */
	public static InputStream getClassPathInputStream(String path){
		return ClassPathUtil.class.getClassLoader().getResourceAsStream(path);
	}
}
