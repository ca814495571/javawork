package com.cqfc.management.model;

import java.io.Serializable;

/**
 * 用户角色
 * 
 * @author Administrator
 *
 */
public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6660540956816887578L;

	private int id;

	private String name;

	private String lastUpdateTime;

	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Role(int id, String name, String lastUpdateTime) {
		super();
		this.id = id;
		this.name = name;
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
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

}
