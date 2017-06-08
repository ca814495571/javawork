package com.cqfc.useraccount.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.cqfc.protocol.useraccount.UserAccount;
import com.cqfc.protocol.useraccount.UserInfo;
import com.cqfc.protocol.useraccount.UserInfoData;
import com.cqfc.protocol.useraccount.UserPreApply;
import com.cqfc.protocol.useraccount.UserPreApplyData;
import com.cqfc.useraccount.dao.mapper.UserInfoMapper;
import com.cqfc.useraccount.util.DbGenerator;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DaoLevelException;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.util.SolrFactory;
import com.cqfc.util.SolrServer;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Repository
public class UserInfoDao {

	@Autowired
	private UserInfoMapper userInfoMapper;

	/**
	 * 创建用户资料
	 * 
	 * @param userInfo
	 * @return
	 */
	public int createUserInfo(UserInfo userInfo) throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			String tableIndex = DbGenerator.getTableIndex(userInfo.getUserId());
			returnValue = userInfoMapper.createUserInfo(userInfo, tableIndex);
			if (returnValue == 0) {
				Log.run.debug("insert user failed, userInfo=%s", userInfo);
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			Log.run.debug("", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 创建账户
	 * 
	 * @param userAccount
	 * @return
	 */
	public int createUserAccount(UserAccount userAccount)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			String tableIndex = DbGenerator.getTableIndex(userAccount
					.getUserId());
			returnValue = userInfoMapper.createUserAccount(userAccount,
					tableIndex);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 获取当前插入数据用户ID
	 * 
	 * @return
	 */
	public int getCurrentInsertUserId() {
		return userInfoMapper.getCurrentInsertUserId();
	}

	/**
	 * 更新用户ID
	 * 
	 * @return
	 */
	public int updateUserId() {
		return userInfoMapper.updateUserId();
	}

	/**
	 * 修改用户资料
	 * 
	 * @param userInfo
	 * @return
	 */
	public int updateUserInfo(UserInfo userInfo) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = userInfoMapper.updateUserInfo(userInfo,
					DbGenerator.getTableIndex(userInfo.getUserId()));
		} catch (Exception e) {
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 分页查询用户信息
	 * 
	 * @param userInfo
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public UserInfoData getUserInfoList(UserInfo userInfo, int currentPage,
			int pageSize) {
		UserInfoData returnData = new UserInfoData();
		String conditions = "1=1";

		// 搜索参数
		if (null != userInfo && (userInfo.getUserId() > 0)) {
			conditions += " and userId=" + userInfo.getUserId();
			if (null != userInfo.getPartnerId()
					&& !"".equals(userInfo.getPartnerId())) {
				conditions += " and partnerId='" + userInfo.getPartnerId()
						+ "'";
			}
			if (null != userInfo.getPartnerUserId()
					&& !"".equals(userInfo.getPartnerUserId())) {
				conditions += " and partnerUserId='"
						+ userInfo.getPartnerUserId() + "'";
			}
			if (null != userInfo.getMobile()
					&& !"".equals(userInfo.getMobile())) {
				conditions += " and mobile='" + userInfo.getMobile() + "'";
			}
			if (null != userInfo.getCardNo()
					&& !"".equals(userInfo.getCardNo())) {
				conditions += " and cardNo='" + userInfo.getCardNo() + "'";
			}
			int totalSize = countUserInfoSize(conditions, userInfo.getUserId());
			int totalPage = (int) Math.ceil((double) totalSize
					/ (double) pageSize);
			if (totalPage >= currentPage) {
				conditions += " limit " + (currentPage - 1) * pageSize + ","
						+ pageSize;
			}
			List<UserInfo> list = userInfoMapper.getUserInfoList(conditions,
					DbGenerator.getTableIndex(userInfo.getUserId()));

			returnData.setTotalSize(totalSize);
			returnData.setResultList(list);
		} else {
			SolrServer server = SolrFactory
					.getSolrServer(ConstantsUtil.CORENAME_USERINFO);
			Map<String, Object> fields = new HashMap<String, Object>();
			if (null != userInfo.getPartnerId()
					&& !"".equals(userInfo.getPartnerId())) {
				fields.put("partnerId", userInfo.getPartnerId());
			}
			if (null != userInfo.getPartnerUserId()
					&& !"".equals(userInfo.getPartnerUserId())) {
				fields.put("partnerUserId", userInfo.getPartnerUserId());
			}
			if (null != userInfo.getMobile()
					&& !"".equals(userInfo.getMobile())) {
				fields.put("mobile", userInfo.getMobile());
			}
			if (null != userInfo.getCardNo()
					&& !"".equals(userInfo.getCardNo())) {
				fields.put("cardNo", userInfo.getCardNo());
			}
			int start = (currentPage - 1) * pageSize;
			try {
				List<SolrDocument> findData = server.findData(fields, start,
						pageSize * 10);
				returnData.setTotalSize(start + findData.size());
				List<UserInfo> list = new ArrayList<UserInfo>();
				UserInfo user = null;
				int userNum = 0;
				for (SolrDocument doc : findData) {
					user = new UserInfo();
					user.setUserId(Long.parseLong((String) doc
							.getFieldValue("userId")));
					user.setNickName((String) doc.getFieldValue("nickName"));
					user.setCardType((Integer) doc.getFieldValue("cardType"));
					user.setCardNo((String) doc.getFieldValue("cardNo"));
					user.setMobile((String) doc.getFieldValue("mobile"));
					user.setEmail((String) doc.getFieldValue("email"));
					user.setUserName((String) doc.getFieldValue("userName"));
					user.setSex((Integer) doc.getFieldValue("sex"));
					user.setBirthday((String) doc.getFieldValue("birthday"));
					user.setAge((Integer) doc.getFieldValue("age"));
					user.setUserType((Integer) doc.getFieldValue("userType"));
					user.setRegisterTerminal((Integer) doc
							.getFieldValue("registerTerminal"));
					user.setAccountType((Integer) doc
							.getFieldValue("accountType"));
					user.setPartnerId((String) doc.getFieldValue("partnerId"));
					user.setPartnerUserId((String) doc
							.getFieldValue("partnerUserId"));
					user.setCreateTime((String) doc.getFieldValue("createTime"));
					user.setLastUpdateTime((String) doc
							.getFieldValue("lastUpdateTime"));
					list.add(user);
					userNum++;
					if (userNum == pageSize) {
						break;
					}
				}
				returnData.setResultList(list);
			} catch (SolrServerException e) {
				returnData.setTotalSize(start);
				Log.run.debug("", e);
				Log.run.error("Solr Query Failed", e.toString());
			}
		}
		returnData.setCurrentPage(currentPage);
		returnData.setPageSize(pageSize);

		return returnData;
	}

	/**
	 * 计算渠道账户总记录数
	 * 
	 * @param conditions
	 * @return
	 */
	public int countUserInfoSize(String conditions, long userId) {
		// List<String> tableIndexs = DbGenerator.getAllTableIndex();
		// int totalCount = 0;
		// for(String index: tableIndexs){
		// int count = userInfoMapper.countUserInfoSize(conditions,index);
		// totalCount += count;
		// }
		int totalCount = userInfoMapper.countUserInfoSize(conditions,
				DbGenerator.getTableIndex(userId));
		return totalCount;
	}

	/**
	 * 根据用户ID查询用户账户
	 * 
	 * @param userId
	 * @return
	 */
	public UserAccount findUserAccountByUserId(long userId) {
		return userInfoMapper.findUserAccountByUserId(userId,
				DbGenerator.getTableIndex(userId));
	}

	/**
	 * 根据用户ID查询用户资料信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserInfo findUserInfoById(long userId) {
		return userInfoMapper.findUserInfoById(userId,
				DbGenerator.getTableIndex(userId));
	}

	/**
	 * 更新用户账户（冻结、解冻）
	 * 
	 * @param userId
	 * @param state
	 * @return
	 */
	public int modifyUserAccountState(long userId, int state) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = userInfoMapper.modifyUserAccountState(userId, state,
					DbGenerator.getTableIndex(userId));
		} catch (Exception e) {
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 扣除用户帐户金额（总金额、可用金额）,可用金额必须>=扣除金额
	 * 
	 * @param userId
	 *            用户ID
	 * @param deductMoney
	 *            增加金额
	 * @return
	 */
	public int deductUserAccountMoney(long userId, long deductMoney)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = userInfoMapper.deductUserAccountMoney(userId,
					deductMoney, DbGenerator.getTableIndex(userId));
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			Log.run.debug("", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;

	}

	/**
	 * 增加用户帐户金额（总金额、可用金额）
	 * 
	 * @param userId
	 *            用户ID
	 * @param increaseMoney
	 *            增加金额
	 * @return
	 */
	public int increaseUserAccountMoney(long userId, long increaseMoney)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = userInfoMapper.increaseUserAccountMoney(userId,
					increaseMoney, DbGenerator.getTableIndex(userId));
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 冻结账户金额（可用金额、冻结金额）
	 * 
	 * @param userId
	 * @param freezeMoney
	 * @return
	 */
	public int freezeUserAccount(long userId, long freezeMoney)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = userInfoMapper.freezeUserAccount(userId, freezeMoney,
					DbGenerator.getTableIndex(userId));
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 提现扣除冻结金额（总金额、冻结金额）
	 * 
	 * @param userId
	 * @param freezeMoney
	 * @return
	 */
	public int deductFreezeAmount(long userId, long freezeMoney)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = userInfoMapper.deductFreezeAmount(userId,
					freezeMoney, DbGenerator.getTableIndex(userId));
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 退还冻结金额
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public int refundFreezeMoney(long userId, long amount) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = userInfoMapper.refundFreezeMoney(userId, amount,
					DbGenerator.getTableIndex(userId));
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 修改用户账户兑奖密码
	 * 
	 * @param userId
	 * @param oldPasswd
	 * @param newPasswd
	 * @return
	 */
	public int updatePrizePassword(long userId, String oldPasswd,
			String newPasswd) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = userInfoMapper.updatePrizePassword(userId, oldPasswd,
					newPasswd, DbGenerator.getTableIndex(userId));
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 创建用户预申请记录
	 * 
	 * @param userPreApply
	 * @return
	 */
	public int createUserPreApply(UserPreApply userPreApply) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = userInfoMapper.createUserPreApply(userPreApply);
		} catch (DuplicateKeyException e) {
			Log.run.error("duplicate userPreApply.");
			return ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST;
		}
		return returnValue;
	}

	/**
	 * 查询预存款申请记录
	 * 
	 * @param userId
	 * @param partnerUniqueNo
	 * @return
	 */
	public UserPreApply findUserPreApply(String partnerId,
			String partnerUniqueNo) {
		UserPreApply userPreApply = null;
		try {

			userPreApply = userInfoMapper.findUserPreApply(partnerId,
					partnerUniqueNo);
		} catch (Exception e) {
			Log.run.debug(e);
		}
		return userPreApply;
	}

	/**
	 * 查询预存款申请列表
	 * 
	 * @param userPreApply
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public UserPreApplyData getUserPreApplyList(UserPreApply userPreApply,
			int currentPage, int pageSize) {
		UserPreApplyData returnData = new UserPreApplyData();
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(" 1=1");
		if (null != userPreApply) {
			if (userPreApply.getUserId() > 0) {
				strBuf.append(" and userId=" + userPreApply.getUserId());
			}
			if (null != userPreApply.getPartnerUniqueNo()
					&& !"".equals(userPreApply.getPartnerUniqueNo())) {
				strBuf.append(" and partnerUniqueNo='"
						+ userPreApply.getPartnerUniqueNo() + "'");
			}
			
			if (StringUtils.isNotBlank(userPreApply.getPartnerId())
					) {
				strBuf.append(" and partnerId='"
						+ userPreApply.getPartnerId() + "'");
			}
			if (userPreApply.getStatus() > 0) {
				strBuf.append(" and status=" + userPreApply.getStatus());
			}
		}
		strBuf.append(" order by createTime desc ");
		int totalSize = countUserPreApplySize(strBuf.toString());
		int totalPage = (int) Math.ceil((double) totalSize / (double) pageSize);
		if (totalPage >= currentPage) {
			strBuf.append(" limit " + (currentPage - 1) * pageSize + ","
					+ pageSize);
		}
		List<UserPreApply> userPreApplyList = userInfoMapper
				.getUserPreApplyList(strBuf.toString());

		returnData.setCurrentPage(currentPage);
		returnData.setPageSize(pageSize);
		returnData.setTotalSize(totalSize);
		returnData.setResultList(userPreApplyList);
		return returnData;
	}

	/**
	 * 计算预存款申请记录数
	 * 
	 * @param conditions
	 * @return
	 */
	public int countUserPreApplySize(String conditions) {
		int count = userInfoMapper.countUserPreApplySize(conditions);
		return count;
	}

	/**
	 * 通过预存款申请ID查询信息
	 * 
	 * @param preApplyId
	 * @return
	 */
	public UserPreApply findUserPreApplyByPreApplyId(long preApplyId) {
		return userInfoMapper.findUserPreApplyByPreApplyId(preApplyId);
	}

	/**
	 * 更新预存款状态
	 * 
	 * @param preApplyId
	 * @param status
	 * @return
	 */
	public int auditUserPreApply(long preApplyId, int status) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = userInfoMapper.auditUserPreApply(preApplyId, status);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	public int rollbackPreApply(long preApplyId) {
		return userInfoMapper.rollbackPreApply(preApplyId);
	}

	public long totalAccountMoney() {
		List<String> allTableIndex = DbGenerator.getAllTableIndex();
		long totalAccountMoney = 0;
		for(String index:allTableIndex){
			Long money = userInfoMapper.getTotalAccountMoney(index);
			if(money != null){
				totalAccountMoney += money;
			}
		}
		return totalAccountMoney;
	}
}
