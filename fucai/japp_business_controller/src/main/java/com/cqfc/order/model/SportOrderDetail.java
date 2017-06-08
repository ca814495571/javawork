package com.cqfc.order.model;

import java.io.Serializable;

/**
 * 竞技彩订单明细
 * 
 * @author liwh
 * 
 */
public class SportOrderDetail implements Serializable {

	private static final long serialVersionUID = -3044924226085120111L;

	/**
	 * 订单编号
	 */
	private long orderNo;

	/**
	 * 赛事ID
	 */
	private String matchId;

	/**
	 * 赛事转换ID
	 */
	private String transferId;

	/**
	 * 赛事编码
	 */
	private String matchNo;

	/**
	 * 让球
	 */
	private String rq;

	/**
	 * 投注内容
	 */
	private String orderContent;

	/**
	 * 赔率
	 */
	private String sp;

	/**
	 * 赛事状态：1正常 2已取消
	 */
	private int matchStatus;

	/**
	 * 赛事状态
	 */
	private String createTime;

	/**
	 * 最后更新时间
	 */
	private String lastUpdateTime;

	/**
	 * 赛事状态枚举
	 * 
	 */
	public enum MatchStatus {
		MATCH_NORMAL(1, "赛事正常"), MATCH_CANCEL(2, "赛事取消");

		private final Integer value;
		private final String text;

		private MatchStatus(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static MatchStatus getEnum(Integer value) {
			if (value != null) {
				MatchStatus[] values = MatchStatus.values();
				for (MatchStatus val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getMatchNo() {
		return matchNo;
	}

	public void setMatchNo(String matchNo) {
		this.matchNo = matchNo;
	}

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	public String getOrderContent() {
		return orderContent;
	}

	public void setOrderContent(String orderContent) {
		this.orderContent = orderContent;
	}

	public String getSp() {
		return sp;
	}

	public void setSp(String sp) {
		this.sp = sp;
	}

	public int getMatchStatus() {
		return matchStatus;
	}

	public void setMatchStatus(int matchStatus) {
		this.matchStatus = matchStatus;
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

}
