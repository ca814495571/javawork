package com.cqfc.management.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class UserWithDrawCheck implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1577283229948041343L;
	
	private Integer applyId;
	private String userId;
//	private String nickName;
	private String withDrawMoney;
	private String totalMoney;
	private String phone;
	private Integer recieveMode; //帐号类型：1:银行卡 2:cft 3:支付宝
	private String realName;
	private String bankName;
	private String accountNo;
	private String createTime;
	private String partnerId;
	private String partnerApplyId;
	private String fromTime;
	private String toTime;
	private Integer status;

	public UserWithDrawCheck() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserWithDrawCheck(Integer applyId, String userId,
			String withDrawMoney, String totalMoney, String phone,
			Integer recieveMode, String realName, String bankName,
			String accountNo, String createTime, String fromTime,
			String toTime, Integer status) {
		super();
		this.applyId = applyId;
		this.userId = userId;
		this.withDrawMoney = withDrawMoney;
		this.totalMoney = totalMoney;
		this.phone = phone;
		this.recieveMode = recieveMode;
		this.realName = realName;
		this.bankName = bankName;
		this.accountNo = accountNo;
		this.createTime = createTime;
		this.fromTime = fromTime;
		this.toTime = toTime;
		this.status = status;
	}

	public Integer getApplyId() {
		return applyId;
	}

	public void setApplyId(Integer applyId) {
		this.applyId = applyId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getWithDrawMoney() {
		return withDrawMoney;
	}

	public void setWithDrawMoney(String withDrawMoney) {
		this.withDrawMoney = withDrawMoney;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getRecieveMode() {
		return recieveMode;
	}

	public void setRecieveMode(Integer recieveMode) {
		this.recieveMode = recieveMode;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}


	public String getPartnerApplyId() {
		return partnerApplyId;
	}

	public void setPartnerApplyId(String partnerApplyId) {
		this.partnerApplyId = partnerApplyId;
	}

	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		
		if(StringUtils.isNotBlank(this.userId)){
			sb.append(" 用户Id: ");
			sb.append(this.userId);
		}
		if(StringUtils.isNotBlank(this.partnerApplyId)){
			
			sb.append(" 流水号: ");
			sb.append(this.partnerApplyId);
		}
		return sb.toString();
	}
	
}
