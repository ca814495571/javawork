package com.cqfc.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;

public class DateUtil {
	public static final String DATE_FORMAT_WITH_TZ = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	public static final String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

	private static final String sdfOne = "yyyy-MM-dd HH:mm:ss";

	private static final String sdfTwo = "yyyy-MM-dd";

	private static final String sdfThree = "yyyyMMddHHmm";

	private static final String sdfFour = "yyyyMMddHHmmss";

	private static final String dateFormat1 = "yyyy-MM-dd";

	private static final String dateFormat2 = "yyyyMMdd";

	private static final ThreadLocal<DateFormat> sdfOneThread = new ThreadLocal<DateFormat>();
	private static final ThreadLocal<DateFormat> sdfTwoThread = new ThreadLocal<DateFormat>();
	private static final ThreadLocal<DateFormat> sdfThreeThread = new ThreadLocal<DateFormat>();
	private static final ThreadLocal<DateFormat> sdfFourThread = new ThreadLocal<DateFormat>();
	// dateFormat="yyyy-MM-dd"
	private static ThreadLocal<DateFormat> dateFormat1Thread = new ThreadLocal<DateFormat>();
	// dateFormat="yyyyMMdd"
	private static ThreadLocal<DateFormat> dateFormat2Thread = new ThreadLocal<DateFormat>();
	// dateFormat="yyyy-MM-dd HH:mm:ss"
	private static ThreadLocal<DateFormat> dateFormat3Thread = new ThreadLocal<DateFormat>();

	public static DateFormat getSdfOne() {
		DateFormat df = (DateFormat) sdfOneThread.get();
		if (df == null) {
			df = new SimpleDateFormat(sdfOne);
			sdfOneThread.set(df);
		}

		return df;
	}

	public static DateFormat getSdfTwo() {
		DateFormat df = (DateFormat) sdfTwoThread.get();
		if (df == null) {
			df = new SimpleDateFormat(sdfTwo);
			sdfTwoThread.set(df);
		}

		return df;
	}

	public static DateFormat getSdfThree() {
		DateFormat df = (DateFormat) sdfThreeThread.get();
		if (df == null) {
			df = new SimpleDateFormat(sdfThree);
			sdfThreeThread.set(df);
		}

		return df;
	}

	public static DateFormat getSdfFour() {
		DateFormat df = (DateFormat) sdfFourThread.get();
		if (df == null) {
			df = new SimpleDateFormat(sdfFour);
			sdfFourThread.set(df);
		}

		return df;
	}

	public static DateFormat getDateFormat1() {
		DateFormat df = (DateFormat) dateFormat1Thread.get();
		if (df == null) {
			df = new SimpleDateFormat(dateFormat1);
			dateFormat1Thread.set(df);
		}
		return df;
	}

	public static DateFormat getDateFormat2() {
		DateFormat df = (DateFormat) dateFormat2Thread.get();
		if (df == null) {
			df = new SimpleDateFormat(dateFormat2);
			dateFormat2Thread.set(df);
		}
		return df;
	}

	public static DateFormat getDateFormat3() {
		DateFormat df = (DateFormat) dateFormat3Thread.get();
		if (df == null) {
			df = new SimpleDateFormat(DATE_FORMAT_DATETIME);
			dateFormat3Thread.set(df);
		}
		return df;
	}

	/**
	 * 字符串转为Date类型
	 * 
	 * @param aMask
	 *            日期的字符串模式
	 * @param strDate
	 *            日期字符串
	 * @return
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String aMask, String strDate) throws ParseException {
		DateFormat df = null;
		if (aMask.equals(dateFormat1)) {
			df = getDateFormat1();
		} else if (aMask.equals(dateFormat2)) {
			df = getDateFormat2();
		} else {
			df = new SimpleDateFormat(aMask);
		}
		Date date = null;
		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return (date);
	}

	/**
	 * 获取字符串时间
	 * 
	 * @param aMask
	 * @param aDate
	 * @return
	 */
	public static final String getDateTime(String aMask, Date aDate) {
		String returnValue = "";
		DateFormat df = null;
		if (aMask.equals(dateFormat1)) {
			df = getDateFormat1();
		} else if (aMask.equals(dateFormat2)) {
			df = getDateFormat2();
		} else {
			df = new SimpleDateFormat(aMask);
		}
		if (aDate != null) {
			returnValue = df.format(aDate);
		}
		return (returnValue);
	}

	/**
	 * 获取当前日期是星期几
	 * 
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekDays[w];
	}

	/**
	 * 时间分钟、秒钟计算
	 * 
	 * @param day
	 *            初始时间
	 * @param type
	 *            "SECOND"秒、"MINUTE"分钟、"DAY"日
	 * @param offset
	 *            偏移量
	 * @return
	 */
	public static String addDateMinut(String day, String type, int offset) {
		DateFormat format = getDateFormat3();
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
		// 24小时制
		if ("MINUTE".equals(type)) {
			cal.add(Calendar.MINUTE, offset);
		} else if ("SECOND".equals(type)) {
			cal.add(Calendar.SECOND, offset);
		} else if ("HOUR".equals(type)) {
			cal.add(Calendar.HOUR, offset);
		} else if ("DAY".equals(type)) {
			cal.add(Calendar.DATE, offset);
		}
		date = cal.getTime();
		cal = null;
		return format.format(date);
	}

	/**
	 * 比较时间 date1<date2,>0;date1=date2,=0;date1>date2,<0;日期格式:yyyy-MM-dd
	 * HH:mm:ss
	 * 
	 * @param date1
	 *            时间格式 2014-06-20 15:16:34
	 * @param date2
	 *            时间格式 2014-06-20 15:16:34
	 * @return
	 */
	public static int compareDateTime(String date1, String date2) {
		try {
			DateFormat df = getDateFormat3();
			return df.parse(date2).compareTo(df.parse(date1));
		} catch (ParseException e) {
			return 0;
		}
	}

	/**
	 * 比较日期 date1<date2,>0;date1=date2,=0;date1>date2,<0;日期格式：yyyy-MM-dd
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDate(String date1, String date2) {
		try {
			DateFormat df = getDateFormat1();
			return df.parse(date2).compareTo(df.parse(date1));
		} catch (ParseException e) {
			return 0;
		}
	}

	/**
	 * 计算偏移天数的时间
	 * 
	 * @param currentDate
	 *            开始时间
	 * @param offset
	 *            正数往后加，负数往前移动
	 * @return
	 */
	public static Date getOffsetDate(Date currentDate, int offset) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(currentDate);
		calendar.add(Calendar.DATE, offset);// 把日期往后增加一天.整数往后推,负数往前移动
		return calendar.getTime();
	}

	/**
	 * 获取两个日期的时间差
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return 相隔天数
	 */
	public static int getTimeDifference(String begin, String end) {
		long DAY = 0, day = 0;
		try {
			Date beginDate = convertStringToDate("yyyy-MM-dd", begin);
			Date endDate = convertStringToDate("yyyy-MM-dd", end);
			DAY = 24L * 60L * 60L * 1000L;
			day = endDate.getTime() - beginDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Long.valueOf(day / DAY).intValue();
	}

	/**
	 * 获取当前时间 返回 Timestamp
	 * 
	 * @return
	 */
	public static Timestamp getCurrentTime() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 判断是否为除夕至初六这段时间
	 * 
	 * @param time
	 * @return
	 */
	public static boolean checkIsHoliday(String time) {
		boolean flag = false;
		Date timeDate;
		try {
			time = addDateMinut(time, "DAY", 1);
			timeDate = convertStringToDate("yyyy-MM-dd", time);
			Calendar cal = Calendar.getInstance();
			cal.setTime(timeDate);
			Lunar lunar = new Lunar(cal);
			if (lunar.month == 1 && (lunar.day >= 1 && lunar.day <= 7)) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outDateTime;
	}

	public static String yearAdd(Date date, int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.YEAR, num);
		date = cal.getTime();

		return getSdfOne().format(date);
	}

	/**
	 * 验证字符串为时间格式的合法性
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isValidDate(String s) {
		boolean flagOne = true;
		boolean flagTwo = true;
		try {
			getSdfOne().parse(s);
		} catch (ParseException e) {
			flagOne = false;
		}
		try {
			getSdfTwo().parse(s);
		} catch (ParseException e) {
			flagTwo = false;
		}
		if (flagOne || flagTwo) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 得到格式为yyyyMMddHHmm的当前时间
	 * 
	 * @return
	 */
	public static String getCurrentDateTime() {

		return getSdfThree().format(new Date());
	}

	public static String formatStringFour(String time) throws ParseException {

		Date date = null;
		date = getSdfOne().parse(time);

		return getSdfFour().format(date);
	}

	public static String formatStringOne(String time) throws ParseException {

		Date date = null;
		date = getSdfOne().parse(time);

		return getSdfOne().format(date);
	}

	public static String formatStringFour(Date date) throws ParseException {

		return getSdfFour().format(date);
	}

	/**
	 * 将yyyy-MM-dd HH:mm:ss转成yyyy-MM-dd'T'HH:mm:ss'Z'格式的时间， 以保证能存到solr中
	 * 
	 * @param datetime
	 * @return
	 */
	public static final String convertNormal2GMT(String datetime) {
		String result = "";
		if (datetime == null || datetime.length() != DATE_FORMAT_DATETIME.length()) {
			return datetime;
		}
		result = datetime.replace(' ', 'T');
		result += 'Z';
		return result;
	}

	/**
	 * 将yyyy-MM-dd'T'HH:mm:ss'Z'转成yyyy-MM-dd HH:mm:ss格式的时间，
	 * 以方便将solr读到的时间转成数据库的格式
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

	/**
	 * 截取日期.0之前的数据
	 * 
	 * @param dateTime
	 * @return
	 */
	public static final String getSubstringDateTime(String dateTime) {
		String result = "";
		if (StringUtils.isNotBlank(dateTime)) {
			result = dateTime.substring(0, dateTime.indexOf(".0"));
		}
		return result;

	}

	public static void main(String args[]) {
		try {

			System.out.println(checkIsHoliday("2013-02-16 15:03:0.0"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
