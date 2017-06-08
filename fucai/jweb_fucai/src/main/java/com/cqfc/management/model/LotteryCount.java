package com.cqfc.management.model;

import java.io.Serializable;

/**
 * 用户数量统计对象
 * 
 * @author chen_an
 *
 */
public class LotteryCount implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -8551933014142532675L;
	/**
	 */
	private String lotteryType; // 彩票种类
	private int lotteryDailyNum; // 彩票日销售量
	private int lotteryMonthNum; // 彩票月销售量 （存在每月1号清零问题，根据原数据待解决）
	private String countTime; // 彩票销售数量统计的时间
	private int year; // 彩票销售年份
	private int month; // 彩票销售月份
	private int stationId; // 关联站点的信息
	private String lastTime; // 最后的修改时间
	private String enterTag;

	public LotteryCount() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LotteryCount(String lotteryType, int lotteryDailyNum,
			int lotteryMonthNum, String countTime, int year, int month,
			int stationId, String lastTime, String enterTag) {
		super();
		this.lotteryType = lotteryType;
		this.lotteryDailyNum = lotteryDailyNum;
		this.lotteryMonthNum = lotteryMonthNum;
		this.countTime = countTime;
		this.year = year;
		this.month = month;
		this.stationId = stationId;
		this.lastTime = lastTime;
		this.enterTag = enterTag;
	}

	public String getEnterTag() {
		return enterTag;
	}

	public void setEnterTag(String enterTag) {
		this.enterTag = enterTag;
	}

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}

	public int getLotteryDailyNum() {
		return lotteryDailyNum;
	}

	public void setLotteryDailyNum(int lotteryDailyNum) {
		this.lotteryDailyNum = lotteryDailyNum;
	}

	public int getLotteryMonthNum() {
		return lotteryMonthNum;
	}

	public void setLotteryMonthNum(int lotteryMonthNum) {
		this.lotteryMonthNum = lotteryMonthNum;
	}

	public String getCountTime() {
		return countTime;
	}

	public void setCountTime(String countTime) {
		this.countTime = countTime;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

}
