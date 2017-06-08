package com.cqfc.user.order.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.protocol.userorder.Order;
import com.jami.common.BaseMapper;

public interface UserOrderMapper extends BaseMapper {

	@Insert("insert into ${tableName} "
			+ "(lotteryId,partnerId,userId,issueNo,orderNo,orderStatus,"
			+ "totalAmount,winPrizeMoney,prizeAfterTax,orderContent,stakeNum,multiple,playType,paySerialNumber,"
			+ "realName,cardNo,mobile,createTime,ext,orderType,tradeId)"
			+ "values (#{param1.lotteryId},#{param1.partnerId},#{param1.userId},#{param1.issueNo},#{param1.orderNo},"
			+ "#{param1.orderStatus},#{param1.totalAmount},#{param1.winPrizeMoney},#{param1.prizeAfterTax},#{param1.orderContent},#{param1.stakeNum},#{param1.multiple},"
			+ "#{param1.playType},#{param1.paySerialNumber},#{param1.realName},#{param1.cardNo},#{param1.mobile},#{param1.createTime},#{param1.ext},#{param1.orderType},"
			+ "#{param1.tradeId})")
	public int addUserOrder(Order userOrder,
			@Param("tableName") String tableName);

	@Update("update ${tableName} set orderStatus=#{param1.orderStatus},totalAmount=#{param1.totalAmount},winPrizeMoney=#{param1.winPrizeMoney},prizeAfterTax=#{param1.prizeAfterTax},"
			+ "orderContent =#{param1.orderContent},stakeNum =#{param1.stakeNum},multiple =#{param1.multiple},playType=#{param1.playType},ext=#{param1.ext},orderType=#{param1.orderType}"
			+ " where orderId = #{param1.orderId}")
	public int updateUserOrder(Order userOrder,
			@Param("tableName") String tableName);

	@Select("select * from ${tableName} where ${condition}")
	public List<Order> getUserOrder(@Param("condition") String condition,
			@Param("tableName") String tableName);

	@Select("select count(*) from ${tableName} where ${condition}")
	public int getOrderTotal(@Param("condition") String condition,
			@Param("tableName") String tableName);
	
	@Select("select * from ${tableName} where ${condition}")
	public List<Order> getOrderBywhere(@Param("condition") String condition,
			@Param("tableName") String tableName);
	
	@Select("select * from ${tableName} where ${condition}")
	public  Order getMaxIsserNoOrder(@Param("condition") String condition,
			@Param("tableName") String tableName);
	

}
