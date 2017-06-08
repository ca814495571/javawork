package com.cqfc.util;

public class MoneyUtil {
	
	/**
	 * 金额由分转为元
	 * @param num
	 * @return
	 */
	public static String toYuanStr(long num){
		String result = "" + num/100;
		long fen = num%100;
		if(fen >= 10){
			
			result += "." + fen;
		}else if(fen>=0){
			
			result += ".0" + fen;
		}
		return result;
	}
	
	/**
	 * 金额由元转为分
	 * @param num
	 * @return
	 */
	public static long toCent(long num){
		return num * 100;
	}
	
	
	/**
	 * 金额由元转为分
	 * @param num
	 * @return
	 */
	public static long toCent(double num){
		return (long) (num * 100);
	}
	
	
}
