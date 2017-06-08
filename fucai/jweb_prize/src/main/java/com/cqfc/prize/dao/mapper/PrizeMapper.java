package com.cqfc.prize.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.prize.model.Prize;


public interface PrizeMapper extends BaseMapper{

	
	@Insert("replace into ${tableName} (userId,ticketId,status,winAmount,failTimes,gameCode,gameSerial,packageNum,ticketNum,message,createTime)"
			+ " values (#{prize.userId},#{prize.ticketId},#{prize.status},#{prize.winAmount},#{prize.failTimes},#{prize.gameCode},#{prize.gameSerial},#{prize.packageNum},#{prize.ticketNum},#{prize.message},now())")
	public int insert(@Param("prize")Prize prize,@Param("tableName")String tableName);
	
	@Update("update ${tableName} set status=#{prize.status},failTimes=#{prize.failTimes},message=#{prize.message} where userId = #{prize.userId} and ticketId=#{prize.ticketId}")
	public int update(@Param("prize")Prize prize,@Param("tableName")String tableName);
	
	@Select("select * from  ${tableName} where userId = #{prize.userId} and ticketId=#{prize.ticketId}")
	public Prize queryOne(@Param("prize")Prize prize,@Param("tableName")String tableName);
	
	@Select("select count(1) from ${tableName} where userId=#{prize.userId}")
	public int queryForSum(@Param("prize")Prize prize,@Param("tableName")String tableName);
	
	@Select("select * from (select * from ${tableName} where ${where}) t order by t.createTime desc")
	public List<Prize> queryList(@Param("tableName")String tableName,@Param("where")String where);
}
