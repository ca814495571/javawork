package com.cqfc.ticketwinning.util;

import java.util.HashSet;
import java.util.Set;

import com.cqfc.util.LotteryType;

public class JCContentSetUtil {
	private static Set<String> jzspfSet = new HashSet<String>();
	private static Set<String> jzrqspfSet = new HashSet<String>();
	private static Set<String> jzbfSet = new HashSet<String>();
	private static Set<String> jzzjqsSet = new HashSet<String>();
	private static Set<String> jzbqcSet = new HashSet<String>();
	private static Set<String> jzhhggSet = new HashSet<String>();
	
	private static Set<String> jlsfSet = new HashSet<String>();
	private static Set<String> jlrfsfSet = new HashSet<String>();
	private static Set<String> jlsfcSet = new HashSet<String>();
	private static Set<String> jldxfSet = new HashSet<String>();
	private static Set<String> jlhhggSet = new HashSet<String>();
	
	static {
		jzspfSet.add("0");
		jzspfSet.add("1");
		jzspfSet.add("3");
		jzrqspfSet.add("0");
		jzrqspfSet.add("1");
		jzrqspfSet.add("3");
		jzbfSet.add("1:0");
		jzbfSet.add("2:0");
		jzbfSet.add("2:1");
		jzbfSet.add("3:0");
		jzbfSet.add("3:1");
		jzbfSet.add("3:2");
		jzbfSet.add("4:0");
		jzbfSet.add("4:1");
		jzbfSet.add("4:2");
		jzbfSet.add("5:0");
		jzbfSet.add("5:1");
		jzbfSet.add("5:2");
		jzbfSet.add("1");
		jzbfSet.add("0:0");
		jzbfSet.add("1:1");
		jzbfSet.add("2:2");
		jzbfSet.add("3:3");
		jzbfSet.add("3");
		jzbfSet.add("0:1");
		jzbfSet.add("0:2");
		jzbfSet.add("0:3");
		jzbfSet.add("0:4");
		jzbfSet.add("0:5");
		jzbfSet.add("1:2");
		jzbfSet.add("1:3");
		jzbfSet.add("1:4");
		jzbfSet.add("1:5");
		jzbfSet.add("2:3");
		jzbfSet.add("2:4");
		jzbfSet.add("2:5");
		jzbfSet.add("0");
		jzzjqsSet.add("0");
		jzzjqsSet.add("1");
		jzzjqsSet.add("2");
		jzzjqsSet.add("3");
		jzzjqsSet.add("4");
		jzzjqsSet.add("5");
		jzzjqsSet.add("6");
		jzzjqsSet.add("7");
		jzbqcSet.add("00");
		jzbqcSet.add("01");
		jzbqcSet.add("03");
		jzbqcSet.add("10");
		jzbqcSet.add("11");
		jzbqcSet.add("13");
		jzbqcSet.add("30");
		jzbqcSet.add("31");
		jzbqcSet.add("33");
		
		jzhhggSet.add("SPF#0");
		jzhhggSet.add("SPF#1");
		jzhhggSet.add("SPF#3");
		jzhhggSet.add("RQSPF#0");
		jzhhggSet.add("RQSPF#1");
		jzhhggSet.add("RQSPF#3");
		jzhhggSet.add("BF#1:0");
		jzhhggSet.add("BF#2:0");
		jzhhggSet.add("BF#2:1");
		jzhhggSet.add("BF#3:0");
		jzhhggSet.add("BF#3:1");
		jzhhggSet.add("BF#3:2");
		jzhhggSet.add("BF#4:0");
		jzhhggSet.add("BF#4:1");
		jzhhggSet.add("BF#4:2");
		jzhhggSet.add("BF#5:0");
		jzhhggSet.add("BF#5:1");
		jzhhggSet.add("BF#5:2");
		jzhhggSet.add("BF#1");
		jzhhggSet.add("BF#0:0");
		jzhhggSet.add("BF#1:1");
		jzhhggSet.add("BF#2:2");
		jzhhggSet.add("BF#3:3");
		jzhhggSet.add("BF#3");
		jzhhggSet.add("BF#0:1");
		jzhhggSet.add("BF#0:2");
		jzhhggSet.add("BF#0:3");
		jzhhggSet.add("BF#0:4");
		jzhhggSet.add("BF#0:5");
		jzhhggSet.add("BF#1:2");
		jzhhggSet.add("BF#1:3");
		jzhhggSet.add("BF#1:4");
		jzhhggSet.add("BF#1:5");
		jzhhggSet.add("BF#2:3");
		jzhhggSet.add("BF#2:4");
		jzhhggSet.add("BF#2:5");
		jzhhggSet.add("BF#0");
		jzhhggSet.add("JQS#0");
		jzhhggSet.add("JQS#1");
		jzhhggSet.add("JQS#2");
		jzhhggSet.add("JQS#3");
		jzhhggSet.add("JQS#4");
		jzhhggSet.add("JQS#5");
		jzhhggSet.add("JQS#6");
		jzhhggSet.add("JQS#7");
		jzhhggSet.add("BQC#00");
		jzhhggSet.add("BQC#01");
		jzhhggSet.add("BQC#03");
		jzhhggSet.add("BQC#10");
		jzhhggSet.add("BQC#11");
		jzhhggSet.add("BQC#13");
		jzhhggSet.add("BQC#30");
		jzhhggSet.add("BQC#31");
		jzhhggSet.add("BQC#33");
		
		jlsfSet.add("0");
		jlsfSet.add("1");
		jlrfsfSet.add("0");
		jlrfsfSet.add("1");
		jlsfcSet.add("101");
		jlsfcSet.add("106");
		jlsfcSet.add("111");
		jlsfcSet.add("116");
		jlsfcSet.add("121");
		jlsfcSet.add("126");
		jlsfcSet.add("001");
		jlsfcSet.add("006");
		jlsfcSet.add("011");
		jlsfcSet.add("016");
		jlsfcSet.add("021");
		jlsfcSet.add("026");
		jldxfSet.add("0");
		jldxfSet.add("1");
		
		jlhhggSet.add("SF#0");
		jlhhggSet.add("SF#1");
		jlhhggSet.add("RFSF#0");
		jlhhggSet.add("RFSF#1");
		jlhhggSet.add("SFC#101");
		jlhhggSet.add("SFC#106");
		jlhhggSet.add("SFC#111");
		jlhhggSet.add("SFC#116");
		jlhhggSet.add("SFC#121");
		jlhhggSet.add("SFC#126");
		jlhhggSet.add("SFC#001");
		jlhhggSet.add("SFC#006");
		jlhhggSet.add("SFC#011");
		jlhhggSet.add("SFC#016");
		jlhhggSet.add("SFC#021");
		jlhhggSet.add("SFC#026");
		jlhhggSet.add("DXF#0");
		jlhhggSet.add("DXF#1");
		
	}	
	
	public static Set<String> getSetContentByLotteryId(String lotteryId){
		Set<String> retSet = null;
		
		if(lotteryId.equals(LotteryType.JCZQSPF.getText())){
			retSet = jzspfSet;
		}
		else if(lotteryId.equals(LotteryType.JCZQRQSPF.getText())){
			retSet = jzrqspfSet;
		}
		else if(lotteryId.equals(LotteryType.JCZQBF.getText())){
			retSet = jzbfSet;
		}
		else if(lotteryId.equals(LotteryType.JCZQJQS.getText())){
			retSet = jzzjqsSet;
		}
		else if(lotteryId.equals(LotteryType.JCZQBQC.getText())){
			retSet = jzbqcSet;
		}
		else if(lotteryId.equals(LotteryType.JCZQHHGG.getText())){
			retSet = jzhhggSet;
		}
		else if(lotteryId.equals(LotteryType.JCLQSF.getText())){
			retSet = jlsfSet;
		}
		else if(lotteryId.equals(LotteryType.JCLQRFSF.getText())){
			retSet = jlrfsfSet;
		}
		else if(lotteryId.equals(LotteryType.JCLQSFC.getText())){
			retSet = jlsfcSet;
		}
		else if(lotteryId.equals(LotteryType.JCLQDXF.getText())){
			retSet = jldxfSet;
		}
		else if(lotteryId.equals(LotteryType.JCLQHHGG.getText())){
			retSet = jlhhggSet;
		}
		
		return retSet;
	}	
}
