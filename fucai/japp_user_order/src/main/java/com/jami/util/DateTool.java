package com.jami.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTool {

	/**
	 * 获取当前时间字符串
	 * 
	 * @param format
	 *            格式
	 * @return
	 */
	public static String getCurDateTime(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date dateCurrentTime = new Date();
		String sCurrentTime = formatter.format(dateCurrentTime);
		return sCurrentTime;
	}

	/**
	 * Date转String
	 * 
	 * @param inputDateTime
	 *            Date
	 * @param format
	 *            String
	 * @return String
	 */
	public static String dateToString(Date inputDateTime, String format) {
		String outDateTime = "0000-00-00 00:00:00";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			outDateTime = formatter.format(inputDateTime);
		} catch (Exception ex) {

		}
		return outDateTime;
	}

}