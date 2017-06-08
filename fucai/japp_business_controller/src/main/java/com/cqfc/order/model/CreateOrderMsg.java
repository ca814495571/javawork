package com.cqfc.order.model;

import java.io.Serializable;

/**
 * @author liwh
 */
public class CreateOrderMsg implements Serializable {

	private static final long serialVersionUID = 5715917082075369546L;

	private Boolean isBackReq;

	private int operateIdentifier;

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

}
