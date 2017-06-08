package com.cqfc.statistics.model;

import java.math.BigDecimal;

public class CountryLottery {

	private long time;// 统计时间
	private String lottery;// 彩种
	private BigDecimal pay_money;// 当天销售额
	private BigDecimal pay_cnt;// 当天订单数量
	private String country_code;// 区域编码
	private String country_name;// 区域名称
	private String parentCode;
	public CountryLottery() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CountryLottery(long time, String lottery, BigDecimal pay_money,
			BigDecimal pay_cnt, String country_code, String country_name,
			String parentCode) {
		super();
		this.time = time;
		this.lottery = lottery;
		this.pay_money = pay_money;
		this.pay_cnt = pay_cnt;
		this.country_code = country_code;
		this.country_name = country_name;
		this.parentCode = parentCode;
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
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public String getCountry_name() {
		return country_name;
	}
	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}


}
