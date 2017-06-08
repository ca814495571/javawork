package com.cqfc.ticketwinning.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.protocol.ticketwinning.WinningOrderInfo;
import com.cqfc.ticketwinning.model.WinningOrder;
import com.jami.common.BaseMapper;

public interface WinningOrderMapper extends BaseMapper{
	
	@Select("select * from ${tableName} where ${conditions}")
	public List<WinningOrder> getOrderList(@Param("conditions") String conditions, @Param("tableName") String tableName);
	
	@Select("select * from ${tableName} where orderNo=#{orderNo}")
	public WinningOrderInfo getOrderByOrderNo(@Param("orderNo") String orderNo, @Param("tableName") String tableName);

	@Select("select count(*) from ${tableName} where ${conditions}")
	public int countTotalSize(@Param("conditions") String conditions, @Param("tableName") String tableName);
	
	@Update("update ${tableName} set orderStatus=#{orderStatus}, winPrizeMoney=#{winPrizeMoney} where orderNo=#{orderNo}")
	public int updateWinningOrderByOrderNo(@Param("orderStatus") int orderStatus, @Param("winPrizeMoney") long winningAmout, @Param("orderNo") String orderNo, @Param("tableName") String tableName);
	
	@Update("update ${tableName} set orderStatus=#{orderStatus}, winPrizeMoney=#{winPrizeMoney} where ${conditions}")
	public int updateUnWinningOrder(@Param("orderStatus") int orderStatus, @Param("winPrizeMoney") long winningAmout, @Param("conditions") String conditions, @Param("tableName") String tableName);
	
	@Select("select count(*) from ${tableName} where lotteryId=#{lotteryId} and issueNo=#{issueNo}")
	public int getOrderCount(@Param("lotteryId") String lotteryId, @Param("issueNo") String issueNo, @Param("tableName") String tableName);
}
