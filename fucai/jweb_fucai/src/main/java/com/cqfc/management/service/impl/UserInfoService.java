package com.cqfc.management.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.management.dao.UserInfoDao;
import com.cqfc.management.model.UserInfo;
import com.cqfc.management.service.IUserInfoService;

@Service
public class UserInfoService implements IUserInfoService {

	@Autowired
	private UserInfoDao userInfoDao;

	@Override
	public int insert(UserInfo userInfo) {
		return userInfoDao.insert(userInfo);
	}

	@Override
	public int update(UserInfo userInfo) {
		return userInfoDao.update(userInfo);
	}

	@Override
	public UserInfo getUserInfoById(int id) {
		return userInfoDao.getUserInfoById(id);
	}

	@Override
	public List<UserInfo> getUserInfoByWhereAnd(UserInfo userInfo) {

		return userInfoDao.getUserInfoByWhereAnd(userInfo);
	}

	@Override
	public int getFailCountByName(String name) {
		
		return userInfoDao.getFailCountByName(name);
	}

}
