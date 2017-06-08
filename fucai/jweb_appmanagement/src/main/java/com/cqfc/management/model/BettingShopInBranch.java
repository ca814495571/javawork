package com.cqfc.management.model;

import java.io.Serializable;

/**
 * 分中心管理设置下的投注站的信息
 * 
 * @author Administrator
 *
 */
public class BettingShopInBranch implements Serializable {

	private static final long serialVersionUID = 1362147589958234781L;
	private String code;
	private String branchName;
	private String LinkMan;
	private String phone;
	private String address;
	private String bettingShopName;
	private int bettingShopId;
	private int branchId;

	public BettingShopInBranch(String code, String branchName, String linkMan,
			String phone, String address, String bettingShopName,
			int bettingShopId, int branchId) {
		super();
		this.code = code;
		this.branchName = branchName;
		LinkMan = linkMan;
		this.phone = phone;
		this.address = address;
		this.bettingShopName = bettingShopName;
		this.bettingShopId = bettingShopId;
		this.branchId = branchId;
	}

	public BettingShopInBranch() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getLinkMan() {
		return LinkMan;
	}

	public void setLinkMan(String linkMan) {
		LinkMan = linkMan;
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

	public String getBettingShopName() {
		return bettingShopName;
	}

	public void setBettingShopName(String bettingShopName) {
		this.bettingShopName = bettingShopName;
	}

	public int getBettingShopId() {
		return bettingShopId;
	}

	public void setBettingShopId(int bettingShopId) {
		this.bettingShopId = bettingShopId;
	}

}
