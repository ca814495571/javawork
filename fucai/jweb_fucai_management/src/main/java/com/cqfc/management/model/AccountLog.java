package com.cqfc.management.model;

import java.io.Serializable;

public class AccountLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4613493916352562264L;

	private String accountNo;

	private String accountName;

	private String createTime;

	private String tradeType;

	private String serialNum;

	private String tradeMoney;//user交易总金额为彩金+账户金额
	
	private String accountAmount;
	
	private String handselAmount;

	public AccountLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getTradeMoney() {
		return tradeMoney;
	}

	public void setTradeMoney(String tradeMoney) {
		this.tradeMoney = tradeMoney;
	}

	public String getAccountAmount() {
		return accountAmount;
	}

	public void setAccountAmount(String accountAmount) {
		this.accountAmount = accountAmount;
	}

	public String getHandselAmount() {
		return handselAmount;
	}

	public void setHandselAmount(String handselAmount) {
		this.handselAmount = handselAmount;
	}

	
}
