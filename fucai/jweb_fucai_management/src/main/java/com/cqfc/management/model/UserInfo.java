package com.cqfc.management.model;

import java.io.Serializable;

public class UserInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1544570602772164291L;
	// 昵称、帐号、账户总金额、绑定手机号码、身份证号码、注册时间、渠道号、合作商名称
	private String realName;
	private Long userId;
	private String balance;
	private String phone;
	private String cardNo;
	private String partnerId;
	private String partenrName;
	private String createTime;

	public UserInfo() {
		super();
	}

	public UserInfo(String realName, Long userId, String balance, String phone,
			String cardNo, String partnerId, String partenrName,
			String createTime) {
		super();
		this.realName = realName;
		this.userId = userId;
		this.balance = balance;
		this.phone = phone;
		this.cardNo = cardNo;
		this.partnerId = partnerId;
		this.partenrName = partenrName;
		this.createTime = createTime;
	}

	public String getrealName() {
		return realName;
	}

	public void setrealName(String realName) {
		this.realName = realName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getPartenrName() {
		return partenrName;
	}

	public void setPartenrName(String partenrName) {
		this.partenrName = partenrName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}



}
