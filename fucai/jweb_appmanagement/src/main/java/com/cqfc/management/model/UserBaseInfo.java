package com.cqfc.management.model;

import java.io.Serializable;

public class UserBaseInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3761456642229167603L;

	private int roleId;

	private int id;

	private String code;

	private String name;

	public UserBaseInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserBaseInfo(int roleId, int id, String code, String name) {
		super();
		this.roleId = roleId;
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
