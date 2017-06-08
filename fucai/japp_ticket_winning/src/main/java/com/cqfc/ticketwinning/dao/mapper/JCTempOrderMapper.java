package com.cqfc.ticketwinning.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.ticketwinning.model.JCTempOrder;
import com.cqfc.ticketwinning.model.OrderNoCount;
import com.jami.common.BaseMapper;

public interface JCTempOrderMapper extends BaseMapper{
	
	@Select("select * from ${tableName} where ${conditions}")
	public List<JCTempOrder> getOrderList(@Param("conditions") String conditions, @Param("tableName") String tableName);
	
	@Select("select count(*) from ${tableName} where ${conditions}")
	public int countTotalSize(@Param("conditions") String conditions, @Param("tableName") String tableName);
	
	@Update("update ${tableName} set winOdds=#{winOdds} and matchStatus=#{matchStatus} where transferId=#{transferId} and orderNo=#{orderNo}")
	public int updateJCTempOrderWinOdds(@Param("transferId") String transferId, @Param("orderNo") String orderNo, @Param("winOdds") String winOdds, @Param("matchStatus") int matchStatus, @Param("tableName") String tableName);
	
	@Select("select count(*) as orderNoCount, sum(case when matchStatus = 1 then 1 else  0 end) as winOddsCount from ${tableName} where ${conditions}")
	public OrderNoCount getOrderNoCountAndWinOddsCount(@Param("conditions") String conditions, @Param("tableName") String tableName);
	
}
