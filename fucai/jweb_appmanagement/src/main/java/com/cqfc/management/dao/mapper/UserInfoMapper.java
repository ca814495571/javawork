package com.cqfc.management.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.common.dao.BaseMapper;
import com.cqfc.management.model.UserInfo;

public interface UserInfoMapper extends BaseMapper {

	@Insert("insert into t_user_info (name ,password ,stationCode ,roleId,loginFailCount ,createTime ,loginTime) "
			+ "values (#{name},#{password},#{stationCode},#{roleId},#{loginFailCount},now(),now())")
	public int insert(UserInfo userInfo);

	@Update("update t_user_info set name=#{name},password=#{password},stationCode=#{stationCode},roleId=#{roleId},"
			+ "loginFailCount=#{loginFailCount},createTime=#{createTime},loginTime=#{loginTime},active=#{active} where id = #{id}")
	public int update(UserInfo userInfo);
	
	@Select("select * from t_user_info where id =#{0} and active = 1")
	public UserInfo getUserInfoById(int id);
	
	
	@Select("select * from t_user_info where ${where} ")
	public List<UserInfo> getUserInfoByWhereAnd(@Param("where") String where);
	
	
	@Select("select count(*) from t_user_info where active=1 ")
	public int getAllUserNum();

	
	@Select("select *  from t_user_info where ${where}")
	public List<UserInfo> getUserInfoByWhere(@Param("where") String where);
	
	@Update("update t_user_info set stationCode = #{1} where stationCode = #{0}")
	public int updateByCode(String original ,String nameOrCode);
	
}
