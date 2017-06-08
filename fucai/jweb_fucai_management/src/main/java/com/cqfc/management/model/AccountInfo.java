package com.cqfc.management.model;

import java.io.Serializable;

public class AccountInfo implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2755311460486069313L;

	private String accountNo;
	
	private String accountName;
	
	private String sumMoney;
	
	private String realMoney;
	
	private String handselMoney;
	
	private String useableMoney;
	
	private String freezeMoney;

	public AccountInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AccountInfo(String accountNo, String accountName, String sumMoney,
			String realMoney, String handselMoney, String useableMoney,
			String freezeMoney) {
		super();
		this.accountNo = accountNo;
		this.accountName = accountName;
		this.sumMoney = sumMoney;
		this.realMoney = realMoney;
		this.handselMoney = handselMoney;
		this.useableMoney = useableMoney;
		this.freezeMoney = freezeMoney;
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

	public String getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(String sumMoney) {
		this.sumMoney = sumMoney;
	}

	public String getRealMoney() {
		return realMoney;
	}

	public void setRealMoney(String realMoney) {
		this.realMoney = realMoney;
	}

	public String getHandselMoney() {
		return handselMoney;
	}

	public void setHandselMoney(String handselMoney) {
		this.handselMoney = handselMoney;
	}

	public String getUseableMoney() {
		return useableMoney;
	}

	public void setUseableMoney(String useableMoney) {
		this.useableMoney = useableMoney;
	}

	public String getFreezeMoney() {
		return freezeMoney;
	}

	public void setFreezeMoney(String freezeMoney) {
		this.freezeMoney = freezeMoney;
	}

	
}
