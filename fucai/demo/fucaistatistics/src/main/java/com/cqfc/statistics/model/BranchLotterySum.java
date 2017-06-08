package com.cqfc.statistics.model;

import java.math.BigDecimal;

/**
 * User: giantspider@126.com
 * Date: 14-11-29 下午10:48
 */
public class BranchLotterySum {
    long time;           // 日期:20141012
    String lottery;      // 彩种
    BigDecimal pay_money;     // 截止到当天销售额
    BigDecimal pay_cnt;       // 截止到当天订单数
    String bet_station; // 分中心编码
    String branchName;

    public BranchLotterySum(long time, String lottery, BigDecimal pay_money,
			BigDecimal pay_cnt, String bet_station, String branchName) {
		super();
		this.time = time;
		this.lottery = lottery;
		this.pay_money = pay_money;
		this.pay_cnt = pay_cnt;
		this.bet_station = bet_station;
		this.branchName = branchName;
	}

    
    public long getTime() {
		return time;
	}


	public void setTime(long time) {
		this.time = time;
	}


	public String getLottery() {
		return lottery;
	}


	public void setLottery(String lottery) {
		this.lottery = lottery;
	}


	public BigDecimal getPay_money() {
		return pay_money;
	}


	public void setPay_money(BigDecimal pay_money) {
		this.pay_money = pay_money;
	}


	public BigDecimal getPay_cnt() {
		return pay_cnt;
	}


	public void setPay_cnt(BigDecimal pay_cnt) {
		this.pay_cnt = pay_cnt;
	}


	public String getBet_station() {
		return bet_station;
	}


	public void setBet_station(String bet_station) {
		this.bet_station = bet_station;
	}


	public String getBranchName() {
		return branchName;
	}


	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}


	@Override
    public String toString() {
        return "BranchLotterySum{" +
                "time=" + time +
                ", lottery='" + lottery + '\'' +
                ", pay_money=" + pay_money +
                ", pay_cnt=" + pay_cnt +
                ", bet_station='" + bet_station + '\'' +
                '}';
    }
}
