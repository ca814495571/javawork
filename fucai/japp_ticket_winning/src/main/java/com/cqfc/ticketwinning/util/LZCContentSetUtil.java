package com.cqfc.ticketwinning.util;

import java.util.HashSet;
import java.util.Set;

import com.cqfc.util.LotteryType;

public class LZCContentSetUtil {
	private static Set<String> zc14cSet = new HashSet<String>();
	private static Set<String> zcrx9Set = new HashSet<String>();
	private static Set<String> zc6cbqcSet = new HashSet<String>();
	private static Set<String> zc4cjqcSet = new HashSet<String>();
	
	static {
		zc14cSet.add("0");
		zc14cSet.add("1");
		zc14cSet.add("3");
		zcrx9Set.add("0");
		zcrx9Set.add("1");
		zcrx9Set.add("3");
		zc6cbqcSet.add("00");
		zc6cbqcSet.add("01");
		zc6cbqcSet.add("03");
		zc6cbqcSet.add("10");
		zc6cbqcSet.add("11");
		zc6cbqcSet.add("13");
		zc6cbqcSet.add("30");
		zc6cbqcSet.add("31");
		zc6cbqcSet.add("33");
		zc4cjqcSet.add("0");
		zc4cjqcSet.add("1");
		zc4cjqcSet.add("2");
		zc4cjqcSet.add("3");
	}
	
	public static Set<String> getSetContentByLotteryId(String lotteryId){
		Set<String> retSet = null;
		
		if(lotteryId.equals(LotteryType.ZCSFC.getText())){
			retSet = zc14cSet;
		}
		else if(lotteryId.equals(LotteryType.ZC6CBQC.getText())){
			retSet = zc6cbqcSet;
		}
		else if(lotteryId.equals(LotteryType.ZC4CJQ.getText())){
			retSet = zc4cjqcSet;
		}
		else if(lotteryId.equals(LotteryType.ZCRX9.getText())){
			retSet = zcrx9Set;
		}
		
		return retSet;
	}
}
