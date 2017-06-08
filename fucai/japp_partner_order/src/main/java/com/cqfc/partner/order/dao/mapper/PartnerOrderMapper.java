package com.cqfc.partner.order.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.protocol.partnerorder.DailySaleAndCharge;
import com.cqfc.protocol.partnerorder.IssueSaleAndReward;
import com.cqfc.protocol.partnerorder.LotteryDaySale;
import com.cqfc.protocol.partnerorder.LotteryIssueSale;
import com.cqfc.protocol.partnerorder.Order;
import com.cqfc.protocol.partnerorder.OrderDetail;
import com.jami.common.BaseMapper;

public interface PartnerOrderMapper extends BaseMapper {

	@Insert("insert into ${tableName} "
			+ "(lotteryId,partnerId,userId,issueNo,orderNo,orderStatus,"
			+ "totalAmount,winPrizeMoney,prizeAfterTax,orderContent,stakeNum,multiple,playType,paySerialNumber,"
			+ "realName,cardNo,mobile,createTime,ext,orderType,tradeId)"
			+ "values (#{order.lotteryId},#{order.partnerId},#{order.userId},#{order.issueNo},#{order.orderNo},"
			+ "#{order.orderStatus},#{order.totalAmount},#{order.winPrizeMoney},#{order.prizeAfterTax},#{order.orderContent},#{order.stakeNum},#{order.multiple},"
			+ "#{order.playType},#{order.paySerialNumber},#{order.realName},#{order.cardNo},#{order.mobile},#{order.createTime},#{order.ext},"
			+ "#{order.orderType},#{order.tradeId})")
	public int addPartnerOrder(@Param("order")Order partnerOrder ,@Param("tableName") String tableName);

	
	@Update("update ${tableName} set "
			+ "orderStatus=#{order.orderStatus},totalAmount=#{order.totalAmount},winPrizeMoney=#{order.winPrizeMoney},prizeAfterTax=#{order.prizeAfterTax},"
			+ "stakeNum=#{order.stakeNum},multiple=#{order.multiple}"
			+ " where orderId = #{order.orderId}")
	public int updatePartnerOrder(@Param("order")Order partnerOrder ,@Param("tableName") String tableName);
	
	
	
	@Select("select count(*) from ${tableName} where ${where}")
	public int getPartnerOrderNumByWhere(@Param("where") String where,@Param("tableName") String tableName );
	
	
	@Select("select * from ${tableName} where ${where}")
	public List<Order> getPartnerOrderByWhere(@Param("where") String where,@Param("tableName") String tableName );
	

	/**
	 * 查询彩种一期所有合作商的销量信息
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	@Select("select lotteryId,issueNo, sum(sucMoney) as sucMoney"
			+" from t_lottery_issue_sale_count  where  lotteryId=#{lotteryId} and issueNo = #{issueNo} group by lotteryId,issueNo")
	public List<LotteryIssueSale> getAllLotteryIssueSale(@Param("lotteryId")String lotteryId, @Param("issueNo") String issueNo);

	
	/**
	 * 查询合作商彩种期号所有订单类型的销售中奖情况
	 * @param partnerId
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	@Select("select t1.partnerId as partnerId,t1.lotteryId,t1.issueNo as issueNo, t1.orderType as orderType, sucNum, sucMoney, failNum,"
			  +"failMoney,case when smallPrizeNum>0 then  smallPrizeNum else  0 end as smallPrizeNum,case when smallPrizeMoney>0 then  smallPrizeMoney else  0 end  as smallPrizeMoney,"
			  + "case when bigPrizeNum>0 then bigPrizeNum else  0 end  as bigPrizeNum,case when bigPrizeMoney>0 then  bigPrizeMoney else  0 end	 as bigPrizeMoney ,t1.createTime from "
			  + "t_lottery_issue_sale_count t1 left join t_lottery_issue_reward_count t2  on  t1.partnerId = t2.partnerId and t1.lotteryId = t2.lotteryId and t1.issueNo = t2.issueNo "
			  + " and t1.orderType = t2.orderType where t1.partnerId=#{partnerId} and t1.lotteryId=#{lotteryId} and t1.issueNo = #{issueNo}")
	public List<IssueSaleAndReward> getIssueSaleAndReward(@Param("partnerId") String partnerId,@Param("lotteryId") String lotteryId,@Param("issueNo") String issueNo);

	
	/**
	 * 查询合作商彩种期号的销售中奖情况(不区分订单类型)
	 * @param partnerId
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	@Select("select t1.partnerId as partnerId,t1.lotteryId as lotteryId,t1.issueNo as issueNo,sum(sucNum) as sucNum,sum(sucMoney) as sucMoney,sum(failNum) as failNum,"
			+" sum(failMoney) as failMoney,case when sum(smallPrizeNum)>0 then  sum(smallPrizeNum) else  0 end as smallPrizeNum,case when sum(smallPrizeMoney)>0 then sum(smallPrizeMoney) else  0 end  as smallPrizeMoney,"
			+" case when sum(bigPrizeNum)>0 then  sum(bigPrizeNum) else  0 end  as bigPrizeNum,case when sum(bigPrizeMoney)>0 then  sum(bigPrizeMoney) else  0 end	 as bigPrizeMoney,t1.createTime "
			+"  from t_lottery_issue_sale_count t1 left join t_lottery_issue_reward_count t2  on "
			+" t1.partnerId = t2.partnerId and t1.lotteryId = t2.lotteryId and t1.issueNo = t2.issueNo and t1.orderType = t2.orderType where t1.partnerId=#{partnerId} and t1.lotteryId=#{lotteryId} and t1.issueNo = #{issueNo} group by t1.partnerId,t1.lotteryId,t1.issueNo")
	public List<LotteryIssueSale> getIssueSaleAndRewardByGroup(@Param("partnerId") String partnerId,@Param("lotteryId") String lotteryId,@Param("issueNo") String issueNo);
	

	/**
	 * 查询合作商某天的销量兑奖充值提现信息
	 * @param partnerId
	 * @param countTime
	 * @return
	 */
	@Select("select t1.partnerId,t1.lotteryId,t1.totalMoney,case when t4.awardPrizeMoney>0 then t4.awardPrizeMoney "
			+ " else 0 end as awardPrizeMoney, case when t2.chargeTotalMoney>0 then t2.chargeTotalMoney else 0 end as chargeTotalMoney,"
			+ " case when t3.encashTotalMoney>0 then t3.encashTotalMoney else 0 end as encashTotalMoney,"
			+ " t1.countTime  from t_partner_daily_sale_count t1 left join t_partner_daily_charge_count t2 on "
			+ " t1.partnerId= t2.partnerId and t1.countTime = t2.countTime"
			+ " left join t_partner_daily_encash_count t3 on t1.partnerId= t3.partnerId and t1.countTime = t3.countTime "
			+ " left join t_partner_daily_award_count t4 on  t1.partnerId= t4.partnerId and t1.countTime = t4.countTime and "
			+ " t1.lotteryId=t4.lotteryId where t1.partnerId=#{partnerId} and t1.countTime=#{countTime}")
	public List<DailySaleAndCharge> getDailySaleAndCharge(@Param("partnerId") String partnerId,@Param("countTime")String countTime);

	
	@Select("select count(*) from (select partnerId from t_lottery_issue_sale_count where ${where}) t")
	public int getLotteryIssueSaleSum(@Param("where") String where);
	
	/**
	 * 根据多条件获取合作商销售情况（没有区别订单类型）
	 * @param where
	 * @return
	 */
	@Select("select * from (select * from t_lottery_issue_sale_count where ${where}) t order by issueNo desc")
	public List<LotteryIssueSale> getLotteryIssueSale(@Param("where") String where);
	

	@Select("select count(*) from (select partnerId from t_partner_daily_sale_count t1 where ${where}) t2")
	public int getLotteryDaySalesSum(@Param("where") String where);
	
	/**
	 * 根据多条件查询日销量兑奖提现充值信息
	 * @param where
	 * @return
	 */
	@Select("select t1.partnerId,"
			+ "sum(case when t4.awardPrizeMoney>0 then t4.awardPrizeMoney else 0 end) as awardPrizeTotalMoney, "
			+ "case when t2.chargeTotalMoney>0 then t2.chargeTotalMoney else 0 end as chargeTotalMoney,  "
			+ "case when t3.encashTotalMoney>0 then t3.encashTotalMoney else 0 end as encashTotalMoney,  "
			+ "sum(case when t1.totalMoney>0 then t1.totalMoney else 0 end)  as saleTotalMoney ,t1.countTime  from t_partner_daily_sale_count t1 left join t_partner_daily_charge_count t2 on "
			+ " t1.partnerId= t2.partnerId and t1.countTime = t2.countTime left join t_partner_daily_encash_count t3 on "
			+ " t1.partnerId= t3.partnerId and t1.countTime = t3.countTime left join t_partner_daily_award_count t4 on  t1.partnerId= t4.partnerId "
			+ "and t1.countTime = t4.countTime and t1.lotteryId=t4.lotteryId where ${where}")
	public List<LotteryDaySale> getLotteryDaySales(@Param("where") String where);

	/**
	 * 根据订单号查询竞彩的内容详情
	 * @param orderNo
	 * @return
	 */
	@Select("select * from ${tableName} where orderNo=#{orderNo}")
	public List<OrderDetail> getOrderDetails(@Param("tableName")String tableName , @Param("orderNo")String orderNo);
}
