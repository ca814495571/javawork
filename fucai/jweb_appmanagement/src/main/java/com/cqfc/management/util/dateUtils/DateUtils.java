package com.cqfc.management.util.dateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.cqfc.management.model.PcResultObj;

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

	public static Date stringToDateOne(String str) throws ParseException {
		Date date = null;
		
		date = sdfOne.parse(str);
		
		return date;
	}

	public static Date stringToDateTwo(String str) throws ParseException {
		Date date = null;
		
		date = sdfTwo.parse(str);
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
	 * 
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
	 * 
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
	 * 
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
	 * 增加分钟
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static String addDateMin(Date date, int num) {

		Calendar c = Calendar.getInstance();

		c.setTime(date);

		c.add(Calendar.MINUTE, num);

		return formatDateOne(c.getTime());
	}
	/**
	 * 比较时间大小
	 * 
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
	 * true 以当前时间为准之前的时间 时间格式为：yyyy-MM-dd
	 * 
	 * @param time
	 * @return
	 * @throws ParseException 
	 */
	public static boolean ifPreDay(Date time) throws ParseException {

		boolean flag = false;


		Date dateTwo = stringToDateTwo(formatDateTwo(new Date()));
		Calendar calOne = Calendar.getInstance();
		Calendar calTwo = Calendar.getInstance();

		calOne.setTime(time);
		calTwo.setTime(dateTwo);

		if (calOne.getTimeInMillis() < calTwo.getTimeInMillis()) {

			flag = true;

		}

		return flag;
	}

	/**
	 * true 以当前时间为准之前（包括今天）的时间 时间格式为：yyyy-MM-dd
	 * 
	 * @param time
	 * @return
	 */
	public static boolean ifPreDayOrToday(Date form, Date to) {

		boolean flag = false;

		Calendar calOne = Calendar.getInstance();
		Calendar calTwo = Calendar.getInstance();

		calOne.setTime(form);
		calTwo.setTime(to);

		if (calOne.getTimeInMillis() <= calTwo.getTimeInMillis()) {

			flag = true;

		}

		return flag;
	}

	
	
	public static boolean ifOutOfTime(Date form, Date to){
		
		boolean flag = false;


		Calendar calOne = Calendar.getInstance();
		Calendar calTwo = Calendar.getInstance();

		calOne.setTime(form);
		calTwo.setTime(to);
		
		calOne.add(Calendar.DAY_OF_MONTH, 30);
		
		if (calOne.getTimeInMillis() < calTwo.getTimeInMillis()) {

			flag = true;

		}

		
		return flag;
	}
	
	
	/**
	 * 对时间进行校验
	 * 
	 * @return
	 */
	public static PcResultObj checkDate(String fromDate, String toDate) {

		PcResultObj pcResultObj = new PcResultObj();

		
		Date from;
		Date to;
		try {
			from = stringToDateTwo(fromDate);
			to = stringToDateTwo(toDate);
		} catch (ParseException e) {

			 pcResultObj.setEntity(2);
			 pcResultObj.setMsgCode("2");
			 pcResultObj.setMsg("时间格式不正确!");
			 return pcResultObj;
		}
		
		
		
		if (fromDate == null || "".equals(fromDate) || toDate == null
				|| "".equals(fromDate)) {
			
			 pcResultObj.setEntity(2);
			 pcResultObj.setMsgCode("2");
			 pcResultObj.setMsg("开始时间或者结束时间不能为空!");
			 return pcResultObj;
			
		}
		
		if(!ifPreDayOrToday(from,new Date()) 
				|| !ifPreDayOrToday(to,new Date() )){
			
			 pcResultObj.setEntity(2);
			 pcResultObj.setMsgCode("2");
			 pcResultObj.setMsg("开始时间或者结束时间不能大于当前时间!");
			 return pcResultObj;
		}
		
		if(!ifPreDayOrToday(from,to)){
			
				pcResultObj.setEntity(2);
			 pcResultObj.setMsgCode("2");
			 pcResultObj.setMsg("开始时间不能大于结束时间!");
			 return pcResultObj;
		}

		if(ifOutOfTime(from, to)){
			
			 pcResultObj.setEntity(2);
			 pcResultObj.setMsgCode("2");
			 pcResultObj.setMsg("开始时间与结束时间不能大于31天!");
			 return pcResultObj;
		}
		
		pcResultObj.setEntity(1);
		pcResultObj.setMsg("1");
		pcResultObj.setMsg("校验通过!");
		return pcResultObj;
	}

	public static void main(String[] args) throws ParseException {

		System.out.println(getMonthFristAndLastDay()[1] +" "+getMonthFristAndLastDay()[0] );
	}

}
