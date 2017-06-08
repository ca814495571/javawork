package com.cqfc.management.model;

import java.io.Serializable;

public class OperateLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3874365246607748770L;
	private Integer id ;
	private String  operator;
	private String  action;
	private Integer fuserId;
	private String operateTime;
	private Integer type; //根据一级菜单分类
	public OperateLog() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OperateLog(Integer id, String operator, String action,
			String operateTime, Integer type) {
		super();
		this.id = id;
		this.operator = operator;
		this.action = action;
		this.operateTime = operateTime;
		this.type = type;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getFuserId() {
		return fuserId;
	}
	public void setFuserId(Integer fuserId) {
		this.fuserId = fuserId;
	}
	
	
}
