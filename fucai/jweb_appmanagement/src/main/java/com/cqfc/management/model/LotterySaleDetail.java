package com.cqfc.management.model;

import java.io.Serializable;

/**
 * 站点各种彩票的某年某月的销售量
 * 
 * @author Administrator
 *
 */
public class LotterySaleDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6845725451355426686L;

	private int day;

	private int saleNum;

	private String lotteryType;

	public LotterySaleDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LotterySaleDetail(int day, int saleNum, String lotteryType) {
		super();
		this.day = day;
		this.saleNum = saleNum;
		this.lotteryType = lotteryType;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}

}
