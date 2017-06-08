package com.cqfc.partneraccount.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;

import javax.annotation.Resource;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.cqfc.partneraccount.dao.PartnerAccountDao;
import com.cqfc.partneraccount.datacenter.PartnerAccountBuffer;
import com.cqfc.partneraccount.service.IPartnerAccountService;
import com.cqfc.protocol.partneraccount.PartnerAccount;
import com.cqfc.protocol.partneraccount.PartnerAccountData;
import com.cqfc.protocol.partneraccount.PartnerAccountLog;
import com.cqfc.protocol.partneraccount.PartnerAccountLogData;
import com.cqfc.protocol.partneraccount.PartnerPreApply;
import com.cqfc.protocol.partneraccount.PartnerPreApplyData;
import com.cqfc.protocol.partneraccount.PartnerRecharge;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DaoLevelException;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IdWorker;
import com.cqfc.util.PartnerAccountConstant;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;
import com.jami.util.PartnerLogger;

/**
 * @author liwh
 */
@Service
public class PartnerAccountServiceImpl implements IPartnerAccountService {

	/**
	 * 支付标志
	 */
	private static final String PAY_FLAG = "_pay_";
	/**
	 * 退款标志
	 */
	private static final String REFUND_FLAG = "_refund_";
	/**
	 * 派奖标志
	 */
	private static final String SEND_PRIZE_FLAG = "_sendPrize_";
	@Resource
	private PartnerAccountDao partnerAccountDao;

	@Autowired
	private MemcachedClient memcachedClient;

	private Map<String, Lock> lockMap = new HashMap<String, Lock>();

	@Override
	public int addPartnerAccount(PartnerAccount partnerAccount) {

		int isSuccess = 0;
		try {
			isSuccess = partnerAccountDao.addPartnerAccount(partnerAccount);
			partnerAccount.setCreateTime(DateUtil.dateToString(new Date(),
					DateUtil.DATE_FORMAT_DATETIME));
			PartnerAccountBuffer.addPartnerAccount(partnerAccount);
		} catch (DaoLevelException e) {
			return Integer.valueOf(e.getMessage());
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error("addPartnerAccount error,  err=%s", e.toString());
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
		return isSuccess;
	}

	@Override
	public PartnerAccountData getPartnerAccountList(
			PartnerAccount partnerAccount, int currentPage, int pageSize) {
		// return partnerAccountDao.getPartnerAccountList(partnerAccount,
		// currentPage, pageSize);
		return PartnerAccountBuffer.getPartnerAccountList(partnerAccount,
				currentPage, pageSize);
	}

	@Override
	public PartnerAccount findPartnerAccountByPartnerId(String partnerId) {
		// return partnerAccountDao.findPartnerAccountByPartnerId(partnerId);
		return PartnerAccountBuffer.findPartnerAccountByPartnerId(partnerId);
	}

	@Override
	public int updatePartnerAccountState(String partnerId, int state) {
		int isSuccess = 0;
		try {
			isSuccess = partnerAccountDao.updatePartnerAccountState(partnerId,
					state);
			PartnerAccountBuffer.updatePartnerAccountState(partnerId, state);
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error("payParterAccount error,  err=%s", e.toString());
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
		return isSuccess;
	}

	private String getMemcacheKey(String partnerId, String serialNumber,
			String flag) {
		return ConstantsUtil.MODULENAME_PARTNER_ACCOUNT + flag + partnerId
				+ "_" + serialNumber;
	}

	@Override
	// @Transactional
	public int payPartnerAccount(String partnerId, long amount,
			String serialNumber) {
		Log.run.debug(
				"payPartnerAccount, partnerId=%s, amount=%d, serialNumber=%s",
				partnerId, amount, serialNumber);
		int isSuccess = 0;// , isLogSuccess = 0;
		try {
			String payKey = getMemcacheKey(partnerId, serialNumber, PAY_FLAG);
			Object object = memcachedClient.get(payKey);
			if (object != null) {
				Log.run.warn("Has already pay.");
				return ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST;
			}
			isSuccess = PartnerAccountBuffer.updateAccount(partnerId,
					0 - amount, serialNumber,
					PartnerAccountConstant.PartnerAccountLogType.PAYMENT,
					"支付扣款");

			if (isSuccess > 0) {
				memcachedClient.set(payKey, 0, serialNumber);
			}
			// 写支付流水日志
			// PartnerAccountLog partnerAccountLog = new PartnerAccountLog();
			// partnerAccountLog.setPartnerId(partnerId);
			// partnerAccountLog.setState(PartnerAccountConstant.PartnerAccountLogType.PAYMENT.getValue());
			// partnerAccountLog.setAccountAmount(amount);
			// partnerAccountLog.setSerialNumber(serialNumber);
			// partnerAccountLog.setRemark("支付扣款");
			// // isLogSuccess =
			// partnerAccountDao.addPartnerAccountLog(partnerAccountLog);
			// // if (isLogSuccess <= 0) {
			// // return isLogSuccess;
			// // }
			//
			// LotteryLogger logger = PartnerLogger.getDynamicLogger(partnerId);
			// logger.info(PartnerAccountLogUtil.convertLog2Str(partnerAccountLog));
			// memcachedClient.set(payKey, 0, serialNumber);
			//
			// // 扣账户金额
			// isSuccess = partnerAccountDao.updateDeductAccount(partnerId,
			// amount);
		} catch (DaoLevelException e) {
			// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Log.run.debug("", e);
			Log.run.error("payParterAccount error, serialNum = %s, err=%s",
					serialNumber, e.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Log.run.debug("", e);
			Log.run.error("payParterAccount error, serialNum = %s, err=%s",
					serialNumber, e.toString());
		} catch (MemcachedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Log.run.debug("", e);
			Log.run.error("payParterAccount error, serialNum = %s, err=%s",
					serialNumber, e.toString());
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error("payParterAccount error, serialNum = %s, err=%s",
					serialNumber, e.toString());
		}
		return isSuccess;
	}

	@Override
	public PartnerAccountLogData getPartnerAccountLogList(
			PartnerAccountLog partnerAccountLog, int currentPage, int pageSize) {
		return partnerAccountDao.getPartnerAccountLogList(partnerAccountLog,
				currentPage, pageSize);
	}

	@Override
	// @Transactional
	public int addPartnerRecharge(PartnerRecharge partnerRecharge) {
		Log.run.debug(
				"addPartnerRecharge partnerId=%s, amount=%d, serialNumber=%s",
				partnerRecharge.getPartnerId(),
				partnerRecharge.getRechargeAmount(),
				partnerRecharge.getSerialNumber());
		int isSuccess = 0, isLogSuccess = 0, isAccountSuccess = 0;
		try {
			// 1、写流水日志
			// PartnerAccountLog partnerAccountLog = new PartnerAccountLog();
			// partnerAccountLog.setPartnerId(partnerRecharge.getPartnerId());
			// partnerAccountLog.setState(PartnerAccountConstant.PartnerAccountLogType.RECHARGE.getValue());
			// partnerAccountLog.setAccountAmount(partnerRecharge.getRechargeAmount());
			// partnerAccountLog.setSerialNumber(partnerRecharge.getSerialNumber());
			// partnerAccountLog.setRemark("充值");
			// LotteryLogger logger =
			// PartnerLogger.getDynamicLogger(partnerRecharge.getPartnerId());
			// logger.info(PartnerAccountLogUtil.convertLog2Str(partnerAccountLog));
			// isLogSuccess =
			// partnerAccountDao.addPartnerAccountLog(partnerAccountLog);
			// if (isLogSuccess <= 0) {
			// return isLogSuccess;
			// }
			// 2、新增充值记录
			isSuccess = partnerAccountDao.addPartnerRecharge(partnerRecharge);
			if (isSuccess <= 0) {
				return isSuccess;
			}
			isAccountSuccess = PartnerAccountBuffer
					.updateAccount(
							partnerRecharge.getPartnerId(),
							partnerRecharge.getRechargeAmount(),
							partnerRecharge.getSerialNumber(),
							PartnerAccountConstant.PartnerAccountLogType.RECHARGE,
							"充值");
			// 3、更新账户金额
			// isAccountSuccess =
			// partnerAccountDao.increaseAccount(partnerRecharge.getPartnerId(),
			// partnerRecharge.getRechargeAmount());
		} catch (DaoLevelException e) {
			// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error("addPartnerRecharge error,  err=%s", e.toString());
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
		return isAccountSuccess;
	}

	@Override
	// @Transactional
	public int modifyRefund(String partnerId, long amount,
			String paySerialNumber, String refundSerialNumber) {
		Log.run.debug(
				"payPartnerAccount, partnerId=%s, amount=%d, serialNumber=%s",
				partnerId, amount, refundSerialNumber);
		int isSuccess = 0, isLogSuccess = 0;
		long hasRefundMoney = 0, totalRefundMoney = 0;
		try {
			String payKey = getMemcacheKey(partnerId, paySerialNumber, PAY_FLAG);

			Object object = memcachedClient.get(payKey);
			// 未付款
			if (object == null) {
				Log.run.warn("Has not ready pay.");
				PartnerLogger
						.getDynamicLogger(partnerId + "_err")
						.info("Not found pay record when refund, paySN=%s, refundSN=%s, amount=%d",
								paySerialNumber, refundSerialNumber, amount);
				// return
				// ServiceStatusCodeUtil.STATUS_CODE_PAYACCOUNTLOG_NOTEXIST;
			}
			// 支付流水日志
			// PartnerAccountLog log =
			// findPartnerAccountLogBySerialNumber(partnerId, paySerialNumber);
			// if (null == log) {
			// return ServiceStatusCodeUtil.STATUS_CODE_PAYACCOUNTLOG_NOTEXIST;
			// }

			String refundKey = getMemcacheKey(partnerId, paySerialNumber,
					REFUND_FLAG);

			Object refundObject = memcachedClient.get(refundKey);
			// 未付款
			if (refundObject != null) {
				Log.run.warn("Has already refund,refundSerialNumber = %s",
						refundObject);
				return ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST;
			}
			// // 已退款流水日志列表
			// List<PartnerAccountLog> logList =
			// getPartnerAccountLogListByExt(partnerId, paySerialNumber);
			// for (PartnerAccountLog partnerLog : logList) {
			// hasRefundMoney += partnerLog.getAccountAmount();
			// }
			// totalRefundMoney = hasRefundMoney + amount;
			// if (totalRefundMoney > log.getAccountAmount()) {
			// return ServiceStatusCodeUtil.STATUS_CODE_REFUND_OVERPAY;
			// }
			// 写退款流水日志
			// PartnerAccountLog partnerAccountLog = new PartnerAccountLog();
			// partnerAccountLog.setPartnerId(partnerId);
			// partnerAccountLog.setState(PartnerAccountConstant.PartnerAccountLogType.REFUND.getValue());
			// partnerAccountLog.setAccountAmount(amount);
			// partnerAccountLog.setSerialNumber(refundSerialNumber);
			// partnerAccountLog.setExt(paySerialNumber);
			// partnerAccountLog.setRemark("退款");
			// LotteryLogger logger = PartnerLogger.getDynamicLogger(partnerId);
			// logger.info(PartnerAccountLogUtil.convertLog2Str(partnerAccountLog));

			// isLogSuccess =
			// partnerAccountDao.addPartnerAccountLog(partnerAccountLog);
			// if (isLogSuccess <= 0) {
			// return isLogSuccess;
			// }
			// 增加账户金额
			isSuccess = PartnerAccountBuffer.updateAccount(partnerId, amount,
					refundSerialNumber,
					PartnerAccountConstant.PartnerAccountLogType.REFUND, "退款");

			if (isSuccess > 0) {
				memcachedClient.set(refundKey, 0, refundSerialNumber);
			}
			// isSuccess = partnerAccountDao.increaseAccount(partnerId, amount);
		} catch (DaoLevelException e) {
			// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Log.run.debug("", e);
			Log.run.error("modifyRefund error, %s", e.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Log.run.debug("", e);
			Log.run.error("modifyRefund error, %s", e.toString());
		} catch (MemcachedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Log.run.debug("", e);
			Log.run.error("modifyRefund error, %s", e.toString());
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error("modifyRefund error,  err=%s", e.toString());
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
		return isSuccess;
	}

	@Override
	// @Transactional
	public int sendPrize(String partnerId, long amount, String serialNumber) {
		Log.run.debug("sendPrize, partnerId=%s, amount=%d, serialNumber=%s",
				partnerId, amount, serialNumber);
		int isSuccess = 0, isLogSuccess = 0;
		try {
			String key = getMemcacheKey(partnerId, serialNumber,
					SEND_PRIZE_FLAG);
			Object object = memcachedClient.get(key);
			if (object != null) {
				Log.run.warn("Has already sendPrize.serialNumber = %s",
						serialNumber);
				return isSuccess;
			}
			// 写派奖流水日志
			// PartnerAccountLog partnerAccountLog = new PartnerAccountLog();
			// partnerAccountLog.setPartnerId(partnerId);
			// partnerAccountLog.setState(PartnerAccountConstant.PartnerAccountLogType.PRIZE.getValue());
			// partnerAccountLog.setAccountAmount(amount);
			// partnerAccountLog.setSerialNumber(serialNumber);
			// partnerAccountLog.setRemark("派奖");
			// LotteryLogger logger = PartnerLogger.getDynamicLogger(partnerId);
			// logger.info(PartnerAccountLogUtil.convertLog2Str(partnerAccountLog));

			// isLogSuccess =
			// partnerAccountDao.addPartnerAccountLog(partnerAccountLog);
			// if (isLogSuccess <= 0) {
			// return isLogSuccess;
			// }
			// 增加账户金额
			// isSuccess = partnerAccountDao.increaseAccount(partnerId, amount);
			isSuccess = PartnerAccountBuffer.updateAccount(partnerId, amount,
					serialNumber,
					PartnerAccountConstant.PartnerAccountLogType.PRIZE, "派奖");
			if (isSuccess > 0) {
				memcachedClient.set(key, 0, serialNumber);
			}
		} catch (DaoLevelException e) {
			// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Log.run.debug("", e);
			Log.run.error("sendPrize error, %s", e.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Log.run.debug("", e);
			Log.run.error("sendPrize error, %s", e.toString());
		} catch (MemcachedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Log.run.debug("", e);
			Log.run.error("sendPrize error, %s", e.toString());
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error("sendPrize error,  err=%s", e.toString());
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
		return isSuccess;
	}

	@Override
	public long countPartnerRechargeByDate(String partnerId, String dateTime) {
		long totalRecharge = 0;
		String beginTime = dateTime + " 00:00:00";
		String endTime = dateTime + " 23:59:59";
		List<PartnerRecharge> partnerRechargeList = partnerAccountDao
				.getPartnerRechargeList(partnerId, beginTime, endTime);
		if (null != partnerRechargeList && partnerRechargeList.size() > 0) {
			for (PartnerRecharge partnerRecharge : partnerRechargeList) {
				totalRecharge += partnerRecharge.getRechargeAmount();
			}
		}
		return totalRecharge;
	}

	@Override
	public int createPartnerPreApply(PartnerPreApply partnerPreApply) {
		int isSuccess = partnerAccountDao
				.createPartnerPreApply(partnerPreApply);
		return isSuccess;
	}

	@Override
	public PartnerPreApply findPartnerPreApply(String partnerId,
			String partnerUniqueNo) {
		return partnerAccountDao
				.findPartnerPreApply(partnerId, partnerUniqueNo);
	}

	@Override
	public PartnerPreApplyData getPartnerPreApplyList(
			PartnerPreApply partnerPreApply, int currentPage, int pageSize) {
		return partnerAccountDao.getPartnerPreApplyList(partnerPreApply,
				currentPage, pageSize);
	}

	/**
	 * 查询扩展信息保存的已退款的支付流水（用于统计已退款金额）
	 * 
	 * @param ext
	 * @return
	 */
	private List<PartnerAccountLog> getPartnerAccountLogListByExt(
			String partnerId, String ext) {
		return partnerAccountDao.getPartnerAccountLogListByExt(partnerId, ext);
	}

	/**
	 * 根据流水号查询日志
	 * 
	 * @param serialNumber
	 * @return
	 */
	private PartnerAccountLog findPartnerAccountLogBySerialNumber(
			String partnerId, String serialNumber) {
		return partnerAccountDao.findPartnerAccountLogBySerialNumber(partnerId,
				serialNumber);
	}

	@Override
	public PartnerPreApply findPartnerPreApplyByPreApplyId(long preApplyId) {
		return partnerAccountDao.findPartnerPreApplyByPreApplyId(preApplyId);
	}

	@Override
	public int auditPartnerPreApply(long preApplyId, int status) {
		// TODO Auto-generated method stub
		int isSuccess = 0, isLogSuccess = 0;
		PartnerPreApply partnerPreApply = partnerAccountDao
				.findPartnerPreApplyByPreApplyId(preApplyId);
		try {
			if (partnerPreApply.getStatus() != PartnerAccountConstant.PartnerPreApplyState.NOT_AUDIT
					.getValue()) {
				return ServiceStatusCodeUtil.STATUS_CODE_PARTNERPREAPPLY_ISAUDIT;
			}
			isSuccess = partnerAccountDao.auditPartnerPreApply(preApplyId,
					status);
			if (isSuccess <= 0) {
				return isSuccess;
			}

			String serialNumber = IdWorker.getSerialNumber();
			isSuccess = PartnerAccountBuffer.updateAccount(
					partnerPreApply.getPartnerId(),
					partnerPreApply.getPreMoney(), serialNumber,
					PartnerAccountConstant.PartnerAccountLogType.USERPREAPPLY,
					"渠道账户预存款审核通过");
			// 写提现流水日志
			// PartnerAccountLog partnerAccountLog = new PartnerAccountLog();
			// partnerAccountLog.setPartnerId(partnerPreApply.getPartnerId());
			// partnerAccountLog.setState(PartnerAccountConstant.PartnerAccountLogType.USERPREAPPLY.getValue());
			// partnerAccountLog.setAccountAmount(partnerPreApply.getPreMoney());
			// partnerAccountLog.setSerialNumber(serialNumber);
			// partnerAccountLog.setRemark("渠道账户预存款审核通过");
			// LotteryLogger logger =
			// PartnerLogger.getDynamicLogger(partnerPreApply.getPartnerId());
			// logger.info(PartnerAccountLogUtil.convertLog2Str(partnerAccountLog));
			// // isLogSuccess =
			// partnerAccountDao.addPartnerAccountLog(partnerAccountLog);
			// // if (isLogSuccess <= 0) {
			// // return isLogSuccess;
			// // }
			//
			// if (isSuccess > 0 && status ==
			// PartnerAccountConstant.PartnerPreApplyState.AUDIT_PASS.getValue())
			// {
			// String partnerId = partnerPreApply.getPartnerId();
			// long amount = partnerPreApply.getPreMoney();
			// isSuccess = addPartnerPreApplyAmount(partnerId, amount);
			// }
		} catch (DaoLevelException e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error("auditPartnerPreApply error,  err=%s", e.toString());
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
		return isSuccess;
	}

	@Override
	public int addPartnerPreApplyAmount(String partnerId, long amount) {
		int isSuccess = 0;
		// 增加账户金额
		try {
			isSuccess = partnerAccountDao.increaseAccount(partnerId, amount);
		} catch (DaoLevelException e) {
			return Integer.valueOf(e.getMessage());
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error("addPartnerPreApplyAmount error,  err=%s",
					e.toString());
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
		return isSuccess;
	}

	@Override
	public Map<String, Long> statisticRecharge(String date) {
		String beginTime = date + " 00:00:00";
		String endTime = date + " 23:59:59";
		return partnerAccountDao.countRecharge(beginTime, endTime);
	}

	@Override
	public long totalAccountMoney() {
		return PartnerAccountBuffer.totalAccountMoney();
	}

	@Override
	public long totalPaylogNum(String date) {
		String beginTime = date + " 00:00:00";
		String endTime = date + " 23:59:59";
		return partnerAccountDao.totalPaylogNum(beginTime, endTime);
	}

	@Override
	public int updatePartnerAccountCreditLimit(String partnerId,
			long creditLimit) {
		int isSuccess = 0;
		try {
			isSuccess = partnerAccountDao.updatePartnerAccountCreditLimit(partnerId,
					creditLimit);
			PartnerAccountBuffer.updatePartnerAccountCreditLimit(partnerId, creditLimit);
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error("payParterAccount error,  err=%s", e.toString());
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
		return isSuccess;
	}

}
