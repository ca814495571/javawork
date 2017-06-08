package com.cqfc.management.model;

import java.io.Serializable;

/**
 * 用户数量统计对象
 * 
 * @author chen_an
 *
 */
public class UserCount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6729828770474263117L;

	private int userTotalNum;
	private int userDailyAddNum;
	private String countTime;
	private int year;
	private int month;
	private int stationId;
	private String lastTime;
	private String enterTag ;

	public UserCount() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public UserCount(int userTotalNum, int userDailyAddNum, String countTime,
			int year, int month, int stationId, String lastTime, String enterTag) {
		super();
		this.userTotalNum = userTotalNum;
		this.userDailyAddNum = userDailyAddNum;
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


	public int getUserTotalNum() {
		return userTotalNum;
	}

	public void setUserTotalNum(int userTotalNum) {
		this.userTotalNum = userTotalNum;
	}

	public int getUserDailyAddNum() {
		return userDailyAddNum;
	}

	public void setUserDailyAddNum(int userDailyAddNum) {
		this.userDailyAddNum = userDailyAddNum;
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
