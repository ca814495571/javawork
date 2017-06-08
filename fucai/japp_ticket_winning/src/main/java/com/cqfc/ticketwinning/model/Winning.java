package com.cqfc.ticketwinning.model;

import java.io.Serializable;

public class Winning implements Serializable {

	private static final long serialVersionUID = 6634822298702065506L;

	/**
	 * 中奖ID（自增）
	 */
	private long id;
	/**
	 * 渠道ID
	 */
	private String partnerId;

	/**
	 * 用户ID
	 */
	private long userId;

	/**
	 * 中奖订单编号
	 */
	private String orderNo;
	/**
	 * 中奖订单类型，1：直投订单，2：追号订单
	 */
	private Integer orderType;
	/**
	 * 中奖订单数据库名
	 */
	private String dbName;
	/**
	 * 中奖订单表名
	 */
	private String tableName;
	/**
	 * 彩种ID
	 */
	private String lotteryId;
	/**
	 * 期号
	 */
	private String issueNo;
	/**
	 * 投注内容
	 */
	private String orderContent;
	/**
	 * 玩法
	 */
	private String playType;
	/**
	 * 中奖金额
	 */
	private long winningAmount;
	/**
	 * 倍数
	 */
	private int multiple;
	/**
	 * 派奖状态
	 */
	private int sendPrizeState;
	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 最后更新时间
	 */
	private String lastUpdateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public String getOrderContent() {
		return orderContent;
	}

	public void setOrderContent(String orderContent) {
		this.orderContent = orderContent;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
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

	public long getWinningAmount() {
		return winningAmount;
	}

	public void setWinningAmount(long winningAmount) {
		this.winningAmount = winningAmount;
	}

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getSendPrizeState() {
		return sendPrizeState;
	}

	public void setSendPrizeState(int sendPrizeState) {
		this.sendPrizeState = sendPrizeState;
	}

	/**
	 * 订单类型：1直投订单 2追号订单
	 * 
	 */
	public enum OrderType {
		DIRECT_ORDER(1, "直投订单"), AFTER_ORDER(2, "追号订单");

		private final Integer value;
		private final String text;

		private OrderType(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static OrderType getEnum(Integer value) {
			if (value != null) {
				OrderType[] values = OrderType.values();
				for (OrderType val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	public enum SendPrizeState {
		NO_SENDPRIZE(1, "未派奖"), HAS_SENDPRIZE(2, "已派奖");

		private final Integer value;
		private final String text;

		private SendPrizeState(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static SendPrizeState getEnum(Integer value) {
			if (value != null) {
				SendPrizeState[] values = SendPrizeState.values();
				for (SendPrizeState val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

}
