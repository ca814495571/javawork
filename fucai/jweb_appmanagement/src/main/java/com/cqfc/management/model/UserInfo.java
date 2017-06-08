package com.cqfc.management.model;

import java.io.Serializable;

/**
 * 后台管理人员信息表
 * 
 * @author Administrator
 *
 */
public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1412586351897142873L;
	private int id;
	private String name;
	private String password;
	private String stationCode;
	private int roleId;
	private int loginFailCount;
	private String createTime;
	private String loginTime;
	private String lastUpdateTime;
	private int active;

	public UserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}


	public UserInfo(int id, String name, String password, String stationCode,
			int roleId, int loginFailCount, String createTime,
			String loginTime, String lastUpdateTime, int active) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.stationCode = stationCode;
		this.roleId = roleId;
		this.loginFailCount = loginFailCount;
		this.createTime = createTime;
		this.loginTime = loginTime;
		this.lastUpdateTime = lastUpdateTime;
		this.active = active;
	}


	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getLoginFailCount() {
		return loginFailCount;
	}

	public void setLoginFailCount(int loginFailCount) {
		this.loginFailCount = loginFailCount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}
