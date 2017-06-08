package com.cqfc.statistics.model;

import java.math.BigDecimal;

public class CenterSum {

    long time;           //日期:20141012
    BigDecimal pay_money;     //截止到当天总销售额
    BigDecimal pay_cnt;        //截止到当天总订单数
    long new_user;       //截止到当天总用户数
    String bet_station;  //中心编码
    String centerName;
    
	public CenterSum(long time, BigDecimal pay_money, BigDecimal pay_cnt,
			long new_user, String bet_station, String centerName) {
		super();
		this.time = time;
		this.pay_money = pay_money;
		this.pay_cnt = pay_cnt;
		this.new_user = new_user;
		this.bet_station = bet_station;
		this.centerName = centerName;
	}
	public CenterSum() {
		super();
		// TODO Auto-generated constructor stub
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
	public String getCenterName() {
		return centerName;
	}
	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}
	
	
}
