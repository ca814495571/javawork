package com.cqfc.management.model;

import java.io.Serializable;
import java.util.List;

public class RoleMenus implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5638510361732559937L;
	private String name ;
	private Integer roleId;
	private String remark;
	private Boolean checked ;
	private String partnerId;
	private List<RoleMenus> roleMenus;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public List<RoleMenus> getRoleMenus() {
		return roleMenus;
	}
	public void setRoleMenus(List<RoleMenus> roleMenus) {
		this.roleMenus = roleMenus;
	}
	public RoleMenus(String name, Integer roleId, String remark,
			Boolean checked, List<RoleMenus> roleMenus) {
		super();
		this.name = name;
		this.roleId = roleId;
		this.remark = remark;
		this.checked = checked;
		this.roleMenus = roleMenus;
	}
	
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public RoleMenus() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
