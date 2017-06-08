package com.cqfc.order.model;

import java.io.Serializable;

/**
 * @author liwh
 */
public class Order implements Serializable {

	private static final long serialVersionUID = 5942126807868718893L;

	/**
	 * 彩种ID
	 */
	private String lotteryId;

	/**
	 * 渠道ID
	 */
	private String partnerId;

	/**
	 * 用户ID
	 */
	private long userId;

	/**
	 * 期号
	 */
	private String issueNo;

	/**
	 * 订单编号
	 */
	private long orderNo;

	/**
	 * 订单类型：1直投订单 2追号订单
	 */
	private int orderType;

	/**
	 * 订单状态：1待付款 2已付款 3出票中 4已出票待开奖 5出票失败 6未中奖 7待领奖 8已领奖 9退款中 10退款成功 11订单取消
	 */
	private int orderStatus;

	/**
	 * 投注总金额
	 */
	private long totalAmount;

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
	 * 支付流水号
	 */
	private String paySerialNumber;

	/**
	 * 是否同步到全局数据库 0未同步 1同步成功 2同步失败
	 */
	private int isSyncSuccess;

	/**
	 * 合作商交易ID
	 */
	private String tradeId;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 身份证号
	 */
	private String cardNo;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 异常状态码
	 */
	private String errCodeStatus;

	/**
	 * 异常备注
	 */
	private String errCodeRemark;

	/**
	 * 拆票注数
	 */
	private int ticketNum;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 最后更新时间
	 */
	private String lastUpdateTime;

	/**
	 * 订单状态枚举
	 * 
	 */
	public enum OrderStatus {
		WAIT_PAYMENT(1, "待付款"), HAS_PAYMENT(2, "已付款"), IN_TICKET(3, "出票中"), HASTICKET_WAITLOTTERY(4, "已出票待开奖"), DRAWER_FAILURE(
				5, "出票失败"), NOT_WINNING(6, "未中奖"), WAIT_AWARD(7, "待领奖"), HAS_PRIZE(8, "已领奖"), REFUNDING(9, "退款中"), REFUND_SUCCESS(
				10, "退款成功"), ORDER_CANCEL(11, "订单取消");

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

	/**
	 * 是否同步到全局数据库 0未同步 1同步成功 2同步失败
	 * 
	 */
	public enum OrderSync {
		NOT_SYNC(0, "未同步"), SYNC_SUCCESS(1, "同步成功"), SYNC_FAILURE(2, "同步失败");

		private final Integer value;
		private final String text;

		private OrderSync(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static OrderSync getEnum(Integer value) {
			if (value != null) {
				OrderSync[] values = OrderSync.values();
				for (OrderSync val : values) {
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

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
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

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
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

	public String getPaySerialNumber() {
		return paySerialNumber;
	}

	public void setPaySerialNumber(String paySerialNumber) {
		this.paySerialNumber = paySerialNumber;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public int getIsSyncSuccess() {
		return isSyncSuccess;
	}

	public void setIsSyncSuccess(int isSyncSuccess) {
		this.isSyncSuccess = isSyncSuccess;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getErrCodeStatus() {
		return errCodeStatus;
	}

	public void setErrCodeStatus(String errCodeStatus) {
		this.errCodeStatus = errCodeStatus;
	}

	public String getErrCodeRemark() {
		return errCodeRemark;
	}

	public void setErrCodeRemark(String errCodeRemark) {
		this.errCodeRemark = errCodeRemark;
	}

	public int getTicketNum() {
		return ticketNum;
	}

	public void setTicketNum(int ticketNum) {
		this.ticketNum = ticketNum;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

}
