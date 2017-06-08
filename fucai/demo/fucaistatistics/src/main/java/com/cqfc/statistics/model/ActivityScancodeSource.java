package com.cqfc.statistics.model;

public class ActivityScancodeSource {
	long time;
	Long scancodeEntranceNumber;
	String betStationName;
	String betStationAddress;
	String betStation;
	int ifJoin;
	int station_type;
	int business_type;
	public ActivityScancodeSource() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ActivityScancodeSource(long time, Long scancodeEntranceNumber,
			String betStationName, String betStationAddress, String betStation,
			int ifJoin, int station_type, int business_type) {
		super();
		this.time = time;
		this.scancodeEntranceNumber = scancodeEntranceNumber;
		this.betStationName = betStationName;
		this.betStationAddress = betStationAddress;
		this.betStation = betStation;
		this.ifJoin = ifJoin;
		this.station_type = station_type;
		this.business_type = business_type;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public Long getScancodeEntranceNumber() {
		return scancodeEntranceNumber;
	}
	public void setScancodeEntranceNumber(Long scancodeEntranceNumber) {
		this.scancodeEntranceNumber = scancodeEntranceNumber;
	}
	public String getBetStationName() {
		return betStationName;
	}
	public void setBetStationName(String betStationName) {
		this.betStationName = betStationName;
	}
	public String getBetStationAddress() {
		return betStationAddress;
	}
	public void setBetStationAddress(String betStationAddress) {
		this.betStationAddress = betStationAddress;
	}
	public String getBetStation() {
		return betStation;
	}
	public void setBetStation(String betStation) {
		this.betStation = betStation;
	}
	public int getIfJoin() {
		return ifJoin;
	}
	public void setIfJoin(int ifJoin) {
		this.ifJoin = ifJoin;
	}
	public int getStation_type() {
		return station_type;
	}
	public void setStation_type(int station_type) {
		this.station_type = station_type;
	}
	public int getBusiness_type() {
		return business_type;
	}
	public void setBusiness_type(int business_type) {
		this.business_type = business_type;
	}
	

}
