package com.cqfc.processor;

public class ReturnMessage {
	
	//返回状态码
	private String statusCode;
	//返回状态码信息说明
	private String msg;
	//返回信息
	private Object obj;
	
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		return "ReturnMessage [statusCode=" + statusCode + ", msg=" + msg
				+ ", obj=" + obj + "]";
	}
	
}
