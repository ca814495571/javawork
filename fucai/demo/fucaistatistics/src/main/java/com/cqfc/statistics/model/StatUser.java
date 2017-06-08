package com.cqfc.statistics.model;

public class StatUser {

	
	private String bet_station;
	
	private String nick;
	
	private String bind_time;
	
	private int business_type;
	
	private String user_id;

	public StatUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StatUser(String bet_station, String nick, String bind_time,
			int business_type, String user_id) {
		super();
		this.bet_station = bet_station;
		this.nick = nick;
		this.bind_time = bind_time;
		this.business_type = business_type;
		this.user_id = user_id;
	}

	public String getBet_station() {
		return bet_station;
	}

	public void setBet_station(String bet_station) {
		this.bet_station = bet_station;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getBind_time() {
		return bind_time;
	}

	public void setBind_time(String bind_time) {
		this.bind_time = bind_time;
	}

	public int getBusiness_type() {
		return business_type;
	}

	public void setBusiness_type(int business_type) {
		this.business_type = business_type;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	
	
}
