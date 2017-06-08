package com.cqfc.management.model;

import java.io.Serializable;
import java.util.List;

/**
 * 中心销售详情
 * 
 * @author Administrator
 *
 */
public class LotterySale implements Serializable {

	private static final long serialVersionUID = 5504157531284094875L;

	private String date;

	private List<LotterySaleDetail> lotterySales;

	private int userAddNum;

	public LotterySale() {
		super();
	}

	public LotterySale(String date, List<LotterySaleDetail> lotterySales,
			int userAddNum) {
		super();
		this.date = date;
		this.lotterySales = lotterySales;
		this.userAddNum = userAddNum;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<LotterySaleDetail> getLotterySales() {
		return lotterySales;
	}

	public void setLotterySales(List<LotterySaleDetail> lotterySales) {
		this.lotterySales = lotterySales;
	}

	public int getUserAddNum() {
		return userAddNum;
	}

	public void setUserAddNum(int userAddNum) {
		this.userAddNum = userAddNum;
	}

}
