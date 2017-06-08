package com.cqfc.useraccount.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.protocol.useraccount.UserAccountLog;
import com.cqfc.protocol.useraccount.UserHandsel;
import com.cqfc.protocol.useraccount.UserHandselCount;
import com.cqfc.protocol.useraccount.UserRecharge;
import com.cqfc.protocol.useraccount.WithdrawAccount;
import com.cqfc.protocol.useraccount.WithdrawApply;
import com.cqfc.util.Pair;
import com.jami.common.BaseMapper;
import com.jami.common.StatisticResult;

/**
 * @author liwh
 */
public interface AccountInfoMapper extends BaseMapper {

	/**
	 * 创建彩金
	 * 
	 * @param userHandsel
	 * @return
	 */
	@Insert("insert into t_lottery_user_handsel${tableIndex}"
			+ "(userId,partnerId,activityId,partnerHandselId,serialNumber,presentAmount,usableAmount,usedAmount,presentTime,validTime,failureTime,createTime) "
			+ "values(#{userHandsel.userId},#{userHandsel.partnerId},#{userHandsel.activityId},#{userHandsel.partnerHandselId},#{userHandsel.serialNumber},#{userHandsel.presentAmount},#{userHandsel.usableAmount},#{userHandsel.usedAmount},#{userHandsel.presentTime},#{userHandsel.validTime},#{userHandsel.failureTime},now())")
	public int createUserHandsel(@Param("userHandsel") UserHandsel userHandsel, @Param("tableIndex") String tableIndex);

	/**
	 * 创建账户流水
	 * 
	 * @param userAccountLog
	 * @return
	 */
	@Insert("insert into t_lottery_account_log${tableIndex}"
			+ "(userId,nickName,partnerId,logType,totalAmount,accountAmount,handselAmount,serialNumber,ext,remark,handselRemark,createTime) "
			+ "values(#{accountLog.userId},#{accountLog.nickName},#{accountLog.partnerId},#{accountLog.logType},#{accountLog.totalAmount},#{accountLog.accountAmount},#{accountLog.handselAmount},#{accountLog.serialNumber},#{accountLog.ext},#{accountLog.remark},#{accountLog.handselRemark},now())")
	public int createUserAccountLog(@Param("accountLog") UserAccountLog userAccountLog, @Param("tableIndex") String tableIndex);

	/**
	 * 分页查询赠送彩金记录
	 * 
	 * @param conditions
	 *            搜索条件字符串
	 * @return
	 */
	@Select("select * from t_lottery_user_handsel${tableIndex} where ${conditions}")
	public List<UserHandsel> getUserHandselList(@Param("conditions") String conditions, @Param("tableIndex") String tableIndex);

	/**
	 * 计算赠送彩金记录总数
	 * 
	 * @param conditions
	 *            搜索条件字符串
	 * @return
	 */
	@Select("select count(*) from t_lottery_user_handsel${tableIndex} where ${conditions}")
	public int countUserHandselSize(@Param("conditions") String conditions, @Param("tableIndex") String tableIndex);

	/**
	 * 通过用户ID或彩金状态查询彩金列表
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	@Select("select * from t_lottery_user_handsel${tableIndex} where ${conditions}")
	public List<UserHandsel> getUserHandselListByUserId(@Param("conditions") String conditions, @Param("tableIndex") String tableIndex);

	/**
	 * 更新彩金状态
	 * 
	 * @param handselId
	 *            彩金ID
	 * @param state
	 *            状态：1有效 2无效
	 * @return
	 */
	@Update("update t_lottery_user_handsel${tableIndex} set state=#{state} where handselId=#{handselId}")
	public int updateUserHandselState(@Param("handselId") long handselId, @Param("state") int state, @Param("tableIndex") String tableIndex);

	/**
	 * 创建提款帐号信息
	 * 
	 * @param withdrawAccount
	 * @return
	 */
	@Insert("insert into t_lottery_withdraw_account${tableIndex}"
			+ "(userId,realName,accountType,accountNo,bankType,bankName,bankCardType,payAccountState,accountAddress,createTime) "
			+ "values(#{withdrawAccount.userId},#{withdrawAccount.realName},#{withdrawAccount.accountType},#{withdrawAccount.accountNo},#{withdrawAccount.bankType},#{withdrawAccount.bankName},#{withdrawAccount.bankCardType},#{withdrawAccount.payAccountState},#{withdrawAccount.accountAddress},now())")
	public int createWithdrawAccount(@Param("withdrawAccount") WithdrawAccount withdrawAccount, @Param("tableIndex") String tableIndex);

	/**
	 * 更新提款帐号信息
	 * 
	 * @param withdrawAccount
	 * @return
	 */
	@Update("update t_lottery_withdraw_account${tableIndex} set "
			+ "accountType=#{withdrawAccount.accountType},accountId=#{withdrawAccount.accountId},bankType=#{withdrawAccount.bankType},bankName=#{withdrawAccount.bankName},"
			+ "bankCardType=#{withdrawAccount.bankCardType},payAccountState=#{withdrawAccount.payAccountState},"
			+ "accountAddress=#{withdrawAccount.accountAddress} where withdrawAccountId=#{withdrawAccount.withdrawAccountId}")
	public int updateWithdrawAccount(@Param("withdrawAccount") WithdrawAccount withdrawAccount, @Param("tableIndex") String tableIndex);

	/**
	 * 查询用户提款帐号列表
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	@Select("select * from t_lottery_withdraw_account${tableIndex} where userId=#{userId}")
	public List<WithdrawAccount> getWithdrawAccountListByUserId(@Param("userId") long userId, @Param("tableIndex") String tableIndex);

	/**
	 * 根据提款帐号ID查询
	 * 
	 * @param withdrawAccountId
	 *            提款帐号ID
	 * @return
	 */
	@Select("select * from t_lottery_withdraw_account${tableIndex} where withdrawAccountId=#{withdrawAccountId}")
	public WithdrawAccount findWithdrawAccountById(@Param("withdrawAccountId") int withdrawAccountId, @Param("tableIndex") String tableIndex);

	/**
	 * 创建提现记录
	 * 
	 * @param withdrawApply
	 * @return
	 */
	@Insert("insert into t_lottery_withdraw_apply"
			+ "(userId,realName,partnerId,serialNumber,withdrawAccountId,withdrawAmount,totalAmount,partnerApplyId,withdrawMsgId,createTime) "
			+ "values(#{withdrawApply.userId},#{withdrawApply.realName},#{withdrawApply.partnerId},#{withdrawApply.serialNumber},#{withdrawApply.withdrawAccountId},#{withdrawApply.withdrawAmount},#{withdrawApply.totalAmount},#{withdrawApply.partnerApplyId},#{withdrawApply.withdrawMsgId},now())")
	public int createWithdrawApply(@Param("withdrawApply") WithdrawApply withdrawApply);

	/**
	 * 修改提现申请
	 * 
	 * @param withdrawApply
	 * @return
	 */
	/*
	 * @Update("update t_lottery_withdraw_Apply set " +
	 * "nickName=#{nickName},serialNumber=#{serialNumber},withdrawAccountId=#{withdrawAccountId},withdrawAmount=#{withdrawAmount}"
	 * +" where applyId=#{applyId}") public int
	 * updateWithdrawApply(WithdrawApply withdrawApply);
	 */

	/**
	 * 根据提现ID查询提现记录
	 * 
	 * @param applyId
	 *            提现申请ID
	 * @return
	 */
	@Select("select * from t_lottery_withdraw_apply where applyId=#{applyId}")
	public WithdrawApply findWithdrawApplyByApplyId(@Param("applyId") int applyId);

	/**
	 * 根据用户ID、流水号查询提现记录
	 * 
	 * @param userId
	 *            用户ID
	 * @param serialNumber
	 *            流水号
	 * @return
	 */
	@Select("select * from t_lottery_withdraw_apply where userId=#{userId} and serialNumber=#{serialNumber}")
	public WithdrawApply findWithdrawApply(@Param("userId") long userId, @Param("serialNumber") String serialNumber);

	/**
	 * 删除已创建的提现记录
	 * @param userId
	 *            用户ID
	 * @param serialNumber
	 *            流水号
	 * @return
	 */
	@Delete("delete from t_lottery_withdraw_apply where userId=#{userId} and serialNumber=#{serialNumber}")
	public int deleteWithdrawApply(@Param("userId") long userId, @Param("serialNumber") String serialNumber);
	/**
	 * 根据用户ID查询提现列表
	 * 
	 * @param userId
	 * @return
	 */
	@Select("select * from t_lottery_withdraw_apply where userId=#{userId}")
	public List<WithdrawApply> getWithdrawApplyListByUserId(@Param("userId") long userId);

	/**
	 * 提现审核（用户ID、流水号、审核状态、审核人ID、审核人、审核备注）、成功:处理金额
	 * 
	 * @param withdrawApply
	 * @return
	 */
	@Update("update t_lottery_withdraw_apply set auditState=#{withdrawApply.auditState},auditRemark=#{withdrawApply.auditRemark},"
			+ "auditId=#{withdrawApply.auditId},auditName=#{withdrawApply.auditName},auditTime=#{withdrawApply.auditTime} where applyId=#{withdrawApply.applyId} and auditState=1")
	public int auditWithdrawApply(@Param("withdrawApply") WithdrawApply withdrawApply);

	/**
	 * 回滚提现申请审批
	 * @param applyId
	 * @return
	 */
	@Update("update t_lottery_withdraw_apply set auditState=1  where applyId=#{applyId}")
	public int rollbackWithdrawApply(@Param("applyId") int applyId);
	
	/**
	 * 创建充值记录
	 * 
	 * @param userRecharge
	 * @return
	 */
	@Insert("insert into t_lottery_user_recharge${tableIndex}"
			+ "(userId,nickName,partnerId,serialNumber,partnerRechargeId,rechargeAmount,rechargeType,remark,createTime) "
			+ "values(#{userRecharge.userId},#{userRecharge.nickName},#{userRecharge.partnerId},#{userRecharge.serialNumber},#{userRecharge.partnerRechargeId},#{userRecharge.rechargeAmount},#{userRecharge.rechargeType},#{userRecharge.remark},now())")
	public int createUserRecharge(@Param("userRecharge") UserRecharge userRecharge, @Param("tableIndex") String tableIndex);

	/**
	 * 查询用户充值记录列表
	 * 
	 * @param userId
	 * @return
	 */
	@Select("select * from t_lottery_user_recharge${tableIndex} where userId=#{userId}")
	public List<UserRecharge> getUserRechargeList(@Param("userId") long userId, @Param("tableIndex") String tableIndex);

	/**
	 * 分页查询流水日志
	 * 
	 * @param conditions
	 * @return
	 */
	@Select("select * from t_lottery_account_log${tableIndex} where ${conditions}")
	public List<UserAccountLog> getUserAccountLogList(@Param("conditions") String conditions, @Param("tableIndex") String tableIndex);

	/**
	 * 计算流水日志总记录数
	 * 
	 * @param conditions
	 * @return
	 */
	@Select("select count(*) from t_lottery_account_log${tableIndex} where ${conditions}")
	public int countUserAccountLogSize(@Param("conditions") String conditions, @Param("tableIndex") String tableIndex);

	/**
	 * 根据状态、流水号查询
	 * 
	 * @param logType
	 *            日志类型：1充值 2支付 3提现 4退款 5派奖 6彩金赠送 7彩金失效
	 * @param serialNumber
	 *            流水号
	 * @return
	 */
	@Select("select * from t_lottery_account_log${tableIndex} where logType=#{logType} and serialNumber=#{serialNumber}")
	public UserAccountLog findUserAccountLog(@Param("logType") int logType, @Param("serialNumber") String serialNumber, @Param("tableIndex") String tableIndex);

	/**
	 * 修改彩金金额
	 * 
	 * @param userHandsel
	 * @return
	 */
	@Update("update t_lottery_user_handsel${tableIndex} set usableAmount=#{userHandsel.usableAmount},usedAmount=#{userHandsel.usedAmount}, version=version+1"
			+ " where handselId=#{userHandsel.handselId} and version=#{userHandsel.version}")
	public int modifyUserHandsel(@Param("userHandsel") UserHandsel userHandsel, @Param("tableIndex") String tableIndex);

	/**
	 * 通过彩金ID查询彩金
	 * 
	 * @param handselId
	 * @return
	 */
	@Select("select * from t_lottery_user_handsel${tableIndex} where handselId=#{handselId}")
	public UserHandsel findUserHandselById(@Param("handselId") int handselId, @Param("tableIndex") String tableIndex);

	/**
	 * 查询已退款流水列表
	 * 
	 * @param ext
	 *            支付流水号
	 * @return
	 */
	@Select("select * from t_lottery_account_log${tableIndex} where ext=#{ext}")
	public List<UserAccountLog> findUserAccountLogByExt(@Param("ext") String ext, @Param("tableIndex") String tableIndex);

	/**
	 * 查询充值记录
	 * 
	 * @param partnerId
	 * @param partnerChargeId
	 * @return
	 */
	@Select("select * from t_lottery_user_recharge${tableIndex} where partnerId=#{partnerId} and partnerRechargeId=#{partnerRechargeId}")
	public UserRecharge findUserRecharge(@Param("partnerId") String partnerId,
			@Param("partnerRechargeId") String partnerChargeId, @Param("tableIndex") String tableIndex);
	
	@Select("select partnerId, sum(rechargeAmount) as totalAmount from t_lottery_user_recharge${tableIndex} where createTime>=#{beginTime} and createTime<=#{endTime} group by partnerId")
	public List<StatisticResult> countRecharge(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("tableIndex") String tableIndex);;
	/**
	 * 分页查询提现申请
	 * 
	 * @param conditions
	 * @return
	 */
	@Select("select * from t_lottery_withdraw_apply where ${conditions} ")
	public List<WithdrawApply> getWithdrawApplyList(@Param("conditions") String conditions);

	/**
	 * 计算提现申请记录
	 * 
	 * @param conditions
	 * @return
	 */
	@Select("select count(*) from t_lottery_withdraw_apply where ${conditions}")
	public int countWithdrawApplySize(@Param("conditions") String conditions);

	/**
	 * 根据彩金交易在合作商的id查询
	 * 
	 * @param partnerHandselId
	 * @param string 
	 * @return
	 */
	@Select("select * from t_lottery_user_handsel${tableIndex} where partnerId=#{partnerId} and partnerHandselId=#{partnerHandselId}")
	public UserHandsel findUserHandselByPartnerId(@Param("partnerId") String partnerId, @Param("partnerHandselId") String partnerHandselId, @Param("tableIndex") String tableIndex);

	/**
	 * 根据帐号查询提现帐号信息
	 * 
	 * @param accountNo
	 * @return
	 */
	@Select("select * from t_lottery_withdraw_account${tableIndex} where accountNo=#{accountNo}")
	public WithdrawAccount findWithdrawAccountByNo(@Param("accountNo") String accountNo, @Param("tableIndex") String tableIndex);

	/**
	 * 获取当前创建帐号ID
	 * 
	 * @return
	 */
	@Select("SELECT LAST_INSERT_ID()")
	public int getCurrentInsertAccountId();

	/**
	 * 按渠道商统计提现金额总数
	 * @param beginTime 开始时间
	 * @param endTime 结束时间 
	 * @param state 状态为审核通过
	 * @return
	 */
	@Select("select partnerId, sum(withdrawAmount) as totalAmount from t_lottery_withdraw_apply where createTime>=#{beginTime} and createTime<=#{endTime} and auditState=#{state} group by partnerId")
	public List<StatisticResult> countWithdraw(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("state") int state);
	
	@Select("select sum(usableAmount) from t_lottery_user_handsel${tableIndex} where failureTime < now()")
	public Long getTotalInvalidHandsel(@Param("tableIndex") String tableIndex);
	
	@Update("update t_lottery_user_handsel${tableIndex} set state=2 where failureTime < now()")
	public int updateHandselState(@Param("tableIndex") String tableIndex);

	@Insert("insert into t_lottery_handsel_daily_count(day) values(#{day})")
	public int createHandselCount(@Param("day")String day);
	
	@Update("update t_lottery_handsel_daily_count set totalHandsel=totalHandsel+#{money} where day=#{day}")
	public int increaseHandselCount(@Param("day")String day, @Param("money") long money);

	@Update("update t_lottery_handsel_daily_count set totalInvalidHandsel=#{money} where day=#{day}")
	public int setInvalidHandsel(@Param("day")String day, @Param("money") long money);

	@Select("select * from t_lottery_handsel_daily_count where day=#{day}")
	public UserHandselCount getUserHandselCount(@Param("day")String day);

	@Select("select logType as first, count(*) as second from t_lottery_account_log${tableIndex} where createTime>=#{beginTime} and createTime<=#{endTime} and  (logType=2 or logType=4) group by logType")
	public  List<Pair<Integer, Long>> totalPaylogNum(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("tableIndex") String tableIndex);
}
