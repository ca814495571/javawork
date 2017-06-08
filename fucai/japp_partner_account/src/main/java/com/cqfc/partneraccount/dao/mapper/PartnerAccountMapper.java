package com.cqfc.partneraccount.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.protocol.partneraccount.PartnerAccount;
import com.cqfc.protocol.partneraccount.PartnerAccountLog;
import com.cqfc.protocol.partneraccount.PartnerPreApply;
import com.cqfc.protocol.partneraccount.PartnerRecharge;
import com.cqfc.util.Pair;
import com.jami.common.BaseMapper;
import com.jami.common.StatisticResult;

/**
 * @author liwh
 */
public interface PartnerAccountMapper extends BaseMapper {

	@Insert("insert into t_lottery_partner_account(partnerId,creditLimit,alarmValue,createTime) "
			+ "values(#{partnerId},#{creditLimit},#{alarmValue},now())")
	public int addPartnerAccount(PartnerAccount partnerAccount);

	@Select("select * from t_lottery_partner_account where ${conditions}")
	public List<PartnerAccount> getPartnerAccountList(@Param("conditions") String conditions);

	@Select("select count(*) from t_lottery_partner_account where ${conditions}")
	public int countAccountSize(@Param("conditions") String conditions);

	@Select("select * from t_lottery_partner_account where partnerId = #{partnerId}")
	public PartnerAccount findPartnerAccountByPartnerId(@Param("partnerId") String partnerId);

	@Update("update t_lottery_partner_account set state = #{state} where partnerId = #{partnerId}")
	public int updatePartnerAccountState(@Param("partnerId") String partnerId, @Param("state") int state);

	@Update("update t_lottery_partner_account set totalAmount=totalAmount-#{amount},usableAmount=usableAmount-#{amount} where partnerId = #{partnerId} and (usableAmount + creditLimit)  >= #{amount}")
	public int updateDeductAccount(@Param("partnerId") String partnerId, @Param("amount") long amount);

	@Insert("insert into t_lottery_partner_account_log "
			+ "(partnerId,state,accountAmount,ext,serialNumber,remark,createTime)"
			+ "values (#{partnerId},#{state},#{accountAmount},#{ext},#{serialNumber},#{remark},now())")
	public int addPartnerAccountLog(PartnerAccountLog partnerAccountLog);

	@Select("select * from t_lottery_partner_account_log where serialNumber = #{serialNumber} and state = #{state}")
	public PartnerAccountLog findPartnerAccountLog(@Param("serialNumber") String serialNumber, @Param("state") int state);

	@Select("select * from t_lottery_partner_account_log where ${conditions}")
	public List<PartnerAccountLog> getPartnerAccountLogList(@Param("conditions") String conditions);

	@Select("select count(*) from t_lottery_partner_account_log where ${conditions}")
	public int countAccountLogSize(@Param("conditions") String conditions);

	@Insert("insert into t_lottery_partner_recharge "
			+ "(partnerId,serialNumber,rechargeAmount,rechargeType,remark,createTime)"
			+ "values (#{partnerId},#{serialNumber},#{rechargeAmount},#{rechargeType},#{remark},now())")
	public int addPartnerRecharge(PartnerRecharge partnerRecharge);

	@Update("update t_lottery_partner_account set totalAmount=totalAmount+#{amount},usableAmount=usableAmount+#{amount} where partnerId=#{partnerId}")
	public int increaseAccount(@Param("partnerId") String partnerId, @Param("amount") long amount);

	@Select("select * from t_lottery_partner_recharge where serialNumber = #{serialNumber}")
	public PartnerRecharge findPartnerRecharge(@Param("serialNumber") String serialNumber);

	@Update("update t_lottery_partner_account_log set refundAmount=refundAmount+#{refundAmount} where serialNumber=#{serialNumber} and accountAmount>=refundAmount+#{refundAmount} and state=2")
	public int updatePartnerAccountLog(@Param("serialNumber") String serialNumber,
			@Param("refundAmount") long refundAmount);

	@Select("select * from t_lottery_partner_account_log where ext=#{ext} and partnerId=#{partnerId}")
	public List<PartnerAccountLog> getPartnerAccountLogListByExt(@Param("partnerId") String partnerId,
			@Param("ext") String ext);

	@Select("select * from t_lottery_partner_account_log where serialNumber=#{serialNumber} and partnerId=#{partnerId}")
	public PartnerAccountLog findPartnerAccountLogBySerialNumber(@Param("partnerId") String partnerId,
			@Param("serialNumber") String serialNumber);

	@Select("select * from t_lottery_partner_recharge where partnerId=#{partnerId} and createTime>=#{beginTime} and createTime<=#{endTime}")
	public List<PartnerRecharge> getPartnerRechargeList(@Param("partnerId") String partnerId,
			@Param("beginTime") String beginTime, @Param("endTime") String endTime);

	@Insert("insert into t_lottery_partner_preapply(partnerId,partnerUniqueNo,preMoney,createTime)"
			+ "values (#{partnerId},#{partnerUniqueNo},#{preMoney},now())")
	public int createPartnerPreApply(PartnerPreApply partnerPreApply);

	@Select("select * from t_lottery_partner_preapply where partnerId=#{partnerId} and partnerUniqueNo=#{partnerUniqueNo}")
	public PartnerPreApply findPartnerPreApply(@Param("partnerId") String partnerId,
			@Param("partnerUniqueNo") String partnerUniqueNo);

	@Select("select * from t_lottery_partner_preapply where ${conditions}")
	public List<PartnerPreApply> getPartnerPreApplyList(@Param("conditions") String conditions);

	@Select("select count(*) from t_lottery_partner_preapply where ${conditions}")
	public int countPartnerPreApplySize(@Param("conditions") String conditions);

	@Select("select * from t_lottery_partner_preapply where preApplyId=#{preApplyId}")
	public PartnerPreApply findPartnerPreApplyByPreApplyId(@Param("preApplyId") long preApplyId);

	@Update("update t_lottery_partner_preapply set status=#{status} where preApplyId=#{preApplyId} and status=0")
	public int auditPartnerPreApply(@Param("preApplyId") long preApplyId, @Param("status") int status);

	@Select("select partnerId, sum(rechargeAmount) as totalAmount from t_lottery_partner_recharge where createTime>=#{beginTime} and createTime<=#{endTime} group by partnerId")
	public List<StatisticResult> countRecharge(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

	@Select("select state as first, count(*) as second from t_lottery_partner_account_log where createTime>=#{beginTime} and createTime<=#{endTime} and (state=2 or state=4) group by state")
	public List<Pair<Integer, Long>> totalPaylogNum(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

	@Delete("delete from t_lottery_partner_account_log where createTime < #{time} limit 2500")
	public int clearAccountLog(@Param("time") String time);

	@Update("update t_lottery_partner_account set creditLimit = #{creditLimit} where partnerId = #{partnerId}")
	public int updatePartnerAccountCreditLimit(@Param("partnerId") String partnerId,
			@Param("creditLimit") long creditLimit);
}
