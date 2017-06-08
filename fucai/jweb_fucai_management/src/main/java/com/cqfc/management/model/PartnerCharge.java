package com.cqfc.management.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class PartnerCharge implements Serializable{

	private static final long serialVersionUID = -1871169144624486429L;
	public String partnerId;
	public Double rechargeAmount;
	public String remark;

	public PartnerCharge() {
		super();
	}

	public PartnerCharge(String partnerId, Double rechargeAmount, String remark) {
		super();
		this.partnerId = partnerId;
		this.rechargeAmount = rechargeAmount;
		this.remark = remark;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public Double getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(Double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		if(StringUtils.isNotBlank(this.partnerId)){
			
			sb.append(" 合作商Id： ");
			sb.append(this.partnerId);
		}
		
		if(this.rechargeAmount >0){
			
			sb.append(" 充值金额： ");
			sb.append(this.rechargeAmount);
		}
		
		return sb.toString();
	}

	
	
	
}
