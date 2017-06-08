package com.cqfc.management.model;

import java.io.Serializable;

public class BranchBaseInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3447146050267481763L;

	private String branchName;
	private int bettingShopNum;
	private String linkMan;
	private String phone;
	private String address;
	private int branchId;
	private String branchCode;

	public BranchBaseInfo() {
		super();
	}

	public BranchBaseInfo(String branchName, int bettingShopNum,
			String linkMan, String phone, String address, int branchId,
			String branchCode) {
		super();
		this.branchName = branchName;
		this.bettingShopNum = bettingShopNum;
		this.linkMan = linkMan;
		this.phone = phone;
		this.address = address;
		this.branchId = branchId;
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public int getBettingShopNum() {
		return bettingShopNum;
	}

	public void setBettingShopNum(int bettingShopNum) {
		this.bettingShopNum = bettingShopNum;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

}
