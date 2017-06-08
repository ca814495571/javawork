package com.cqfc.management.service;

import java.util.List;

import com.cqfc.management.model.UserInfo;

public interface IUserInfoService {

	
	
	public int insert(UserInfo userInfo);
	
	
	public int update(UserInfo userInfo);
	
	
	public UserInfo getUserInfoById(int id);
	
	
	public List<UserInfo> getUserInfoByWhereAnd(UserInfo userInfo);
	
	public int getFailCountByName(String name);
}
