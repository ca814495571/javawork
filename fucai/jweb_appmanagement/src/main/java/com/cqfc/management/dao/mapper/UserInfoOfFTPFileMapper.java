package com.cqfc.management.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.cqfc.common.dao.BaseMapper;
import com.cqfc.management.model.UserInfoOfFTPFile;

public interface UserInfoOfFTPFileMapper extends BaseMapper{

	@Insert("insert into t_ftp_user_info (userId ,ext,registTime) values (#{userId},#{ext},#{registTime})")
	public int insert(UserInfoOfFTPFile userInfoOfFTP);
	
	
	@Select("select * from t_ftp_user_info ")
	public List<UserInfoOfFTPFile> getAllUserInfoOfFTPs();
	
	
	@Select("select ifNULL(count(*),0) from t_ftp_user_info where ext = #{0} and registTime= #{1}")
	public int getBettingShopUserAddNum(String enterTag ,String Date);
	
	@Select("select *  from t_ftp_user_info where userId = #{userId}")
	public List<UserInfoOfFTPFile> getByUserId(UserInfoOfFTPFile userInfoOfFTP);
}
