package com.cqfc.util;


public enum LotteryType {
	SSQ(1, "SSQ", "双色球"),
	QLC(2, "QLC", "七乐彩"),
	FC3D(3, "D3", "福彩3D"),
	XYNC(4, "CQKL10", "幸运农场"),
	SSC(5, "CQSSC", "时时彩"),
	DLT(6, "DLT", "大乐透"),
	QXC(7, "QXC", "七星彩"),
	PLS(8, "PLS", "排列3"),
	PLW(9, "PLW", "排列5"),
	ZJSYXW(10, "ZJSYXW", "浙江11选5"),
	JCZQSPF(11, "JZSPF", "竞彩足球胜平负"),
	JCZQBF(12, "JZBF", "竞彩足球比分"),
	JCZQJQS(13, "JZJQS", "竞彩足球总进球数"),
	JCZQBQC(14, "JZBQC", "竞彩足球半全场胜平负"),
	JCZQHHGG(15, "JZHHGG", "竞彩足球混合过关"),
	JCZQRQSPF(16, "JZRQSPF", "竞彩足球让球胜平负"),
	GYJJC(17, "GYJJC", "冠亚军竞猜"),
	JCLQSF(18, "JLSF", "竞彩篮球胜负"),
	JCLQRFSF(19, "JLRFSF", "竞彩篮球让分胜负"),
	JCLQSFC(20, "JLSFC", "竞彩篮球胜分差"),
	JCLQDXF(21, "JLDXF", "竞彩篮球大小分"),
	JCLQHHGG(22, "JLHHGG", "竞彩篮球混合过关"),
	ZCSFC(23, "ZCSF", "足彩胜负彩"),
	ZCRX9(24, "ZCR9", "足彩任选9"),
	ZC4CJQ(25, "ZC4JQS", "足彩4场进球"),
	ZC6CBQC(26, "ZC6BQC", "足彩6场半全场"),
	BDSPF(27, "BDSPF", "单场胜平负"),
	BDBF(28, "BDBF", "单场正确比分"),
	BDSXDS(29, "BDSXDS", "单场上下单双"),
	BDZJQS(30, "BDJQS", "单场进球数"),
	BDBQCSPC(31, "BDBQC", "单场半全场"),
	BDXBC(32, "BDXBC", "单场下半场比分"),
	BDSFGG(33, "BDSF", "单场胜负过关");

	private final Integer id;
	private final String text;
	private final String name;
	
	private LotteryType(Integer id, String text, String name) {
		this.id = id;
		this.text = text;
		this.name = name;
	}
	
	public Integer getValue() {
		return id;
	}

	public String getText() {
		return text;
	}
	
	public String getDesc() {
		return name;
	}
	
	public static LotteryType getEnum(Integer id) {
		if (id != null) {
			LotteryType[] values = LotteryType.values();
			for (LotteryType val : values) {
				if (val.getValue().intValue() == id.intValue()) {
					return val;
				}
			}
		}
		return null;
	}
	
	
	
	public static String getLotteryName(String text) {
		
		if(LotteryType.SSQ.getText().equals(text)){
			return LotteryType.SSQ.getDesc();
		}
		
		if(LotteryType.QLC.getText().equals(text)){
			return LotteryType.QLC.getDesc();
		}
		if(LotteryType.FC3D.getText().equals(text)){
			return LotteryType.FC3D.getDesc();
		}
		if(LotteryType.XYNC.getText().equals(text)){
			return LotteryType.XYNC.getDesc();
		}
		if(LotteryType.SSC.getText().equals(text)){
			return LotteryType.SSC.getDesc();
		}
		if(LotteryType.DLT.getText().equals(text)){
			return LotteryType.DLT.getDesc();
		}
		if(LotteryType.QXC.getText().equals(text)){
			return LotteryType.QXC.getDesc();
		}
		if(LotteryType.PLS.getText().equals(text)){
			return LotteryType.PLS.getDesc();
		}
		if(LotteryType.PLW.getText().equals(text)){
			return LotteryType.PLW.getDesc();
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(getLotteryName("SSC"));
	}
}
