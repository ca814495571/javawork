package com.cqfc.statistics.model;

public class StatTpActivityInfo {

	
	private String nick ;
	private String userId;
	private String voteTime;
	private String betStation;
	private String openId;
	private int handselCharge ;//是否赠送彩金
	private int verify;//是否兑换刮刮乐
	public StatTpActivityInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StatTpActivityInfo(String nick, String userId, String voteTime,
			String betStation, String openId, int handselCharge, int verify) {
		super();
		this.nick = nick;
		this.userId = userId;
		this.voteTime = voteTime;
		this.betStation = betStation;
		this.openId = openId;
		this.handselCharge = handselCharge;
		this.verify = verify;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getVoteTime() {
		return voteTime;
	}
	public void setVoteTime(String voteTime) {
		this.voteTime = voteTime;
	}
	public String getBetStation() {
		return betStation;
	}
	public void setBetStation(String betStation) {
		this.betStation = betStation;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public int getHandselCharge() {
		return handselCharge;
	}
	public void setHandselCharge(int handselCharge) {
		this.handselCharge = handselCharge;
	}
	public int getVerify() {
		return verify;
	}
	public void setVerify(int verify) {
		this.verify = verify;
	}

	
	
}
