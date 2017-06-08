package com.cqfc.management.model;

import java.io.Serializable;

public class BettingShopBaseInfo implements Serializable {

	private static final long serialVersionUID = 4688597775307324431L;

	private String bettingShopName;

	private String bettingShopCode;

	private String branchName;
	
	private String linkMan;

	private int bettingShopId;

	private int branchId;
	
	private String bettingShopTel;

	private String addOne;

	private String addTwo;

	private String lon;

	private String lat;

	public BettingShopBaseInfo() {
		super();
		// TODO Auto-generated constructor stub
	}


	public BettingShopBaseInfo(String bettingShopName, String bettingShopCode,
			String branchName, String linkMan, int bettingShopId, int branchId,
			String bettingShopTel, String addOne, String addTwo, String lon,
			String lat) {
		super();
		this.bettingShopName = bettingShopName;
		this.bettingShopCode = bettingShopCode;
		this.branchName = branchName;
		this.linkMan = linkMan;
		this.bettingShopId = bettingShopId;
		this.branchId = branchId;
		this.bettingShopTel = bettingShopTel;
		this.addOne = addOne;
		this.addTwo = addTwo;
		this.lon = lon;
		this.lat = lat;
	}


	public String getLinkMan() {
		return linkMan;
	}


	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}


	public int getBettingShopId() {
		return bettingShopId;
	}

	public void setBettingShopId(int bettingShopId) {
		this.bettingShopId = bettingShopId;
	}

	public String getBettingShopName() {
		return bettingShopName;
	}

	public void setBettingShopName(String bettingShopName) {
		this.bettingShopName = bettingShopName;
	}

	public String getBettingShopCode() {
		return bettingShopCode;
	}

	public void setBettingShopCode(String bettingShopCode) {
		this.bettingShopCode = bettingShopCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBettingShopTel() {
		return bettingShopTel;
	}

	public void setBettingShopTel(String bettingShopTel) {
		this.bettingShopTel = bettingShopTel;
	}

	public String getAddOne() {
		return addOne;
	}

	public void setAddOne(String addOne) {
		this.addOne = addOne;
	}

	public String getAddTwo() {
		return addTwo;
	}

	public void setAddTwo(String addTwo) {
		this.addTwo = addTwo;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

}
