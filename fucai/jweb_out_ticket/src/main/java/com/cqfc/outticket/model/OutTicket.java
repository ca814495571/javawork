package com.cqfc.outticket.model;

public class OutTicket {
	private String orderNo;
	private int resultNum;
	private String lotteryId;
	private String issueNo;
	private String playType;
	private long orderMoney;
	private int mutiple;
	private String orderContentOdds;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getResultNum() {
		return resultNum;
	}

	public void setResultNum(int resultNum) {
		this.resultNum = resultNum;
	}

	public String getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public long getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(long orderMoney) {
		this.orderMoney = orderMoney;
	}

	public int getMutiple() {
		return mutiple;
	}

	public void setMutiple(int mutiple) {
		this.mutiple = mutiple;
	}

	public String getOrderContentOdds() {
		return orderContentOdds;
	}

	public void setOrderContentOdds(String orderContentOdds) {
		this.orderContentOdds = orderContentOdds;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

}
