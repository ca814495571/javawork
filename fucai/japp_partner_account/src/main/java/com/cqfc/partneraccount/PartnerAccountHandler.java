package com.cqfc.partneraccount;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import com.cqfc.partneraccount.service.IPartnerAccountService;
import com.cqfc.protocol.partneraccount.PartnerAccount;
import com.cqfc.protocol.partneraccount.PartnerAccountData;
import com.cqfc.protocol.partneraccount.PartnerAccountLog;
import com.cqfc.protocol.partneraccount.PartnerAccountLogData;
import com.cqfc.protocol.partneraccount.PartnerAccountService;
import com.cqfc.protocol.partneraccount.PartnerPreApply;
import com.cqfc.protocol.partneraccount.PartnerPreApplyData;
import com.cqfc.protocol.partneraccount.PartnerRecharge;
import com.cqfc.util.PartnerAccountConstant;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Service
public class PartnerAccountHandler implements PartnerAccountService.Iface {

	@Resource(name = "partnerAccountServiceImpl")
	private IPartnerAccountService partnerAccountService;

	/**
	 * 新增渠道账户
	 * 
	 * @param partnerAccount
	 * @return
	 * @throws TException
	 */
	@Override
	public int addPartnerAccount(PartnerAccount partnerAccount) throws TException {
		return partnerAccountService.addPartnerAccount(partnerAccount);
	}

	/**
	 * 查询渠道账户列表
	 * 
	 * @param partnerAccount
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws TException
	 */
	@Override
	public PartnerAccountData getPartnerAccountList(PartnerAccount partnerAccount, int currentPage, int pageSize)
			throws TException {
		return partnerAccountService.getPartnerAccountList(partnerAccount, currentPage, pageSize);
	}

	/**
	 * 根据渠道ID查询账户信息
	 * 
	 * @param partnerId
	 * @return
	 * @throws TException
	 */
	@Override
	public PartnerAccount findPartnerAccountByPartnerId(String partnerId) throws TException {
		return partnerAccountService.findPartnerAccountByPartnerId(partnerId);
	}

	/**
	 * 更新渠道账户状态(冻结、解冻)
	 * 
	 * @param partnerId
	 * @param state
	 * @return
	 * @throws TException
	 */
	@Override
	public int updatePartnerAccountState(String partnerId, int state) throws TException {
		return partnerAccountService.updatePartnerAccountState(partnerId, state);
	}

	/**
	 * 支付
	 * 
	 * @param partnerId
	 * @param amount
	 * @param serialNumber
	 * @return
	 * @throws TException
	 */
	@Override
	public int payPartnerAccount(String partnerId, long amount, String serialNumber) throws TException {
//		return 1;
		return partnerAccountService.payPartnerAccount(partnerId, amount, serialNumber);
	}

	/**
	 * 查询渠道账户流水日志列表
	 * 
	 * @param partnerAccountLog
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws TException
	 */
	@Override
	public PartnerAccountLogData getPartnerAccountLogList(PartnerAccountLog partnerAccountLog, int currentPage,
			int pageSize) throws TException {
		return partnerAccountService.getPartnerAccountLogList(partnerAccountLog, currentPage, pageSize);
	}

	/**
	 * 新增充值
	 * 
	 * @param partnerRecharge
	 * @return
	 * @throws TException
	 */
	@Override
	public int addPartnerRecharge(PartnerRecharge partnerRecharge) throws TException {
		return partnerAccountService.addPartnerRecharge(partnerRecharge);
	}

	/**
	 * 退款
	 * 
	 * @param partnerId
	 * @param amount
	 * @param paySerialNumber
	 * @param refundSerialNumber
	 * @return
	 * @throws TException
	 */
	@Override
	public int modifyRefund(String partnerId, long amount, String paySerialNumber, String refundSerialNumber)
			throws TException {
		return partnerAccountService.modifyRefund(partnerId, amount, paySerialNumber, refundSerialNumber);
	}

	/**
	 * 派奖
	 * 
	 * @param partnerId
	 * @param amount
	 * @param serialNumber
	 * @return
	 * @throws TException
	 */
	@Override
	public int sendPrize(String partnerId, long amount, String serialNumber) throws TException {
		return partnerAccountService.sendPrize(partnerId, amount, serialNumber);
	}

	/**
	 * 计算渠道商充值记录
	 * 
	 * @param partnerId
	 * @param dateTime
	 * @return
	 * @throws TException
	 */
	@Override
	public long countPartnerRechargeByDate(String partnerId, String dateTime) throws TException {
		return partnerAccountService.countPartnerRechargeByDate(partnerId, dateTime);
	}

	/**
	 * 创建渠道存款预申请记录
	 * 
	 * @param partnerPreApply
	 * @return
	 * @throws TException
	 */
	@Override
	public int createPartnerPreApply(PartnerPreApply partnerPreApply) throws TException {
		return partnerAccountService.createPartnerPreApply(partnerPreApply);
	}

	/**
	 * 查询渠道存款预申请记录
	 * 
	 * @param partnerId
	 * @param partnerUniqueNo
	 * @return
	 * @throws TException
	 */
	@Override
	public PartnerPreApply findPartnerPreApply(String partnerId, String partnerUniqueNo) throws TException {
		return partnerAccountService.findPartnerPreApply(partnerId, partnerUniqueNo);
	}

	/**
	 * 分页查询预存款申请记录
	 * 
	 * @param partnerPreApply
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws TException
	 */
	@Override
	public PartnerPreApplyData getPartnerPreApplyList(PartnerPreApply partnerPreApply, int currentPage, int pageSize)
			throws TException {
		return partnerAccountService.getPartnerPreApplyList(partnerPreApply, currentPage, pageSize);
	}

	/**
	 * 预存款申请审核
	 * 
	 * @param preApplyId
	 * @param status
	 * @return
	 * @throws TException
	 */
	@Override
	public int auditPartnerPreApply(long preApplyId, int status) throws TException {
		// TODO Auto-generated method stub
		int isSuccess = 0;
		isSuccess = partnerAccountService.auditPartnerPreApply(preApplyId, status);
		return isSuccess;
	}

	@Override
	public Map<String, Long> statisticRecharge(String date) throws TException {
		Log.run.debug("statisticRecharge date=%s", date);
		Map<String, Long> map = partnerAccountService.statisticRecharge(date);
		Log.run.debug("result is %s", map);
		return map;
	}

	@Override
	public long totalAccountMoney() throws TException {
		Log.run.debug("get partner totalAccountMoney" );
		long totalMoney = partnerAccountService.totalAccountMoney();
		Log.run.debug("totalAccountMoney is %d", totalMoney);
		return totalMoney;
	}

	@Override
	public long totalPaylogNum(String date) throws TException {
		Log.run.debug("get partner totalPaylogNum" );
		long totalPaylogNum = 0;
		try {
			totalPaylogNum = partnerAccountService.totalPaylogNum(date);
		} catch (Exception e) {
			Log.run.debug("", e);
		}
		Log.run.debug("totalPaylogNum is %d", totalPaylogNum);
		return totalPaylogNum;
	}

	@Override
	public int updatePartnerAccountCreditLimit(String partnerId,
			long creditLimit) throws TException {
		return partnerAccountService.updatePartnerAccountCreditLimit(partnerId, creditLimit);
	}

}
