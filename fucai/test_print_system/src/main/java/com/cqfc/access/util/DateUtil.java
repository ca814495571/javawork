package com.cqfc.access.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
	// dateFormat="yyyyMMdd"
	private static final String dateFormat = "yyyyMMdd";
	private static ThreadLocal<DateFormat> dateFormatThread = new ThreadLocal<DateFormat>();
	// dateFormat="yyyy-MM-dd HH:mm:ss"
	private static final String dateFormat2 = "yyyy-MM-dd HH:mm:ss";
	private static ThreadLocal<DateFormat> dateFormatThread2 = new ThreadLocal<DateFormat>();
	
	private static DateFormat getDateFormatyyyyMMdd() {
		DateFormat df = (DateFormat) dateFormatThread.get();
		if (df == null) {
			df = new SimpleDateFormat(dateFormat);
			dateFormatThread.set(df);
		}
		return df;
	}
	
	private static DateFormat getDateFormat2() {
		DateFormat df = (DateFormat) dateFormatThread2.get();
		if (df == null) {
			df = new SimpleDateFormat(dateFormat2);
			dateFormatThread2.set(df);
		}
		return df;
	}
	
	public static String getDateyyyyMMdd(String date){
		if(date != null && !"".equals(date)){
			return getDateFormatyyyyMMdd().format(date);
		}
		return getDateFormatyyyyMMdd().format(new Date());
	}
	
	public static String getDateyyyyMMdd(){
		return getDateFormatyyyyMMdd().format(new Date());
	}
	
	public static String getDateTime(String dateTime){
		if(dateTime != null && !"".equals(dateTime)){
			return getDateFormat2().format(dateTime);
		}
		return getDateFormat2().format(new Date());
	}
	
	public static String getDateTime(){
		return getDateFormat2().format(new Date());
	}

}
