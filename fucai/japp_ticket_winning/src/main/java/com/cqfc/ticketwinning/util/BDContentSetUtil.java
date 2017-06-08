package com.cqfc.ticketwinning.util;

import java.util.HashSet;
import java.util.Set;

import com.cqfc.util.LotteryType;

public class BDContentSetUtil {
	private static Set<String> bdspfSet = new HashSet<String>();
	private static Set<String> bdsxdsSet = new HashSet<String>();
	private static Set<String> bdbfSet = new HashSet<String>();
	private static Set<String> bdzjqsSet = new HashSet<String>();
	private static Set<String> bdbqcSet = new HashSet<String>();
	private static Set<String> bdxbcSet = new HashSet<String>();
	private static Set<String> bdsfSet = new HashSet<String>();
	
	static {
		bdspfSet.add("0");
		bdspfSet.add("1");
		bdspfSet.add("3");
		bdsxdsSet.add("1");
		bdsxdsSet.add("2");
		bdsxdsSet.add("3");
		bdsxdsSet.add("4");
		bdbfSet.add("1:0");
		bdbfSet.add("2:0");
		bdbfSet.add("2:1");
		bdbfSet.add("3:0");
		bdbfSet.add("3:1");
		bdbfSet.add("3:2");
		bdbfSet.add("4:0");
		bdbfSet.add("4:1");
		bdbfSet.add("4:2");
		bdbfSet.add("1");
		bdbfSet.add("0:0");
		bdbfSet.add("1:1");
		bdbfSet.add("2:2");
		bdbfSet.add("3:3");
		bdbfSet.add("3");
		bdbfSet.add("0:1");
		bdbfSet.add("0:2");
		bdbfSet.add("1:2");
		bdbfSet.add("0:3");
		bdbfSet.add("1:3");
		bdbfSet.add("2:3");
		bdbfSet.add("0:4");
		bdbfSet.add("1:4");
		bdbfSet.add("2:4");
		bdbfSet.add("0");
		bdzjqsSet.add("0");
		bdzjqsSet.add("1");
		bdzjqsSet.add("2");
		bdzjqsSet.add("3");
		bdzjqsSet.add("4");
		bdzjqsSet.add("5");
		bdzjqsSet.add("6");
		bdzjqsSet.add("7");
		bdbqcSet.add("00");
		bdbqcSet.add("01");
		bdbqcSet.add("03");
		bdbqcSet.add("10");
		bdbqcSet.add("11");
		bdbqcSet.add("13");
		bdbqcSet.add("30");
		bdbqcSet.add("31");
		bdbqcSet.add("33");
		
		bdxbcSet.add("1:0");
		bdxbcSet.add("2:0");
		bdxbcSet.add("2:1");
		bdxbcSet.add("3:0");
		bdxbcSet.add("3:1");
		bdxbcSet.add("3:2");
		bdxbcSet.add("4:0");
		bdxbcSet.add("4:1");
		bdxbcSet.add("4:2");
		bdxbcSet.add("1");
		bdxbcSet.add("0:0");
		bdxbcSet.add("1:1");
		bdxbcSet.add("2:2");
		bdxbcSet.add("3:3");
		bdxbcSet.add("3");
		bdxbcSet.add("0:1");
		bdxbcSet.add("0:2");
		bdxbcSet.add("1:2");
		bdxbcSet.add("0:3");
		bdxbcSet.add("1:3");
		bdxbcSet.add("2:3");
		bdxbcSet.add("0:4");
		bdxbcSet.add("1:4");
		bdxbcSet.add("2:4");
		bdxbcSet.add("0");	
		
		bdsfSet.add("0");
		bdsfSet.add("3");
	}	
	
	public static Set<String> getSetContentByLotteryId(String lotteryId){
		Set<String> retSet = null;
		
		if(lotteryId.equals(LotteryType.BDSPF.getText())){
			retSet = bdspfSet;
		}
		else if(lotteryId.equals(LotteryType.BDSXDS.getText())){
			retSet = bdsxdsSet;
		}
		else if(lotteryId.equals(LotteryType.BDBF.getText())){
			retSet = bdbfSet;
		}
		else if(lotteryId.equals(LotteryType.BDZJQS.getText())){
			retSet = bdzjqsSet;
		}
		else if(lotteryId.equals(LotteryType.BDBQCSPC.getText())){
			retSet = bdbqcSet;
		}
		else if(lotteryId.equals(LotteryType.BDXBC.getText())){
			retSet = bdxbcSet;
		}
		else if(lotteryId.equals(LotteryType.BDSFGG.getText())){
			retSet = bdsfSet;
		}
		
		return retSet;
	}	
}
