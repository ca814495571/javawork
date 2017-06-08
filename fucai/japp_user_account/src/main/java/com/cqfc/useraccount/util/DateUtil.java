package com.cqfc.useraccount.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_WITH_TZ = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	/**
	 * 时间分钟、秒钟计算
	 * 
	 * @param day
	 * @param type
	 *            "SECOND"秒、"MINUTE"分钟
	 * @param offset
	 *            偏移量
	 * @return
	 */
	public static String addDateMinut(String day, String type, int offset) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null) {
			return "";
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if ("MINUTE".equals(type)) {
			cal.add(Calendar.MINUTE, offset);// 24小时制
		}
		if ("SECOND".equals(type)) {
			cal.add(Calendar.SECOND, offset);// 24小时制
		}
		date = cal.getTime();
		cal = null;
		return format.format(date);
	}

	/**
	 * 时间格式 2014-06-20 15:16:34 如果date1<date2 返回>=0 否则<0
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDate(String date1, String date2) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return df.parse(date2).compareTo(df.parse(date1));
		} catch (ParseException e) {
			return 0;
		}
	}

	/**
	 * 获取当前日期是星期几
	 * 
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekDays[w];
	}

	/**
	 * 字符串时间转换
	 * 
	 * @param aMask
	 *            日期的字符串模式
	 * @param strDate
	 *            日期字符串
	 * @return
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String aMask, String strDate)
			throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(aMask);
		Date date = null;
		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return date;
	}

	/**
	 * 获取date字符串
	 * 
	 * @param aMask
	 *            字符串格式
	 * @param aDate
	 *            date
	 * @return
	 */
	public static final String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}
		return returnValue;
	}

	/**
	 * 将yyyy-MM-dd HH:mm:ss转成yyyy-MM-dd'T'HH:mm:ss'Z'格式的时间， 以保证能存到solr中
	 * 
	 * @param datetime
	 * @return
	 */
	public static final String convertNormal2GMT(String datetime) {
		String result = "";
		if (datetime == null || datetime.length() != DATE_FORMAT.length()) {
			return datetime;
		}
		result = datetime.replace(' ', 'T');
		result += 'Z';
		return result;
	}

	/**
	 * 将yyyy-MM-dd'T'HH:mm:ss'Z'转成yyyy-MM-dd HH:mm:ss格式的时间， 以保证能存到solr中
	 * 
	 * @param datetime
	 * @return
	 */
	public static final String convertGMT2Nomal(String datetime) {
		String result = "";
		if (datetime == null || datetime.length() != DATE_FORMAT_WITH_TZ.length()) {
			return datetime;
		}
		result = datetime.replace('T', ' ');
		result = result.substring(0, result.length() - 1);
		return result;
	}

	public static void main(String args[]) {
		System.out.println(compareDate("2014-06-20 19:32:33",
				"2014-06-20 19:32:34"));
	}

}
