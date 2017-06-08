package com.cqfc.statistics.model;

/**
 * @author: giantspider@126.com
 */
public class Deal {
	long time; // 日期:20141012
	String content; // 内容：内容：下单时间##客户名称##彩种##注数##userId##订单id
	String bet_station; // 投注站编码
	String parentCode;
	String stationName;
	String address;

	public Deal() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Deal(long time, String content, String bet_station,
			String parentCode, String stationName, String address) {
		super();
		this.time = time;
		this.content = content;
		this.bet_station = bet_station;
		this.parentCode = parentCode;
		this.stationName = stationName;
		this.address = address;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
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
		return "Deal{" + "time=" + time + ", content='" + content + '\''
				+ ", bet_station='" + bet_station + '\'' + '}';
	}
}
