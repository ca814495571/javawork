package com.cqfc.management.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cqfc.management.model.AccountInfo;
import com.cqfc.management.model.AccountLog;
import com.cqfc.management.model.OrderInfo;
import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.ResultObj;
import com.cqfc.management.model.UserDepositCheck;
import com.cqfc.management.model.UserWithDrawCheck;
import com.cqfc.management.service.IUserService;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.useraccount.UserAccount;
import com.cqfc.protocol.useraccount.UserAccountLog;
import com.cqfc.protocol.useraccount.UserAccountLogData;
import com.cqfc.protocol.useraccount.UserHandsel;
import com.cqfc.protocol.useraccount.UserInfo;
import com.cqfc.protocol.useraccount.UserInfoData;
import com.cqfc.protocol.useraccount.UserPreApply;
import com.cqfc.protocol.useraccount.UserPreApplyData;
import com.cqfc.protocol.useraccount.WithdrawAccount;
import com.cqfc.protocol.useraccount.WithdrawApply;
import com.cqfc.protocol.useraccount.WithdrawApplyData;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.UserAccountConstant;
import com.cqfc.util.UserAccountConstant.UserPreApplyAuditState;
import com.jami.util.Log;

@Service
public class UserServiceImpl implements IUserService {

	@Override
	public PcResultObj getUsers(UserInfo userInfo, int pageNum, int pageSize) {

		PcResultObj pcResultObj = new PcResultObj();

		List<UserInfo> userInfos = new ArrayList<UserInfo>();

		com.cqfc.processor.ReturnMessage message = TransactionProcessor
				.dynamicInvoke("userAccount", "getUserInfoList", userInfo,
						pageNum, pageSize);

		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {

			UserInfoData userInfoDate = (UserInfoData) message.getObj();

			userInfos = userInfoDate.getResultList();

			if (userInfos.size() > 0) {

				pcResultObj.setEntity(userInfos);
				pcResultObj.setMsg("查询成功");
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				return pcResultObj;
			}

		}

		pcResultObj.setMsg("查询失败");
		pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		return pcResultObj;
	}

	@Override
	public PcResultObj getUserById(long id) {

		PcResultObj pcResultObj = new PcResultObj();

		UserInfo userInfo = new UserInfo();

		com.cqfc.processor.ReturnMessage message = TransactionProcessor
				.dynamicInvoke("userAccount", "findUserInfoById", id);

		if (message.getObj() != null) {

			userInfo = (UserInfo) message.getObj();
			com.cqfc.management.model.UserInfo user = new com.cqfc.management.model.UserInfo();
			user.setUserId(userInfo.getUserId());
			user.setCardNo(userInfo.getCardNo());
			user.setBalance(MoneyUtil.toYuanStr(userInfo.getUserAccount()
					.getTotalAmount()));
			user.setrealName(userInfo.getUserName());
			user.setPartnerId(userInfo.getPartnerId());
			user.setPhone(userInfo.getMobile());
			user.setCreateTime(DateUtil.getSubstringDateTime(userInfo.getCreateTime()));
			

			message = TransactionProcessor.dynamicInvoke("partner",
					"findLotteryPartnerById", userInfo.getPartnerId());

			if (message.getObj() != null) {
				LotteryPartner partner = (LotteryPartner) message.getObj();
				user.setPartenrName(partner.getPartnerName());
			}

			pcResultObj.setEntity(user);
			pcResultObj.setMsg("查询成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);

			return pcResultObj;
		} else {

			pcResultObj.setMsg("查询用户不存在");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);

			return pcResultObj;

		}

	}

	@Override
	public PcResultObj getUserWithdrawApply(
			UserWithDrawCheck userWithDrawCheck, int pageNum, int pageSize) {
		Log.run.debug("查询用户提现记录");
		PcResultObj pcResultObj = new PcResultObj();
		List<WithdrawApply> withdrawApplies = new ArrayList<WithdrawApply>();

		WithdrawApply withdrawApply = new WithdrawApply();

		if (userWithDrawCheck.getUserId() != null
				&& !"".equals(userWithDrawCheck.getUserId())) {

			try {
				withdrawApply.setUserId(Long.parseLong(userWithDrawCheck
						.getUserId()));
			} catch (Exception e) {

				pcResultObj.setMsg("用户Id格式不正确");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				return pcResultObj;
			}
		}
		if (userWithDrawCheck.getFromTime() != null
				&& !"".equals(userWithDrawCheck.getFromTime())) {
			withdrawApply.setSearchBeginTime(userWithDrawCheck.getFromTime()
					+ " 00:00:00");
		}
		if (userWithDrawCheck.getToTime() != null
				&& !"".equals(userWithDrawCheck.getToTime())) {
			withdrawApply.setSearchEndTime(userWithDrawCheck.getToTime()
					+ " 23:59:59");
		}
		
		if (userWithDrawCheck.getStatus() == null
				|| "".equals(userWithDrawCheck.getStatus())) {

			withdrawApply.setAuditState(0);
		} else {

			withdrawApply.setAuditState(userWithDrawCheck.getStatus());
		}
		
		withdrawApply.setPartnerApplyId(userWithDrawCheck.getPartnerApplyId());
		
		withdrawApply.setPartnerApplyId(userWithDrawCheck.getPartnerApplyId());
		com.cqfc.processor.ReturnMessage message = TransactionProcessor
				.dynamicInvoke("userAccount", "getWithdrawApplyList",
						withdrawApply, pageNum, pageSize);

		List<UserWithDrawCheck> drawChecks = new ArrayList<UserWithDrawCheck>();
		if (message.getObj() != null) {
			Log.run.debug("调用userAcount中getWithdrawApplyList方法成功，返回结果有值");
			WithdrawApplyData withdrawApplyData = (WithdrawApplyData) message
					.getObj();
			withdrawApplies = withdrawApplyData.getResultList();

			if (withdrawApplies.size() > 0) {

				UserWithDrawCheck temp = null;
				WithdrawAccount withdrawAccount = null;
				UserInfo userInfo = null;

				for (int i = 0; i < withdrawApplies.size(); i++) {

					temp = new UserWithDrawCheck();
					withdrawApply = withdrawApplies.get(i);
					withdrawAccount = withdrawApply.getWithdrawAccount();
					temp.setApplyId(withdrawApply.getApplyId());
					temp.setBankName(withdrawAccount.getBankName());
					temp.setCreateTime(DateUtil.getSubstringDateTime(withdrawApply.getCreateTime()));
					temp.setRealName(withdrawApply.getRealName());
					temp.setRecieveMode(withdrawAccount.getAccountType());
					temp.setAccountNo(withdrawAccount.getAccountNo());
					temp.setStatus(withdrawApply.getAuditState());
					temp.setUserId(String.valueOf(withdrawApply.getUserId()));
					message = TransactionProcessor.dynamicInvoke("userAccount",
							"findUserInfoById", withdrawApply.getUserId());
					if (message.getObj() != null) {

						userInfo = (UserInfo) message.getObj();
						temp.setPhone(userInfo.getMobile());
						temp.setPartnerId(userInfo.getPartnerId());
					}
					temp.setTotalMoney(MoneyUtil.toYuanStr(withdrawApply
							.getTotalAmount()));
					temp.setPartnerApplyId(withdrawApply.getPartnerApplyId());
					temp.setWithDrawMoney(MoneyUtil.toYuanStr(withdrawApply
							.getWithdrawAmount()));

					drawChecks.add(temp);
				}
			}
			ResultObj resultObj = new ResultObj();
			resultObj.setObjects(drawChecks);
			resultObj.setRecordTotal(withdrawApplyData.getTotalSize());
			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsg("查询成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			return pcResultObj;
		}
		pcResultObj.setMsg("查询无记录");
		pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		return pcResultObj;
	}

	@Override
	public PcResultObj checkWithdrawSuc(int id) {

		PcResultObj pcResultObj = new PcResultObj();

		WithdrawApply withdrawApply = new WithdrawApply();

		com.cqfc.processor.ReturnMessage message = TransactionProcessor
				.dynamicInvoke("userAccount", "findWithdrawApplyByApplyId", id);

		if (message.getObj() != null) {

			withdrawApply = (WithdrawApply) message.getObj();
		} else {

			pcResultObj.setMsg("审核数据id有误");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			return pcResultObj;
		}

		int flag = 0;
		withdrawApply
				.setAuditState(UserAccountConstant.WithdrawAuditState.AUDIT_PASS
						.getValue());

		message = TransactionProcessor.dynamicInvoke("userAccount",
				"auditWithdrawApply", withdrawApply);

		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {

			flag = (Integer) message.getObj();

			if (flag == 1) {

				pcResultObj.setMsg("审核成功");
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);

				return pcResultObj;
			} else if (flag == -4) {

				pcResultObj.setMsg("已审核");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);

				return pcResultObj;

			}

		}
		pcResultObj.setMsg("操作失败");
		pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);

		return pcResultObj;
	}

	@Override
	public PcResultObj checkWithdrawFail(int id) {

		PcResultObj pcResultObj = new PcResultObj();

		WithdrawApply withdrawApply = new WithdrawApply();

		com.cqfc.processor.ReturnMessage message = TransactionProcessor
				.dynamicInvoke("userAccount", "findWithdrawApplyByApplyId", id);

		if (message.getObj() != null) {

			withdrawApply = (WithdrawApply) message.getObj();
		} else {

			pcResultObj.setMsg("审核数据id有误");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			return pcResultObj;
		}

		int flag = 0;
		withdrawApply
				.setAuditState(UserAccountConstant.WithdrawAuditState.AUDIT_NOPASS
						.getValue());

		message = TransactionProcessor.dynamicInvoke("userAccount",
				"auditWithdrawApply", withdrawApply);

		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {

			flag = (Integer) message.getObj();

			if (flag == 1) {

				pcResultObj.setMsg("审核成功");
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);

				return pcResultObj;
			}

		}
		pcResultObj.setMsg("操作失败");
		pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);

		return pcResultObj;
	}

	@Override
	public PcResultObj getAccountInfoByWhere(AccountInfo accountInfo) {

		PcResultObj pcResultObj = new PcResultObj();
		UserAccount userAccount = new UserAccount();
		UserInfo userInfo = new UserInfo();
		List<UserHandsel> userHandsels = new ArrayList<UserHandsel>();

		try {

			if (accountInfo.getAccountNo() != null
					&& !"".equals(accountInfo.getAccountNo())) {

				String id = accountInfo.getAccountNo();
				com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor
						.dynamicInvoke("userAccount", "findUserInfoById",
								Long.parseLong(id));

				if (returnMessage.getObj() != null) {

					long handselTotal = 0;
					userInfo = (UserInfo) returnMessage.getObj();

					userAccount = userInfo.getUserAccount();

					userHandsels = userInfo.getUserHandselList();
					if (userHandsels != null) {

						for (int i = 0; i < userHandsels.size(); i++) {

							handselTotal += userHandsels.get(i)
									.getUsableAmount();
						}

					}
					accountInfo.setAccountName(userInfo.getUserName());
					accountInfo.setHandselMoney(MoneyUtil
							.toYuanStr(handselTotal));
					accountInfo.setAccountNo(id);
					accountInfo.setFreezeMoney(MoneyUtil.toYuanStr(userAccount
							.getFreezeAmount()));
					accountInfo
							.setRealMoney((MoneyUtil.toYuanStr(userAccount
									.getFreezeAmount()
									+ userAccount.getUsableAmount())));
					accountInfo
							.setSumMoney(MoneyUtil.toYuanStr((userAccount
									.getFreezeAmount()
									+ userAccount.getUsableAmount() + handselTotal)));
					accountInfo.setUseableMoney(MoneyUtil.toYuanStr(userAccount
							.getUsableAmount()));

					pcResultObj.setEntity(accountInfo);
					pcResultObj.setMsg("查询成功");
					pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
					return pcResultObj;

				}

			}

		} catch (java.lang.NumberFormatException e) {

			pcResultObj.setMsg("帐号格式错误");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			return pcResultObj;
		}

		pcResultObj.setMsg("查询失败");
		pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		return pcResultObj;

	}

	@Override
	public PcResultObj getLogInfoByWhere(AccountLog accountLog,
			String fromTime, String toTime, int pageNum, int pageSize) {

		PcResultObj pcResultObj = new PcResultObj();

		ResultObj resultObj = new ResultObj();

		List<AccountLog> accountLogs = new ArrayList<AccountLog>();
		UserAccountLog userAccountLog = new UserAccountLog();
		UserAccountLogData userAccountLogData = new UserAccountLogData();
		UserInfo userInfo = null;
		try {

			String id = accountLog.getAccountNo();

			String type = accountLog.getTradeType();
			userAccountLog.setUserId(Long.parseLong(id));
			userAccountLog.setLogType(Integer.parseInt(type));

			if (fromTime != null && !"".equals(fromTime)) {

				userAccountLog.setSearchBeginTime(fromTime + " 00:00:00");
			}
			if (toTime != null && !"".equals(toTime)) {

				userAccountLog.setSearchEndTime(toTime + " 23:59:59");
			}

			com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor
					.dynamicInvoke("userAccount", "getUserAccountLogList",
							userAccountLog, pageNum, pageSize);

			if (returnMessage.getObj() != null) {

				userAccountLogData = (UserAccountLogData) returnMessage
						.getObj();
				List<UserAccountLog> userAccountLogs = userAccountLogData
						.getResultList();

				for (int j = 0; j < userAccountLogs.size(); j++) {

					userAccountLog = userAccountLogs.get(j);

					com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
							.dynamicInvoke("userAccount", "findUserInfoById",
									userAccountLog.getUserId());

					if (reMsg.getObj() != null) {
						userInfo = (UserInfo) reMsg.getObj();
					}
					accountLog = new AccountLog();
					accountLog.setAccountName(userInfo.getUserName());
					accountLog.setAccountNo(String.valueOf(userAccountLog
							.getUserId()));
					accountLog.setCreateTime(DateUtil.getSubstringDateTime(userAccountLog.getCreateTime()));
					accountLog.setSerialNum(userAccountLog.getSerialNumber());
					accountLog.setTradeMoney(MoneyUtil.toYuanStr(userAccountLog
							.getTotalAmount()));
					accountLog.setAccountAmount(MoneyUtil
							.toYuanStr(userAccountLog.getAccountAmount()));
					accountLog.setHandselAmount(MoneyUtil
							.toYuanStr(userAccountLog.getHandselAmount()));
					accountLog
							.setTradeType(UserAccountConstant.UserAccountLogType
									.getLogTypeName(userAccountLog.getLogType()));

					accountLogs.add(accountLog);
				}

				resultObj.setObjects(accountLogs);
				resultObj.setRecordTotal(userAccountLogData.getTotalSize());
				pcResultObj.setEntity(resultObj);
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				pcResultObj.setMsg("查询成功");
				return pcResultObj;

			}

		} catch (java.lang.NumberFormatException e) {

			pcResultObj.setMsg("帐号格式错误");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			return pcResultObj;
		}

		pcResultObj.setMsg("查询失败");
		pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		return pcResultObj;

	}

	@Override
	public PcResultObj getUserPreSave(UserDepositCheck userDepositCheck,
			int pageNum, int pageSize) {

		ResultObj resultObj = new ResultObj();
		PcResultObj pcResultObj = new PcResultObj();
		UserPreApply userPreApply = new UserPreApply();
		UserInfo userInfo = new UserInfo();
		List<UserPreApply> userPreApplies = new ArrayList<UserPreApply>();
		List<UserDepositCheck> userDepositChecks = new ArrayList<UserDepositCheck>();

		if (!"".equals(userDepositCheck.getUserId())
				&& userDepositCheck.getUserId() != null) {

			try {

				userPreApply.setUserId(Long.parseLong(userDepositCheck
						.getUserId()));

			} catch (Exception e) {

				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				pcResultObj.setMsg("用户Id格式不正确");
				return pcResultObj;
			}
		}

		userPreApply.setPartnerUniqueNo(userDepositCheck.getPartnerUniqueNo());
		userPreApply.setStatus(userDepositCheck.getStatus());

		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("userAccount", "getUserPreApplyList",
						userPreApply, 1, 10000);

		if (reMsg.getObj() != null) {

			UserPreApplyData userPreApplyData = (UserPreApplyData) reMsg
					.getObj();
			userPreApplies = userPreApplyData.getResultList();

			for (int i = 0; i < userPreApplies.size(); i++) {

				userPreApply = userPreApplies.get(i);

				userDepositCheck = new UserDepositCheck();

				userDepositCheck.setUserId(String.valueOf(userPreApply
						.getUserId()));

				com.cqfc.processor.ReturnMessage message = TransactionProcessor
						.dynamicInvoke("userAccount", "findUserInfoById",
								userPreApply.getUserId());

				if (message.getObj() != null) {

					userInfo = (UserInfo) message.getObj();
					userDepositCheck.setUserName(userInfo.getUserName());
					userDepositCheck.setPhone(userInfo.getMobile());
				}
				userDepositCheck.setMoney(MoneyUtil.toYuanStr(userPreApply
						.getPreMoney()));
				userDepositCheck.setPartnerUniqueNo(userPreApply
						.getPartnerUniqueNo());
				userDepositCheck.setStatus(userPreApply.getStatus());
				userDepositCheck.setPartnerId(userPreApply.getPartnerId());
				userDepositCheck.setPartnerUniqueNo(userPreApply
						.getPartnerUniqueNo());
				userDepositCheck.setCreateTime(DateUtil.getSubstringDateTime(userPreApply.getCreateTime()));
				userDepositCheck.setId((int)userPreApply
						.getPreApplyId());
				userDepositChecks.add(userDepositCheck);
			}

			resultObj.setObjects(userDepositChecks);
			resultObj.setRecordTotal(userPreApplyData.getTotalSize());
			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			pcResultObj.setMsg("查询成功");
			return pcResultObj;
		}

		pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		pcResultObj.setMsg("查询失败");
		return pcResultObj;
	}

	@Override
	public PcResultObj CheckUserPreSaveAgree(int applyId) {

		PcResultObj pcResultObj = new PcResultObj();

		int flag = 0;

		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("userAccount", "auditUserPreApply",
						(long) applyId, UserPreApplyAuditState.getEnum(2)
								.getValue());

		if (reMsg.getObj() != null) {

			flag = (Integer) reMsg.getObj();

			if (flag == 1) {

				pcResultObj.setMsg("操作成功");
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				return pcResultObj;
			}

		}

		pcResultObj.setMsgCode("2");
		pcResultObj.setMsg("操作失败");

		return pcResultObj;
	}

	@Override
	public PcResultObj CheckUserPreSaveDisAgree(int applyId) {

		PcResultObj pcResultObj = new PcResultObj();

		int flag = 0;

		ReturnMessage reMsg = TransactionProcessor.dynamicInvoke("userAccount",
				"auditUserPreApply", (long) applyId, UserPreApplyAuditState
						.getEnum(3).getValue());

		if (reMsg.getObj() != null) {

			flag = (Integer) reMsg.getObj();

			if (flag == 1) {

				pcResultObj.setMsg("审核成功");
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				return pcResultObj;
			}

		}

		pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		pcResultObj.setMsg("操作失败");

		return pcResultObj;
	}

	@Override
	public PcResultObj getOrderByWhere(OrderInfo orderInfo) {

		PcResultObj pcResultObj = new PcResultObj();

		return pcResultObj;

	}

}
