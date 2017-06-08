package com.cqfc.management.model;

import java.io.Serializable;

public class PartnerDayReport implements Serializable {

	private static final long serialVersionUID = 3602922160719476053L;
	/**
	 * 
	 */

	private String lotteryId;
	private String totalMoney;
	private String awardPrizeMoney;
	private String afterPrizeMoney;
	private String countTime;
	public PartnerDayReport(String lotteryId, String totalMoney,
			String awardPrizeMoney, String afterPrizeMoney, String countTime) {
		super();
		this.lotteryId = lotteryId;
		this.totalMoney = totalMoney;
		this.awardPrizeMoney = awardPrizeMoney;
		this.afterPrizeMoney = afterPrizeMoney;
		this.countTime = countTime;
	}
	public PartnerDayReport() {
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
	public String getAwardPrizeMoney() {
		return awardPrizeMoney;
	}
	public void setAwardPrizeMoney(String awardPrizeMoney) {
		this.awardPrizeMoney = awardPrizeMoney;
	}
	public String getAfterPrizeMoney() {
		return afterPrizeMoney;
	}
	public void setAfterPrizeMoney(String afterPrizeMoney) {
		this.afterPrizeMoney = afterPrizeMoney;
	}
	public String getCountTime() {
		return countTime;
	}
	public void setCountTime(String countTime) {
		this.countTime = countTime;
	}
	

}
