package com.cqfc.statistics.model;

import java.math.BigDecimal;

public class CenterLottery {
    long time; // 统计时间
    String lottery; // 彩种
    BigDecimal pay_money; //当天销售额
    BigDecimal pay_cnt; //当天订单数
    String centerCode; //中心编码
    String centerName; //中心名称
    
	public CenterLottery() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CenterLottery(long time, String lottery, BigDecimal pay_money,
			BigDecimal pay_cnt, String centerCode, String centerName) {
		super();
		this.time = time;
		this.lottery = lottery;
		this.pay_money = pay_money;
		this.pay_cnt = pay_cnt;
		this.centerCode = centerCode;
		this.centerName = centerName;
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
	public String getCenterCode() {
		return centerCode;
	}
	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}
	public String getCenterName() {
		return centerName;
	}
	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

}
