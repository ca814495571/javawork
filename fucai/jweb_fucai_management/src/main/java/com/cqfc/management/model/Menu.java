package com.cqfc.management.model;

import java.io.Serializable;

public class Menu implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1700633479299080450L;
	private String first;
	private String second;
	private String url;
	private Integer role;
	public Menu(String first, String second, String url, Integer role) {
		super();
		this.first = first;
		this.second = second;
		this.url = url;
		this.role = role;
	}
	public Menu() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getSecond() {
		return second;
	}
	public void setSecond(String second) {
		this.second = second;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	
	
	
	
	
	
}
