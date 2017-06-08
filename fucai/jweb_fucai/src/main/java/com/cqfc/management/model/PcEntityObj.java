package com.cqfc.management.model;

import java.io.Serializable;

public class PcEntityObj implements Serializable {

	private static final long serialVersionUID = 1003576823643178222L;

	private EntityObj entity;

	private String msgCode;

	private String msg;

	public PcEntityObj() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PcEntityObj(EntityObj entity, String msgCode, String msg) {
		super();
		this.entity = entity;
		this.msgCode = msgCode;
		this.msg = msg;
	}

	public EntityObj getEntity() {
		return entity;
	}

	public void setEntity(EntityObj entity) {
		this.entity = entity;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
