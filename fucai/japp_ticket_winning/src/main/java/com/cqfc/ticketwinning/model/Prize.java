package com.cqfc.ticketwinning.model;

public class Prize {
	// 是否中奖
	private boolean isPrize;
	// 中奖金额
	private long prizeAmount;

	public boolean isPrize() {
		return isPrize;
	}

	public void setPrize(boolean isPrize) {
		this.isPrize = isPrize;
	}

	public long getPrizeAmount() {
		return prizeAmount;
	}

	public void setPrizeAmount(long prizeAmount) {
		this.prizeAmount = prizeAmount;
	}

}
