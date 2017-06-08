package com.cqfc.useraccount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cqfc.protocol.useraccount.UserAccount;
import com.cqfc.protocol.useraccount.UserAccountLog;
import com.cqfc.protocol.useraccount.UserAccountLogData;
import com.cqfc.protocol.useraccount.UserAccountService;
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
import com.cqfc.useraccount.service.IUserAccountService;
import com.cqfc.useraccount.task.StatisticTask;
import com.cqfc.useraccount.util.DataSourceContextHolder;
import com.cqfc.useraccount.util.DbGenerator;
import com.cqfc.util.Configuration;
import com.cqfc.util.DaoLevelException;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.util.SolrFactory;
import com.cqfc.util.SolrServer;
import com.cqfc.util.UserAccountConstant;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Service
public class UserAccountHandler implements UserAccountService.Iface {

	@Resource(name = "userAccountServiceImpl")
	private IUserAccountService userAccountService;

	@Autowired
	ThreadPoolTaskExecutor threadPoolTaskExecutor;

	private SolrServer server;

	private synchronized void getSolr() {
		if (server == null) {
			server = SolrFactory.getSolrServer("userinfo");
		}
	}

	/**
	 * 创建用户资料并创建账户
	 * 
	 * @param userInfo
	 * @return
	 * @throws TException
	 */
	@Override
	public long createUserInfo(UserInfo userInfo) throws TException {
		Log.run.debug("create user info partnerId=%s, partnerUserId=%s",
				userInfo.getPartnerId(), userInfo.getPartnerUserId());
		// DbGenerator.setDynamicDataSource(DbGenerator.MASTER,
		// userInfo.getPartnerId());
		// 校验来源渠道ID、渠道账户ID是否已经创建用户
		if (userAccountService.checkUserExist(userInfo)) {
			return ServiceStatusCodeUtil.STATUS_CODE_USER_ISEXIST;
		}
		String userIdDb = DbGenerator.getUserIdDbName(userInfo);
		DataSourceContextHolder.setDataSourceType(userIdDb);
		long userId = 0;
		try {
			userId = userAccountService.genUserId(userInfo);
			userInfo.setUserId(userId);
			DbGenerator.setDynamicDataSource(DbGenerator.MASTER,
					userInfo.getUserId());
			return userAccountService.createUserInfo(userInfo);
		} catch (Exception e) {
			Log.run.debug(e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	/**
	 * 分页查询用户信息
	 * 
	 * @param userInfo
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * @return
	 * @throws TException
	 */
	@Override
	public UserInfoData getUserInfoList(UserInfo userInfo, int currentPage,
			int pageSize) throws TException {
		Log.run.debug(
				"get user info list userId=%d, nickName=%s,mobile=%s,cardno=%s currentPate=%d, pageSize=%d",
				userInfo.getUserId(), userInfo.getNickName(),
				userInfo.getMobile(), userInfo.getCardNo(), currentPage,
				pageSize);
		DbGenerator.setDynamicDataSource(DbGenerator.SLAVE,
				userInfo.getUserId());
		return userAccountService.getUserInfoList(userInfo, currentPage,
				pageSize);
	}

	/**
	 * 根据用户ID查询用户信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws TException
	 */
	@Override
	public UserInfo findUserInfoById(long userId) throws TException {
		Log.run.debug("find user userId=%d", userId);
		DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
		return userAccountService.findUserInfoById(userId);
	}

	/**
	 * 
	 * @param userId
	 * @param state
	 * @return
	 * @throws TException
	 */
	@Override
	public int modifyUserAccountState(long userId, int state) throws TException {
		Log.run.debug("modify user account userId=%d, state=%d", userId, state);
		try {
			DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userId);
			return userAccountService.modifyUserAccountState(userId, state);
		}  catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	/**
	 * 根据用户ID查询账户信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws TException
	 */
	@Override
	public UserAccount findUserAccountByUserId(long userId) throws TException {
		Log.run.debug("find user account, userId=%d", userId);
		DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
		return userAccountService.findUserAccountByUserId(userId);
	}

	/**
	 * 冻结用户账户资金(追号使用)
	 * 
	 * @param userId
	 *            用户ID
	 * @param amount
	 *            冻结金额
	 * @param freezeSerialNumber
	 *            冻结流水号
	 * @return
	 * @throws TException
	 */
	@Override
	public int freezeUserAccount(long userId, long amount,
			String freezeSerialNumber) throws TException {
		Log.run.debug("freeze user account, userId=%d, amount=%d, freezeSN=%s",
				userId, amount, freezeSerialNumber);
		try {
			DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userId);
			return userAccountService.freezeUserAccount(userId, amount,
					freezeSerialNumber);
		}  catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	/**
	 * 扣除冻结资金（追号详情完成一项）
	 * 
	 * @param userId
	 *            用户ID
	 * @param amount
	 *            扣除冻结金额
	 * @param paySerialNumber
	 *            扣除冻结金额流水号
	 * @return
	 * @throws TException
	 */
	@Override
	public int deductFreezeMoney(long userId, long amount,
			String paySerialNumber) throws TException {
		Log.run.debug("deduct freeze money, userId=%d, amount=%d, paySN=%s",
				userId, amount, paySerialNumber);
		try {
			DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userId);
			return userAccountService.deductFreezeMoney(userId, amount,
					paySerialNumber);
		}  catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	/**
	 * 退还冻结资金（直接退还到可用资金账户）---追号订单追号失败退款
	 * 
	 * @param userId
	 *            用户ID
	 * @param amount
	 *            退还金额
	 * @param freezeSerialNumber
	 *            扣除冻结金额流水号
	 * @param refundSerialNumber
	 *            退还冻结金额流水号
	 * @return
	 * @throws TException
	 */
	@Override
	public int refundFreezeMoney(long userId, long amount,
			String freezeSerialNumber, String refundSerialNumber)
			throws TException {
		Log.run.debug(
				"refund freeze money userId=%d, amount=%d, freezeSN=%s, refundSN=%s",
				userId, amount, freezeSerialNumber, refundSerialNumber);
		try {
			DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userId);
			return userAccountService.refundFreezeMoney(userId, amount,
					freezeSerialNumber, refundSerialNumber);
		} catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	/**
	 * 修改用户账户兑奖密码
	 * 
	 * @param userId
	 *            用户ID
	 * @param oldPasswd
	 *            旧密码
	 * @param newPasswd
	 *            新密码
	 * @return
	 * @throws TException
	 */
	@Override
	public int updatePrizePassword(long userId, String oldPasswd,
			String newPasswd) throws TException {
		Log.run.debug("update prize password userId=%d", userId);
		try {
			DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userId);
			return userAccountService.updatePrizePassword(userId, oldPasswd,
					newPasswd);
		} catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	/**
	 * 创建用户预申请记录
	 * 
	 * @param userPreApply
	 * @return
	 * @throws TException
	 */
	@Override
	public int createUserPreApply(UserPreApply userPreApply) throws TException {
		Log.run.debug(
				"create user preapply, userId=%d,partnerId=%s,preMoney=%d",
				userPreApply.getUserId(), userPreApply.getPartnerId(),
				userPreApply.getPreMoney());
		try {
			DbGenerator.setFucaiDataSource(DbGenerator.MASTER);
			return userAccountService.createUserPreApply(userPreApply);
		}  catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	/**
	 * 彩金赠送(写流水日志)
	 * 
	 * @param userHandsel
	 * @return
	 * @throws TException
	 */
	@Override
	public int createUserHandsel(UserHandsel userHandsel) throws TException {
		Log.run.debug(
				"create user handsel userId=%d, partnerId=%s, partnerHandselId=%s, usedAmount=%d, usableAmount = %d",
				userHandsel.getUserId(), userHandsel.getPartnerId(),
				userHandsel.getPartnerHandselId(), userHandsel.getUsedAmount(),
				userHandsel.getUsableAmount());
		try {
			DbGenerator.setDynamicDataSource(DbGenerator.MASTER,
					userHandsel.getUserId());
			int result = userAccountService.createUserHandsel(userHandsel);
			if(result>0){
				DbGenerator.setFucaiDataSource(DbGenerator.MASTER);
				userAccountService.increaseHandselCount(userHandsel);
			}
			return result;
		}  catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	/**
	 * 分页查询彩金列表
	 * 
	 * @param userHandsel
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * @return
	 * @throws TException
	 */
	@Override
	public UserHandselData getUserHandselList(UserHandsel userHandsel,
			int currentPage, int pageSize) throws TException {
		Log.run.debug("get user handsel list, userId=%d",
				userHandsel.getUserId());
		DbGenerator.setDynamicDataSource(DbGenerator.SLAVE,
				userHandsel.getUserId());
		return userAccountService.getUserHandselList(userHandsel, currentPage,
				pageSize);
	}

	/**
	 * 通过用户ID查询彩金列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param state
	 *            状态：1有效 2无效 0查询所有
	 * @return
	 * @throws TException
	 */
	@Override
	public List<UserHandsel> getUserHandselListByUserId(long userId, int state)
			throws TException {
		Log.run.debug("get user handsel list by userId userId=%d, state=%d",
				userId, state);
		DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
		return userAccountService.getUserHandselListByUserId(userId, state);
	}

	/**
	 * 彩金状态修改(判断时间是否失效)
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws TException
	 */
	@Override
	public int modifyUserHandselState(long userId) throws TException {
		Log.run.debug("modify user handsel state userId=%d", userId);
		try {
			DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userId);
			return userAccountService.modifyUserHandselState(userId);
		}  catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	/**
	 * 查询用户可使用彩金列表（按过期时间升序）
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws TException
	 */
	@Override
	public List<UserHandsel> getUsableUserHandselList(long userId)
			throws TException {
		Log.run.debug("get usable user handsel,userId=%d", userId);
		DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
		return userAccountService.getUsableUserHandselList(userId);
	}

	/**
	 * 根据彩金交易在合作商的id查询
	 * 
	 * @param partnerHandselId
	 *            彩金交易ID
	 * @return
	 * @throws TException
	 */
	@Override
	public UserHandsel findUserHandselByPartnerId(String partnerId,
			String partnerHandselId) throws TException {
		Log.run.debug("find user handsel by partnerId=%s, partnerHandselId=%s",
				partnerId, partnerHandselId);
		DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, partnerHandselId);
		return userAccountService.findUserHandselByPartnerId(partnerId,
				partnerHandselId);
	}

	/**
	 * 创建提款帐号
	 * 
	 * @param withdrawAccount
	 * @return
	 * @throws TException
	 */
	@Override
	public int createWithdrawAccount(WithdrawAccount withdrawAccount)
			throws TException {
		Log.run.debug(
				"create withdraw acccount, userId=%d,bankName=%s, accountNo=%s",
				withdrawAccount.getUserId(), withdrawAccount.getBankName(),
				withdrawAccount.getAccountNo());
		try {
			DbGenerator.setDynamicDataSource(DbGenerator.MASTER,
					withdrawAccount.getUserId());
			return userAccountService.createWithdrawAccount(withdrawAccount);
		}  catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	/**
	 * 查询用户提款帐号列表
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws TException
	 */
	@Override
	public List<WithdrawAccount> getWithdrawAccountListByUserId(long userId)
			throws TException {
		Log.run.debug("get withdraw acccount, userId=%d", userId);
		DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
		return userAccountService.getWithdrawAccountListByUserId(userId);
	}

	/**
	 * 创建提现记录
	 * 
	 * @param withdrawApply
	 * @return
	 * @throws TException
	 */
	@Override
	public int createWithdrawApply(WithdrawApply withdrawApply)
			throws TException {
		Log.run.debug("create withdraw apply, userId=%d,msgId=%s, amount=%d",
				withdrawApply.getUserId(), withdrawApply.getWithdrawMsgId(),
				withdrawApply.getWithdrawAmount());
		try {
			DbGenerator.setFucaiDataSource(DbGenerator.MASTER);
			int isSuccess = 0;
			try {
				WithdrawAccount account = withdrawApply.getWithdrawAccount();
				long userId = withdrawApply.getUserId();
				DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
				UserInfo userInfo = userAccountService.findUserInfoById(userId);
				if (null == userInfo) {
					return ServiceStatusCodeUtil.STATUS_CODE_USER_NOTEXIST;
				}
				WithdrawAccount withdrawAccount = userAccountService
						.findWithdrawAccountByNo(account.getAccountNo(),
								withdrawApply.getUserId());
				if (null == withdrawAccount) {
					DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userId);
					
					int accountId = userAccountService.createAccountAndGetId(account);
					withdrawApply.setWithdrawAccountId(accountId);
					DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
				} else {
					if (!account.getRealName()
							.equals(withdrawAccount.getRealName())) {
						return ServiceStatusCodeUtil.STATUS_CODE_WITHDRAWACCOUNT_ERROR;
					}
					withdrawApply.setWithdrawAccountId(withdrawAccount
							.getWithdrawAccountId());
				}
				// 创建提现记录
				UserAccount userAccount = userAccountService.findUserAccountByUserId(userId);
				if (userAccount != null) {
					withdrawApply.setTotalAmount(userAccount.getUsableAmount());
				}
				DbGenerator.setFucaiDataSource(DbGenerator.MASTER);
				isSuccess = userAccountService.createWithdrawApply(withdrawApply);
				if (isSuccess <= 0) {
					Log.run.error("create withdraw apply failed.");
					return isSuccess;
				}
				DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userId);
				isSuccess = userAccountService.freezeUserAccoutByWithdraw(withdrawApply);
				if (isSuccess <= 0) {
					Log.run.error("freeze account by withdraw apply failed.rollback withdraw apply.");
					DbGenerator.setFucaiDataSource(DbGenerator.MASTER);
					userAccountService.deleteWithdrawApply(userId,
							withdrawApply.getSerialNumber());
				}

				// }
			} catch (DaoLevelException e) {
				return Integer.valueOf(e.getMessage());
			}
			return isSuccess;
		}  catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	/**
	 * 根据提现ID查询提现记录
	 * 
	 * @param applyId
	 *            申请提现ID
	 * @return
	 * @throws TException
	 */
	@Override
	public WithdrawApply findWithdrawApplyByApplyId(int applyId)
			throws TException {
		Log.run.debug("find withdraw apply, userId=%d", applyId);
		DbGenerator.setFucaiDataSource(DbGenerator.SLAVE);
		return userAccountService.findWithdrawApplyByApplyId(applyId);
	}

	/**
	 * 根据用户ID、流水号查询提现记录
	 * 
	 * @param userId
	 *            用户ID
	 * @param serialNumber
	 *            提现流水号
	 * @return
	 * @throws TException
	 */
	@Override
	public WithdrawApply findWithdrawApply(long userId, String serialNumber)
			throws TException {
		Log.run.debug("find withdraw apply, userId=%d,SN=%s", userId,
				serialNumber);
		DbGenerator.setFucaiDataSource(DbGenerator.SLAVE);
		return userAccountService.findWithdrawApply(userId, serialNumber);
	}

	/**
	 * 根据用户ID查询提现列表
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws TException
	 */
	@Override
	public List<WithdrawApply> getWithdrawApplyListByUserId(long userId)
			throws TException {
		Log.run.debug("get withdraw apply list, userId=%d", userId);
		DbGenerator.setFucaiDataSource(DbGenerator.SLAVE);
		return userAccountService.getWithdrawApplyListByUserId(userId);
	}

	/**
	 * 提现审核（用户ID、流水号、审核状态、审核人ID、审核人、审核备注）、成功:处理金额
	 * 
	 * @param withdrawApply
	 * @return
	 * @throws TException
	 */
	@Override
	public int auditWithdrawApply(WithdrawApply withdrawApply)
			throws TException {
		Log.run.debug(
				"audit withdraw apply, userId=%d, applyId= %d, msgId=%s, amount=%s",
				withdrawApply.getUserId(), withdrawApply.getApplyId(),
				withdrawApply.getWithdrawMsgId(),
				withdrawApply.getWithdrawAmount());
		try {
			int isSuccess = userAccountService.auditWithdrawApply(withdrawApply);

			if (isSuccess <= 0) {
				return isSuccess;
			}
			DbGenerator.setDynamicDataSource(DbGenerator.MASTER,
					withdrawApply.getUserId());
			// 审核状态修改成功,更新用户账户
			isSuccess = userAccountService.updateUserAccountByWithdraw(withdrawApply);
			if (isSuccess <= 0) {
				DbGenerator.setFucaiDataSource(DbGenerator.MASTER);;
				userAccountService
						.rollbackWithdrawApply(withdrawApply.getApplyId());
			}
			return isSuccess;
		}  catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	/**
	 * 分页查询提现申请记录
	 * 
	 * @param withdrawApply
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * @return
	 * @throws TException
	 */
	@Override
	public WithdrawApplyData getWithdrawApplyList(WithdrawApply withdrawApply,
			int currentPage, int pageSize) throws TException {
		Log.run.debug(
				"get withdraw apply, userId=%d, applyId=%d, currentPage=%d, pageSize=%d",
				withdrawApply.getUserId(), withdrawApply.getApplyId(),
				currentPage, pageSize);
		DbGenerator.setFucaiDataSource(DbGenerator.SLAVE);
		return userAccountService.getWithdrawApplyList(withdrawApply,
				currentPage, pageSize);
	}

	/**
	 * 查询预存款申请记录
	 * 
	 * @param partnerId
	 *            用户ID
	 * @param partnerUniqueNo
	 *            预存款合作商交易ID
	 * @return
	 * @throws TException
	 */
	@Override
	public UserPreApply findUserPreApply(String partnerId,
			String partnerUniqueNo) throws TException {
		Log.run.debug("findUserPreApply, partnerId=%s, partnerUniqueNo=%s",
				partnerId, partnerUniqueNo);
		DbGenerator.setFucaiDataSource(DbGenerator.SLAVE);
		return userAccountService.findUserPreApply(partnerId, partnerUniqueNo);
	}

	/**
	 * 创建充值记录(金额、流水处理)
	 * 
	 * @param userRecharge
	 * @return
	 * @throws TException
	 */
	@Override
	public int createUserRecharge(UserRecharge userRecharge) throws TException {
		Log.run.debug("create user recharge, userId=%d, amount=%d",
				userRecharge.getUserId(), userRecharge.getRechargeAmount());
		try {
			DbGenerator.setDynamicDataSource(DbGenerator.MASTER,
					userRecharge.getUserId());
			return userAccountService.createUserRecharge(userRecharge);
		}  catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	/**
	 * 查询用户充值记录列表
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws TException
	 */
	@Override
	public List<UserRecharge> getUserRechargeList(long userId)
			throws TException {
		Log.run.debug("get user recharge, userId=%d", userId);
		DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
		return userAccountService.getUserRechargeList(userId);
	}

	/**
	 * 查询用户充值记录
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param partnerChargeId
	 *            合作商充值ID
	 * @return
	 * @throws TException
	 */
	@Override
	public UserRecharge findUserRecharge(String partnerId,
			String partnerChargeId) throws TException {
		Log.run.debug("find user recharge, partnerId=%s,partnerChargeId=%s",
				partnerId, partnerChargeId);
		DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, partnerId);
		return userAccountService.findUserRecharge(partnerId, partnerChargeId);
	}

	/**
	 * 分页查询流水日志
	 * 
	 * @param userAccountLog
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * @return
	 * @throws TException
	 */
	@Override
	public UserAccountLogData getUserAccountLogList(
			UserAccountLog userAccountLog, int currentPage, int pageSize)
			throws TException {
		Log.run.debug(
				"get user accountLog, userId=%d, currentPage=%d,currentPage=%d",
				userAccountLog.getUserId(), currentPage, pageSize);
		DbGenerator.setDynamicDataSource(DbGenerator.SLAVE,
				userAccountLog.getUserId());
		return userAccountService.getUserAccountLogList(userAccountLog,
				currentPage, pageSize);
	}

	/**
	 * 退款
	 * 
	 * @param userId
	 *            用户ID
	 * @param paySerialNumber
	 *            支付流水号
	 * @param refundSerialNumber
	 *            退款流水号
	 * @param amount
	 *            退款金额
	 * @return
	 * @throws TException
	 */
	@Override
	public int modifyRefund(long userId, String paySerialNumber,
			String refundSerialNumber, long amount) throws TException {

		Log.run.debug(
				"modify refund, userId=%d, paySN=%s,refundSN=%s, amount=%d",
				userId, paySerialNumber, refundSerialNumber, amount);
		try {
			DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userId);
			return userAccountService.modifyRefund(userId, paySerialNumber,
					refundSerialNumber, amount);
		} catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	/**
	 * 支付（优先支付彩金,根据过期时间顺序,然后账户）
	 * 
	 * @param userId
	 *            用户ID
	 * @param serialNumber
	 *            流水号
	 * @param amount
	 *            支付金额
	 * @return
	 * @throws TException
	 */
	@Override
	public int payUserAccount(long userId, String serialNumber, long amount)
			throws TException {
		Log.run.debug("pay user account, userId=%d, SN=%s, amount=%d", userId,
				serialNumber, amount);
		try {
			DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userId);
			return userAccountService.payUserAccount(userId, serialNumber,
					amount);
		} catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	/**
	 * 派奖(直接增加到用户帐户、写流水日志)
	 * 
	 * @param userId
	 *            用户ID
	 * @param serialNumber
	 *            流水号
	 * @param amount
	 *            派奖金额
	 * @return
	 * @throws TException
	 */
	@Override
	public int sendPrize(long userId, String serialNumber, long amount)
			throws TException {
		Log.run.debug("send prize, userId=%d, SN=%s, amount=%d", userId,
				serialNumber, amount);
		try {
			DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userId);
			return userAccountService.sendPrize(userId, serialNumber, amount);
		}  catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	/**
	 * 分页查询预存款申请记录
	 * 
	 * @param userPreApply
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws TException
	 */
	@Override
	public UserPreApplyData getUserPreApplyList(UserPreApply userPreApply,
			int currentPage, int pageSize) throws TException {
		Log.run.debug("getUserPreApplyList, userId=%d, SN=%d, amount=%d",
				userPreApply.getUserId(), currentPage, pageSize);
		DbGenerator.setFucaiDataSource(DbGenerator.SLAVE);
		return userAccountService.getUserPreApplyList(userPreApply,
				currentPage, pageSize);
	}

	/**
	 * 预存款申请审核
	 * 
	 * @param preApplyId
	 *            预存款申请ID
	 * @param status
	 *            审核状态
	 * @return
	 * @throws TException
	 */
	@Override
	public int auditUserPreApply(long preApplyId, int status) throws TException {
		Log.run.debug("auditUserPreApply, preApplyId=%d, status=%d",
				preApplyId, status);
		try {
			int isSuccess = userAccountService
					.auditUserPreApply(preApplyId, status);
			DbGenerator.setFucaiDataSource(DbGenerator.SLAVE);
			UserPreApply userPreApply = userAccountService
					.findUserPreApplyByPreApplyId(preApplyId);
			if (isSuccess > 0) {
				DbGenerator.setDynamicDataSource(DbGenerator.MASTER,
						userPreApply.getUserId());
				if (status == UserAccountConstant.UserPreApplyAuditState.AUDIT_PASS
						.getValue()) {
					isSuccess = userAccountService.updateUserAccountByPreapply(userPreApply);
					if (isSuccess <= 0) {
						DbGenerator.setFucaiDataSource(DbGenerator.MASTER);
						userAccountService.rollbackPreApply(preApplyId);
					}
				}
			}
			return isSuccess;
		}  catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	@Override
	public Map<String, Long> statisticRecharge(String date) throws TException {
		Log.run.debug("statisticRecharge, date=%s", date);

		int dbStartNum = Integer.valueOf(Configuration
				.getConfigValue("DB_START_NUM"));
		int dbEndNum = Integer.valueOf(Configuration
				.getConfigValue("DB_END_NUM"));
		CountDownLatch latch = new CountDownLatch(dbEndNum - dbStartNum);
		Queue<Map<String, Long>> resultQueue = new ConcurrentLinkedQueue<Map<String, Long>>();
		for (int i = dbStartNum; i < dbEndNum; i++) {
			threadPoolTaskExecutor
					.submit(new StatisticTask(latch, DbGenerator
							.getDbNameByIndex(i), resultQueue, date,
							userAccountService));
		}
		Map<String, Long> resultMap = new HashMap<String, Long>();
		try {
			latch.await();
			resultMap.putAll(resultQueue.remove());
			int size = resultQueue.size();
			Map<String, Long> tmpMap = null;
			String partnerId = null;
			long totalAmount = 0;
			for (int i = 0; i < size; i++) {
				tmpMap = resultQueue.remove();
				for (Entry<String, Long> entry : tmpMap.entrySet()) {
					partnerId = entry.getKey();
					totalAmount = entry.getValue();
					if (resultMap.containsKey(partnerId)) {
						totalAmount += resultMap.get(partnerId);
					}
					resultMap.put(partnerId, totalAmount);
				}
			}

		} catch (Exception e) {
			Log.run.debug("", e);
		}

		Log.run.debug("statisticRecharge end, size = %d", resultMap.size());
		return resultMap;
	}

	@Override
	public Map<String, Long> statisticWithdraw(String date) throws TException {
		Log.run.debug("statisticWithdraw, date=%s", date);
		DbGenerator.setFucaiDataSource(DbGenerator.SLAVE);
		Map<String, Long> map = userAccountService.statisticWithdraw(date);
		Log.run.debug("statisticWithdraw end, size = %d", map.size());
		return map;
	}

	@Override
	public boolean checkUserExist(long userId) throws TException {
		try {
			Log.run.debug("check user exist, userId = %d", userId);
			DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
			return userAccountService.checkUserExist(userId);
		} catch (Exception e) {
			Log.run.debug("", e);
			return false;
		}
	}

	@Override
	public int initHandselCount(String day) throws TException {
		try {
			Log.run.debug("initHandselCount, day = %s", day);
			DbGenerator.setFucaiDataSource(DbGenerator.MASTER);
			return userAccountService.initHandselCount(day);
		} catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	@Override
	public UserHandselCount getUserHandselCount(String date) throws TException {
		try {
			Log.run.debug("getUserHandselCount, day = %s", date);
			DbGenerator.setFucaiDataSource(DbGenerator.SLAVE);
			return userAccountService.getUserHandselCount(date);
		} catch (Exception e) {
			Log.run.debug("", e);
			return null;
		}
	}

	@Override
	public long totalAccountMoney() throws TException {
		try {
			Log.run.debug("get user totalAccountMoney");
			DbGenerator.setFucaiDataSource(DbGenerator.SLAVE);
			long totalMoney = userAccountService.totalAccountMoney();
			Log.run.debug("totalAccountMoney is %d", totalMoney);
			return totalMoney;
		} catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}
	@Override
	public long totalPaylogNum(String date) throws TException {
		try {
			Log.run.debug("get user totalPaylogNum");
			DbGenerator.setFucaiDataSource(DbGenerator.SLAVE);
			long totalPaylog = userAccountService.totalPaylogNum(date);
			Log.run.debug("totalPaylogNum is %d", totalPaylog);
			return totalPaylog;
		} catch (Exception e) {
			Log.run.debug("", e);
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

}
