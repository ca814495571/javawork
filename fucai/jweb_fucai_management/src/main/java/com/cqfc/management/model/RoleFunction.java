package com.cqfc.management.model;

import java.io.Serializable;

public class RoleFunction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6656650935316108791L;

	/**
	 * 
	 */

	private Integer roleId;
	
	private Integer functionId ; 
	
	private String createTime;
	
	private String lastUpdateTime ;

	public RoleFunction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoleFunction(Integer roleId, Integer functionId, String createTime,
			String lastUpdateTime) {
		super();
		this.roleId = roleId;
		this.functionId = functionId;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}


	
}
