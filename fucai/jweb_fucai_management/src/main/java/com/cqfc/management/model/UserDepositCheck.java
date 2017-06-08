package com.cqfc.management.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class UserDepositCheck implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5381515529044964021L;
	/**
	 * 
	 */
	private Integer id;
	private String partnerId;
	private String partnerName;
	private String partnerUniqueNo;
	private String userId;
	private String userName;
	private String createTime;
	private String phone;
	private Integer status;
	private String money;
	public UserDepositCheck() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserDepositCheck(Integer id, String partnerId, String partnerName,
			String partnerUniqueNo, String userId, String userName,
			String createTime, String phone, Integer status, String money) {
		super();
		this.id = id;
		this.partnerId = partnerId;
		this.partnerName = partnerName;
		this.partnerUniqueNo = partnerUniqueNo;
		this.userId = userId;
		this.userName = userName;
		this.createTime = createTime;
		this.phone = phone;
		this.status = status;
		this.money = money;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public String getPartnerUniqueNo() {
		return partnerUniqueNo;
	}
	public void setPartnerUniqueNo(String partnerUniqueNo) {
		this.partnerUniqueNo = partnerUniqueNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		
		if(StringUtils.isNotBlank(this.userId)){
			sb.append(" 用户Id: ");
			sb.append(this.userId);
		}
		if(StringUtils.isNotBlank(this.partnerUniqueNo)){
			
			sb.append(" 渠道订单唯一号: ");
			sb.append(this.partnerUniqueNo);
		}
		return sb.toString();
	}


	
}
