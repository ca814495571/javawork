package com.cqfc.ticketwinning.model;

import java.io.Serializable;

public class JCTempOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6857675391967900438L;

	/**
	 * 彩种
	 */
	private String lotteryId;
	/**
	 * 期号
	 */
	private String issueNo;

	/**
	 * 比赛Id
	 */
	private String transferId;

	/**
	 * 比赛编号
	 */
	private String matchNo;

	/**
	 * 比赛类型
	 */
	private Integer matchType;

	/**
	 * 订单编号
	 */
	private String orderNo;

	/**
	 * 投注内容
	 */
	private String content;

	/**
	 * 让球让分
	 */
	private String rq;

	/**
	 * 赔率
	 */
	private String odds;

	/**
	 * 中奖赔率
	 */
	private Integer winOdds;

	/**
	 * 比赛状态
	 */
	private String matchStatus;

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

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getMatchNo() {
		return matchNo;
	}

	public void setMatchNo(String matchNo) {
		this.matchNo = matchNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public Integer getMatchType() {
		return matchType;
	}

	public void setMatchType(Integer matchType) {
		this.matchType = matchType;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	public String getOdds() {
		return odds;
	}

	public void setOdds(String odds) {
		this.odds = odds;
	}

	public Integer getWinOdds() {
		return winOdds;
	}

	public void setWinOdds(Integer winOdds) {
		this.winOdds = winOdds;
	}

	public String getMatchStatus() {
		return matchStatus;
	}

	public void setMatchStatus(String matchStatus) {
		this.matchStatus = matchStatus;
	}

}
