package com.cqfc.partneraccount.datacenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;

import com.cqfc.protocol.partneraccount.PartnerAccount;
import com.cqfc.protocol.partneraccount.PartnerAccountData;
import com.cqfc.protocol.partneraccount.PartnerAccountLog;
import com.cqfc.util.DaoLevelException;
import com.cqfc.util.PartnerAccountConstant.PartnerAccountLogType;
import com.cqfc.util.PartnerAccountLogUtil;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;
import com.jami.util.LotteryLogger;
import com.jami.util.PartnerLogger;

@Component
public class PartnerAccountBuffer {
	private static final Map<String, PartnerAccount> accountBuffer = new HashMap<String, PartnerAccount>();
	private static final Lock lock = new ReentrantLock();
	private static final int MAX_QUEUE_SIZE = 50000;
	private static final BlockingQueue<PartnerAccountLog> queue = new LinkedBlockingQueue<PartnerAccountLog>(
			MAX_QUEUE_SIZE);

	/**
	 * 获取队列
	 * 
	 * @return
	 */
	public static BlockingQueue<PartnerAccountLog> getQueue() {
		return queue;
	}

	public static void initBuffer(List<PartnerAccount> accountList) {
		accountBuffer.clear();
		if (accountList == null) {
			return;
		}
		for (PartnerAccount account : accountList) {
			accountBuffer.put(account.getPartnerId(), account);
		}
	}

	/**
	 * 添加帐号
	 * 
	 * @param partnerAccount
	 * @throws DaoLevelException
	 */
	public static void addPartnerAccount(PartnerAccount partnerAccount)
			throws DaoLevelException {
		lock.lock();
		try {
			accountBuffer.put(partnerAccount.getPartnerId(), partnerAccount);
		} catch (Exception e) {
			Log.run.equals("addPartnerAccount failed.");
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 查询渠道账户列表
	 * 
	 * @param partnerAccount
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public static PartnerAccountData getPartnerAccountList(
			PartnerAccount partnerAccount, int currentPage, int pageSize) {

		PartnerAccountData returnData = new PartnerAccountData();
		List<PartnerAccount> list = new ArrayList<PartnerAccount>();
		returnData.setCurrentPage(currentPage);
		returnData.setPageSize(pageSize);
		lock.lock();
		try {
			if (null != partnerAccount) {
				String partnerId = partnerAccount.getPartnerId();
				if (null != partnerId && !"".equals(partnerId)) {
					if (accountBuffer.containsKey(partnerId)) {
						list.add(accountBuffer.get(partnerId));
						returnData.setTotalSize(1);
						returnData.setResultList(list);
						return returnData;
					}
					returnData.setTotalSize(0);
					returnData.setResultList(list);
					return returnData;
				}
			}
			int totalSize = accountBuffer.size();
			returnData.setTotalSize(totalSize);
			int startIndex = (currentPage - 1) * pageSize;
			if (startIndex > totalSize) {
				returnData.setResultList(list);
				return returnData;
			}
			int endIndex = startIndex + pageSize;
			if (endIndex > totalSize) {
				endIndex = totalSize;
			}
			Object[] array = accountBuffer.values().toArray();
			for (int i = startIndex; i < endIndex; i++) {
				list.add((PartnerAccount) array[i]);
			}
		} catch (Exception e) {
			Log.run.equals("getPartnerAccountList failed.");
		} finally {
			lock.unlock();
		}
		return returnData;
	}

	/**
	 * 根据渠道ID查询账户信息
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @return
	 */
	public static PartnerAccount findPartnerAccountByPartnerId(String partnerId) {
		lock.lock();
		try {
			return accountBuffer.get(partnerId);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 更新渠道账户状态(冻结、解冻)
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param state
	 *            状态：1正常 2冻结
	 * @return
	 */
	public static int updatePartnerAccountState(String partnerId, int state) {
		lock.lock();
		try {
			if (!accountBuffer.containsKey(partnerId)) {
				Log.run.error("partner account %s not found.", partnerId);
				return ServiceStatusCodeUtil.STATUS_CODE_USER_NOTEXIST;
			}
			PartnerAccount partnerAccount = accountBuffer.get(partnerId);
			partnerAccount.setState(state);
		} finally {
			lock.unlock();
		}
		return ServiceStatusCodeUtil.STATUS_CODE_OPERATE_SUCCESS;
	}

	/**
	 * 扣除渠道账户金额（总金额、可用金额）
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param amount
	 *            金额
	 * @return
	 */
	public static int updateAccount(String partnerId, long amount,
			String serialNumber, PartnerAccountLogType logType, String remark)
			throws DaoLevelException {
		lock.lock();
		try {
			if (!accountBuffer.containsKey(partnerId)) {
				Log.run.error("partner account %s not found.", partnerId);
				return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
			}
			PartnerAccount partnerAccount = accountBuffer.get(partnerId);
			if (partnerAccount.getState() == 2){
				Log.run.error("partner account %s not is freezed.", partnerId);
				return ServiceStatusCodeUtil.STATUS_CODE_ACCOUNT_BE_FREEZED;
			}
			long totalUsableAmount = partnerAccount.getUsableAmount();
			if ((amount < 0)
					&& (amount + totalUsableAmount
							+ partnerAccount.getCreditLimit() < 0)) {
				return ServiceStatusCodeUtil.STATUS_CODE_ACCOUNT_MONEY_NOT_ENOUGH;
			}
			long remainAmount = totalUsableAmount + amount;
			PartnerAccountLog partnerAccountLog = new PartnerAccountLog();
			partnerAccountLog.setPartnerId(partnerId);
			partnerAccountLog.setState(logType.getValue());
			partnerAccountLog.setTotalAmount(totalUsableAmount);
			partnerAccountLog.setAccountAmount(amount);
			partnerAccountLog.setRemainAmount(remainAmount);
			partnerAccountLog.setSerialNumber(serialNumber);
			partnerAccountLog.setRemark(remark);
			LotteryLogger logger = PartnerLogger.getDynamicLogger(partnerId);
			logger.info(PartnerAccountLogUtil.convertLog2Str(partnerAccountLog));
			partnerAccount.setTotalAmount(partnerAccount.getTotalAmount()
					+ amount);
			partnerAccount.setUsableAmount(remainAmount);
			if (!PartnerAccountLogType.ROLLBACK.equals(logType)) {
				addPartnerAccountLog(partnerAccountLog);
			}
		} finally {
			lock.unlock();
		}
		return ServiceStatusCodeUtil.STATUS_CODE_OPERATE_SUCCESS;
	}

	/**
	 * 写流水日志
	 * 
	 * @param partnerAccountLog
	 * @return
	 */
	public static void addPartnerAccountLog(PartnerAccountLog partnerAccountLog)
			throws DaoLevelException {
		try {
			queue.put(partnerAccountLog);
		} catch (InterruptedException e) {
			Log.run.error("put partner account log error,partnerId=%s,sn=%s",
					partnerAccountLog.getPartnerId(),
					partnerAccountLog.getSerialNumber());
		}
	}

	public static long totalAccountMoney() {
		long totalMoney = 0;
		for(PartnerAccount account: accountBuffer.values()){
			totalMoney += account.getUsableAmount();
		}
		return totalMoney;
	}

	public static int updatePartnerAccountCreditLimit(String partnerId,
			long creditLimit) {
		lock.lock();
		try {
			if (!accountBuffer.containsKey(partnerId)) {
				Log.run.error("partner account %s not found.", partnerId);
				return ServiceStatusCodeUtil.STATUS_CODE_USER_NOTEXIST;
			}
			PartnerAccount partnerAccount = accountBuffer.get(partnerId);
			partnerAccount.setCreditLimit(creditLimit);
		} finally {
			lock.unlock();
		}
		return ServiceStatusCodeUtil.STATUS_CODE_OPERATE_SUCCESS;
	}

}
