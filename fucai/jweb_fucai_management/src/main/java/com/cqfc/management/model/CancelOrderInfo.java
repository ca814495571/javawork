package com.cqfc.management.model;

import java.io.Serializable;

public class CancelOrderInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3153786482774539935L;

	private String orderNo;//订单编号
	private String partnerId;//渠道ID
	private String userId;//用户ID
	private String lotteryId;//彩种ID
	private String issueNo;//期号
	private Integer outTicketStatus;//订单出票状态：3出票中 4已出票待开奖 5出票失败
	private String totalAmount;//投注总金额
	private String orderContent;//投注内容
	private String playType;//	玩法
	private Integer multiple;//倍数
	private String createTime;//创建时间
	private String lastUpdateTime;//最后更新时间
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public int getOutTicketStatus() {
		return outTicketStatus;
	}
	public void setOutTicketStatus(Integer outTicketStatus) {
		this.outTicketStatus = outTicketStatus;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getOrderContent() {
		return orderContent;
	}
	public void setOrderContent(String orderContent) {
		this.orderContent = orderContent;
	}
	public String getPlayType() {
		return playType;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	public int getMultiple() {
		return multiple;
	}
	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	 
	
	
	
}
