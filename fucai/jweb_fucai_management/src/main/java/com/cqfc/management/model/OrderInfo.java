package com.cqfc.management.model;

import java.io.Serializable;

public class OrderInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7828015076424942445L;
	private String orderNo;
	private String userId;
	private String partnerId;
	private String ticketId;
	private String totalMoney;
	private String lotteryId;
	private String playType;
	private String issueNo;
	private String content;
	private Short partnerType;
	private String partnerName;
	private String userName;
	private Integer stakeNum;
	private Integer multiple;
	private int orderType;
	private String createTime;
	private Integer status;
	private String prizeMoney;
	private String afterPrizeMoney;

	public OrderInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderInfo(String orderNo, String userId, String partnerId,
			String ticketId, String totalMoney, String lotteryId,
			String playType, String issueNo, String content, Short partnerType,
			String partnerName, String userName, Integer stakeNum,
			Integer multiple, int orderType, String createTime, Integer status,
			String prizeMoney, String afterPrizeMoney) {
		super();
		this.orderNo = orderNo;
		this.userId = userId;
		this.partnerId = partnerId;
		this.ticketId = ticketId;
		this.totalMoney = totalMoney;
		this.lotteryId = lotteryId;
		this.playType = playType;
		this.issueNo = issueNo;
		this.content = content;
		this.partnerType = partnerType;
		this.partnerName = partnerName;
		this.userName = userName;
		this.stakeNum = stakeNum;
		this.multiple = multiple;
		this.orderType = orderType;
		this.createTime = createTime;
		this.status = status;
		this.prizeMoney = prizeMoney;
		this.afterPrizeMoney = afterPrizeMoney;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Short getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(Short partnerType) {
		this.partnerType = partnerType;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getStakeNum() {
		return stakeNum;
	}

	public void setStakeNum(Integer stakeNum) {
		this.stakeNum = stakeNum;
	}

	public Integer getMultiple() {
		return multiple;
	}

	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPrizeMoney() {
		return prizeMoney;
	}

	public void setPrizeMoney(String prizeMoney) {
		this.prizeMoney = prizeMoney;
	}

	public String getAfterPrizeMoney() {
		return afterPrizeMoney;
	}

	public void setAfterPrizeMoney(String afterPrizeMoney) {
		this.afterPrizeMoney = afterPrizeMoney;
	}
	
	
}
