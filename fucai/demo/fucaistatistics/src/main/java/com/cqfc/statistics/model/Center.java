package com.cqfc.statistics.model;

import java.math.BigDecimal;

public class Center {

    long time; //统计时间
    BigDecimal pay_money; //当天销售额
    BigDecimal pay_cnt;//当天订单数
    long new_user;//当天新增用户
    String centerCode;//中心编码
    String centerName; //中心名称
    
	


	public Center() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public Center(long time, BigDecimal pay_money, BigDecimal pay_cnt,
			long new_user, String centerCode, String centerName) {
		super();
		this.time = time;
		this.pay_money = pay_money;
		this.pay_cnt = pay_cnt;
		this.new_user = new_user;
		this.centerCode = centerCode;
		this.centerName = centerName;
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
