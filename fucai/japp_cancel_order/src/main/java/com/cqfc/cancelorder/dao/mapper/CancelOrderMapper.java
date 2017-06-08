package com.cqfc.cancelorder.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.protocol.cancelorder.CancelOrder;
import com.cqfc.protocol.cancelorder.SportCancelOrder;
import com.cqfc.protocol.cancelorder.SportCancelOrderDetail;
import com.jami.common.BaseMapper;

/**
 * @author liwh
 */
public interface CancelOrderMapper extends BaseMapper {

	/**
	 * 创建撤单订单
	 * 
	 * @param cancelOrder
	 * @return
	 */
	@Insert("insert into t_lottery_cancel_order"
			+ "(orderNo,partnerId,userId,lotteryId,issueNo,outTicketStatus,totalAmount,"
			+ "orderContent,playType,multiple,createTime) "
			+ "values(#{orderNo},#{partnerId},#{userId},#{lotteryId},#{issueNo},"
			+ "#{outTicketStatus},#{totalAmount},#{orderContent},#{playType},#{multiple},now())")
	public int createCancelOrder(CancelOrder cancelOrder);

	/**
	 * 获取撤单订单列表
	 * 
	 * @param conditions
	 * @return
	 */
	@Select("select * from t_lottery_cancel_order where ${conditions}")
	public List<CancelOrder> getCancelOrderListForScan(@Param("conditions") String conditions);

	/**
	 * 更新撤单订单出票状态
	 * 
	 * @param orderNo
	 * @param outTicketStatus
	 * @return
	 */
	@Update("update t_lottery_cancel_order set outTicketStatus=#{outTicketStatus} where orderNo=#{orderNo} and outTicketStatus=3")
	public int updateOutTicketStatus(@Param("orderNo") String orderNo, @Param("outTicketStatus") int outTicketStatus);

	/**
	 * 获取转移单成功的总数
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	@Select("select count(*) from t_lottery_cancel_order where lotteryId=#{lotteryId} and issueNo=#{issueNo} and outTicketStatus=4")
	public long getSuccessTicketCancelOrder(@Param("lotteryId") String lotteryId, @Param("issueNo") String issueNo);

	/**
	 * 分页查询转移单列表
	 * 
	 * @param conditions
	 * @return
	 */
	@Select("select * from t_lottery_cancel_order where ${conditions}")
	public List<CancelOrder> getCancelOrderList(@Param("conditions") String conditions);

	@Select("select count(*) from t_lottery_cancel_order where ${conditions}")
	public int countTotalSize(@Param("conditions") String conditions);

	/**
	 * 创建竞技彩转移订单主表
	 * 
	 * @param sportCancelOrder
	 * @return
	 */
	@Insert("insert into t_lottery_sport_cancel_order"
			+ "(orderNo,lotteryId,partnerId,userId,issueNo,totalAmount,orderContent,"
			+ "multiple,playType,tradeId,planId,printProvince,createTime) "
			+ "values(#{orderNo},#{lotteryId},#{partnerId},#{userId},#{issueNo},#{totalAmount},"
			+ "#{orderContent},#{multiple},#{playType},#{tradeId},#{planId},#{printProvince},now())")
	public int createSportCancelOrder(SportCancelOrder sportCancelOrder);

	/**
	 * 创建竞技彩转移订单细表
	 * 
	 * @param sportCancelOrderDetail
	 * @return
	 */
	@Insert(" insert into t_lottery_sport_cancel_order_detail"
			+ "(orderNo,matchId,transferId,matchNo,orderContent,matchStatus,createTime) "
			+ "values(#{orderNo},#{matchId},#{transferId},#{matchNo},#{orderContent},#{matchStatus},now())")
	public int createSportCancelOrderDetail(SportCancelOrderDetail sportCancelOrderDetail);

	/**
	 * 获取竞技彩转移订单列表
	 * 
	 * @param outTicketStatus
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Select("select * from t_lottery_sport_cancel_order where ${conditions}")
	public List<SportCancelOrder> getSportCancelOrderListForScan(@Param("conditions") String conditions);

	/**
	 * 获取竞技彩订单细表list
	 * 
	 * @param orderNo
	 * @return
	 */
	@Select("select * from t_lottery_sport_cancel_order_detail where orderNo=#{orderNo}")
	public List<SportCancelOrderDetail> getSportOrderDetail(@Param("orderNo") long orderNo);

	/**
	 * 更新竞技彩转移订单主表出票信息
	 * 
	 * @param orderNo
	 * @param outTicketStatus
	 * @param ticketNo
	 * @param printTime
	 * @return
	 */
	@Update("update t_lottery_sport_cancel_order set outTicketStatus=#{outTicketStatus},ticketNo=#{ticketNo},"
			+ "printTime=#{printTime} where orderNo=#{orderNo} and outTicketStatus=3")
	public int updateSportCancelOrderMain(@Param("orderNo") long orderNo,
			@Param("outTicketStatus") int outTicketStatus, @Param("ticketNo") String ticketNo,
			@Param("printTime") String printTime);

	/**
	 * 更新竞技彩转移订单细表出票信息
	 * 
	 * @param orderNo
	 * @param transferId
	 * @param rq
	 * @param sp
	 * @return
	 */
	@Update("update t_lottery_sport_cancel_order_detail set rq=#{rq},sp=#{sp}"
			+ " where orderNo=#{orderNo} and transferId=#{transferId}")
	public int updateSportCancelOrderDetail(@Param("orderNo") long orderNo, @Param("transferId") String transferId,
			@Param("rq") String rq, @Param("sp") String sp);

}
