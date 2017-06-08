package com.cqfc.statistics.model;

import java.math.BigDecimal;

public class Branch {

	private long time; // 统计时间
	private BigDecimal pay_money; // 当天销售额
	private BigDecimal pay_cnt;// 当天订单数
	private long new_user;// 当天新增用户
	private String branchCode;// 分中心编码
	private String branchName; // 分中心名称
	private int business_type;

	public Branch() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Branch(long time, BigDecimal pay_money, BigDecimal pay_cnt,
			long new_user, String branchCode, String branchName,
			int business_type) {
		super();
		this.time = time;
		this.pay_money = pay_money;
		this.pay_cnt = pay_cnt;
		this.new_user = new_user;
		this.branchCode = branchCode;
		this.branchName = branchName;
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

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public int getBusiness_type() {
		return business_type;
	}

	public void setBusiness_type(int business_type) {
		this.business_type = business_type;
	}

}
