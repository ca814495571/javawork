package com.cqfc.statistics.model;

import java.math.BigDecimal;
import java.util.Date;

public class StatDeal {

	long time; // 日期:20141012
	String buyTime;//下单时间
	String nick;//客户名称
	String lottery;//彩种中文名称
	Long totalAmount;//订单总额
	String userId;//用户Id
	Long dealId;//订单Id
	int business_type;//投注站业务类型
	String bet_station; // 投注站编码
	public StatDeal() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StatDeal(long time, String buyTime, String nick, String lottery,
			Long totalAmount, String userId, Long dealId, int business_type,
			String bet_station) {
		super();
		this.time = time;
		this.buyTime = buyTime;
		this.nick = nick;
		this.lottery = lottery;
		this.totalAmount = totalAmount;
		this.userId = userId;
		this.dealId = dealId;
		this.business_type = business_type;
		this.bet_station = bet_station;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getLottery() {
		return lottery;
	}
	public void setLottery(String lottery) {
		this.lottery = lottery;
	}
	public Long getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getDealId() {
		return dealId;
	}
	public void setDealId(Long dealId) {
		this.dealId = dealId;
	}
	public int getBusiness_type() {
		return business_type;
	}
	public void setBusiness_type(int business_type) {
		this.business_type = business_type;
	}
	public String getBet_station() {
		return bet_station;
	}
	public void setBet_station(String bet_station) {
		this.bet_station = bet_station;
	}

	
	
}
