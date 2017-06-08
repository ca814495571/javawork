package com.cqfc.order.model;

import java.io.Serializable;

/**
 * @author liwh
 */
public class UpdateSyncStatus implements Serializable {

	private static final long serialVersionUID = 3157685985268008728L;

	private long orderNo;

	private int sync;

	private String tradeId;

	private Boolean isBackReq;

	private int operateIdentifier;

	public int getSync() {
		return sync;
	}

	public void setSync(int sync) {
		this.sync = sync;
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
