package com.cqfc.order.model;

import java.io.Serializable;

/**
 * @author liwh
 */
public class UpdateOrderStatus implements Serializable {

	private static final long serialVersionUID = -7946837578036768837L;

	private long orderNo;

	private int orderStatus;

	private String tradeId;

	private Boolean isBackReq;

	private int operateIdentifier;

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public Boolean getIsBackReq() {
		return isBackReq;
	}

	public void setIsBackReq(Boolean isBackReq) {
		this.isBackReq = isBackReq;
	}

	public int getOperateIdentifier() {
		return operateIdentifier;
	}

	public void setOperateIdentifier(int operateIdentifier) {
		this.operateIdentifier = operateIdentifier;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

}
