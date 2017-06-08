package com.cqfc.management.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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

	private Integer roleId;

	private String name;

	private String remark;

	private String createTime;

	private String lastUpdateTime;

	private List<Function> functions;
	
	public Role(){
		
	}
	
	public Role(Integer roleId, String name, String remark, String createTime,
			String lastUpdateTime, List<Function> functions) {
		super();
		this.roleId = roleId;
		this.name = name;
		this.remark = remark;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
		this.functions = functions;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public List<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(List<Function> functions) {
		this.functions = functions;
	}

	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		if(this.roleId!=null&& this.roleId!=0){
			
			sb.append(" 角色Id: ");
			sb.append(this.roleId);
		}
		
		if(StringUtils.isNotBlank(this.name)){
			
			sb.append(" 角色名称: ");
			sb.append(this.name);
		}
		return sb.toString();
	}
	


}
