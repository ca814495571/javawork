package com.cqfc.ticketwinning.model;

import java.io.Serializable;

public class WinningOrder implements Serializable {

	private static final long serialVersionUID = 5942126807868718893L;

	/**
	 * 彩种ID
	 */
	private String lotteryId;

	/**
	 * 期号
	 */
	private String issueNo;
	/**
	 * 渠道ID
	 */
	private String partnerId;
	/**
	 * 用户ID
	 */
	private long userId;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 订单类型：1直投订单 2追号订单
	 */
	private String orderType;
	/**
	 * 订单状态：1待付款 2已付款 3出票中 4已出票待开奖 5出票失败 6未中奖 7待领奖 8已领奖 9退款中 10退款成功 11订单取消
	 */
	private int orderStatus;
	/**
	 * 投注内容
	 */
	private String orderContent;

	/**
	 * 倍数
	 */
	private int multiple;

	/**
	 * 玩法
	 */
	private String playType;

	/**
	 * 是否中奖
	 */
	private boolean isPrize;

	/**
	 * 中奖金额
	 */
	private long winningAmount;
	
	/**
	 * 扩展信息
	 */
	private String ext;
	
	

	/**
	 * 订单状态枚举
	 * 
	 */
	public enum OrderStatus {
		WAIT_PAYMENT(1, "待付款"), HAS_PAYMENT(2, "已付款"), IN_TICKET(3, "出票中"), HASTICKET_WAITLOTTERY(
				4, "已出票待开奖"), DRAWER_FAILURE(5, "出票失败"), NOT_WINNING(6, "未中奖"), WAIT_AWARD(
				7, "待领奖"), HAS_PRIZE(8, "已领奖"), REFUNDING(9, "退款中"), REFUND_SUCCESS(
				10, "退款成功"), ORDER_CANCEL(11, "订单取消"), ORDER_CALCULATINGWINNING(
				12, "算奖中"), HASSEND_PRIZE(13, "已派奖");

		private final Integer value;
		private final String text;

		private OrderStatus(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static OrderStatus getEnum(Integer value) {
			if (value != null) {
				OrderStatus[] values = OrderStatus.values();
				for (OrderStatus val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	public String getLotteryId() {
		return lotteryId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderContent() {
		return orderContent;
	}

	public void setOrderContent(String orderContent) {
		this.orderContent = orderContent;
	}

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public long getWinningAmount() {
		return winningAmount;
	}

	public void setWinningAmount(long winningAmount) {
		this.winningAmount = winningAmount;
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

	public boolean isPrize() {
		return isPrize;
	}

	public void setPrize(boolean isPrize) {
		this.isPrize = isPrize;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

}
