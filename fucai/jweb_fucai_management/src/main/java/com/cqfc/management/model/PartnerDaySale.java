package com.cqfc.management.model;

import java.io.Serializable;

public class PartnerDaySale implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7928398056616044975L;

	private String partnerId;
	private Short partnerType;
	private String saleTotalMoney;
	private String awardPrizeTotalMoney;
	private String chargeTotalMoney;
	private String withDrawTotalMoney;
	private String countTime;

	
	public PartnerDaySale() {
		super();
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public Short getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(Short partnerType) {
		this.partnerType = partnerType;
	}

	public String getSaleTotalMoney() {
		return saleTotalMoney;
	}

	public void setSaleTotalMoney(String saleTotalMoney) {
		this.saleTotalMoney = saleTotalMoney;
	}

	public String getAwardPrizeTotalMoney() {
		return awardPrizeTotalMoney;
	}

	public void setAwardPrizeTotalMoney(String awardPrizeTotalMoney) {
		this.awardPrizeTotalMoney = awardPrizeTotalMoney;
	}

	public String getChargeTotalMoney() {
		return chargeTotalMoney;
	}

	public void setChargeTotalMoney(String chargeTotalMoney) {
		this.chargeTotalMoney = chargeTotalMoney;
	}

	public String getWithDrawTotalMoney() {
		return withDrawTotalMoney;
	}

	public void setWithDrawTotalMoney(String withDrawTotalMoney) {
		this.withDrawTotalMoney = withDrawTotalMoney;
	}

	public String getCountTime() {
		return countTime;
	}

	public void setCountTime(String countTime) {
		this.countTime = countTime;
	}

	

}
