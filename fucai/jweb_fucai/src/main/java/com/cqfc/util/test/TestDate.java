package com.cqfc.util.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class TestDate {

	
	
	
	
	public static void main(String[] args) throws ParseException, ClassNotFoundException {
		
		
//		System.out.println(new Date().toLocaleString());
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//		String timeStr = sdf.format(new Date());
//		System.out.println(timeStr);
////		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date d = sdf.parse(timeStr);
//		System.out.println(d.getTime());
//		System.out.println(new Date().getTime());
		Class<?> driverClass = Class.forName("com.mysql.jdbc.Driver");
	}
}
