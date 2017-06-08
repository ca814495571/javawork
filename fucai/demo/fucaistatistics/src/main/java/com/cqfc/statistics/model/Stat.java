package com.cqfc.statistics.model;

import java.math.BigDecimal;

/**
 * @author: giantspider@126.com
 */
public class Stat {
    long time;
    BigDecimal pay_money;
    BigDecimal pay_cnt;
    long new_user;
    String bet_station;
    String parentCode;
    String stationName;
    String address;
    int station_type;
    int business_type;
    public Stat() {
		super();
	}

	public Stat(long time, BigDecimal pay_money, BigDecimal pay_cnt,
			long new_user, String bet_station, String parentCode,
			String stationName, String address, int station_type,
			int business_type) {
		super();
		this.time = time;
		this.pay_money = pay_money;
		this.pay_cnt = pay_cnt;
		this.new_user = new_user;
		this.bet_station = bet_station;
		this.parentCode = parentCode;
		this.stationName = stationName;
		this.address = address;
		this.station_type = station_type;
		this.business_type = business_type;
	}

	public long getTime() {
		return time;
	}


	public void setTime(long time) {
		this.time = time;
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


	public long getNew_user() {
		return new_user;
	}


	public void setNew_user(long new_user) {
		this.new_user = new_user;
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


	@Override
    public String toString() {
        return "Stat{" +
                "time=" + time +
                ", pay_money=" + pay_money +
                ", pay_cnt=" + pay_cnt +
                ", new_user=" + new_user +
                ", bet_station='" + bet_station + '\'' +
                ", parentCode='" + parentCode + '\'' +
                ", stationName='" + stationName + '\'' +
                '}';
    }
}
