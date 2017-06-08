package com.cqfc.useraccount.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.cqfc.protocol.useraccount.UserAccountLog;
import com.cqfc.protocol.useraccount.UserAccountLogData;
import com.cqfc.protocol.useraccount.UserHandsel;
import com.cqfc.protocol.useraccount.UserHandselCount;
import com.cqfc.protocol.useraccount.UserHandselData;
import com.cqfc.protocol.useraccount.UserRecharge;
import com.cqfc.protocol.useraccount.WithdrawAccount;
import com.cqfc.protocol.useraccount.WithdrawApply;
import com.cqfc.protocol.useraccount.WithdrawApplyData;
import com.cqfc.useraccount.dao.mapper.AccountInfoMapper;
import com.cqfc.useraccount.util.DateUtil;
import com.cqfc.useraccount.util.DbGenerator;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DaoLevelException;
import com.cqfc.util.Pair;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.util.SolrFactory;
import com.cqfc.util.SolrServer;
import com.cqfc.util.UserAccountConstant;
import com.cqfc.util.UserAccountConstant.UserAccountLogType;
import com.jami.common.StatisticResult;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Repository
public class AccountInfoDao {

	@Autowired
	private AccountInfoMapper accountInfoMapper;

	/**
	 * 创建彩金
	 * 
	 * @param userHandsel
	 * @return
	 */
	public int createUserHandsel(UserHandsel userHandsel)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = accountInfoMapper.createUserHandsel(userHandsel,
					DbGenerator.getTableIndex(userHandsel.getUserId()));
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (DuplicateKeyException e) {
			Log.run.debug("", e);
			throw new DaoLevelException(
					String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.debug("", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 创建流水日志
	 * 
	 * @param userAccountLog
	 * @return
	 */
	public int createUserAccountLog(UserAccountLog userAccountLog)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = accountInfoMapper.createUserAccountLog(
					userAccountLog,
					DbGenerator.getTableIndex(userAccountLog.getUserId()));
			if (returnValue <= 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (DuplicateKeyException e) {
			Log.run.debug(e);
			throw new DaoLevelException(
					String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.debug("", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 分页查询彩金列表
	 * 
	 * @param userHandsel
	 * @return
	 */
	public UserHandselData getUserHandselList(UserHandsel userHandsel,
			int currentPage, int pageSize) {
		UserHandselData returnData = new UserHandselData();
		String conditions = "1=1";
		int totalSize = 0;
		List<UserHandsel> list = new ArrayList<UserHandsel>();
		// 搜索参数
		if (null != userHandsel && userHandsel.getUserId() > 0) {
			conditions += " and userId=" + userHandsel.getUserId();
			if (null != userHandsel.getPartnerId()
					&& !"".equals(userHandsel.getPartnerId())) {
				conditions += " and partnerId='" + userHandsel.getPartnerId()
						+ "'";
			}

			totalSize = countUserHandselSize(conditions,
					userHandsel.getUserId());
			returnData.setTotalSize(totalSize);
			int totalPage = (int) Math.ceil((double) totalSize
					/ (double) pageSize);
			if (totalPage >= currentPage) {
				conditions += " limit " + (currentPage - 1) * pageSize + ","
						+ pageSize;
			}
			list = accountInfoMapper.getUserHandselList(conditions,
					DbGenerator.getTableIndex(userHandsel.getUserId()));
		} else {
			SolrServer server = SolrFactory
					.getSolrServer(ConstantsUtil.CORENAME_HANDSEL);
			String searchText = "";
			if (null != userHandsel.getPartnerId()
					&& !"".equals(userHandsel.getPartnerId())) {
				searchText = "partnerId:\"" + userHandsel.getPartnerId() + "\"";
			}
			searchText += " AND failureTime:["
					+ DateUtil.getDateTime(DateUtil.DATE_FORMAT_WITH_TZ,
							new Date()) + " TO *]";
			int start = (currentPage - 1) * pageSize;
			try {
				List<SolrDocument> findData = server.findData(searchText,
						start, pageSize * 10);
				returnData.setTotalSize(start + findData.size());
				list = new ArrayList<UserHandsel>();
				int userNum = 0;
				Map<Long, String> userPartnerMap = new HashMap<Long, String>();
				for (SolrDocument doc : findData) {
					Long userId = (Long) doc.getFieldValue("userId");
					userPartnerMap.put(userId,
							(String) doc.getFieldValue("partnerId"));
				}
				String limitStr = " limit " + (currentPage - 1) * pageSize
						+ "," + pageSize;
				String tmpConditions = "";
				List<UserHandsel> tmpList = null;

				for (Entry<Long, String> entry : userPartnerMap.entrySet()) {
					Long userId = entry.getKey();
					DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
					tmpConditions = "userId=" + userId;
					tmpConditions += " and partnerId='"
							+ userHandsel.getPartnerId() + "'";
					tmpConditions += limitStr;
					tmpList = accountInfoMapper.getUserHandselList(
							tmpConditions, DbGenerator.getTableIndex(userId));
					userNum += tmpList.size();
					if (userNum >= pageSize) {
						for (int i = 0; i < tmpList.size()
								- (userNum - pageSize); i++) {
							list.add(tmpList.get(i));
						}
						break;
					} else {
						list.addAll(tmpList);
					}
				}
			} catch (SolrServerException e) {
				returnData.setTotalSize(start);
				Log.run.debug("", e);
				Log.run.error("Solr Query Failed", e.toString());
			}
		}

		returnData.setCurrentPage(currentPage);
		returnData.setPageSize(pageSize);
		returnData.setResultList(list);
		return returnData;
	}

	/**
	 * 计算赠送彩金总记录数
	 * 
	 * @param conditions
	 * @return
	 */
	public int countUserHandselSize(String conditions, long userId) {
		// List<String> tableIndexs = DbGenerator.getAllTableIndex();
		// int totalCount = 0;
		// for(String index:tableIndexs){
		// int count = accountInfoMapper.countUserHandselSize(conditions,index);
		// totalCount += count;
		// }
		int totalCount = accountInfoMapper.countUserHandselSize(conditions,
				DbGenerator.getTableIndex(userId));
		return totalCount;
	}

	/**
	 * 通过用户ID查询彩金列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param state
	 *            状态：1有效 2无效 0查询所有
	 * @param isUsable
	 *            是否可用：true(1可用金额大于0、2彩金在有效期内) false(不处理可用金额..)
	 * @return
	 */
	public List<UserHandsel> getUserHandselListByUserId(long userId, int state,
			boolean isUsable) {
		String conditions = "userId=" + userId;
		if (state > 0) {
			conditions += " and state=" + state;
		}
		if (isUsable) {
			conditions += " and usableAmount>0 and validTime <= now() and failureTime >= now() order by failureTime";
		}
		return accountInfoMapper.getUserHandselListByUserId(conditions,
				DbGenerator.getTableIndex(userId));
	}

	/**
	 * 更新彩金状态
	 * 
	 * @param handselId
	 *            彩金ID
	 * 
	 * @param userId
	 *            用户ID
	 * 
	 * @param state
	 *            状态：1有效 2无效
	 * @return
	 */
	public int updateUserHandselState(long handselId, long userId, int state)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = accountInfoMapper.updateUserHandselState(handselId,
					state, DbGenerator.getTableIndex(userId));
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (DuplicateKeyException e) {
			Log.run.debug("", e);
			throw new DaoLevelException(
					String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.debug("", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 创建提款帐号信息
	 * 
	 * @param withdrawAccount
	 * @return
	 */
	public int createWithdrawAccount(WithdrawAccount withdrawAccount)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = accountInfoMapper.createWithdrawAccount(
					withdrawAccount,
					DbGenerator.getTableIndex(withdrawAccount.getUserId()));
		} catch (DuplicateKeyException e) {
			Log.run.debug("", e);
			throw new DaoLevelException(
					String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.debug("", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 更新提款帐号信息
	 * 
	 * @param withdrawAccount
	 * @return
	 */
	public int updateWithdrawAccount(WithdrawAccount withdrawAccount)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = accountInfoMapper.updateWithdrawAccount(
					withdrawAccount,
					DbGenerator.getTableIndex(withdrawAccount.getUserId()));
		} catch (DuplicateKeyException e) {
			Log.run.debug("", e);
			throw new DaoLevelException(
					String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.debug("", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 查询用户提款帐号列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<WithdrawAccount> getWithdrawAccountListByUserId(long userId) {
		return accountInfoMapper.getWithdrawAccountListByUserId(userId,
				DbGenerator.getTableIndex(userId));
	}

	/**
	 * 根据提款帐号ID查询
	 * 
	 * @param withdrawAccountId
	 *            提款帐号ID
	 * @return
	 */
	public WithdrawAccount findWithdrawAccountById(int withdrawAccountId,
			long userId) {
		return accountInfoMapper.findWithdrawAccountById(withdrawAccountId,
				DbGenerator.getTableIndex(userId));
	}

	/**
	 * 创建提现记录
	 * 
	 * @param withdrawApply
	 * @return
	 */
	public int createWithdrawApply(WithdrawApply withdrawApply)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = accountInfoMapper.createWithdrawApply(withdrawApply);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (DuplicateKeyException e) {
			Log.run.debug("", e);
			throw new DaoLevelException(
					String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.debug("", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 删除已创建的提现记录
	 * 
	 * @param userId
	 *            用户ID
	 * @param serialNumber
	 *            流水号
	 * @return
	 */
	public int deleteWithdrawApply(long userId, String serialNumber) {
		return accountInfoMapper.deleteWithdrawApply(userId, serialNumber);
	}

	/**
	 * 修改提现申请
	 * 
	 * @param withdrawApply
	 * @return
	 */
	/*
	 * public int updateWithdrawApply(WithdrawApply withdrawApply) throws
	 * DaoLevelException { int returnValue =
	 * ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR; try{ returnValue =
	 * accountInfoMapper.updateWithdrawApply(withdrawApply);
	 * }catch(DuplicateKeyException e){ return
	 * ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST; }catch(Exception e){
	 * return returnValue; } return returnValue; }
	 */

	/**
	 * 根据提现ID查询提现记录
	 * 
	 * @param applyId
	 * @return
	 */
	public WithdrawApply findWithdrawApplyByApplyId(int applyId) {
		return accountInfoMapper.findWithdrawApplyByApplyId(applyId);
	}

	/**
	 * 根据用户ID、流水号查询提现记录
	 * 
	 * @param userId
	 *            用户ID
	 * @param serialNumber
	 *            流水号
	 * @return
	 */
	public WithdrawApply findWithdrawApply(long userId, String serialNumber) {
		return accountInfoMapper.findWithdrawApply(userId, serialNumber);
	}

	/**
	 * 根据用户ID查询提现列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<WithdrawApply> getWithdrawApplyListByUserId(long userId) {
		return accountInfoMapper.getWithdrawApplyListByUserId(userId);
	}

	/**
	 * 创建充值记录
	 * 
	 * @param userRecharge
	 * @return
	 */
	public int createUserRecharge(UserRecharge userRecharge)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = accountInfoMapper.createUserRecharge(userRecharge,
					DbGenerator.getTableIndex(userRecharge.getUserId()));
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (DuplicateKeyException e) {
			Log.run.debug("", e);
			throw new DaoLevelException(
					String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.debug("", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 查询用户充值记录列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserRecharge> getUserRechargeList(long userId) {
		return accountInfoMapper.getUserRechargeList(userId,
				DbGenerator.getTableIndex(userId));
	}

	/**
	 * 分页查询流水日志
	 * 
	 * @param userAccountLog
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public UserAccountLogData getUserAccountLogList(
			UserAccountLog userAccountLog, int currentPage, int pageSize) {
		UserAccountLogData returnData = new UserAccountLogData();
		String conditions = "1=1";

		// 搜索参数
		if (null != userAccountLog) {
			if (null != userAccountLog.getPartnerId()
					&& !"".equals(userAccountLog.getPartnerId())) {
				conditions += " and partnerId='"
						+ userAccountLog.getPartnerId() + "'";
			}
			if (userAccountLog.getUserId() > 0) {
				conditions += " and userId=" + userAccountLog.getUserId();
			}
			if (userAccountLog.getLogType() > 0) {
				conditions += " and logType=" + userAccountLog.getLogType();
			}
			if (null != userAccountLog.getSearchBeginTime()
					&& !"".equals(userAccountLog.getSearchBeginTime())) {
				conditions += " and createTime>='"
						+ userAccountLog.getSearchBeginTime() + "'";
			}
			if (null != userAccountLog.getSearchEndTime()
					&& !"".equals(userAccountLog.getSearchEndTime())) {
				conditions += " and createTime<='"
						+ userAccountLog.getSearchEndTime() + "'";
			}
		}
		conditions += " order by createTime desc ";
		int totalSize = countUserAccountLogSize(conditions,
				userAccountLog.getUserId());
		int totalPage = (int) Math.ceil((double) totalSize / (double) pageSize);
		if (totalPage >= currentPage) {
			conditions += " limit " + (currentPage - 1) * pageSize + ","
					+ pageSize;
		}
		List<UserAccountLog> list = accountInfoMapper.getUserAccountLogList(
				conditions,
				DbGenerator.getTableIndex(userAccountLog.getUserId()));

		returnData.setCurrentPage(currentPage);
		returnData.setPageSize(pageSize);
		returnData.setTotalSize(totalSize);
		returnData.setResultList(list);
		return returnData;
	}

	/**
	 * 计算流水日志总记录数
	 * 
	 * @param conditions
	 * @return
	 */
	public int countUserAccountLogSize(String conditions, long userId) {
		// List<String> tableIndexs = DbGenerator.getAllTableIndex();
		// int totalCount = 0;
		// for(String index:tableIndexs){
		// int count = accountInfoMapper.countUserAccountLogSize(conditions,
		// index);
		// totalCount += count;
		// }
		int totalCount = accountInfoMapper.countUserAccountLogSize(conditions,
				DbGenerator.getTableIndex(userId));
		return totalCount;
	}

	/**
	 * 根据状态、流水号查询
	 * 
	 * @param state
	 *            收支状态：1充值 2支付 3提现 4退款 5派奖 6彩金赠送 7彩金失效 8彩金退款
	 * @param serialNumber
	 *            流水号
	 * @return
	 */
	public UserAccountLog findUserAccountLog(long userId, int state,
			String serialNumber) {
		return accountInfoMapper.findUserAccountLog(state, serialNumber,
				DbGenerator.getTableIndex(userId));
	}

	/**
	 * 修改彩金金额
	 * 
	 * @param userHandsel
	 * @return
	 */
	public int modifyUserHandsel(UserHandsel userHandsel)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = accountInfoMapper.modifyUserHandsel(userHandsel,
					DbGenerator.getTableIndex(userHandsel.getUserId()));
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (DuplicateKeyException e) {
			Log.run.debug("", e);
			throw new DaoLevelException(
					String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.debug("", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 通过彩金ID查询彩金
	 * 
	 * @param handselId
	 * @return
	 */
	public UserHandsel findUserHandselById(long userId, int handselId) {
		return accountInfoMapper.findUserHandselById(handselId,
				DbGenerator.getTableIndex(userId));
	}

	/**
	 * 提现审核（用户ID、流水号、审核状态、审核人ID、审核人、审核备注）、成功:处理金额
	 * 
	 * @param withdrawApply
	 * @return
	 */
	public int auditWithdrawApply(WithdrawApply withdrawApply) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss",
					new Date());
			withdrawApply.setAuditTime(currentTime);
			returnValue = accountInfoMapper.auditWithdrawApply(withdrawApply);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (DuplicateKeyException e) {
			Log.run.debug("", e);
			throw new DaoLevelException(
					String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.debug("", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 回滚提现申请审批
	 * 
	 * @param applyId
	 * @return
	 */
	public int rollbackWithdrawApply(int applyId) {
		return accountInfoMapper.rollbackWithdrawApply(applyId);
	}

	/**
	 * 查询已退款流水列表
	 * 
	 * @param ext
	 *            支付流水号
	 * @return
	 */
	public List<UserAccountLog> findUserAccountLogByExt(String ext) {
		List<String> tableIndexs = DbGenerator.getAllTableIndex();
		List<UserAccountLog> accountLogs = new ArrayList<UserAccountLog>();
		for (String index : tableIndexs) {
			List<UserAccountLog> list = accountInfoMapper
					.findUserAccountLogByExt(ext, index);
			accountLogs.addAll(list);
		}
		return accountLogs;
	}

	/**
	 * 查询充值记录
	 * 
	 * @param partnerId
	 * @param partnerChargeId
	 * @return
	 */
	public UserRecharge findUserRecharge(String partnerId,
			String partnerChargeId, long userId) {
		return accountInfoMapper.findUserRecharge(partnerId, partnerChargeId,
				DbGenerator.getTableIndex(userId));
	}

	/**
	 * 分页查询提现申请记录
	 * 
	 * @param withdrawApply
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public WithdrawApplyData getWithdrawApplyList(WithdrawApply withdrawApply,
			int currentPage, int pageSize) {
		WithdrawApplyData returnData = new WithdrawApplyData();
		String conditions = "1=1";

		// 搜索参数
		if (null != withdrawApply) {
			if (null != withdrawApply.getSearchBeginTime()
					&& !"".equals(withdrawApply.getSearchBeginTime())) {
				conditions += " and createTime>='"
						+ withdrawApply.getSearchBeginTime() + "'";
			}
			if (null != withdrawApply.getSearchEndTime()
					&& !"".equals(withdrawApply.getSearchEndTime())) {
				conditions += " and createTime<='"
						+ withdrawApply.getSearchEndTime() + "'";
			}
			if (withdrawApply.getUserId() > 0) {
				conditions += " and userId=" + withdrawApply.getUserId();
			}

			if (withdrawApply.getPartnerApplyId() != null
					&& !"".equals(withdrawApply.getPartnerApplyId())) {
				conditions += " and partnerApplyId="
						+ withdrawApply.getPartnerApplyId();
			}
			
			if (StringUtils.isNotBlank(withdrawApply.getSerialNumber())) {
				conditions += " and serialNumber='"
						+ withdrawApply.getSerialNumber()+"'";
			}

			if (withdrawApply.getAuditState() > 0) {
				conditions += " and auditState="
						+ withdrawApply.getAuditState();
			}
		}

		conditions += " order by createTime desc,userId ";
		int totalSize = countWithdrawApplySize(conditions);

		int totalPage = (int) Math.ceil((double) totalSize / (double) pageSize);
		if (totalPage >= currentPage) {
			conditions += " limit " + (currentPage - 1) * pageSize + ","
					+ pageSize;
		}
		List<WithdrawApply> list = accountInfoMapper
				.getWithdrawApplyList(conditions);

		returnData.setCurrentPage(currentPage);
		returnData.setPageSize(pageSize);
		returnData.setTotalSize(totalSize);
		returnData.setResultList(list);
		return returnData;
	}

	/**
	 * 计算提现申请记录总数
	 * 
	 * @param conditions
	 * @return
	 */
	public int countWithdrawApplySize(String conditions) {
		return accountInfoMapper.countWithdrawApplySize(conditions);
	}

	/**
	 * 根据彩金交易在合作商的id查询
	 * 
	 * @param partnerHandselId
	 * @return
	 */
	public UserHandsel findUserHandselByPartnerId(String partnerId,
			String partnerHandselId, long userId) {
		return accountInfoMapper.findUserHandselByPartnerId(partnerId,
				partnerHandselId, DbGenerator.getTableIndex(userId));
	}

	/**
	 * 根据帐号查询提现帐号信息
	 * 
	 * @param accountNo
	 *            帐号
	 * @return
	 */
	public WithdrawAccount findWithdrawAccountByNo(String accountNo, Long userId) {
		return accountInfoMapper.findWithdrawAccountByNo(accountNo,
				DbGenerator.getTableIndex(userId));
	}

	/**
	 * 获取当前创建帐号ID
	 * 
	 * @return
	 */
	public int getCurrentInsertAccountId() {
		return accountInfoMapper.getCurrentInsertAccountId();
	}

	public List<StatisticResult> countRecharge(String beginTime,
			String endTime, String tableIndex) {
		return accountInfoMapper.countRecharge(beginTime, endTime, tableIndex);
	}

	public List<StatisticResult> countWithdraw(String beginTime, String endTime) {
		return accountInfoMapper.countWithdraw(beginTime, endTime,
				UserAccountConstant.WithdrawAuditState.AUDIT_PASS.getValue());
	}

	public void updateHandselState() {
		List<String> allTableIndex = DbGenerator.getAllTableIndex();
		for (String index : allTableIndex) {
			int num = accountInfoMapper.updateHandselState(index);
			// Log.run.debug("Update handsel to failure, %d row affected.",
			// num);
		}
	}

	public long getTotalInvalidHandsel() {
		List<String> allTableIndex = DbGenerator.getAllTableIndex();
		long totalInvalidHandsel = 0;
		for (String index : allTableIndex) {
			Long money = accountInfoMapper.getTotalInvalidHandsel(index);
			if (money != null) {
				totalInvalidHandsel += money;
			}
		}
		return totalInvalidHandsel;
	}

	public int initHandselCount(String day) {
		return accountInfoMapper.createHandselCount(day);
	}

	public int increaseHandselCount(String day, long money) {
		return accountInfoMapper.increaseHandselCount(day, money);
	}

	public int setInvalidHandsel(String day, long money) {
		return accountInfoMapper.setInvalidHandsel(day, money);
	}

	public UserHandselCount getUserHandselCount(String date) {
		return accountInfoMapper.getUserHandselCount(date);
	}

	public long totalPaylogNum(String beginTime, String endTime) {
		List<String> allTableIndex = DbGenerator.getAllTableIndex();
		long totalPaylogNum = 0;
		for (String index : allTableIndex) {
			List<Pair<Integer, Long>> pairList = accountInfoMapper
					.totalPaylogNum(beginTime, endTime, index);
			for (Pair<Integer, Long> pair : pairList) {
				if (UserAccountLogType.PAYMENT.getValue() == pair.first()) {
					totalPaylogNum += pair.second();
				} else {
					totalPaylogNum -= pair.second();
				}
			}
		}
		return totalPaylogNum;
	}
}
