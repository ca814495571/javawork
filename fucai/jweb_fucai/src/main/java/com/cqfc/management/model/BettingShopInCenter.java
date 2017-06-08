package com.cqfc.management.model;

import java.io.Serializable;
import java.util.List;

public class BettingShopInCenter implements Serializable {

	private static final long serialVersionUID = 6423009958083495504L;
	private int branchNum;
	private int bettingShopNum;
	private List<BettingShopInBranch> bettingShopInfos;

	public BettingShopInCenter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BettingShopInCenter(int branchNum, int bettingShopNum,
			List<BettingShopInBranch> bettingShopInfos) {
		super();
		this.branchNum = branchNum;
		this.bettingShopNum = bettingShopNum;
		this.bettingShopInfos = bettingShopInfos;
	}

	public int getBranchNum() {
		return branchNum;
	}

	public void setBranchNum(int branchNum) {
		this.branchNum = branchNum;
	}

	public int getBettingShopNum() {
		return bettingShopNum;
	}

	public void setBettingShopNum(int bettingShopNum) {
		this.bettingShopNum = bettingShopNum;
	}

	public List<BettingShopInBranch> getBettingShopInfos() {
		return bettingShopInfos;
	}

	public void setBettingShopInfos(List<BettingShopInBranch> bettingShopInfos) {
		this.bettingShopInfos = bettingShopInfos;
	}

}
