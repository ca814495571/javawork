package com.cqfc.outticket.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cqfc.outticket.model.OutTicket;
import com.cqfc.outticket.model.Statis;
import com.jami.common.BaseMapper;

public interface OutTicketMapper extends BaseMapper {

	@Select("select * from t_out_ticket where orderNo=#{orderNo}")
	public OutTicket findOrderStaByOrderNo(@Param("orderNo") String orderNum);

	@Select("select count(*) as totalSize, sum(orderMoney) as totalMoney from  ${tableName} where ${conditions}")
	public Statis statisOrder(@Param("conditions") String conditions,
			@Param("tableName") String tableName);
	
	@Insert("insert into t_out_ticket(orderNo,resultNum,lotteryId,issueNo,orderMoney,mutiple,orderContentOdds,playType) values (#{orderNo},#{resultNum},#{lotteryId},#{issueNo},#{orderMoney},#{mutiple},#{orderContentOdds},#{playType})")
	public int addOutTicket(OutTicket outTicket);

}
