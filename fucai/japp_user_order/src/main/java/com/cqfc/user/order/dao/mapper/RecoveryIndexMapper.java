package com.cqfc.user.order.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.protocol.partnerorder.RecoverOrderIndex;
import com.jami.common.BaseMapper;

public interface RecoveryIndexMapper extends BaseMapper{

	
	
	@Select("select * from t_recovery_index where dbName=#{dbName}")
	public RecoverOrderIndex getIndexByDbName(@Param("dbName")String dbName);
	
	@Update("update t_recovery_index set flag =#{flag} where dbName =#{dbName}")
	public int updateFlag(@Param("dbName")String dbName,@Param("flag")int flag); 
	
	
}
