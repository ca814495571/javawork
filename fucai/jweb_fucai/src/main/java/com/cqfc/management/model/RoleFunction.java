package com.cqfc.management.model;

import java.io.Serializable;

public class RoleFunction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6656650935316108791L;

	private int roleId;
	
	private String url ; 
	
	private String lastUpdateTime ;

	public RoleFunction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoleFunction(int roleId, String url, String lastUpdateTime) {
		super();
		this.roleId = roleId;
		this.url = url;
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	
}
