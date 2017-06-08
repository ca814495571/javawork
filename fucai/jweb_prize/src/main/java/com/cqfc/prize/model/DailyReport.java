package com.cqfc.prize.model;

import java.io.Serializable;

public class DailyReport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1548967624348435143L;

	private int status;
	
	private long totalAmount;//总记录数
	
	private long totalMoney;//总金额

	private String countTime;

	public DailyReport(int status, long totalAmount, long totalMoney,
			String countTime) {
		super();
		this.status = status;
		this.totalAmount = totalAmount;
		this.totalMoney = totalMoney;
		this.countTime = countTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public long getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(long totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getCountTime() {
		return countTime;
	}

	public void setCountTime(String countTime) {
		this.countTime = countTime;
	}

	public DailyReport() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "DailyReport [status=" + status + ", totalAmount=" + totalAmount
				+ ", totalMoney=" + totalMoney + ", "
				+ (countTime != null ? "countTime=" + countTime : "") + "]";
	}
	
	
}
