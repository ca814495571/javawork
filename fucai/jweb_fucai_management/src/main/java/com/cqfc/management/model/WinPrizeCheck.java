package com.cqfc.management.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class WinPrizeCheck implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1026836247108488425L;
	private String partnerId;
	private String lotteryId;
	private String name;
	private String issueNo;
	private Integer status;//state值对应：1：中奖审核未审核13 2：中奖审核通过15、16、17 3： 派奖审核未审核 15  4：派奖审核通过17
	private String totalMoney; 

	public WinPrizeCheck(String partnerId, String name, String lotteryId,
			String issueNo, Integer status, String totalMoney) {
		super();
		this.partnerId = partnerId;
		this.name = name;
		this.lotteryId = lotteryId;
		this.issueNo = issueNo;
		this.status = status;
		this.totalMoney = totalMoney;
	}

	public WinPrizeCheck() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		
		if(StringUtils.isNotBlank(this.lotteryId)){
			
			sb.append(" 彩种: ");	
			sb.append(this.lotteryId);	
		}
		
		if(StringUtils.isNotBlank(this.issueNo)){
			
			sb.append(" 期号: ");	
			sb.append(this.issueNo);	
		}
		
		if(StringUtils.isNotBlank(this.partnerId)){
			
			sb.append(" 合作商Id: ");	
			sb.append(this.partnerId);	
		}
		
		if(StringUtils.isNotBlank(this.name)){
			
			sb.append(" 合作商名称: ");	
			sb.append(this.name);	
		}
		
		return sb.toString();
	}

	
	
}
