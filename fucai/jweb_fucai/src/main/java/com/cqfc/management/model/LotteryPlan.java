package com.cqfc.management.model;

import java.io.Serializable;

/**
 * FTP 上的彩票订单方案
 * 
 * @author Administrator
 *
 */
public class LotteryPlan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5171124145994220419L;

	private int id;// 主键

	private String userId; // 用户Id

	private String planId;// 订单Id

	private String lotteryId; // 彩票Id

	private String lotteryName; // 彩票名称

	private String totalAmount; // 购买总金额

	private String createTime; // 发起时间

	private String charOne; // 入口标识

	private String charTwo; // 用户来源属性tag

	private String extInfo; // 扩展信息

	public LotteryPlan() {
		super();
	}

	public LotteryPlan(int id, String userId, String planId, String lotteryId,
			String lotteryName, String totalAmount, String createTime,
			String charOne, String charTwo, String extInfo) {
		super();
		this.id = id;
		this.userId = userId;
		this.planId = planId;
		this.lotteryId = lotteryId;
		this.lotteryName = lotteryName;
		this.totalAmount = totalAmount;
		this.createTime = createTime;
		this.charOne = charOne;
		this.charTwo = charTwo;
		this.extInfo = extInfo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCharOne() {
		return charOne;
	}

	public void setCharOne(String charOne) {
		this.charOne = charOne;
	}

	public String getCharTwo() {
		return charTwo;
	}

	public void setCharTwo(String charTwo) {
		this.charTwo = charTwo;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

}
