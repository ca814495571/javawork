package com.cqfc.management.model;

import java.io.Serializable;

public class MenuTree implements Serializable{

	
	private static final long serialVersionUID = -3441324560029153115L;
	private Integer id;
	private Integer pId;
	private String name;
	private Boolean checked ; //false没有分配资源 true  分配了资源
	private Boolean open;
	public MenuTree() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MenuTree(Integer id, Integer pId, String name, Boolean checked) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.checked = checked;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getpId() {
		return pId;
	}
	public void setpId(Integer pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	
	
}
