package com.cqfc.util.dateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	private static SimpleDateFormat sdfOne = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdfTwo = new SimpleDateFormat("yyyy-MM-dd");

	public static String formatDateOne(Date date) {
		return sdfOne.format(date);
	}

	public static String formatDateTwo(Date date) {
		return sdfTwo.format(date);
	}

	public static Date stringToDateOne(String str) {
		Date date = null;
		try {
			date = sdfOne.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date stringToDateTwo(String str) {
		Date date = null;
		try {
			date = sdfTwo.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static int getYear(Date date) {

		return Integer.parseInt(sdfTwo.format(date).split("-")[0]);
	}

	public static int getMonth(Date date) {

		return Integer.parseInt(sdfTwo.format(date).split("-")[1]);
	}

	/**
	 * 得到前一天的日期
	 * @param date
	 * @return
	 */
	public static String getPreDay(Date date) {
		String preDate = "";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		preDate = sdfTwo.format(calendar.getTime());
		return preDate;
	}

	/**
	 * 得到当天的月首和月尾
	 * @return
	 */
	public static String[] getMonthFristAndLastDay() {
		String[] strs = new String[2];

		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		c.set(Calendar.DAY_OF_MONTH, 1);

		strs[0] = sdfTwo.format(c.getTime());

		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		strs[1] = sdfTwo.format(c.getTime());

		return strs;
	}

	/**
	 * 增加小时
	 * @param date
	 * @param num
	 * @return
	 */
	public static String addDateHour(Date date, int num) {

		Calendar c = Calendar.getInstance();

		c.setTime(date);

		c.add(Calendar.HOUR_OF_DAY, num);

		return formatDateOne(c.getTime());
	}

	/**
	 * 比较时间大小
	 * @param dateOne
	 * @param dateTwo
	 * @return
	 */
	public static boolean compareDate(Date dateOne, Date dateTwo) {
		boolean flag = false;

		Calendar calOne = Calendar.getInstance();
		Calendar calTwo = Calendar.getInstance();
					
		calOne.setTime(dateOne);
		calTwo.setTime(dateTwo);

		if (calOne.getTimeInMillis() > calTwo.getTimeInMillis()) {

			flag = true;

		}

		return flag;
	}
	
	
	/**
	 * true 为以前的数据
	 * @param time
	 * @return
	 */
	public static boolean  ifPreDay(String time){
		
		boolean flag = false;
		
		Date dateOne = stringToDateTwo(time);
		
		Date dateTwo = stringToDateTwo(formatDateTwo(new Date()));
		Calendar calOne = Calendar.getInstance();
		Calendar calTwo = Calendar.getInstance();
					
		calOne.setTime(dateOne);
		calTwo.setTime(dateTwo);

		if (calOne.getTimeInMillis() <= calTwo.getTimeInMillis()) {

			flag = true;

		}
		
		return flag ;
	}
	
	

	public static void main(String[] args) {
		
		System.out.println(ifPreDay("2014-6-10"));
	}

}
