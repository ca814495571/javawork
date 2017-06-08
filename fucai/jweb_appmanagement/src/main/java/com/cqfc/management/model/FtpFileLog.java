package com.cqfc.management.model;

import java.io.Serializable;

public class FtpFileLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7703859555455061724L;
	/**
	 * 
	 */
	private String fileName;
	private String createTime;
	private String downTime;
	private int flag;


	public FtpFileLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public FtpFileLog(String fileName, String createTime, String downTime,
			int flag) {
		super();
		this.fileName = fileName;
		this.createTime = createTime;
		this.downTime = downTime;
		this.flag = flag;
	}


	public String getDownTime() {
		return downTime;
	}


	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
