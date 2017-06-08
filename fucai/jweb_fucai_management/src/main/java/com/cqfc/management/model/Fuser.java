package com.cqfc.management.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class Fuser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5811727514458979314L;
	private Integer fuserId;
	private String name;
	private String password;
	private List<Role> roles;
	private Integer loginFailCount; //默认0
	private String createTime; //默认 now()
	private String loginTime; //默认0000-00-00 00:00:00
	private String lastUpdateTime;//默认系统时间
	private Integer active; //默认1 正常                                      0 锁住
	private Set<String> resources; //url
	private String partnerId;
	public Fuser() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Fuser(Integer fuserId, String name, String password,
			List<Role> roles, Integer loginFailCount, String createTime,
			String loginTime, String lastUpdateTime, Integer active,
			Set<String> resources, String partnerId) {
		super();
		this.fuserId = fuserId;
		this.name = name;
		this.password = password;
		this.roles = roles;
		this.loginFailCount = loginFailCount;
		this.createTime = createTime;
		this.loginTime = loginTime;
		this.lastUpdateTime = lastUpdateTime;
		this.active = active;
		this.resources = resources;
		this.partnerId = partnerId;
	}

	public Integer getFuserId() {
		return fuserId;
	}

	public void setFuserId(Integer fuserId) {
		this.fuserId = fuserId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Integer getLoginFailCount() {
		return loginFailCount;
	}

	public void setLoginFailCount(Integer loginFailCount) {
		this.loginFailCount = loginFailCount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Set<String> getResources() {
		return resources;
	}

	public void setResources(Set<String> resources) {
		this.resources = resources;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		
		if(this.fuserId != null && this.fuserId != 0){
			sb.append(" 用户Id: ");
			sb.append( this.fuserId);
		}
		
		if(StringUtils.isNotBlank(this.name)){
			sb.append(" 用户名: ");
			sb.append( this.name);
			
		}
		return sb.toString();
	}


}
