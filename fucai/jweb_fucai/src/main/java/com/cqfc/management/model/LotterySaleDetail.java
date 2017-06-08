package com.cqfc.management.model;

import java.io.Serializable;
import java.util.List;

/**
 * 中心销售详情
 * 
 * @author Administrator
 *
 */
public class LotterySaleDetail implements Serializable {

	private static final long serialVersionUID = 5504157531284094875L;

	private String date;

	private List<LotterySale> lotterySales;

	private int userAddNum;

	public LotterySaleDetail() {
		super();
	}

	public LotterySaleDetail(String date, List<LotterySale> lotterySales,
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

	public List<LotterySale> getLotterySales() {
		return lotterySales;
	}

	public void setLotterySales(List<LotterySale> lotterySales) {
		this.lotterySales = lotterySales;
	}

	public int getUserAddNum() {
		return userAddNum;
	}

	public void setUserAddNum(int userAddNum) {
		this.userAddNum = userAddNum;
	}

}
