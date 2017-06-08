package com.cqfc.management.model;

import java.io.Serializable;

public class PartnerIssueCount implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6580444413266541482L;
	/**
	 */
	private String partnerId;
	private String issueNo;
	private String lotteryId;
	private Short partnerType;
	private String sucMoney;
	private String failMoney;
	private String smallPrizeMoney;
	private String bigPrizeMoney;
	private Long smallPrizeNum;//小奖数量
	private Long bigPrizeNum; //大奖数量
	private Long prizeTotalNum;//中奖总数量
	private String prizeTotalMoney;//中奖总金额
	private String afterPrizeMoney;//税后总金额
	private Long sucNum; //成功销售数量
	private Long failNum;//失败销售数量
	private Long saleTotalNum;  //销售总数量
	private String saleTotalMoney;//销售总金额
	public PartnerIssueCount() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public PartnerIssueCount(String partnerId, String issueNo,
			String lotteryId, Short partnerType, String sucMoney,
			String failMoney, String smallPrizeMoney, String bigPrizeMoney,
			Long smallPrizeNum, Long bigPrizeNum, Long prizeTotalNum,
			String prizeTotalMoney, String afterPrizeMoney, Long sucNum,
			Long failNum, Long saleTotalNum, String saleTotalMoney) {
		super();
		this.partnerId = partnerId;
		this.issueNo = issueNo;
		this.lotteryId = lotteryId;
		this.partnerType = partnerType;
		this.sucMoney = sucMoney;
		this.failMoney = failMoney;
		this.smallPrizeMoney = smallPrizeMoney;
		this.bigPrizeMoney = bigPrizeMoney;
		this.smallPrizeNum = smallPrizeNum;
		this.bigPrizeNum = bigPrizeNum;
		this.prizeTotalNum = prizeTotalNum;
		this.prizeTotalMoney = prizeTotalMoney;
		this.afterPrizeMoney = afterPrizeMoney;
		this.sucNum = sucNum;
		this.failNum = failNum;
		this.saleTotalNum = saleTotalNum;
		this.saleTotalMoney = saleTotalMoney;
	}


	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getIssueNo() {
		return issueNo;
	}
	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	public Short getPartnerType() {
		return partnerType;
	}
	public void setPartnerType(Short partnerType) {
		this.partnerType = partnerType;
	}
	public String getSucMoney() {
		return sucMoney;
	}
	public void setSucMoney(String sucMoney) {
		this.sucMoney = sucMoney;
	}
	public String getFailMoney() {
		return failMoney;
	}
	public void setFailMoney(String failMoney) {
		this.failMoney = failMoney;
	}
	public String getSmallPrizeMoney() {
		return smallPrizeMoney;
	}
	public void setSmallPrizeMoney(String smallPrizeMoney) {
		this.smallPrizeMoney = smallPrizeMoney;
	}
	public String getBigPrizeMoney() {
		return bigPrizeMoney;
	}
	public void setBigPrizeMoney(String bigPrizeMoney) {
		this.bigPrizeMoney = bigPrizeMoney;
	}
	public Long getSmallPrizeNum() {
		return smallPrizeNum;
	}
	public void setSmallPrizeNum(Long smallPrizeNum) {
		this.smallPrizeNum = smallPrizeNum;
	}
	public Long getBigPrizeNum() {
		return bigPrizeNum;
	}
	public void setBigPrizeNum(Long bigPrizeNum) {
		this.bigPrizeNum = bigPrizeNum;
	}
	public Long getPrizeTotalNum() {
		return prizeTotalNum;
	}
	public void setPrizeTotalNum(Long prizeTotalNum) {
		this.prizeTotalNum = prizeTotalNum;
	}
	public String getPrizeTotalMoney() {
		return prizeTotalMoney;
	}
	public void setPrizeTotalMoney(String prizeTotalMoney) {
		this.prizeTotalMoney = prizeTotalMoney;
	}
	public String getAfterPrizeMoney() {
		return afterPrizeMoney;
	}
	public void setAfterPrizeMoney(String afterPrizeMoney) {
		this.afterPrizeMoney = afterPrizeMoney;
	}
	public Long getSucNum() {
		return sucNum;
	}
	public void setSucNum(Long sucNum) {
		this.sucNum = sucNum;
	}
	public Long getFailNum() {
		return failNum;
	}
	public void setFailNum(Long failNum) {
		this.failNum = failNum;
	}
	public Long getSaleTotalNum() {
		return saleTotalNum;
	}
	public void setSaleTotalNum(Long saleTotalNum) {
		this.saleTotalNum = saleTotalNum;
	}
	public String getSaleTotalMoney() {
		return saleTotalMoney;
	}
	public void setSaleTotalMoney(String saleTotalMoney) {
		this.saleTotalMoney = saleTotalMoney;
	}
	
	
}
