package com.cqfc.statistics.model;

import java.math.BigDecimal;

/**
 * @author: giantspider@126.com
 */
public class StatLottery {
	private long time;
	private String lottery;
	private BigDecimal pay_money;
	private BigDecimal pay_cnt;
	private String bet_station;
	private String parentCode;
	private String stationName;
	private String address;
	private int business_type;
	public StatLottery() {
		super();
	}

	public StatLottery(long time, String lottery, BigDecimal pay_money,
			BigDecimal pay_cnt, String bet_station, String parentCode,
			String stationName, String address, int business_type) {
		super();
		this.time = time;
		this.lottery = lottery;
		this.pay_money = pay_money;
		this.pay_cnt = pay_cnt;
		this.bet_station = bet_station;
		this.parentCode = parentCode;
		this.stationName = stationName;
		this.address = address;
		this.business_type = business_type;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getLottery() {
		return lottery;
	}

	public void setLottery(String lottery) {
		this.lottery = lottery;
	}

	public BigDecimal getPay_money() {
		return pay_money;
	}

	public void setPay_money(BigDecimal pay_money) {
		this.pay_money = pay_money;
	}

	public BigDecimal getPay_cnt() {
		return pay_cnt;
	}

	public void setPay_cnt(BigDecimal pay_cnt) {
		this.pay_cnt = pay_cnt;
	}

	public String getBet_station() {
		return bet_station;
	}

	public void setBet_station(String bet_station) {
		this.bet_station = bet_station;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getBusiness_type() {
		return business_type;
	}

	public void setBusiness_type(int business_type) {
		this.business_type = business_type;
	}

	@Override
	public String toString() {
		return "StatLottery{" + "time=" + time + ", lottery='" + lottery + '\''
				+ ", pay_money=" + pay_money + ", pay_cnt=" + pay_cnt
				+ ", bet_station='" + bet_station + '\'' + '}';
	}
}
