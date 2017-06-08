package com.cqfc.ticketissue.model;

public class TempOrder {
	private String id;
	private long multiple;
	private long playType;
	private long money;
	private String ball;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getMultiple() {
		return multiple;
	}

	public void setMultiple(long multiple) {
		this.multiple = multiple;
	}

	public long getPlayType() {
		return playType;
	}

	public void setPlayType(long playType) {
		this.playType = playType;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public String getBall() {
		return ball;
	}

	public void setBall(String ball) {
		this.ball = ball;
	}

	@Override
	public String toString() {
		return "TempOrder [id=" + id
				+ ", multiple=" + multiple + ", playType=" + playType
				+ ", money=" + money + ", ball=" + ball + "]";
	}
	
	
}
