package com.cqfc.ipquery.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cqfc.ipquery.model.IpSec;
import com.jami.common.BaseMapper;

public interface IpQueryMapper extends BaseMapper{
	@Select("select * from t_lottery_ipinfo where startIndex=#{startIndex}")
	IpSec getIpSecById(@Param("startIndex") Long index);
	
	@Select("select startIndex from t_lottery_ipinfo order by startIndex")
	List<Long> getAllStartIndex();
	
	@Insert("insert into t_lottery_ipinfo(startIndex,endIndex,startIp,endIp,province,detail,use) values(#{ipSec.startIndex},#{ipSec.endIndex},#{ipSec.startIp},#{ipSec.endIp},#{ipSec.province},#{ipSec.detail},#{ipSec.use})")
	int createIpSec(@Param("ipSec")IpSec ipSec);
}
