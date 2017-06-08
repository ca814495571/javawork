package com.cqfc.management.model;

import java.io.Serializable;

public class BranchBaseInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3447146050267481763L;

	private String branchName;

	private String branchCode;

	private String branchTel;

	private String branchLinkman;

	public BranchBaseInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BranchBaseInfo(String branchName, String branchCode,
			String branchTel, String branchLinkman) {
		super();
		this.branchName = branchName;
		this.branchCode = branchCode;
		this.branchTel = branchTel;
		this.branchLinkman = branchLinkman;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchTel() {
		return branchTel;
	}

	public void setBranchTel(String branchTel) {
		this.branchTel = branchTel;
	}

	public String getBranchLinkman() {
		return branchLinkman;
	}

	public void setBranchLinkman(String branchLinkman) {
		this.branchLinkman = branchLinkman;
	}

}
