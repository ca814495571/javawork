package com.cqfc.useraccount.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.cqfc.protocol.useraccount.UserAccount;
import com.cqfc.protocol.useraccount.UserAccountLog;
import com.cqfc.protocol.useraccount.UserAccountLogData;
import com.cqfc.protocol.useraccount.UserHandsel;
import com.cqfc.protocol.useraccount.UserHandselCount;
import com.cqfc.protocol.useraccount.UserHandselData;
import com.cqfc.protocol.useraccount.UserInfo;
import com.cqfc.protocol.useraccount.UserInfoData;
import com.cqfc.protocol.useraccount.UserPreApply;
import com.cqfc.protocol.useraccount.UserPreApplyData;
import com.cqfc.protocol.useraccount.UserRecharge;
import com.cqfc.protocol.useraccount.WithdrawAccount;
import com.cqfc.protocol.useraccount.WithdrawApply;
import com.cqfc.protocol.useraccount.WithdrawApplyData;
import com.cqfc.useraccount.dao.AccountInfoDao;
import com.cqfc.useraccount.dao.UserInfoDao;
import com.cqfc.useraccount.service.IUserAccountService;
import com.cqfc.useraccount.util.DataSourceContextHolder;
import com.cqfc.useraccount.util.DateUtil;
import com.cqfc.useraccount.util.DbGenerator;
import com.cqfc.util.Configuration;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DaoLevelException;
import com.cqfc.util.IdWorker;
import com.cqfc.util.Pair;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.util.SolrFactory;
import com.cqfc.util.SolrServer;
import com.cqfc.util.UserAccountConstant;
import com.jami.common.StatisticResult;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Service
public class UserAccountServiceImpl implements IUserAccountService {

	private static final char SEPERATOR = '_';
	private static final String HANDSEL = "handsel";
	private static final String RECHARGE = "recharge";
	private static final String PASS_PREFIX = "P#";
	private static final String NOPASS_PREFIX = "N#";
	@Resource
	private UserInfoDao userInfoDao;

	@Resource
	private AccountInfoDao accountInfoDao;

	@Autowired
	private MemcachedClient memcachedClient;

	private String getUserInfoKey(UserInfo userInfo) {
		return ConstantsUtil.MODULENAME_USER_ACCOUNT + SEPERATOR
				+ userInfo.getPartnerId() + SEPERATOR
				+ userInfo.getPartnerUserId();
	}

	private String getUserKey(String key) {
		return ConstantsUtil.MODULENAME_USER_ACCOUNT + SEPERATOR + key;
	}

	private String getUserHandselKey(UserHandsel userHandsel) {
		return ConstantsUtil.MODULENAME_USER_ACCOUNT + SEPERATOR + HANDSEL
				+ SEPERATOR + userHandsel.getPartnerId() + SEPERATOR
				+ userHandsel.getPartnerHandselId();
	}

	private String getUserRechargeKey(UserRecharge userRecharge) {
		return ConstantsUtil.MODULENAME_USER_ACCOUNT + SEPERATOR + HANDSEL
				+ SEPERATOR + userRecharge.getPartnerId() + SEPERATOR
				+ userRecharge.getPartnerRechargeId();
	}

	@Override
	@Transactional
	public long createUserInfo(UserInfo userInfo) {
		long returnUserId = 0;
		int isInfoSuccess = 0, isAccountSuccess = 0;
		try {
			// Object oldUserId =
			// memcachedClient.get((getUserInfoKey(userInfo)));
			// if (oldUserId != null) {
			// Log.run.warn("found user in memcachedClient, userId = %s",
			// oldUserId);
			// return ServiceStatusCodeUtil.STATUS_CODE_USER_ISEXIST;
			// }
			// // 校验来源渠道ID、渠道账户ID是否已经创建用户
			// UserInfo info = findUserInfoByPartnerId(userInfo.getPartnerId(),
			// userInfo.getPartnerUserId());
			// if (null != info) {
			// Log.run.warn("found user in db, userId = %s", info.getUserId());
			// memcachedClient.set(getUserInfoKey(userInfo), 0,
			// info.getUserId());
			// return ServiceStatusCodeUtil.STATUS_CODE_USER_ISEXIST;
			// }
			// 创建用户基本资料
			long userId = userInfo.getUserId();
			memcachedClient.set(getUserInfoKey(userInfo), 0, userId);
			isInfoSuccess = userInfoDao.createUserInfo(userInfo);
			if (isInfoSuccess <= 0) {
				Log.run.debug("create user info failed, resultCode=%d",
						isInfoSuccess);
				returnUserId = isInfoSuccess;

				try {
					memcachedClient.delete(getUserInfoKey(userInfo));
				} catch (Exception e1) {
					Log.run.debug("", e1);
				}
				return returnUserId;
			}
			// 获取当前插入数据用户ID
			Log.run.debug("create user info success, userId=%d", userId);
			// 创建用户账户
			UserAccount userAccount = new UserAccount();
			userAccount.setUserId(userId);
			isAccountSuccess = userInfoDao.createUserAccount(userAccount);
			if (isAccountSuccess <= 0) {
				Log.run.debug("create user account failed, userId=%d", userId);
				returnUserId = isAccountSuccess;
				try {
					memcachedClient.delete(getUserInfoKey(userInfo));
				} catch (Exception e1) {
					Log.run.debug("", e1);
				}
				return returnUserId;
			}
			returnUserId = userId;
			memcachedClient.add(getUserKey("" + userId), 0, userInfo);
			SolrServer server = SolrFactory
					.getSolrServer(ConstantsUtil.CORENAME_USERINFO);

			Set<String> excludeFields = new HashSet<String>();
			excludeFields.add("userAccount");
			excludeFields.add("userHandselList");
			excludeFields.add("prizePassword");
			Pair<List<String>, List<Object>> pair = SolrServer.getObjectFields(
					UserInfo._Fields.values(), userInfo, excludeFields);
			List<List<Object>> values = new ArrayList<List<Object>>();
			values.add(pair.second());
			server.addData(pair.first(), values);

		} catch (DaoLevelException e) {
			Log.run.debug("", e);
			Log.run.error(
					"create user failed,userId=%d,partnerId=%s,partnerUserId=%s",
					userInfo.getUserId(), userInfo.getPartnerId(),
					userInfo.getPartnerUserId());
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			try {
				memcachedClient.delete(getUserInfoKey(userInfo));
			} catch (Exception e1) {
				Log.run.debug("", e1);
			}
			return Integer.valueOf(e.getMessage());
		} catch (TimeoutException e) {
			Log.run.debug("", e);
			Log.run.error(
					"create user failed,userId=%d,partnerId=%s,partnerUserId=%s",
					userInfo.getUserId(), userInfo.getPartnerId(),
					userInfo.getPartnerUserId());
		} catch (InterruptedException e) {
			Log.run.debug("", e);
			Log.run.error(
					"create user failed,userId=%d,partnerId=%s,partnerUserId=%s",
					userInfo.getUserId(), userInfo.getPartnerId(),
					userInfo.getPartnerUserId());
		} catch (MemcachedException e) {
			Log.run.debug("", e);
			Log.run.error(
					"create user failed,userId=%d,partnerId=%s,partnerUserId=%s",
					userInfo.getUserId(), userInfo.getPartnerId(),
					userInfo.getPartnerUserId());
		} catch (SolrServerException e) {
			Log.run.debug("", e);
			Log.run.error(
					"create user failed,userId=%d,partnerId=%s,partnerUserId=%s",
					userInfo.getUserId(), userInfo.getPartnerId(),
					userInfo.getPartnerUserId());
		} catch (IOException e) {
			Log.run.debug("", e);
			Log.run.error(
					"create user failed,userId=%d,partnerId=%s,partnerUserId=%s",
					userInfo.getUserId(), userInfo.getPartnerId(),
					userInfo.getPartnerUserId());
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error(
					"create user failed,userId=%d,partnerId=%s,partnerUserId=%s",
					userInfo.getUserId(), userInfo.getPartnerId(),
					userInfo.getPartnerUserId());
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			try {
				memcachedClient.delete(getUserInfoKey(userInfo));
			} catch (Exception e1) {
				Log.run.debug("", e);
			}
		}
		return returnUserId;
	}

	@Override
	public UserInfoData getUserInfoList(UserInfo userInfo, int currentPage,
			int pageSize) {
		UserInfoData userInfoData = null;
		userInfoData = userInfoDao.getUserInfoList(userInfo, currentPage,
				pageSize);
		if (null != userInfoData) {
			List<UserInfo> userInfoList = userInfoData.getResultList();
			if (null != userInfoList && userInfoList.size() > 0) {
				for (UserInfo user : userInfoList) {
					try {
						memcachedClient.add(getUserKey("" + user.getUserId()),
								0, user);
					} catch (Exception e) {
						Log.run.error("add user info to memcached failed.");
						Log.run.debug("", e);
					}

					DbGenerator.setDynamicDataSource(DbGenerator.SLAVE,
							user.getUserId());
					// 用户账户处理
					UserAccount userAccount = userInfoDao
							.findUserAccountByUserId(user.getUserId());
					user.setUserAccount(userAccount);
					// 用户彩金处理
					List<UserHandsel> userHandselList = getUsableUserHandselList(user
							.getUserId());
					user.setUserHandselList(userHandselList);
				}
				userInfoData.setResultList(userInfoList);
			}
		}
		return userInfoData;
	}

	@Override
	public UserInfo findUserInfoById(long userId) {
		UserInfo userInfo = null;
		try {
			Object object = memcachedClient.get(getUserKey("" + userId));
			if (object != null) {
				userInfo = (UserInfo) object;
			} else {
				userInfo = userInfoDao.findUserInfoById(userId);
			}
		} catch (Exception e) {
			Log.run.error("add user info to memcached failed.");
			Log.run.debug("", e);
		}
		if (null != userInfo) {
			try {
				memcachedClient.add(getUserKey("" + userId), 0, userInfo);
			} catch (Exception e) {
				Log.run.error("add user info to memcached failed.");
				Log.run.debug("", e);
			}
			// 用户账户处理
			UserAccount userAccount = userInfoDao
					.findUserAccountByUserId(userId);
			userInfo.setUserAccount(userAccount);
			// 用户彩金处理
			List<UserHandsel> userHandselList = getUsableUserHandselList(userId);
			userInfo.setUserHandselList(userHandselList);
		}
		return userInfo;
	}

	@Override
	public UserAccount findUserAccountByUserId(long userId) {
		UserAccount userAccount = userInfoDao.findUserAccountByUserId(userId);
		if (null != userAccount) {
			// 用户彩金处理
			List<UserHandsel> userHandselList = getUsableUserHandselList(userId);
			userAccount.setUserHandselList(userHandselList);
		}
		return userAccount;
	}

	@Override
	public int modifyUserAccountState(long userId, int state) {
		int isSuccess = 0;
		if (userId <= 0 || state <= 0) {
			return isSuccess;
		}
		isSuccess = userInfoDao.modifyUserAccountState(userId, state);
		return isSuccess;
	}

	@Override
	@Transactional
	public int createUserHandsel(UserHandsel userHandsel) {
		int isSuccess = 0, isLogSuccess = 0;
		try {
			// 1、创建账户流水日志
			UserInfo userInfo = userInfoDao.findUserInfoById(userHandsel
					.getUserId());
			if (null == userInfo) {
				return ServiceStatusCodeUtil.STATUS_CODE_USER_NOTEXIST;
			}
			UserAccountLog userAccountLog = new UserAccountLog();
			userAccountLog.setUserId(userHandsel.getUserId());
			userAccountLog.setNickName(userInfo.getNickName());
			userAccountLog.setPartnerId(userHandsel.getPartnerId());
			// 日志类型：1充值 2支付 3提现 4退款 5派奖 6彩金赠送 7彩金失效
			userAccountLog
					.setLogType(UserAccountConstant.UserAccountLogType.HANDSEL_PRESENT
							.getValue());
			userAccountLog.setTotalAmount(userHandsel.getPresentAmount());
			userAccountLog.setAccountAmount(0);
			userAccountLog.setHandselAmount(userHandsel.getPresentAmount());
			userAccountLog.setSerialNumber(userHandsel.getSerialNumber());
			userAccountLog.setRemark("彩金赠送");
			isLogSuccess = createUserAccountLog(userAccountLog);
			if (isLogSuccess <= 0) {
				return isLogSuccess;
			}
			// 2、创建彩金记录
			isSuccess = accountInfoDao.createUserHandsel(userHandsel);

			memcachedClient.add(getUserHandselKey(userHandsel), 0,
					userHandsel.getUserId());
			SolrServer server = SolrFactory
					.getSolrServer(ConstantsUtil.CORENAME_HANDSEL);

			Set<String> excludeFields = new HashSet<String>();
			excludeFields.add("presentTime");
			excludeFields.add("usableAmount");
			excludeFields.add("usedAmount");
			excludeFields.add("presentAmount");
			excludeFields.add("activityId");
			excludeFields.add("handselId");
			excludeFields.add("version");
			userHandsel.setValidTime(DateUtil.convertNormal2GMT(userHandsel
					.getValidTime()));
			userHandsel.setFailureTime(DateUtil.convertNormal2GMT(userHandsel
					.getFailureTime()));
			Pair<List<String>, List<Object>> pair = SolrServer.getObjectFields(
					UserHandsel._Fields.values(), userHandsel, excludeFields);
			List<List<Object>> values = new ArrayList<List<Object>>();
			pair.first().add("handselId");
			pair.second().add(
					userHandsel.getPartnerId() + SEPERATOR
							+ userHandsel.getPartnerHandselId());
			values.add(pair.second());
			server.addData(pair.first(), values);

		} catch (DaoLevelException e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			Log.run.error(
					"create user handsel failed,userId=%d,partnerId=%s,serialNumber=%s",
					userHandsel.getUserId(), userHandsel.getPartnerId(),
					userHandsel.getSerialNumber());
			return Integer.valueOf(e.getMessage());
		} catch (SolrServerException e) {
			Log.run.debug("", e);
			Log.run.error(
					"create user handsel failed,userId=%d,partnerId=%s,serialNumber=%s",
					userHandsel.getUserId(), userHandsel.getPartnerId(),
					userHandsel.getSerialNumber());
		} catch (IOException e) {
			Log.run.debug("", e);
			Log.run.error(
					"create user handsel failed,userId=%d,partnerId=%s,serialNumber=%s",
					userHandsel.getUserId(), userHandsel.getPartnerId(),
					userHandsel.getSerialNumber());
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error(
					"create user handsel failed,userId=%d,partnerId=%s,serialNumber=%s",
					userHandsel.getUserId(), userHandsel.getPartnerId(),
					userHandsel.getSerialNumber());
		}
		return isSuccess;
	}

	@Override
	public UserHandselData getUserHandselList(UserHandsel userHandsel,
			int currentPage, int pageSize) {
		return accountInfoDao.getUserHandselList(userHandsel, currentPage,
				pageSize);
	}

	@Override
	public List<UserHandsel> getUserHandselListByUserId(long userId, int state) {
		List<UserHandsel> userHandselList = accountInfoDao
				.getUserHandselListByUserId(userId, state, false);
		return userHandselList;
	}

	@Override
	@Transactional
	public int modifyUserHandselState(long userId) {
		try {
			// 查询状态为有效的彩金记录
			List<UserHandsel> userHandselList = accountInfoDao
					.getUserHandselListByUserId(userId,
							UserAccountConstant.UserHandselState.VALID
									.getValue(), false);
			// 判断是否过期,处理彩金状态
			for (UserHandsel userHandsel : userHandselList) {
				String currentTime = DateUtil.getDateTime(
						"yyyy-MM-dd HH:mm:ss", new Date());
				String failureTime = userHandsel.getFailureTime();
				int validResult = DateUtil
						.compareDate(currentTime, failureTime);
				if (validResult < 0) {
					// 更新彩金状态为失效
					int isSuccess = accountInfoDao.updateUserHandselState(
							userHandsel.getHandselId(),
							userHandsel.getUserId(),
							UserAccountConstant.UserHandselState.INVALID
									.getValue());
					if (isSuccess <= 0) {
						return isSuccess;
					}
					// 写彩金状态更改日志
					UserInfo userInfo = userInfoDao
							.findUserInfoById(userHandsel.getUserId());
					UserAccountLog userAccountLog = new UserAccountLog();
					userAccountLog.setUserId(userHandsel.getUserId());
					userAccountLog.setNickName(userInfo.getNickName());
					userAccountLog.setPartnerId(userHandsel.getPartnerId());
					// 日志类型：1充值 2支付 3提现 4退款 5派奖 6彩金赠送 7彩金失效
					userAccountLog
							.setLogType(UserAccountConstant.UserAccountLogType.HANDSEL_FAILURE
									.getValue());
					userAccountLog.setTotalAmount(userHandsel
							.getPresentAmount());
					userAccountLog.setAccountAmount(0);
					userAccountLog.setHandselAmount(userHandsel
							.getPresentAmount());
					userAccountLog.setSerialNumber(userHandsel
							.getSerialNumber());
					userAccountLog.setRemark("彩金失效状态修改");
					int isLogSuccess = createUserAccountLog(userAccountLog);
					if (isLogSuccess <= 0) {
						return isLogSuccess;
					}
				}
			}
		} catch (DaoLevelException e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		}
		return ServiceStatusCodeUtil.STATUS_CODE_OPERATE_SUCCESS;
	}

	@Override
	public List<UserHandsel> getUsableUserHandselList(long userId) {
		List<UserHandsel> userHandselList = accountInfoDao
				.getUserHandselListByUserId(userId,
						UserAccountConstant.UserHandselState.VALID.getValue(),
						true);
		return userHandselList;
	}

	@Override
	public int createWithdrawAccount(WithdrawAccount withdrawAccount) {
		int isSuccess = 0;
		try {
			UserInfo userInfo = userInfoDao.findUserInfoById(withdrawAccount
					.getUserId());
			if (null == userInfo) {
				return ServiceStatusCodeUtil.STATUS_CODE_USER_NOTEXIST;
			}
			isSuccess = accountInfoDao.createWithdrawAccount(withdrawAccount);
		} catch (DaoLevelException e) {
			return Integer.valueOf(e.getMessage());
		}
		return isSuccess;
	}

	@Override
	public int updateWithdrawAccount(WithdrawAccount withdrawAccount) {
		int isSuccess = 0;
		try {
			DbGenerator.setDynamicDataSource(DbGenerator.MASTER,
					withdrawAccount.getUserId());
			UserInfo userInfo = userInfoDao.findUserInfoById(withdrawAccount
					.getUserId());
			if (null == userInfo) {
				return ServiceStatusCodeUtil.STATUS_CODE_USER_NOTEXIST;
			}
			isSuccess = accountInfoDao.updateWithdrawAccount(withdrawAccount);
		} catch (DaoLevelException e) {
			return Integer.valueOf(e.getMessage());
		}
		return isSuccess;
	}

	@Override
	public List<WithdrawAccount> getWithdrawAccountListByUserId(long userId) {
		return accountInfoDao.getWithdrawAccountListByUserId(userId);
	}
	
	@Transactional
	public int createAccountAndGetId(WithdrawAccount account){
		accountInfoDao.createWithdrawAccount(account);
		int accountId = accountInfoDao.getCurrentInsertAccountId();
		Log.run.debug("withdraw account id is %d", accountId);
		return accountId;
	}
	
	@Override
	public int createWithdrawApply(WithdrawApply withdrawApply) {
		return  accountInfoDao.createWithdrawApply(withdrawApply);

	}

	@Override
	public WithdrawApply findWithdrawApplyByApplyId(int applyId) {
		return accountInfoDao.findWithdrawApplyByApplyId(applyId);
	}

	@Override
	public WithdrawApply findWithdrawApply(long userId, String serialNumber) {
		return accountInfoDao.findWithdrawApply(userId, serialNumber);
	}

	@Override
	public List<WithdrawApply> getWithdrawApplyListByUserId(long userId) {
		List<WithdrawApply> withdrawApplyList = accountInfoDao
				.getWithdrawApplyListByUserId(userId);
		return withdrawApplyList;
	}

	@Override
	public int auditWithdrawApply(WithdrawApply withdrawApply) {
		int isSuccess = 0, isLogSuccess = 0;
		try {
			// 查找出需要审核的申请记录
			DbGenerator.setFucaiDataSource(DbGenerator.SLAVE);
			WithdrawApply apply = accountInfoDao.findWithdrawApply(
					withdrawApply.getUserId(), withdrawApply.getSerialNumber());
			if (null == apply) {
				return ServiceStatusCodeUtil.STATUS_CODE_WITHDRAWAPPLY_NOTEXIST;
			}
			// 判断该审核记录是否已审核
			if (apply.getAuditState() == UserAccountConstant.WithdrawAuditState.AUDIT_PASS
					.getValue()
					|| apply.getAuditState() == UserAccountConstant.WithdrawAuditState.AUDIT_NOPASS
							.getValue()) {
				return ServiceStatusCodeUtil.STATUS_CODE_WITHDRAWAPPLY_ISAUDIT;
			}
			// 修改提现记录审核状态
			withdrawApply.setApplyId(apply.getApplyId());
			DbGenerator.setFucaiDataSource(DbGenerator.MASTER);
			isSuccess = accountInfoDao.auditWithdrawApply(withdrawApply);
			if (isSuccess <= 0) {
				return isSuccess;
			}
			// 写提现流水日志
			// UserAccountLog userAccountLog = new UserAccountLog();
			// userAccountLog.setUserId(apply.getUserId());
			// userAccountLog.setNickName(apply.getRealName());
			// userAccountLog.setPartnerId(apply.getPartnerId());
			// userAccountLog
			// .setLogType(UserAccountConstant.UserAccountLogType.WITHDRAW
			// .getValue());
			// userAccountLog.setTotalAmount(apply.getWithdrawAmount());
			// userAccountLog.setAccountAmount(0);
			// userAccountLog.setHandselAmount(0);
			// userAccountLog.setSerialNumber(withdrawApply.getSerialNumber());
			// userAccountLog.setRemark("用户提现申请");
			// isLogSuccess = createUserAccountLog(userAccountLog);
			// if (isLogSuccess <= 0) {
			// return isLogSuccess;
			// }
		} catch (DaoLevelException e) {
			return Integer.valueOf(e.getMessage());
		}
		return ServiceStatusCodeUtil.STATUS_CODE_OPERATE_SUCCESS;
	}

	/**
	 * 创建日志流水
	 * 
	 * @param userId
	 * @param nickName
	 * @param partnerId
	 * @param logType
	 * @param totalAmount
	 * @param accountAmount
	 * @param handselAmount
	 * @param serialNumber
	 * @param remark
	 * @return
	 */
	@Override
	public int createUserAccountLog(long userId, String nickName,
			String partnerId, int logType, long totalAmount,
			long accountAmount, long handselAmount, String serialNumber,
			String remark) {
		int isLogSuccess = 0;
		// 写提现流水日志
		UserAccountLog userAccountLog = new UserAccountLog();
		userAccountLog.setUserId(userId);
		userAccountLog.setNickName(nickName);
		userAccountLog.setPartnerId(partnerId);
		userAccountLog.setLogType(logType);
		userAccountLog.setTotalAmount(totalAmount);
		userAccountLog.setAccountAmount(accountAmount);
		userAccountLog.setHandselAmount(handselAmount);
		userAccountLog.setSerialNumber(serialNumber);
		userAccountLog.setRemark(remark);
		isLogSuccess = createUserAccountLog(userAccountLog);
		if (isLogSuccess <= 0) {
			return isLogSuccess;
		}
		return ServiceStatusCodeUtil.STATUS_CODE_OPERATE_SUCCESS;
	}

	@Override
	@Transactional
	public int createUserRecharge(UserRecharge userRecharge) {
		int isSuccess = 0, isAccountSuccess = 0, isLogSuccess = 0;
		try {
			// 创建充值记录
			UserInfo userInfo = userInfoDao.findUserInfoById(userRecharge
					.getUserId());
			if (null == userInfo) {
				return ServiceStatusCodeUtil.STATUS_CODE_USER_NOTEXIST;
			}
			userRecharge.setNickName(userInfo.getNickName());
			isSuccess = accountInfoDao.createUserRecharge(userRecharge);
			if (isSuccess <= 0) {
				return isSuccess;
			}
			// 增加用户帐户金额（总金额、可用金额）
			isAccountSuccess = userInfoDao.increaseUserAccountMoney(
					userRecharge.getUserId(), userRecharge.getRechargeAmount());
			if (isAccountSuccess <= 0) {
				return isAccountSuccess;
			}
			// 写充值流水日志
			UserAccountLog userAccountLog = new UserAccountLog();
			userAccountLog.setUserId(userRecharge.getUserId());
			userAccountLog.setNickName(userRecharge.getNickName());
			userAccountLog.setPartnerId(userRecharge.getPartnerId());
			userAccountLog
					.setLogType(UserAccountConstant.UserAccountLogType.RECHARGE
							.getValue());
			userAccountLog.setTotalAmount(userRecharge.getRechargeAmount());
			userAccountLog.setAccountAmount(userRecharge.getRechargeAmount());
			userAccountLog.setHandselAmount(0);
			userAccountLog.setSerialNumber(userRecharge.getSerialNumber());
			userAccountLog.setRemark("用户充值");
			isLogSuccess = createUserAccountLog(userAccountLog);
			if (isLogSuccess <= 0) {
				return isLogSuccess;
			}

			memcachedClient.add(getUserRechargeKey(userRecharge), 0,
					userRecharge.getUserId());
			SolrServer server = SolrFactory
					.getSolrServer(ConstantsUtil.CORENAME_RECHARGE);

			Set<String> excludeFields = new HashSet<String>();
			excludeFields.add("rechargeAmount");
			excludeFields.add("rechargeType");
			excludeFields.add("remark");
			excludeFields.add("rechargeId");
			Pair<List<String>, List<Object>> pair = SolrServer.getObjectFields(
					UserRecharge._Fields.values(), userRecharge, excludeFields);
			pair.first().add("rechargeId");
			pair.second().add(
					userRecharge.getPartnerId() + SEPERATOR
							+ userRecharge.getPartnerRechargeId());
			List<List<Object>> values = new ArrayList<List<Object>>();
			values.add(pair.second());
			server.addData(pair.first(), values);

		} catch (DaoLevelException e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		} catch (SolrServerException e) {
			Log.run.debug("", e);
			Log.run.error(
					"create user recharge failed,userId=%d,partnerId=%s,serialNumber=%s",
					userRecharge.getUserId(), userRecharge.getPartnerId(),
					userRecharge.getSerialNumber());
		} catch (IOException e) {
			Log.run.debug("", e);
			Log.run.error(
					"create user recharge failed,userId=%d,partnerId=%s,serialNumber=%s",
					userRecharge.getUserId(), userRecharge.getPartnerId(),
					userRecharge.getSerialNumber());
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error(
					"create user recharge failed,userId=%d,partnerId=%s,serialNumber=%s",
					userRecharge.getUserId(), userRecharge.getPartnerId(),
					userRecharge.getSerialNumber());
		}
		return ServiceStatusCodeUtil.STATUS_CODE_OPERATE_SUCCESS;
	}

	@Override
	public List<UserRecharge> getUserRechargeList(long userId) {
		List<UserRecharge> userRechargeList = accountInfoDao
				.getUserRechargeList(userId);
		return userRechargeList;
	}

	@Override
	public UserAccountLogData getUserAccountLogList(
			UserAccountLog userAccountLog, int currentPage, int pageSize) {
		return accountInfoDao.getUserAccountLogList(userAccountLog,
				currentPage, pageSize);
	}

	/**
	 * 根据状态、流水号查询
	 * 
	 * @param logType
	 *            日志类型：1充值 2支付 3提现 4退款 5派奖 6彩金赠送 7彩金失效
	 * @param serialNumber
	 *            流水号
	 * @param userId
	 *            用户Id
	 * @return
	 */
	private UserAccountLog findUserAccountLog(int logType, String serialNumber,
			long userId) {
		return accountInfoDao.findUserAccountLog(userId, logType, serialNumber);
	}

	@Override
	@Transactional
	public int modifyRefund(long userId, String paySerialNumber,
			String refundSerialNumber, long amount) {
		int isSuccess = 0, isLogSuccess = 0;
		long needRefundMoney = amount, userAccountRefundMoney = 0, handselRefundMoney = 0;
		try {
			String newHandselRemark = ""; // 格式：彩金ID=金额，彩金ID=金额...
			UserInfo userInfo = userInfoDao.findUserInfoById(userId);
			if (null == userInfo) {
				return ServiceStatusCodeUtil.STATUS_CODE_USER_NOTEXIST;
			}
			// 查询支付流水日志
			UserAccountLog userAccountLog = findUserAccountLog(
					UserAccountConstant.UserAccountLogType.PAYMENT.getValue(),
					paySerialNumber, userId);
			if (null == userAccountLog) {
				return ServiceStatusCodeUtil.STATUS_CODE_PAYACCOUNTLOG_NOTEXIST;
			}
			// 计算退款总金额是否超过支付金额
			List<UserAccountLog> userAccountLogList = findUserAccountLogByExt(paySerialNumber);
			long hasRefundMoney = 0, totalRefundMoney = 0;
			if (null != userAccountLogList && userAccountLogList.size() > 0) {
				for (UserAccountLog log : userAccountLogList) {
					hasRefundMoney += log.getTotalAmount();
				}
			}
			totalRefundMoney = hasRefundMoney + amount;
			if (totalRefundMoney > userAccountLog.getTotalAmount()) {
				for (UserAccountLog refundLog : userAccountLogList) {
					if (refundSerialNumber.equals(refundLog.getSerialNumber())) {
						return ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST;
					}
				}
				return ServiceStatusCodeUtil.STATUS_CODE_REFUND_OVERPAY;
			}

			if (null != userAccountLog.getHandselRemark()
					&& !"".equals(userAccountLog.getHandselRemark())) {
				// 使用彩金支付,优先退还彩金
				String[] handselRemark = userAccountLog.getHandselRemark()
						.split(",");
				for (int i = handselRemark.length - 1; i >= 0; i--) {
					if (needRefundMoney == 0) {
						break;
					}
					String handselStr = handselRemark[i]; // 格式：彩金ID=金额
					String[] handselDeal = handselStr.split("=");
					UserHandsel handsel = findUserHandselById(
							userAccountLog.getUserId(),
							Integer.valueOf(handselDeal[0]));
					if (null == handsel) {
						return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
					}
					long singleHandselMoney = Integer.valueOf(handselDeal[1]); // 支付日志中彩金支付金额
					if (singleHandselMoney <= needRefundMoney) {
						needRefundMoney -= singleHandselMoney;
						handsel.setUsableAmount(singleHandselMoney);
						handsel.setUsedAmount(handsel.getUsedAmount()
								- singleHandselMoney);
						handselRefundMoney += singleHandselMoney;
						// 彩金备注处理
						newHandselRemark += handsel.getHandselId() + "="
								+ singleHandselMoney + ",";
					} else {
						handselRefundMoney += needRefundMoney;
						handsel.setUsableAmount(needRefundMoney);
						handsel.setUsedAmount(handsel.getUsedAmount()
								- needRefundMoney);
						// 彩金备注处理
						newHandselRemark += handsel.getHandselId() + "="
								+ needRefundMoney + ",";
						needRefundMoney = 0;
					}
					int updateHandsel = modifyUserHandsel(handsel);
					if (updateHandsel <= 0) {
						return updateHandsel;
					}
				}
			}
			// 退款到用户帐户
			if (needRefundMoney > 0) {
				userAccountRefundMoney = needRefundMoney;
				isSuccess = userInfoDao.increaseUserAccountMoney(userId,
						needRefundMoney);
				if (isSuccess <= 0) {
					return isSuccess;
				}
			}
			// 写退款到用户帐户流水日志
			UserAccountLog accountLog = new UserAccountLog();
			accountLog.setUserId(userInfo.getUserId());
			accountLog.setNickName(userInfo.getNickName());
			accountLog.setPartnerId(userInfo.getPartnerId());
			accountLog.setLogType(UserAccountConstant.UserAccountLogType.REFUND
					.getValue());
			accountLog.setTotalAmount(amount);
			accountLog.setAccountAmount(userAccountRefundMoney);
			accountLog.setHandselAmount(handselRefundMoney);
			accountLog.setSerialNumber(refundSerialNumber);
			accountLog.setExt(paySerialNumber); // 将支付流水号写入退款流水中
			accountLog.setRemark("退款金额到用户帐户");
			accountLog.setHandselRemark(newHandselRemark);
			isLogSuccess = createUserAccountLog(accountLog);
			if (isLogSuccess <= 0) {
				return isLogSuccess;
			}
		} catch (DaoLevelException e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		}
		return ServiceStatusCodeUtil.STATUS_CODE_OPERATE_SUCCESS;
	}

	@Override
	@Transactional
	public int payUserAccount(long userId, String serialNumber, long amount) {
		Log.run.debug("payUserAccount userId=%d,serialNumber=%s, amount=%d",
				userId, serialNumber, amount);
		int isSuccess = 0, isLogSuccess = 0;
		long needPayMoney = amount;
		try {
			String handselRemark = ""; // 彩金备注格式：彩金ID=金额，彩金ID=金额...
			// 查询用户可使用彩金列表（按过期时间升序)
			List<UserHandsel> userHandselList = getUsableUserHandselList(userId);
			// 先写支付流水 --- 计算彩金支付金额、账户支付金额(优先写流水日志,性能提高)
			long logCountNeedPay = amount, logCountHandselPay = 0, logCountAccountPay = 0;
			for (UserHandsel userHandsel : userHandselList) {
				if (logCountNeedPay == 0) {
					break;
				}
				if (userHandsel.getUsableAmount() <= logCountNeedPay) {
					// 需支付金额大于等于彩金可支付金额
					logCountNeedPay -= userHandsel.getUsableAmount();
					logCountHandselPay += userHandsel.getUsableAmount();
					handselRemark += userHandsel.getHandselId() + "="
							+ userHandsel.getUsableAmount() + ",";
				} else {
					logCountHandselPay += logCountNeedPay;
					handselRemark += userHandsel.getHandselId() + "="
							+ logCountNeedPay + ",";
					logCountNeedPay = 0;
				}
			}
			logCountAccountPay = logCountNeedPay != 0 ? logCountNeedPay : 0;
			UserInfo userInfo = userInfoDao.findUserInfoById(userId);
			if (null == userInfo) {
				return ServiceStatusCodeUtil.STATUS_CODE_USER_NOTEXIST;
			}
			UserAccountLog userAccountLog = new UserAccountLog();
			userAccountLog.setUserId(userId);
			userAccountLog.setNickName(userInfo.getNickName());
			userAccountLog.setPartnerId(userInfo.getPartnerId());
			// 日志类型：1充值 2支付 3提现 4退款 5派奖 6彩金赠送 7彩金失效
			userAccountLog
					.setLogType(UserAccountConstant.UserAccountLogType.PAYMENT
							.getValue());
			userAccountLog.setTotalAmount(amount);
			userAccountLog.setAccountAmount(0 - logCountAccountPay);
			userAccountLog.setHandselAmount(0 - logCountHandselPay);
			userAccountLog.setSerialNumber(serialNumber);
			userAccountLog.setRemark("用户支付");
			if (!"".equals(handselRemark)) {
				handselRemark = handselRemark.substring(0,
						handselRemark.length() - 1);
				userAccountLog.setHandselRemark(handselRemark);
			}
			isLogSuccess = createUserAccountLog(userAccountLog);
			// 彩金支付
			if (!userHandselList.isEmpty() && userHandselList.size() > 0) {
				for (UserHandsel userHandsel : userHandselList) {
					if (needPayMoney == 0) {
						break;
					}
					if (userHandsel.getUsableAmount() <= needPayMoney) {
						// 需支付金额大于等于彩金可支付金额
						needPayMoney -= userHandsel.getUsableAmount();
						userHandsel.setUsableAmount(0);
						userHandsel.setUsedAmount(userHandsel.getUsedAmount()
								+ userHandsel.getUsableAmount());
					} else {
						userHandsel.setUsableAmount(userHandsel
								.getUsableAmount() - needPayMoney);
						userHandsel.setUsedAmount(userHandsel.getUsedAmount()
								+ needPayMoney);
						needPayMoney = 0;
					}
					// 修改彩金可用金额、已使用金额
					int updateHandsel = modifyUserHandsel(userHandsel);
					if (updateHandsel <= 0) {
						return updateHandsel;
					}
				}
			}
			// 判断是否需要用户帐户支付
			if (needPayMoney != 0) {
				// 彩金支付未完成,需要用户帐户支付
				isSuccess = userInfoDao.deductUserAccountMoney(userId,
						needPayMoney);
				if (isSuccess <= 0) {
					return isSuccess;
				}
			}
		} catch (DaoLevelException e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		}
		return ServiceStatusCodeUtil.STATUS_CODE_OPERATE_SUCCESS;
	}

	@Override
	@Transactional
	public int sendPrize(long userId, String serialNumber, long amount) {
		int isSuccess = 0, isLogSuccess = 0;
		try {
			// 增加帐户资金
			isSuccess = userInfoDao.increaseUserAccountMoney(userId, amount);
			if (isSuccess <= 0) {
				return isSuccess;
			}
			// 写派奖流水日志
			UserInfo userInfo = userInfoDao.findUserInfoById(userId);
			if (null == userInfo) {
				return ServiceStatusCodeUtil.STATUS_CODE_USER_NOTEXIST;
			}
			UserAccountLog userAccountLog = new UserAccountLog();
			userAccountLog.setUserId(userInfo.getUserId());
			userAccountLog.setNickName(userInfo.getNickName());
			userAccountLog.setPartnerId(userInfo.getPartnerId());
			userAccountLog
					.setLogType(UserAccountConstant.UserAccountLogType.PRIZE
							.getValue());
			userAccountLog.setTotalAmount(amount);
			userAccountLog.setAccountAmount(amount);
			userAccountLog.setHandselAmount(0);
			userAccountLog.setSerialNumber(serialNumber);
			userAccountLog.setRemark("派奖");
			isLogSuccess = createUserAccountLog(userAccountLog);
			if (isLogSuccess <= 0) {
				return isLogSuccess;
			}
		} catch (DaoLevelException e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		}
		return ServiceStatusCodeUtil.STATUS_CODE_OPERATE_SUCCESS;
	}

	@Override
	public UserRecharge findUserRecharge(String partnerId,
			String partnerChargeId) {
		String key = getUserKey(RECHARGE + SEPERATOR + partnerId + SEPERATOR
				+ partnerChargeId);
		try {
			Object object = memcachedClient.get(key);
			if (object != null) {
				long userId = (Long) object;
				DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
				return accountInfoDao.findUserRecharge(partnerId,
						partnerChargeId, userId);
			}
			SolrServer solrServer = SolrFactory
					.getSolrServer(ConstantsUtil.CORENAME_RECHARGE);
			Map<String, Object> fields = new HashMap<String, Object>();
			fields.put("partnerId", partnerId);
			fields.put("partnerRechargeId", partnerChargeId);
			List<SolrDocument> data = solrServer.findData(fields, 0, 10);
			if (data.size() > 0) {
				SolrDocument doc = data.get(0);
				long userId = (Long) doc.getFieldValue("userId");
				DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
				UserRecharge userRecharge = accountInfoDao.findUserRecharge(
						partnerId, partnerChargeId, userId);
				Log.run.debug("found recharge.");
				return userRecharge;
			} else {
				Log.run.debug("found from solr failed");
			}

		} catch (Exception e) {
			Log.run.error("find user recharge failed.");
			Log.run.debug("", e);
		}
		Log.run.debug("cannot found recharge.");
		return null;
	}

	@Override
	public WithdrawApplyData getWithdrawApplyList(WithdrawApply withdrawApply,
			int currentPage, int pageSize) {
		WithdrawApplyData withdrawApplyData = null;
		withdrawApplyData = accountInfoDao.getWithdrawApplyList(withdrawApply,
				currentPage, pageSize);
		if (null != withdrawApplyData) {
			List<WithdrawApply> withdrawApplyList = withdrawApplyData
					.getResultList();
			long userId = 0, lastUserId = -1;
			for (WithdrawApply withdraw : withdrawApplyList) {
				userId = withdraw.getUserId();
				if (userId != lastUserId) {
					DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
				}
				// 提现帐号处理
				WithdrawAccount withdrawAccount = accountInfoDao
						.findWithdrawAccountById(
								withdraw.getWithdrawAccountId(), userId);
				withdraw.setWithdrawAccount(withdrawAccount);
			}
			withdrawApplyData.setResultList(withdrawApplyList);
		}
		return withdrawApplyData;
	}

	@Override
	public UserHandsel findUserHandselByPartnerId(String partnerId,
			String partnerHandselId) {
		String key = getUserKey(HANDSEL + SEPERATOR + partnerId + SEPERATOR
				+ partnerHandselId);
		try {
			Object object = memcachedClient.get(key);
			if (object != null) {
				long userId = (Long) object;
				DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
				return accountInfoDao.findUserHandselByPartnerId(partnerId,
						partnerHandselId, userId);
			}
			SolrServer solrServer = SolrFactory
					.getSolrServer(ConstantsUtil.CORENAME_HANDSEL);
			Map<String, Object> fields = new HashMap<String, Object>();
			fields.put("partnerId", partnerId);
			fields.put("partnerHandselId", partnerHandselId);
			List<SolrDocument> data = solrServer.findData(fields, 0, 10);
			if (data.size() > 0) {
				SolrDocument doc = data.get(0);
				long userId = (Long) doc.getFieldValue("userId");
				DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
				UserHandsel userHandsel = accountInfoDao
						.findUserHandselByPartnerId(partnerId,
								partnerHandselId, userId);
				Log.run.debug("found handsel.");
				return userHandsel;
			} else {
				Log.run.debug("found from solr failed");
			}

		} catch (Exception e) {
			Log.run.error("find user handsel failed.");
			Log.run.debug("", e);
		}
		Log.run.debug("can not found handsel.");
		return null;
	}

	// @Override
	@Transactional
	public int freezeUserAccoutByWithdraw(WithdrawApply withdrawApply) {
		// 冻结用户账户提现金额
		int isAccountSuccess = 0;
		try {
			isAccountSuccess = userInfoDao.freezeUserAccount(
					withdrawApply.getUserId(),
					withdrawApply.getWithdrawAmount());
			if (isAccountSuccess > 0) {
				createUserAccountLog(withdrawApply.getUserId(),
						withdrawApply.getRealName(),
						withdrawApply.getPartnerId(),
						UserAccountConstant.UserAccountLogType.FREEZE_AMOUNT
								.getValue(), withdrawApply.getWithdrawAmount(),
						withdrawApply.getWithdrawAmount(), 0,
						withdrawApply.getSerialNumber(), "用户提现申请");
			}
		} catch (DaoLevelException e) {
			Log.run.error("freeze user account when create withdraw apply failed.");
			try {
				TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
			} catch (Exception e1) {
				Log.run.debug("", e1);
			}
		}
		return isAccountSuccess;
	}

	@Transactional
	public int updateUserAccountByWithdraw(WithdrawApply withdrawApply) {
		// 审核状态修改成功,更新用户账户
		int isSuccess = 0;

		try {
			long userId = withdrawApply.getUserId();
			long amount = withdrawApply.getWithdrawAmount();
			String freezeSerialNumber = withdrawApply.getSerialNumber();
			String refundSerialNumber = IdWorker.getSerialNumber();

			if (withdrawApply.getAuditState() == UserAccountConstant.WithdrawAuditState.AUDIT_PASS
					.getValue()) {
				isSuccess = userInfoDao.deductFreezeAmount(userId, amount);
				if (isSuccess > 0) {
					long minusAmount = 0 - amount;
					createUserAccountLog(withdrawApply.getUserId(),
							withdrawApply.getRealName(),
							withdrawApply.getPartnerId(),
							UserAccountConstant.UserAccountLogType.WITHDRAW
									.getValue(), minusAmount, minusAmount, 0,
									PASS_PREFIX + freezeSerialNumber, "用户提现申请审核通过");
				} else {
					Log.run.error(
							"audit withdraw apply pass, but deduct freeze money failed, userId=%d,amount=%d",
							withdrawApply.getUserId(), amount);
				}
			} else {
				isSuccess = userInfoDao.refundFreezeMoney(userId, amount);;
				if (isSuccess > 0) {
					createUserAccountLog(withdrawApply.getUserId(),
							withdrawApply.getRealName(),
							withdrawApply.getPartnerId(),
							UserAccountConstant.UserAccountLogType.WITHDRAW
									.getValue(), amount, amount, 0,
									NOPASS_PREFIX + freezeSerialNumber, "用户提现申请审核不通过");
				} else {
					Log.run.error(
							"audit withdraw apply pass, but deduct freeze money failed, userId=%d,amount=%d",
							withdrawApply.getUserId(),
							withdrawApply.getWithdrawAmount());
				}
			}
		} catch (Exception e) {
			Log.run.error("update user account when audit withdraw apply failed.");
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
		}
		return isSuccess;
	}

	@Transactional
	public int updateUserAccountByPreapply(UserPreApply userPreApply) {
		int isSuccess = 0;
		try {
			long userId = userPreApply.getUserId();
			long amount = userPreApply.getPreMoney();
			createUserAccountLog(userId, "", userPreApply.getPartnerId(),
					UserAccountConstant.UserAccountLogType.USERPREAPPLY
							.getValue(), amount, amount, 0,
					IdWorker.getSerialNumber(), "用户预存款审核通过");
			isSuccess = addUserPreApplyAmount(userId, amount);
		} catch (Exception e) {
			Log.run.error("update user account by preApply failed.");
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
		}
		return isSuccess;
	}

	@Override
	@Transactional
	public int freezeUserAccount(long userId, long amount,
			String freezeSerialNumber) {
		int isFreezeSuccess = 0;
		try {
			UserInfo userInfo = userInfoDao.findUserInfoById(userId);
			if (null == userInfo) {
				return ServiceStatusCodeUtil.STATUS_CODE_USER_NOTEXIST;
			}
			// 写冻结用户账户金额流水日志
			UserAccountLog userAccountLog = new UserAccountLog();
			userAccountLog.setUserId(userInfo.getUserId());
			userAccountLog.setNickName(userInfo.getNickName());
			userAccountLog.setPartnerId(userInfo.getPartnerId());
			userAccountLog
					.setLogType(UserAccountConstant.UserAccountLogType.FREEZE_AMOUNT
							.getValue());
			userAccountLog.setTotalAmount(amount);
			userAccountLog.setAccountAmount(amount);
			userAccountLog.setHandselAmount(0);
			userAccountLog.setSerialNumber(freezeSerialNumber);
			userAccountLog.setRemark("用户追号冻结金额");
			int isLogSuccess = createUserAccountLog(userAccountLog);
			if (isLogSuccess <= 0) {
				return isLogSuccess;
			}
			isFreezeSuccess = userInfoDao.freezeUserAccount(userId, amount);
			if (isFreezeSuccess <= 0) {
				return isFreezeSuccess;
			}
		} catch (DaoLevelException e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		}
		return ServiceStatusCodeUtil.STATUS_CODE_OPERATE_SUCCESS;
	}
	
	@Override
	@Transactional
	public int deductFreezeMoney(long userId, long amount,
			String paySerialNumber){
		int isDeductSuccess = 0;
		try {
			// 写追号订单扣除冻结金额日志
			UserInfo userInfo = userInfoDao.findUserInfoById(userId);
			if (null == userInfo) {
				Log.run.error("user %d not exist.", userId);
				return ServiceStatusCodeUtil.STATUS_CODE_USER_NOTEXIST;
			}
			UserAccountLog userAccountLog = new UserAccountLog();
			userAccountLog.setUserId(userId);
			userAccountLog.setNickName(userInfo.getNickName());
			userAccountLog.setPartnerId(userInfo.getPartnerId());
			userAccountLog
					.setLogType(UserAccountConstant.UserAccountLogType.PAYMENT
							.getValue());
			userAccountLog.setTotalAmount(0 - amount);
			userAccountLog.setAccountAmount(0 - amount);
			userAccountLog.setHandselAmount(0);
			userAccountLog.setSerialNumber(paySerialNumber);
			userAccountLog.setRemark("追号订单扣除冻结金额");
			int isLogSuccess = createUserAccountLog(userAccountLog);
			if (isLogSuccess <= 0) {
				Log.run.error("create account log failed.");
				return isLogSuccess;
			}
			isDeductSuccess = userInfoDao.deductFreezeAmount(userId, amount);
			if (isDeductSuccess <= 0) {
				return isDeductSuccess;
			}
		} catch (DaoLevelException e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		}
		return isDeductSuccess;
	}

	@Override
	@Transactional
	public int refundFreezeMoney(long userId, long amount,
			String freezeSerialNumber, String refundSerialNumber) {
		int isRefundSuccess = 0;
		try {
			// 查询冻结流水日志
			UserAccountLog accountLog = findUserAccountLog(
					UserAccountConstant.UserAccountLogType.FREEZE_AMOUNT
							.getValue(),
					freezeSerialNumber, userId);
			if (null == accountLog) {
				return ServiceStatusCodeUtil.STATUS_CODE_PAYACCOUNTLOG_NOTEXIST;
			}
			// 计算退款总金额是否超过冻结金额
			List<UserAccountLog> userAccountLogList = findUserAccountLogByExt(freezeSerialNumber);
			long hasRefundMoney = 0, totalRefundMoney = 0;
			if (null != userAccountLogList && userAccountLogList.size() > 0) {
				for (UserAccountLog log : userAccountLogList) {
					hasRefundMoney += log.getTotalAmount();
				}
			}
			totalRefundMoney = hasRefundMoney + amount;
			if (totalRefundMoney > accountLog.getTotalAmount()) {
				return ServiceStatusCodeUtil.STATUS_CODE_REFUND_OVERPAY;
			}
			// 写追号订单扣除冻结金额日志
			UserInfo userInfo = userInfoDao.findUserInfoById(userId);
			if (null == userInfo) {
				return ServiceStatusCodeUtil.STATUS_CODE_USER_NOTEXIST;
			}
			UserAccountLog userAccountLog = new UserAccountLog();
			userAccountLog.setUserId(userId);
			userAccountLog.setNickName(userInfo.getNickName());
			userAccountLog.setPartnerId(userInfo.getPartnerId());
			userAccountLog
					.setLogType(UserAccountConstant.UserAccountLogType.REFUND
							.getValue());
			userAccountLog.setTotalAmount(amount);
			userAccountLog.setAccountAmount(amount);
			userAccountLog.setHandselAmount(0);
			userAccountLog.setSerialNumber(refundSerialNumber);
			userAccountLog.setExt(freezeSerialNumber);
			userAccountLog.setRemark("提现审核未通过退还冻结资金");
			int isLogSuccess = createUserAccountLog(userAccountLog);
			if (isLogSuccess <= 0) {
				return isLogSuccess;
			}
			isRefundSuccess = userInfoDao.refundFreezeMoney(userId, amount);
		} catch (DaoLevelException e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		}
		return isRefundSuccess;
	}

	@Override
	@Transactional
	public int updatePrizePassword(long userId, String oldPasswd,
			String newPasswd) {
		int isSuccess = 0;
		try {
			UserInfo userInfo = userInfoDao.findUserInfoById(userId);
			if (null == userInfo) {
				return ServiceStatusCodeUtil.STATUS_CODE_USER_NOTEXIST;
			}
			isSuccess = userInfoDao.updatePrizePassword(userId, oldPasswd,
					newPasswd);
		} catch (DaoLevelException e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		}
		return isSuccess;
	}

	@Override
	public int createUserPreApply(UserPreApply userPreApply) {
		int isSuccess = userInfoDao.createUserPreApply(userPreApply);
		return isSuccess;
	}

	@Override
	public UserPreApply findUserPreApply(String partnerId,
			String partnerUniqueNo) {
		return userInfoDao.findUserPreApply(partnerId, partnerUniqueNo);
	}

	/**
	 * 通过彩金ID查询彩金
	 * 
	 * @param handselId
	 *            彩金ID
	 * @return
	 */
	private UserHandsel findUserHandselById(long userId, int handselId) {
		UserHandsel handsel = accountInfoDao.findUserHandselById(userId,
				handselId);
		if (handsel != null) {
			try {
				memcachedClient.add(getUserHandselKey(handsel), 0,
						handsel.getUserId());
			} catch (Exception e) {
				Log.run.error("add handsel to memcache failed.");
			}
		}
		return handsel;
	}

	/**
	 * 新增流水日志
	 * 
	 * @param userAccountLog
	 * @return @
	 */
	private int createUserAccountLog(UserAccountLog userAccountLog)
			throws DaoLevelException {
		int isSuccess = accountInfoDao.createUserAccountLog(userAccountLog);
		return isSuccess;
	}

	/**
	 * 修改彩金金额(可用金额、已使用金额)
	 * 
	 * @param userHandsel
	 * @return @
	 */
	private int modifyUserHandsel(UserHandsel userHandsel)
			throws DaoLevelException {
		int isSuccess = accountInfoDao.modifyUserHandsel(userHandsel);
		return isSuccess;
	}

	/**
	 * 查询已退款流水列表
	 * 
	 * @param ext
	 *            支付流水号
	 * @return
	 */
	private List<UserAccountLog> findUserAccountLogByExt(String ext) {
		List<UserAccountLog> userAccountLogList = accountInfoDao
				.findUserAccountLogByExt(ext);
		return userAccountLogList;
	}

	@Override
	public boolean checkUserExist(UserInfo userInfo) {

		try {
			Object oldUserId = memcachedClient.get((getUserInfoKey(userInfo)));
			if (oldUserId != null) {
				Log.run.warn("found user in memcachedClient, userId = %s",
						oldUserId);
				return true;
			}
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error("memcached error.%s", e.toString());
		}

		// 校验来源渠道ID、渠道账户ID是否已经创建用户
		SolrServer server = SolrFactory
				.getSolrServer(ConstantsUtil.CORENAME_USERINFO);
		try {
			List<SolrDocument> data = server.findData(
					"partnerId:\"" + userInfo.getPartnerId()
							+ "\" AND partnerUserId:\""
							+ userInfo.getPartnerUserId() + "\"", 0, 10);
			if (data.size() > 0) {
				Log.run.warn("user already exist, userId=%s", data.get(0)
						.getFieldValue("userId"));
				return true;
			}
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error("SolrServer error.%s", e.toString());
		}
		return false;
	}

	@Override
	@Transactional
	public long genUserId(UserInfo userInfo) throws Exception {
		try {
			userInfoDao.updateUserId();
			// 获取当前插入数据用户ID
			int userId = userInfoDao.getCurrentInsertUserId();
			return userId;
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			throw e;
		}
	}

	@Override
	public UserPreApplyData getUserPreApplyList(UserPreApply userPreApply,
			int currentPage, int pageSize) {
		return userInfoDao.getUserPreApplyList(userPreApply, currentPage,
				pageSize);
	}

	@Override
	public UserPreApply findUserPreApplyByPreApplyId(long preApplyId) {
		return userInfoDao.findUserPreApplyByPreApplyId(preApplyId);
	}

	/**
	 * 因涉及多个库，把切换数据库的语句放在这边
	 */
	@Override
	public int auditUserPreApply(long preApplyId, int status) {
		int isSuccess = 0;
		try {
			DbGenerator.setFucaiDataSource(DbGenerator.SLAVE);
			UserPreApply userPreApply = userInfoDao
					.findUserPreApplyByPreApplyId(preApplyId);
			if (userPreApply.getStatus() != UserAccountConstant.UserPreApplyAuditState.NOT_AUDIT
					.getValue()) {
				return ServiceStatusCodeUtil.STATUS_CODE_USERPREAPPLY_ISAUDIT;
			}
			DbGenerator.setFucaiDataSource(DbGenerator.MASTER);
			isSuccess = userInfoDao.auditUserPreApply(preApplyId, status);
			if (isSuccess <= 0) {
				return isSuccess;
			}
			// String serialNumber = IdWorker.getSerialNumber();
			// // 写提现流水日志
			// UserAccountLog userAccountLog = new UserAccountLog();
			// userAccountLog.setUserId(userPreApply.getUserId());
			// userAccountLog.setPartnerId(userPreApply.getPartnerId());
			// userAccountLog
			// .setLogType(UserAccountConstant.UserAccountLogType.USERPREAPPLY
			// .getValue());
			// userAccountLog.setTotalAmount(userPreApply.getPreMoney());
			// userAccountLog.setAccountAmount(0);
			// userAccountLog.setHandselAmount(0);
			// userAccountLog.setSerialNumber(serialNumber);
			// userAccountLog.setRemark("用户预存款审核通过");
			// isLogSuccess = createUserAccountLog(userAccountLog);
			// if (isLogSuccess <= 0) {
			// return isLogSuccess;
			// }
		} catch (DaoLevelException e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		}
		return ServiceStatusCodeUtil.STATUS_CODE_OPERATE_SUCCESS;
	}

	@Override
	public int addUserPreApplyAmount(long userId, long amount) {
		int isSuccess = 0;
		isSuccess = userInfoDao.increaseUserAccountMoney(userId, amount);
		return isSuccess;
	}

	@Override
	public Map<String, Long> statisticRecharge(String date) {
		String beginTime = date + " 00:00:00";
		String endTime = date + " 23:59:59";
		List<StatisticResult> list = null;
		String partnerId = null;
		long totalAmount = 0;
		Map<String, Long> resultMap = new HashMap<String, Long>();
		List<String> allTableIndex = DbGenerator.getAllTableIndex();
		for (String tableIndex : allTableIndex) {
			list = accountInfoDao.countRecharge(beginTime, endTime, tableIndex);
			for (StatisticResult result : list) {
				partnerId = result.getPartnerId();
				totalAmount = result.getTotalAmount();
				if (resultMap.containsKey(partnerId)) {
					totalAmount += resultMap.get(partnerId);
				}
				resultMap.put(partnerId, totalAmount);
			}
		}
		return resultMap;
	}

	@Override
	public Map<String, Long> statisticWithdraw(String date) {
		String beginTime = date + " 00:00:00";
		String endTime = date + " 23:59:59";
		List<StatisticResult> list = accountInfoDao.countWithdraw(beginTime,
				endTime);
		Map<String, Long> resultMap = new HashMap<String, Long>();
		for (StatisticResult result : list) {
			resultMap.put(result.getPartnerId(), result.getTotalAmount());
		}
		return resultMap;
	}

	@Override
	public void updateHandselState() {
		Log.run.debug("updateHandselState start");
		int dbStartNum = Integer.valueOf(Configuration
				.getConfigValue("DB_START_NUM"));
		int dbEndNum = Integer.valueOf(Configuration
				.getConfigValue("DB_END_NUM"));
		long totalInvalidHandsel = 0;
		for (int i = dbStartNum; i < dbEndNum; i++) {
			String dbName = DbGenerator.getDbNameByIndex(i);
			DataSourceContextHolder.setDataSourceType(DbGenerator.MASTER
					+ dbName);
			totalInvalidHandsel += accountInfoDao.getTotalInvalidHandsel();
			accountInfoDao.updateHandselState();
		}
		DbGenerator.setFucaiDataSource(DbGenerator.MASTER);
		String today = DateUtil.getDateTime("yyyy-MM-dd", new Date());
		accountInfoDao.setInvalidHandsel(today, totalInvalidHandsel);
		Log.run.debug("updateHandselState end");
	}

	@Override
	public boolean checkUserExist(long userId) {
		try {
			Object oldUserId = memcachedClient.get((getUserKey("" + userId)));
			if (oldUserId != null) {
				Log.run.warn("found user in memcachedClient, userId = %s",
						oldUserId);
				return true;
			}
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error("memcached error.%s", e.toString());
		}

		// 校验来源渠道ID、渠道账户ID是否已经创建用户
		SolrServer server = SolrFactory
				.getSolrServer(ConstantsUtil.CORENAME_USERINFO);
		try {
			List<SolrDocument> data = server.findData("userId:\"" + userId
					+ "\"", 0, 10);
			if (data.size() > 0) {
				Log.run.warn("user already exist。");
				return true;
			}
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error("SolrServer error.%s", e.toString());
		}
		UserAccount userInfo = userInfoDao.findUserAccountByUserId(userId);
		if (userInfo != null) {
			Log.run.debug("found user in database");
			return true;
		}
		return false;
	}

	@Override
	public int initHandselCount(String day) {
		return accountInfoDao.initHandselCount(day);
	}

	@Override
	public void increaseHandselCount(UserHandsel userHandsel) {
		try {
			String today = DateUtil.getDateTime("yyyy-MM-dd", new Date());
			int result = accountInfoDao.increaseHandselCount(today,
					userHandsel.getUsableAmount());
			if (result <= 0) {
				accountInfoDao.initHandselCount(today);
				accountInfoDao.increaseHandselCount(today,
						userHandsel.getUsableAmount());
			}
		} catch (Exception e) {
			Log.run.debug("", e);
		}
	}

	@Override
	public UserHandselCount getUserHandselCount(String date) {
		return accountInfoDao.getUserHandselCount(date);
	}

	@Override
	public long totalAccountMoney() {
		Log.run.debug("get user totalAccountMoney start");
		int dbStartNum = Integer.valueOf(Configuration
				.getConfigValue("DB_START_NUM"));
		int dbEndNum = Integer.valueOf(Configuration
				.getConfigValue("DB_END_NUM"));
		long totalMoneyl = 0;
		for (int i = dbStartNum; i < dbEndNum; i++) {
			String dbName = DbGenerator.getDbNameByIndex(i);
			DataSourceContextHolder.setDataSourceType(DbGenerator.SLAVE
					+ dbName);
			totalMoneyl += userInfoDao.totalAccountMoney();
		}
		Log.run.debug("totalAccountMoney is %d", totalMoneyl);
		return totalMoneyl;
	}

	@Override
	public long totalPaylogNum(String date) {
		String beginTime = date + " 00:00:00";
		String endTime = date + " 23:59:59";
		int dbStartNum = Integer.valueOf(Configuration
				.getConfigValue("DB_START_NUM"));
		int dbEndNum = Integer.valueOf(Configuration
				.getConfigValue("DB_END_NUM"));
		long totalPaylogNum = 0;
		for (int i = dbStartNum; i < dbEndNum; i++) {
			String dbName = DbGenerator.getDbNameByIndex(i);
			DataSourceContextHolder.setDataSourceType(DbGenerator.SLAVE
					+ dbName);
			totalPaylogNum += accountInfoDao.totalPaylogNum(beginTime, endTime);
		}
		return totalPaylogNum;
	}

	@Override
	public WithdrawAccount findWithdrawAccountByNo(String accountNo, long userId) {
		return accountInfoDao.findWithdrawAccountByNo(accountNo, userId);
	}

	@Override
	public int deleteWithdrawApply(long userId, String serialNumber) {
		return accountInfoDao.deleteWithdrawApply(userId, serialNumber);
	}

	@Override
	public int rollbackWithdrawApply(int applyId) {
		return accountInfoDao.rollbackWithdrawApply(applyId);
	}

	@Override
	public int rollbackPreApply(long preApplyId) {
		return userInfoDao.rollbackPreApply(preApplyId);
	}

}
