package com.cqfc.management.model;

import java.io.Serializable;

public class PcResultObj implements Serializable {

	private static final long serialVersionUID = 1003576823643178222L;

	public static final String SUCCESS_CODE = "1";
	public static final String FAIL_CODE = "2";
	public static final String NO_RIGHT_CODE = "3";
	
	private Object entity;

	private String msgCode;

	private String msg;

	public PcResultObj() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PcResultObj(Object entity, String msgCode, String msg) {
		super();
		this.entity = entity;
		this.msgCode = msgCode;
		this.msg = msg;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
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

	@Override
	public String toString() {
		
		return "entity："+this.entity +"\n"+"msgCode："+ this.msgCode +"msg:"+this.msg;
	}


}
