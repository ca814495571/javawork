package com.cqfc.management.model;

import java.io.Serializable;
import java.util.List;
/**
 * 中心管理设置
 * @author Administrator
 *
 */
public class Center implements Serializable {

	private static final long serialVersionUID = -5666952148210360469L;

	private int allBranchNum;
	private int allbettingShopNum;
	private List<BranchInCenter> branchInfos ;
	public Center() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Center(int allBranchNum, int allbettingShopNum,
			List<BranchInCenter> branchInfos) {
		super();
		this.allBranchNum = allBranchNum;
		this.allbettingShopNum = allbettingShopNum;
		this.branchInfos = branchInfos;
	}
	public int getAllBranchNum() {
		return allBranchNum;
	}
	public void setAllBranchNum(int allBranchNum) {
		this.allBranchNum = allBranchNum;
	}
	public int getAllbettingShopNum() {
		return allbettingShopNum;
	}
	public void setAllbettingShopNum(int allbettingShopNum) {
		this.allbettingShopNum = allbettingShopNum;
	}
	public List<BranchInCenter> getBranchInfos() {
		return branchInfos;
	}
	public void setBranchInfos(List<BranchInCenter> branchInfos) {
		this.branchInfos = branchInfos;
	}
	
	

}
