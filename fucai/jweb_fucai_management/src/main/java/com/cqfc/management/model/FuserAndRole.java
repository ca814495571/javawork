package com.cqfc.management.model;

import java.io.Serializable;

public class FuserAndRole implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 564292337558349074L;

	private Integer id ;
	
	private  Integer fuserId ;
	
	private  Integer roleId;
	
	private String createTime ;
	
	private String lastUpdateTime;

	public FuserAndRole() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FuserAndRole(Integer id, Integer fuserId, Integer roleId, String createTime,
			String lastUpdateTime) {
		super();
		this.id = id;
		this.fuserId = fuserId;
		this.roleId = roleId;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFuserId() {
		return fuserId;
	}

	public void setFuserId(Integer fuserId) {
		this.fuserId = fuserId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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
