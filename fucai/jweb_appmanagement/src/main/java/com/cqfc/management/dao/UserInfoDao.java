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
	
	
	
	public int getAllUserNum(){
		
		return userInfoMapper.getAllUserNum();
		
	}

	public List<UserInfo> getUserInfoByWhereAnd(UserInfo userInfo) {


		filterNullColumn(userInfo);

		StringBuffer sb = new StringBuffer();
		sb.append(" 1=1 ");

		if (!"".equals(userInfo.getId()) && userInfo.getId() != 0) {

			sb.append(" and  id = '");
			sb.append(userInfo.getId());
			sb.append("'");
		}
		if (!"".equals(userInfo.getLoginFailCount())
				&& userInfo.getLoginFailCount() != 0) {

			sb.append(" and  loginFailCount = '");
			sb.append(userInfo.getLoginFailCount());
			sb.append("'");
		}
		if (!"".equals(userInfo.getLoginTime())&&!"0000-00-00".equals(userInfo.getLoginTime())) {

			sb.append(" and  loginTime = '");
			sb.append(userInfo.getLoginTime());
			sb.append("'");
		}
		if (!"".equals(userInfo.getName())) {

			sb.append(" and  name = '");
			sb.append(userInfo.getName());
			sb.append("'");
		}
		if (!"".equals(userInfo.getPassword())) {

			sb.append(" and  password = '");
			sb.append(userInfo.getPassword());
			sb.append("'");
		}
		if (!"".equals(userInfo.getStationCode())) {

			sb.append(" and  stationCode = '");
			sb.append(userInfo.getStationCode());
			sb.append("'");
		}
		if (!"".equals(userInfo.getRoleId()) && userInfo.getRoleId() != 0) {

			sb.append(" and  roleId = '");
			sb.append(userInfo.getRoleId());
			sb.append("'");
		}
		sb.append(" and active= 1 order by createTime desc");
		System.out.println(sb.toString());
		return userInfoMapper.getUserInfoByWhereAnd(sb.toString());
	}

	public int updateByCode(String original ,String nameOrCode){
		
		
		return userInfoMapper.updateByCode(original, nameOrCode);
		
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
	
	
	
	
	public List<UserInfo> getUserInfoByWhere(
			 String keyword , int pageNum , int pageSize ) {

		StringBuffer sb = new StringBuffer(" 1=1 ");
		

	
		if (keyword != null && !"".equals(keyword)) {
			
			sb.append(" and ( name like '%");
			sb.append(keyword);
			sb.append("%'");
			sb.append(" or stationCode like '%");
			sb.append(keyword);
			sb.append("%'");
			sb.append(")");
		}
		
		sb.append(" and active = 1 order by lastUpdateTime desc") ;
		
		if(pageNum != 0 && pageSize!=0){
			
			sb.append(" limit ");
			sb.append((pageNum-1)*pageSize) ;
			sb.append(",");
			sb.append(pageSize) ;
			
		}
		System.out.println(sb.toString());
		
		
		return userInfoMapper.getUserInfoByWhere(sb.toString());

	}

}
