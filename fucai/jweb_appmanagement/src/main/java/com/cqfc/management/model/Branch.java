package com.cqfc.management.model;

import java.io.Serializable;
import java.util.List;

/**
 * 分中心管理设置
 * 
 * @author Administrator
 *
 */
public class Branch implements Serializable {

	private static final long serialVersionUID = 3112296842045809059L;

	private String branchName;
	private int bettingShopNum;
	List<BettingShopInBranch> bettingShopInfos;

	public Branch() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Branch(String branchName, int bettingShopNum,
			List<BettingShopInBranch> bettingShopInfos) {
		super();
		this.branchName = branchName;
		this.bettingShopNum = bettingShopNum;
		this.bettingShopInfos = bettingShopInfos;
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

	public List<BettingShopInBranch> getBettingShopInfos() {
		return bettingShopInfos;
	}

	public void setBettingShopInfos(List<BettingShopInBranch> bettingShopInfos) {
		this.bettingShopInfos = bettingShopInfos;
	}

}
