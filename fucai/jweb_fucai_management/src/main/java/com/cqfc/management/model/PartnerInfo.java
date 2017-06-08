package com.cqfc.management.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.cqfc.util.PartnerConstant;

public class PartnerInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5059473218401036291L;

	// id、名称、类型、状态、可用金额、冻结金额、创建时间

	private String partnerId; // 合作商id
	private String partnerName;// 合作商名称
	private Short partnerType; // 合作商类型 1纯B2B，2平台管理用户帐号，3平台管理用户帐号、用户账户
	private String secretKey; // 密钥文件名称
	private String publicKey; // 公钥文件名称
	private String aliasKey; // 私钥别名
	private String keyStore; // 私钥密码
	private String callbackUrl; // 回调url
	private String userId; // 默认0
	private String ipAddress; // ip地址
	private String creditLimit;// 信用额度
	private String alarmValue;// 告警值
	private String usableMoney; // 可用余额
	private String freezeMoney;// 冻结金额
	private String desc; // 备注
	private Short partnerState; // 合作商状态 1 正常 2锁定
	private Integer accountState; // 账户状态 1 正常 2锁定
	private String createTime;// 创建时间
	private String lastUpdateTime; // 最后修改时间

	public PartnerInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PartnerInfo(String partnerId, String partnerName, Short partnerType,
			String secretKey, String publicKey, String aliasKey,
			String keyStore, String callbackUrl, String userId,
			String ipAddress, String creditLimit, String alarmValue,
			String usableMoney, String freezeMoney, String desc,
			Short partnerState, Integer accountState, String createTime,
			String lastUpdateTime) {
		super();
		this.partnerId = partnerId;
		this.partnerName = partnerName;
		this.partnerType = partnerType;
		this.secretKey = secretKey;
		this.publicKey = publicKey;
		this.aliasKey = aliasKey;
		this.keyStore = keyStore;
		this.callbackUrl = callbackUrl;
		this.userId = userId;
		this.ipAddress = ipAddress;
		this.creditLimit = creditLimit;
		this.alarmValue = alarmValue;
		this.usableMoney = usableMoney;
		this.freezeMoney = freezeMoney;
		this.desc = desc;
		this.partnerState = partnerState;
		this.accountState = accountState;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
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

	public Short getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(Short partnerType) {
		this.partnerType = partnerType;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}

	public String getAlarmValue() {
		return alarmValue;
	}

	public void setAlarmValue(String alarmValue) {
		this.alarmValue = alarmValue;
	}

	public String getUsableMoney() {
		return usableMoney;
	}

	public void setUsableMoney(String usableMoney) {
		this.usableMoney = usableMoney;
	}

	public String getFreezeMoney() {
		return freezeMoney;
	}

	public void setFreezeMoney(String freezeMoney) {
		this.freezeMoney = freezeMoney;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Short getPartnerState() {
		return partnerState;
	}

	public void setPartnerState(Short partnerState) {
		this.partnerState = partnerState;
	}

	public Integer getAccountState() {
		return accountState;
	}

	public void setAccountState(Integer accountState) {
		this.accountState = accountState;
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

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getAliasKey() {
		return aliasKey;
	}

	public void setAliasKey(String aliasKey) {
		this.aliasKey = aliasKey;
	}

	public String getKeyStore() {
		return keyStore;
	}

	public void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();

		if (StringUtils.isNotBlank(this.partnerId)) {
			sb.append(" 合作商Id: ");
			sb.append(this.partnerId);
		}

		if (StringUtils.isNotBlank(this.partnerName)) {
			sb.append(" 合作商名称: ");
			sb.append(this.partnerName);
		}
		if (this.partnerState != null) {
			sb.append(" 合作商原状态: ");
			int lock = PartnerConstant.PartnerState.LOCK.getValue();
			int normal = PartnerConstant.PartnerState.NORMAL.getValue();
			if (this.partnerState == lock) {
				sb.append(PartnerConstant.PartnerState.LOCK.getText());
			} else if (this.partnerState == normal) {
				sb.append(PartnerConstant.PartnerState.NORMAL.getText());
			} else {
				sb.append(this.partnerState);
			}
		}

		return sb.toString();
	}

}
