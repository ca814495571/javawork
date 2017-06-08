package com.cqfc.statistics.model;

import java.math.BigDecimal;

public class BranchLottery {
    long time; // 统计时间
    String lottery; // 彩种
    BigDecimal pay_money; //当天销售额
    BigDecimal pay_cnt; //当天订单数
    String bet_station; //分中心编码
    String branchName;
    
	public BranchLottery(long time, String lottery, BigDecimal pay_money,
			BigDecimal pay_cnt, String bet_station, String branchName) {
		super();
		this.time = time;
		this.lottery = lottery;
		this.pay_money = pay_money;
		this.pay_cnt = pay_cnt;
		this.bet_station = bet_station;
		this.branchName = branchName;
	}

	public BranchLottery() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
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
    
}
