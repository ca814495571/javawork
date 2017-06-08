package com.cqfc.statistics.model;

import java.math.BigDecimal;

public class Country {

	private long time;
	private BigDecimal pay_money;
	private BigDecimal pay_cnt;
	private long new_user;
	private String country_code;
	private String country_name;
	private String parentCode;
	public Country() {
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
	public Country(long time, BigDecimal pay_money, BigDecimal pay_cnt,
			long new_user, String country_code, String country_name,
			String parentCode) {
		super();
		this.time = time;
		this.pay_money = pay_money;
		this.pay_cnt = pay_cnt;
		this.new_user = new_user;
		this.country_code = country_code;
		this.country_name = country_name;
		this.parentCode = parentCode;
	}
	

}
