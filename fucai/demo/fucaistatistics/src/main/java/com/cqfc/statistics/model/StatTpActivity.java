package com.cqfc.statistics.model;


public class StatTpActivity {

	
	private long time ; //截止日期
	private long scr10RecNum;//10元刮刮乐领取数量
	private long scr5RecNum;//5元刮刮乐领取数量
	private long scrRecTotal;//刮刮乐领取总金额
	private long actScr5ExcNum;//实际5元刮刮乐兑换数量
	private long actScr10ExcNum;//实际10元刮刮乐兑换数量
	private long actScrExcTotal;//实际刮刮乐兑换总金额
	private long scr10HandRecNum;//10元彩金领取数量
	private long scr10HandRecMoney;//10元彩金领取金额
	private String address;//投注站地址
	private String name;//投注站名称
	private String bet_station;//投注站编码
	private int station_type;//投注站类型
	private int business_type;//投注站商业类型
	private int if_Join;//是否参加了活动 0未参加 1参加了
	
	public StatTpActivity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StatTpActivity(long time, long scr10RecNum, long scr5RecNum,
			long scrRecTotal, long actScr5ExcNum, long actScr10ExcNum,
			long actScrExcTotal, long scr10HandRecNum, long scr10HandRecMoney,
			String address, String name, String bet_station, int station_type,
			int business_type, int if_Join) {
		super();
		this.time = time;
		this.scr10RecNum = scr10RecNum;
		this.scr5RecNum = scr5RecNum;
		this.scrRecTotal = scrRecTotal;
		this.actScr5ExcNum = actScr5ExcNum;
		this.actScr10ExcNum = actScr10ExcNum;
		this.actScrExcTotal = actScrExcTotal;
		this.scr10HandRecNum = scr10HandRecNum;
		this.scr10HandRecMoney = scr10HandRecMoney;
		this.address = address;
		this.name = name;
		this.bet_station = bet_station;
		this.station_type = station_type;
		this.business_type = business_type;
		this.if_Join = if_Join;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getScr10RecNum() {
		return scr10RecNum;
	}

	public void setScr10RecNum(long scr10RecNum) {
		this.scr10RecNum = scr10RecNum;
	}

	public long getScr5RecNum() {
		return scr5RecNum;
	}

	public void setScr5RecNum(long scr5RecNum) {
		this.scr5RecNum = scr5RecNum;
	}

	public long getScrRecTotal() {
		return scrRecTotal;
	}

	public void setScrRecTotal(long scrRecTotal) {
		this.scrRecTotal = scrRecTotal;
	}

	public long getActScr5ExcNum() {
		return actScr5ExcNum;
	}

	public void setActScr5ExcNum(long actScr5ExcNum) {
		this.actScr5ExcNum = actScr5ExcNum;
	}

	public long getActScr10ExcNum() {
		return actScr10ExcNum;
	}

	public void setActScr10ExcNum(long actScr10ExcNum) {
		this.actScr10ExcNum = actScr10ExcNum;
	}

	public long getActScrExcTotal() {
		return actScrExcTotal;
	}

	public void setActScrExcTotal(long actScrExcTotal) {
		this.actScrExcTotal = actScrExcTotal;
	}

	public long getScr10HandRecNum() {
		return scr10HandRecNum;
	}

	public void setScr10HandRecNum(long scr10HandRecNum) {
		this.scr10HandRecNum = scr10HandRecNum;
	}

	public long getScr10HandRecMoney() {
		return scr10HandRecMoney;
	}

	public void setScr10HandRecMoney(long scr10HandRecMoney) {
		this.scr10HandRecMoney = scr10HandRecMoney;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBet_station() {
		return bet_station;
	}

	public void setBet_station(String bet_station) {
		this.bet_station = bet_station;
	}

	public int getStation_type() {
		return station_type;
	}

	public void setStation_type(int station_type) {
		this.station_type = station_type;
	}

	public int getBusiness_type() {
		return business_type;
	}

	public void setBusiness_type(int business_type) {
		this.business_type = business_type;
	}

	public int getIf_Join() {
		return if_Join;
	}

	public void setIf_Join(int if_Join) {
		this.if_Join = if_Join;
	}
	
}
