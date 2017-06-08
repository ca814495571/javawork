package com.cqfc.management.model;

import java.io.Serializable;
import java.util.List;

public class UserForView implements Serializable{

		/**
	 * 
	 */
	private static final long serialVersionUID = -4802180613134456468L;
		private Integer fuserId;
		private String userName ;
		private Integer active;
		private String createTime;
		private List<Role> roles;
		public UserForView() {
			super();
			// TODO Auto-generated constructor stub
		}
		public UserForView(Integer fuserId, String userName, List<Role> roles) {
			super();
			this.fuserId = fuserId;
			this.userName = userName;
			this.roles = roles;
		}
		public Integer getFuserId() {
			return fuserId;
		}
		public void setFuserId(Integer fuserId) {
			this.fuserId = fuserId;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public List<Role> getRoles() {
			return roles;
		}
		public void setRoles(List<Role> roles) {
			this.roles = roles;
		}
		public Integer getActive() {
			return active;
		}
		public void setActive(Integer active) {
			this.active = active;
		}
		public String getCreateTime() {
			return createTime;
		}
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
		
}
