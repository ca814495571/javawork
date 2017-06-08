package com.cqfc.access.model;

public class LotteryPrizeResultModel {
	private Integer ballCount;
	private String detail;
	private String checkDetailCount;
	private Long amount;
	private String winningBallContent;
	private String prize;

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Integer getBallCount() {
		return ballCount;
	}

	public void setBallCount(Integer ballCount) {
		this.ballCount = ballCount;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getWinningBallContent() {
		return winningBallContent;
	}

	public void setWinningBallContent(String winningBallContent) {
		this.winningBallContent = winningBallContent;
	}

	public String getPrize() {
		return prize;
	}

	public void setPrize(String prize) {
		this.prize = prize;
	}

	public String getCheckDetailCount() {
		return checkDetailCount;
	}

	public void setCheckDetailCount(String checkDetailCount) {
		this.checkDetailCount = checkDetailCount;
	}

}
