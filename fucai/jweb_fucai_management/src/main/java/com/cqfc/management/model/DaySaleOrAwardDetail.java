package com.cqfc.management.model;

import java.io.Serializable;

public class DaySaleOrAwardDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1610073783930857208L;
	private String lotteryId;
	private String totalMoney;
	private String awardMoney;
	public DaySaleOrAwardDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getAwardMoney() {
		return awardMoney;
	}
	public void setAwardMoney(String awardMoney) {
		this.awardMoney = awardMoney;
	}

	

}
