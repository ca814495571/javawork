package com.cqfc.prize.model;

import java.io.Serializable;

public class Prize implements Serializable{

	private static final long serialVersionUID = 4653175306502144484L;
	private String userId ; 
	private String ticketId;
	private int status;
	private long winAmount;
	private int failTimes ; 
	private String gameCode;
	private String gameSerial;
	private String packageNum;
	private String ticketNum;
	private String message;
	private String createTime;
	private String lastUpdateTime;
	
	public Prize(String userId,String ticketId,int status,long winAmount,
			int failTimes,String gameCode,String gameSerial,
			String packageNum,String ticketNum,String message,
			String createTime,String lastUpdateTime) {
		super();
		this.userId = userId;
		this.ticketId = ticketId;
		this.status = status;
		this.winAmount = winAmount;
		this.failTimes = failTimes;
		this.gameCode = gameCode;
		this.gameSerial = gameSerial;
		this.packageNum = packageNum;
		this.ticketNum = ticketNum;
		this.message = message;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
	}
	public Prize() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTicketId() {
		return ticketId;
	}
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getWinAmount() {
		return winAmount;
	}
	public void setWinAmount(long winAmount) {
		this.winAmount = winAmount;
	}
	public int getFailTimes() {
		return failTimes;
	}
	public void setFailTimes(int failTimes) {
		this.failTimes = failTimes;
	}
	public String getGameCode() {
		return gameCode;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public String getGameSerial() {
		return gameSerial;
	}
	public void setGameSerial(String gameSerial) {
		this.gameSerial = gameSerial;
	}
	public String getPackageNum() {
		return packageNum;
	}
	public void setPackageNum(String packageNum) {
		this.packageNum = packageNum;
	}
	public String getTicketNum() {
		return ticketNum;
	}
	public void setTicketNum(String ticketNum) {
		this.ticketNum = ticketNum;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	@Override
	public String toString() {
		return 
				(userId != null ? "userId=" + userId + "," : "")
				+ (ticketId != null ? "ticketId=" + ticketId + "," : "")
				+ "status="
				+ status
				+ ",winAmount="
				+ winAmount
				+ ",failTimes="
				+ failTimes
				+ ","
				+ (gameCode != null ? "gameCode=" + gameCode + "," : "")
				+ (gameSerial != null ? "gameSerial=" + gameSerial + "," : "")
				+ (packageNum != null ? "packageNum=" + packageNum + "," : "")
				+ (ticketNum != null ? "ticketNum=" + ticketNum + "," : "")
				+ (message != null ? "message=" + message + "," : "")
				+ (createTime != null ? "createTime=" + createTime + "," : "")
				+ (lastUpdateTime != null ? "lastUpdateTime=" + lastUpdateTime
						: "");
	}
	
	
}
