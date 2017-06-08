package com.cqfc.partner.order.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cqfc.protocol.partnerorder.DailyAwardCount;
import com.cqfc.protocol.partnerorder.DailySaleCount;
import com.cqfc.protocol.partnerorder.LotteryIssueSale;
import com.jami.common.BaseMapper;


public interface TempOrderMapper extends BaseMapper{

	/**
	 * @param time
	 * @return
	 */
	@Delete("delete from t_temp_order where createTime < #{time} and lotteryId=#{lotteryType} limit 2500")
	public int deleteTempOrder(@Param("time") String time,@Param("lotteryType")int lotteryType);
	
	
	@Delete("delete from t_temp_jc_detail where createTime < #{time} and winOdds!='' limit 2500")
	public int deleteTempJcOrderDetail(@Param("time") String time);

	@Select("select count(*) from t_temp_order "
			+ "where createTime>=#{beginTime} and createTime<=#{endTime} and orderStatus=4")
	public long getTotalTicknumByDay(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

	@Select("select partnerId,lotteryId,issueNo,sum(case when orderStatus in (4,6,7,8,12) then 1 else 0 end) as sucNum,"
			+ " sum(case when orderStatus in (4,6,7,8,12) then totalAmount else 0 end) as sucMoney,sum(case when orderStatus in (10,11) then 1 else 0 end) as failNum,"
			+ " sum(case when orderStatus in (10,11) then totalAmount else 0 end) as failMoney  from  t_temp_order where ${where}")
	public List<LotteryIssueSale> getCurrentIssueSale(@Param("where")String where);


	@Select("select partnerId,lotteryId,sum(case when orderStatus in (4,6,7,8,12) then totalAmount  else 0 end) as totalMoney  from  t_temp_order  where ${where} ")
	public List<DailySaleCount>  getDailyReportSale(@Param("where") String where);


	@Select("select partnerId,lotteryId,sum(winningAmount*multiple) as awardPrizeMoney,sum(case when winningAmount*multiple<=1000000 then winningAmount*multiple when winningAmount*multiple>1000000 then round((winningAmount*multiple*0.8),0) end) as  afterPrizeMoney from  t_lottery_winning_result  where ${where} ")
	public List<DailyAwardCount>  getDailyReportAward(@Param("where") String where);
}
