package com.cqfc.management.model;

import java.io.Serializable;


/**
 * FTP 上用于保存的用户注册信息
 * @author Administrator
 *
 */
public class UserInfoOfFTPFile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6629926750292761677L;

	private int id;

	private String userId; // 用户id

	private String ext; // 扩展信息

	private String registTime; // 注册时间

	public UserInfoOfFTPFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserInfoOfFTPFile(int id, String userId, String ext, String registTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.ext = ext;
		this.registTime = registTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getRegistTime() {
		return registTime;
	}

	public void setRegistTime(String registTime) {
		this.registTime = registTime;
	}


}
