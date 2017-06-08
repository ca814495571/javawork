package com.cqfc.partneraccount.service;

import java.util.Map;

import org.apache.thrift.TException;

import com.cqfc.protocol.partneraccount.PartnerAccount;
import com.cqfc.protocol.partneraccount.PartnerAccountData;
import com.cqfc.protocol.partneraccount.PartnerAccountLog;
import com.cqfc.protocol.partneraccount.PartnerAccountLogData;
import com.cqfc.protocol.partneraccount.PartnerPreApply;
import com.cqfc.protocol.partneraccount.PartnerPreApplyData;
import com.cqfc.protocol.partneraccount.PartnerRecharge;

/**
 * @author liwh
 */
public interface IPartnerAccountService {

	/**
	 * 新增渠道账户
	 * 
	 * @param partnerAccount
	 * @return
	 */
	public int addPartnerAccount(PartnerAccount partnerAccount);

	/**
	 * 查询渠道账户列表
	 * 
	 * @param partnerAccount
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PartnerAccountData getPartnerAccountList(PartnerAccount partnerAccount, int currentPage, int pageSize);

	/**
	 * 根据渠道ID查询账户信息
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @return
	 */
	public PartnerAccount findPartnerAccountByPartnerId(String partnerId);

	/**
	 * 更新渠道账户状态(冻结、解冻)
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param state
	 *            状态：1正常 2冻结
	 * @return
	 */
	public int updatePartnerAccountState(String partnerId, int state);

	/**
	 * 支付
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param amount
	 *            金额
	 * @param serialNumber
	 *            支付流水号
	 * @return
	 */
	public int payPartnerAccount(String partnerId, long amount, String serialNumber);

	/**
	 * 查询渠道账户流水日志列表
	 * 
	 * @param partnerAccountLog
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * @return
	 */
	public PartnerAccountLogData getPartnerAccountLogList(PartnerAccountLog partnerAccountLog, int currentPage,
			int pageSize);

	/**
	 * 新增充值
	 * 
	 * @param partnerRecharge
	 * @return
	 */
	public int addPartnerRecharge(PartnerRecharge partnerRecharge);

	/**
	 * 退款
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param amount
	 *            退款金额
	 * @param paySerialNumber
	 *            支付流水号
	 * @param refundSerialNumber
	 *            退款流水号
	 * @return
	 */
	public int modifyRefund(String partnerId, long amount, String paySerialNumber, String refundSerialNumber);

	/**
	 * 派奖
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param amount
	 *            派奖金额
	 * @param serialNumber
	 *            派奖流水日志
	 * @return
	 */
	public int sendPrize(String partnerId, long amount, String serialNumber);

	/**
	 * 查询渠道商充值记录
	 * 
	 * @param partnerId
	 *            渠道商ID
	 * @param dateTime
	 *            日期
	 * @return
	 */
	public long countPartnerRechargeByDate(String partnerId, String dateTime);

	/**
	 * 创建渠道预存款申请记录
	 * 
	 * @param partnerPreApply
	 * @return
	 */
	public int createPartnerPreApply(PartnerPreApply partnerPreApply);

	/**
	 * 查询渠道存款预申请记录
	 * 
	 * @param partnerId
	 *            渠道商ID
	 * @param partnerUniqueNo
	 *            渠道交易唯一号
	 * @return
	 */
	public PartnerPreApply findPartnerPreApply(String partnerId, String partnerUniqueNo);

	/**
	 * 分页查询预存款申请记录
	 * 
	 * @param partnerPreApply
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws TException
	 */
	public PartnerPreApplyData getPartnerPreApplyList(PartnerPreApply partnerPreApply, int currentPage, int pageSize);

	/**
	 * 通过预存款申请ID查询信息
	 * 
	 * @param preApplyId
	 * @return
	 */
	public PartnerPreApply findPartnerPreApplyByPreApplyId(long preApplyId);

	/**
	 * 审核预存款申请
	 * 
	 * @param preApplyId
	 *            预申请ID
	 * @param status
	 *            状态: 0 待审核 1 审核通过 2 审核未通过
	 * @return
	 */
	public int auditPartnerPreApply(long preApplyId, int status);

	/**
	 * 增加账户预存款金额
	 * 
	 * @param partnerId
	 * @param amount
	 * @return
	 */
	public int addPartnerPreApplyAmount(String partnerId, long amount);
	
	/**
	 * 统计充值金额
	 * @param date 要统计的时间，格式为 yyyy-MM-dd
	 * @return 返回Map,key为渠道商Id(partnerId),value为金额
	 */
	public Map<String,Long> statisticRecharge(String date);

	/**
	 * 统计当前所有渠道商帐户金额
	 * @return
	 */
	public long totalAccountMoney();

	/**
	 * 统计一天的支付流水
	 * @param date
	 * @return
	 */
	public long totalPaylogNum(String date);
	
	/**
	 * 更新渠道账户信用额度
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param creditLimit
	 *            信用额度
	 * @return
	 */
	public int updatePartnerAccountCreditLimit(String partnerId,
			long creditLimit);

}
