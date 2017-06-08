package com.cqfc.ticketwinning.model;

import java.io.Serializable;

public class OrderNoCount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4352917399577011022L;
	
	/**
	 * 订单号数量
	 */
	private Integer orderNoCount;

	/**
	 * 中奖赔率数量
	 */
	private Integer winOddsCount;

	public Integer getOrderNoCount() {
		return orderNoCount;
	}

	public void setOrderNoCount(Integer orderNoCount) {
		this.orderNoCount = orderNoCount;
	}

	public Integer getWinOddsCount() {
		return winOddsCount;
	}

	public void setWinOddsCount(Integer winOddsCount) {
		this.winOddsCount = winOddsCount;
	}

}
