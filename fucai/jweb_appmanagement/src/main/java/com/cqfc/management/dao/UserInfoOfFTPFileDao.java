package com.cqfc.management.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.management.dao.mapper.UserInfoOfFTPFileMapper;
import com.cqfc.management.model.UserInfoOfFTPFile;

@Repository
public class UserInfoOfFTPFileDao {

	@Autowired
	private UserInfoOfFTPFileMapper userInfoOfFTPMapper;

	public int insertUserInfoOfFTP(UserInfoOfFTPFile userInfoOfFTP) {

		filterNullColumn(userInfoOfFTP);
		return userInfoOfFTPMapper.insert(userInfoOfFTP);
	}

	
	public int getBettingShopUserAddNum(String enterTag ,String date){
		
		return userInfoOfFTPMapper.getBettingShopUserAddNum(enterTag, date);
	}
	

	public List<UserInfoOfFTPFile>  getByUserId(UserInfoOfFTPFile userInfoOfFTP){
		
		return userInfoOfFTPMapper.getByUserId(userInfoOfFTP);
	}
	
	
	
	private void filterNullColumn(UserInfoOfFTPFile userInfoOfFTP) {

		if (userInfoOfFTP.getExt() == null
				|| "NULL".equals(userInfoOfFTP.getExt())
				|| "null".equals(userInfoOfFTP.getExt())) {

			userInfoOfFTP.setExt("");
		}

		if (userInfoOfFTP.getRegistTime() == null
				|| "NULL".equals(userInfoOfFTP.getRegistTime())
				|| "null".equals(userInfoOfFTP.getExt())) {

			userInfoOfFTP.setRegistTime("0000-00-00");
		}

		if (userInfoOfFTP.getUserId() == null
				|| "NULL".equals(userInfoOfFTP.getUserId())
				|| "null".equals(userInfoOfFTP.getExt())) {

			userInfoOfFTP.setUserId("");
		}

	}


}
