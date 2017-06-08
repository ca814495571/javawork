package com.cqfc.statistics.model;

import java.math.BigDecimal;

public class StatTpActivitySum {

	/**
	 */

	private long time;
	private long staJoinNum;
	private BigDecimal statRec5ExcNum;
	private BigDecimal statRec10ExcNum;
	private BigDecimal statRecTotalMoney;
	private BigDecimal statSca5ExcNum;
	private BigDecimal statSca10ExcNum;
	private BigDecimal statExcTotalMoney;
	private BigDecimal hand10recNum;
	private BigDecimal hand10recTotalMoney;

	public StatTpActivitySum() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StatTpActivitySum(long time, long staJoinNum,
			BigDecimal statRec5ExcNum, BigDecimal statRec10ExcNum,
			BigDecimal statRecTotalMoney, BigDecimal statSca5ExcNum,
			BigDecimal statSca10ExcNum, BigDecimal statExcTotalMoney,
			BigDecimal hand10recNum, BigDecimal hand10recTotalMoney) {
		super();
		this.time = time;
		this.staJoinNum = staJoinNum;
		this.statRec5ExcNum = statRec5ExcNum;
		this.statRec10ExcNum = statRec10ExcNum;
		this.statRecTotalMoney = statRecTotalMoney;
		this.statSca5ExcNum = statSca5ExcNum;
		this.statSca10ExcNum = statSca10ExcNum;
		this.statExcTotalMoney = statExcTotalMoney;
		this.hand10recNum = hand10recNum;
		this.hand10recTotalMoney = hand10recTotalMoney;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getStaJoinNum() {
		return staJoinNum;
	}

	public void setStaJoinNum(long staJoinNum) {
		this.staJoinNum = staJoinNum;
	}

	public BigDecimal getStatRec5ExcNum() {
		return statRec5ExcNum;
	}

	public void setStatRec5ExcNum(BigDecimal statRec5ExcNum) {
		this.statRec5ExcNum = statRec5ExcNum;
	}

	public BigDecimal getStatRec10ExcNum() {
		return statRec10ExcNum;
	}

	public void setStatRec10ExcNum(BigDecimal statRec10ExcNum) {
		this.statRec10ExcNum = statRec10ExcNum;
	}

	public BigDecimal getStatRecTotalMoney() {
		return statRecTotalMoney;
	}

	public void setStatRecTotalMoney(BigDecimal statRecTotalMoney) {
		this.statRecTotalMoney = statRecTotalMoney;
	}

	public BigDecimal getStatSca5ExcNum() {
		return statSca5ExcNum;
	}

	public void setStatSca5ExcNum(BigDecimal statSca5ExcNum) {
		this.statSca5ExcNum = statSca5ExcNum;
	}

	public BigDecimal getStatSca10ExcNum() {
		return statSca10ExcNum;
	}

	public void setStatSca10ExcNum(BigDecimal statSca10ExcNum) {
		this.statSca10ExcNum = statSca10ExcNum;
	}

	public BigDecimal getStatExcTotalMoney() {
		return statExcTotalMoney;
	}

	public void setStatExcTotalMoney(BigDecimal statExcTotalMoney) {
		this.statExcTotalMoney = statExcTotalMoney;
	}

	public BigDecimal getHand10recNum() {
		return hand10recNum;
	}

	public void setHand10recNum(BigDecimal hand10recNum) {
		this.hand10recNum = hand10recNum;
	}

	public BigDecimal getHand10recTotalMoney() {
		return hand10recTotalMoney;
	}

	public void setHand10recTotalMoney(BigDecimal hand10recTotalMoney) {
		this.hand10recTotalMoney = hand10recTotalMoney;
	}

}
