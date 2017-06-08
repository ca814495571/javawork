package com.cqfc.management.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.management.dao.mapper.UserInfoMapper;
import com.cqfc.management.model.UserInfo;

@Repository
public class UserInfoDao {

	@Autowired
	private UserInfoMapper userInfoMapper;

	public int insert(UserInfo userInfo) {

		filterNullColumn(userInfo);

		return userInfoMapper.insert(userInfo);

	}

	public UserInfo getUserInfoById(int id) {

		return userInfoMapper.getUserInfoById(id);
	}

	public int update(UserInfo userInfo) {

		return userInfoMapper.update(userInfo);
	}
	
	
	
	
	public int getFailCountByName(String name){
		
		return userInfoMapper.getFailCountByName(name);
	}
	
	

	public List<UserInfo> getUserInfoByWhereAnd(UserInfo userInfo) {


		filterNullColumn(userInfo);

		StringBuffer sb = new StringBuffer();
		sb.append(" 1=1 ");

		if (!"".equals(userInfo.getId()) || userInfo.getId() != 0) {

			sb.append(" and where id = '");
			sb.append(userInfo.getId());
			sb.append("'");
		}
		if (!"".equals(userInfo.getLoginFailCount())
				|| userInfo.getLoginFailCount() != 0) {

			sb.append(" and where loginFailCount = '");
			sb.append(userInfo.getLoginFailCount());
			sb.append("'");
		}
		if (!"".equals(userInfo.getLoginTime())) {

			sb.append(" and where loginTime = '");
			sb.append(userInfo.getLoginTime());
			sb.append("'");
		}
		if (!"".equals(userInfo.getName())) {

			sb.append(" and where name = '");
			sb.append(userInfo.getName());
			sb.append("'");
		}
		if (!"".equals(userInfo.getStationCode())) {

			sb.append(" and where stationCode = '");
			sb.append(userInfo.getStationCode());
			sb.append("'");
		}
		if (!"".equals(userInfo.getRoleId()) || userInfo.getRoleId() != 0) {

			sb.append(" and where roleId = '");
			sb.append(userInfo.getRoleId());
			sb.append("'");
		}

		System.out.println(sb.toString());
		return userInfoMapper.getUserInfoByWhereAnd(sb.toString());
	}

	
	public void filterNullColumn(UserInfo userInfo) {

		if (userInfo.getName() == null) {

			userInfo.setName("");
		}

		if (userInfo.getPassword() == null) {

			userInfo.setPassword("");
		}

		if (userInfo.getCreateTime() == null) {

			userInfo.setCreateTime("0000-00-00");
		}

		if (userInfo.getLoginTime() == null) {

			userInfo.setLoginTime("0000-00-00");
		}

		if (userInfo.getStationCode() == null) {

			userInfo.setStationCode("");
		}
	}

}
