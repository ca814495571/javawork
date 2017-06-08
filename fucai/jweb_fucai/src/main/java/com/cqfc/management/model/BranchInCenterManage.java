package com.cqfc.management.model;

import java.io.Serializable;

/**
 * 中心管理设置下的分中心信息
 * @author Administrator
 *
 */
public class BranchInCenterManage implements Serializable{

	
	private static final long serialVersionUID = 5726116037747284417L;
	private String branchName;
	private int bettingShopNum;
	private String linkMan;
	private String phone;
	private String address;
	private int branchId;
	public BranchInCenterManage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BranchInCenterManage(String branchName, int bettingShopNum,
			String linkMan, String phone, String address, int branchId) {
		super();
		this.branchName = branchName;
		this.bettingShopNum = bettingShopNum;
		this.linkMan = linkMan;
		this.phone = phone;
		this.address = address;
		this.branchId = branchId;
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
	
}
