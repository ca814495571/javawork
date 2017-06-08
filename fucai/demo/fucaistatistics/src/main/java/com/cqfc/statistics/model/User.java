package com.cqfc.statistics.model;

/**
 * @author: giantspider@126.com
 */
public class User {
	String content; // 内容：绑定时间##客户名称||绑定时间##客户名称
	String bet_station; // 投注站编码
	String parentCode;
	String stationName;
	String address;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String content, String bet_station, String parentCode,
			String stationName, String address) {
		super();
		this.content = content;
		this.bet_station = bet_station;
		this.parentCode = parentCode;
		this.stationName = stationName;
		this.address = address;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBet_station() {
		return bet_station;
	}

	public void setBet_station(String bet_station) {
		this.bet_station = bet_station;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "User{" + "content='" + content + '\'' + ", bet_station='"
				+ bet_station + '\'' + '}';
	}
}
