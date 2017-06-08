package com.cqfc.order.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.order.model.Order;
import com.cqfc.order.model.SportOrder;
import com.cqfc.order.model.SportOrderDetail;
import com.jami.common.BaseMapper;

/**
 * @author liwh
 */
/**
 * @author Administrator
 * 
 */
public interface OrderMapper extends BaseMapper {

	/**
	 * 创建投注订单
	 * 
	 * @param order
	 * @return
	 */
	@Insert("insert into ${tableName}"
			+ "(lotteryId,partnerId,userId,issueNo,orderNo,orderType,orderStatus,totalAmount,orderContent,multiple,playType,paySerialNumber,tradeId,realName,cardNo,mobile,ticketNum,createTime) "
			+ "values(#{param1.lotteryId},#{param1.partnerId},#{param1.userId},#{param1.issueNo},#{param1.orderNo},#{param1.orderType},#{param1.orderStatus},#{param1.totalAmount},"
			+ "#{param1.orderContent},#{param1.multiple},#{param1.playType},#{param1.paySerialNumber},#{param1.tradeId},#{param1.realName},#{param1.cardNo},#{param1.mobile},#{param1.ticketNum},#{param1.createTime})")
	public int createOrder(Order order, @Param("tableName") String tableName);

	/**
	 * 根据订单编号查询订单信息
	 * 
	 * @param orderNo
	 *            订单编号
	 * @return
	 */
	@Select("select * from ${tableName} where orderNo=#{orderNo}")
	public Order findOrderByOrderNo(@Param("orderNo") long orderNo, @Param("tableName") String tableName);

	/**
	 * 更新订单状态(状态修改只能往后,不能往前)
	 * 
	 * @param orderNo
	 *            订单编号
	 * @param orderStatus
	 *            订单状态：1待付款 2已付款 3出票中 4已出票待开奖 5出票失败 6未中奖 7待领奖 8已领奖 9退款中 10退款成功
	 *            11订单取消
	 * @return
	 */
	@Update("update ${tableName} set orderStatus=#{orderStatus} where orderNo=#{orderNo} and orderStatus<#{orderStatus}")
	public int modifyOrderStatus(@Param("orderNo") long orderNo, @Param("orderStatus") int orderStatus,
			@Param("tableName") String tableName);

	/**
	 * 更新订单状态(错误状态码)
	 * 
	 * @param orderNo
	 * @param orderStatus
	 * @param tableName
	 * @param errorCode
	 * @param errorRemark
	 * @return
	 */
	@Update("update ${tableName} set orderStatus=#{orderStatus},errCodeStatus=#{errorCode},errCodeRemark=#{errorRemark}"
			+ " where orderNo=#{orderNo} and orderStatus<#{orderStatus}")
	public int modifyOrderStatusAndRemark(@Param("orderNo") long orderNo, @Param("orderStatus") int orderStatus,
			@Param("tableName") String tableName, @Param("errorCode") String errorCode,
			@Param("errorRemark") String errorRemark);

	/**
	 * 更新订单是否同步到全局数据库
	 * 
	 * @param orderNo
	 *            订单编号
	 * @param syncValue
	 *            是否同步到全局数据库 0未同步 1同步成功 2同步失败
	 * @return
	 */
	@Update("update ${tableName} set isSyncSuccess=#{syncValue} where orderNo=#{orderNo}")
	public int updateOrderSync(@Param("orderNo") long orderNo, @Param("syncValue") int syncValue,
			@Param("tableName") String tableName);

	/**
	 * 按limit条件获取订单数据
	 * 
	 * @param tableName
	 * @param conditions
	 * @return
	 */
	@Select("select * from ${tableName} where ${conditions}")
	public List<Order> getOrderList(@Param("tableName") String tableName, @Param("conditions") String conditions);

	/**
	 * 期号状态更新为已派奖后，且订单创建超过5天，已同步到全局数据库，则删除订单
	 * 
	 * @param tableName
	 * @param deleteTime
	 * @param isSyncSuccess
	 * @return
	 */
	@Delete("delete from ${tableName} where createTime<#{deleteTime} and isSyncSuccess=#{isSyncSuccess}")
	public int deleteOrder(@Param("tableName") String tableName, @Param("deleteTime") String deleteTime,
			@Param("isSyncSuccess") int isSyncSuccess);

	/**
	 * 更新订单出票结果状态
	 * 
	 * @param orderNo
	 * @param orderStatus
	 * @param tableName
	 * @return
	 */
	@Update("update ${tableName} set orderStatus=#{orderStatus} where orderNo=#{orderNo} and (orderStatus=2 or orderStatus=3)")
	public int modifyTicketResult(@Param("orderNo") long orderNo, @Param("orderStatus") int orderStatus,
			@Param("tableName") String tableName);

	@Select("select * from ${tableName} where partnerId=#{partnerId} and tradeId=#{tradeId}")
	public List<Order> findOrderByParams(@Param("partnerId") String partnerId, @Param("tradeId") String tradeId,
			@Param("tableName") String tableName);

	/**
	 * 查询竞技彩订单
	 * 
	 * @param orderNo
	 * @param tableName
	 * @return
	 */
	@Select("select * from ${tableName} where orderNo=#{orderNo}")
	public SportOrder findSportOrderByOrderNo(@Param("orderNo") long orderNo, @Param("tableName") String tableName);

	/**
	 * 查询竞技彩订单明细
	 * 
	 * @param orderNo
	 * @param tableName
	 * @return
	 */
	@Select("select * from ${tableName} where orderNo=#{orderNo}")
	public List<SportOrderDetail> findSportOrderDetailByOrderNo(@Param("orderNo") long orderNo,
			@Param("tableName") String tableName);

	/**
	 * 竞技彩获取订单list
	 * 
	 * @param tableName
	 * @param conditions
	 * @return
	 */
	@Select("select * from ${tableName} where ${conditions}")
	public List<SportOrder> getSportOrderList(@Param("tableName") String tableName,
			@Param("conditions") String conditions);

	/**
	 * 删除竞技彩订单
	 * 
	 * @param mainTableName
	 * @param detailTableName
	 * @param deleteTime
	 * @param isSyncSuccess
	 * @return
	 */
	@Delete("delete main,detail from ${mainTableName} main,${detailTableName} detail where"
			+ " main.orderNo=detail.orderNo and main.createTime<#{deleteTime} and"
			+ " main.isSyncSuccess=#{isSyncSuccess}")
	public int deleteSportOrder(@Param("mainTableName") String mainTableName,
			@Param("detailTableName") String detailTableName, @Param("deleteTime") String deleteTime,
			@Param("isSyncSuccess") int isSyncSuccess);

	/**
	 * 查询竞技彩订单
	 * 
	 * @param partnerId
	 * @param tradeId
	 * @param mainTableName
	 * @return
	 */
	@Select("select * from ${mainTableName} where partnerId=#{partnerId} and tradeId=#{tradeId}")
	public SportOrder findSportOrderByParams(@Param("partnerId") String partnerId, @Param("tradeId") String tradeId,
			@Param("mainTableName") String mainTableName);

}
