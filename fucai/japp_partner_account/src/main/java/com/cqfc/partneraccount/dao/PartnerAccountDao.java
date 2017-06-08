package com.cqfc.partneraccount.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.cqfc.partneraccount.dao.mapper.PartnerAccountMapper;
import com.cqfc.protocol.partneraccount.PartnerAccount;
import com.cqfc.protocol.partneraccount.PartnerAccountData;
import com.cqfc.protocol.partneraccount.PartnerAccountLog;
import com.cqfc.protocol.partneraccount.PartnerAccountLogData;
import com.cqfc.protocol.partneraccount.PartnerPreApply;
import com.cqfc.protocol.partneraccount.PartnerPreApplyData;
import com.cqfc.protocol.partneraccount.PartnerRecharge;
import com.cqfc.util.DaoLevelException;
import com.cqfc.util.Pair;
import com.cqfc.util.PartnerAccountConstant.PartnerAccountLogType;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.common.StatisticResult;

/**
 * @author liwh
 */
@Repository
public class PartnerAccountDao {

	@Autowired
	private PartnerAccountMapper partnerAccountMapper;

	/**
	 * 新增渠道账户
	 * 
	 * @param partnerAccount
	 * @return
	 */
	public int addPartnerAccount(PartnerAccount partnerAccount)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = partnerAccountMapper
					.addPartnerAccount(partnerAccount);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 查询渠道账户列表
	 * 
	 * @param partnerAccount
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PartnerAccountData getPartnerAccountList(
			PartnerAccount partnerAccount, int currentPage, int pageSize) {

		PartnerAccountData returnData = new PartnerAccountData();
		StringBuffer conditions = new StringBuffer();
		conditions.append(" 1=1");

		if (null != partnerAccount) {
			if (null != partnerAccount.getPartnerId()
					&& !"".equals(partnerAccount.getPartnerId())) {
				conditions.append(" and partnerId='"
						+ partnerAccount.getPartnerId() + "'");
			}
		}

		int totalSize = countAccountSize(conditions.toString());
		int totalPage = (int) Math.ceil((double) totalSize / (double) pageSize);
		if (totalPage >= currentPage) {
			conditions.append(" limit " + (currentPage - 1) * pageSize + ","
					+ pageSize);
		}
		List<PartnerAccount> list = partnerAccountMapper
				.getPartnerAccountList(conditions.toString());

		returnData.setCurrentPage(currentPage);
		returnData.setPageSize(pageSize);
		returnData.setTotalSize(totalSize);
		returnData.setResultList(list);
		return returnData;
	}

	/**
	 * 计算渠道账户总记录数
	 * 
	 * @param conditions
	 * @return
	 */
	public int countAccountSize(String conditions) {
		return partnerAccountMapper.countAccountSize(conditions);
	}

	/**
	 * 根据渠道ID查询账户信息
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @return
	 */
	public PartnerAccount findPartnerAccountByPartnerId(String partnerId) {
		return partnerAccountMapper.findPartnerAccountByPartnerId(partnerId);
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
	public int updatePartnerAccountState(String partnerId, int state) {
		return partnerAccountMapper.updatePartnerAccountState(partnerId, state);
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
	public int updateDeductAccount(String partnerId, long amount)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = partnerAccountMapper.updateDeductAccount(partnerId,
					amount);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 写流水日志
	 * 
	 * @param partnerAccountLog
	 * @return
	 */
	public int addPartnerAccountLog(PartnerAccountLog partnerAccountLog)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = partnerAccountMapper
					.addPartnerAccountLog(partnerAccountLog);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (DuplicateKeyException e) {
			throw new DaoLevelException(
					String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 查找流水日志
	 * 
	 * @param serialNumber
	 *            流水号
	 * @param state
	 *            收支状态: 2支付 4充值
	 * @return
	 */
	public PartnerAccountLog findPartnerAccountLog(String serialNumber,
			int state) {
		return partnerAccountMapper.findPartnerAccountLog(serialNumber, state);
	}

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
	public PartnerAccountLogData getPartnerAccountLogList(
			PartnerAccountLog partnerAccountLog, int currentPage, int pageSize) {
		PartnerAccountLogData returnData = new PartnerAccountLogData();
		StringBuffer conditions = new StringBuffer();
		conditions.append(" 1=1");

		if (null != partnerAccountLog) {
			if (null != partnerAccountLog.getPartnerId()
					&& !"".equals(partnerAccountLog.getPartnerId())) {
				conditions.append(" and partnerId='"
						+ partnerAccountLog.getPartnerId() + "'");
			}
			if (partnerAccountLog.getState() > 0) {
				conditions.append(" and state=" + partnerAccountLog.getState());
			}
			if (null != partnerAccountLog.getSearchBeginTime()
					&& !"".equals(partnerAccountLog.getSearchBeginTime())) {
				conditions.append(" and createTime>='"
						+ partnerAccountLog.getSearchBeginTime() + "'");
			}
			if (null != partnerAccountLog.getSearchEndTime()
					&& !"".equals(partnerAccountLog.getSearchEndTime())) {
				conditions.append(" and createTime<='"
						+ partnerAccountLog.getSearchEndTime() + "'");
			}
		}

		int totalSize = countAccountLogSize(conditions.toString());
		int totalPage = (int) Math.ceil((double) totalSize / (double) pageSize);
		if (totalPage >= currentPage) {
			conditions.append(" limit " + (currentPage - 1) * pageSize + ","
					+ pageSize);
		}
		List<PartnerAccountLog> list = partnerAccountMapper
				.getPartnerAccountLogList(conditions.toString());

		returnData.setCurrentPage(currentPage);
		returnData.setPageSize(pageSize);
		returnData.setTotalSize(totalSize);
		returnData.setResultList(list);
		return returnData;
	}

	/**
	 * 计算渠道账户流水总记录数
	 * 
	 * @param conditions
	 * @return
	 */
	public int countAccountLogSize(String conditions) {
		return partnerAccountMapper.countAccountLogSize(conditions);
	}

	/**
	 * 新增充值记录
	 * 
	 * @param partnerRecharge
	 * @return
	 */
	public int addPartnerRecharge(PartnerRecharge partnerRecharge)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = partnerAccountMapper
					.addPartnerRecharge(partnerRecharge);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (DuplicateKeyException e) {
			throw new DaoLevelException(
					String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 增加渠道账户资金
	 * 
	 * @param partnerId
	 * @param amount
	 * @return
	 */
	public int increaseAccount(String partnerId, long amount)
			throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = partnerAccountMapper.increaseAccount(partnerId,
					amount);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 通过流水号查找充值记录
	 * 
	 * @param serialNumber
	 * @return
	 */
	public PartnerRecharge findPartnerRecharge(String serialNumber) {
		return partnerAccountMapper.findPartnerRecharge(serialNumber);
	}

	/**
	 * 退款更新流水日志
	 * 
	 * @param serialNumber
	 * @param refundAmount
	 * @return
	 */
	public int updatePartnerAccountLog(String serialNumber, long refundAmount) {
		return partnerAccountMapper.updatePartnerAccountLog(serialNumber,
				refundAmount);
	}

	/**
	 * 查询扩展信息保存的已退款的支付流水
	 * 
	 * @param ext
	 * @return
	 */
	public List<PartnerAccountLog> getPartnerAccountLogListByExt(
			String partnerId, String ext) {
		return partnerAccountMapper.getPartnerAccountLogListByExt(partnerId,
				ext);
	}

	/**
	 * 根据流水号查询日志
	 * 
	 * @param serialNumber
	 * @return
	 */
	public PartnerAccountLog findPartnerAccountLogBySerialNumber(
			String partnerId, String serialNumber) {
		return partnerAccountMapper.findPartnerAccountLogBySerialNumber(
				partnerId, serialNumber);
	}

	/**
	 * 查询渠道商充值记录
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public List<PartnerRecharge> getPartnerRechargeList(String partnerId,
			String beginTime, String endTime) {
		return partnerAccountMapper.getPartnerRechargeList(partnerId,
				beginTime, endTime);
	}

	/**
	 * 创建渠道预存款申请记录
	 * 
	 * @param partnerPreApply
	 * @return
	 */
	public int createPartnerPreApply(PartnerPreApply partnerPreApply) {
		return partnerAccountMapper.createPartnerPreApply(partnerPreApply);
	}

	/**
	 * 查询渠道存款预申请记录
	 * 
	 * @param partnerId
	 * @param partnerUniqueNo
	 * @return
	 */
	public PartnerPreApply findPartnerPreApply(String partnerId,
			String partnerUniqueNo) {
		return partnerAccountMapper.findPartnerPreApply(partnerId,
				partnerUniqueNo);
	}

	/**
	 * 查询预存款申请列表
	 * 
	 * @param userPreApply
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PartnerPreApplyData getPartnerPreApplyList(
			PartnerPreApply partnerPreApply, int currentPage, int pageSize) {
		PartnerPreApplyData returnData = new PartnerPreApplyData();
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(" 1=1");
		if (null != partnerPreApply) {
			if (null != partnerPreApply.getPartnerId()
					&& !"".equals(partnerPreApply.getPartnerId())) {
				strBuf.append(" and partnerId="
						+ partnerPreApply.getPartnerId());
			}
			if (null != partnerPreApply.getPartnerUniqueNo()
					&& !"".equals(partnerPreApply.getPartnerUniqueNo())) {
				strBuf.append(" and partnerUniqueNo='"
						+ partnerPreApply.getPartnerUniqueNo() + "'");
			}
			if (partnerPreApply.getStatus() >= 0) {
				strBuf.append(" and status=" + partnerPreApply.getStatus());
			}
		}

		int totalSize = countPartnerPreApplySize(strBuf.toString());
		int totalPage = (int) Math.ceil((double) totalSize / (double) pageSize);
		if (totalPage >= currentPage) {
			strBuf.append(" limit " + (currentPage - 1) * pageSize + ","
					+ pageSize);
		}
		List<PartnerPreApply> userPreApplyList = partnerAccountMapper
				.getPartnerPreApplyList(strBuf.toString());

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
	public int countPartnerPreApplySize(String conditions) {
		int count = partnerAccountMapper.countPartnerPreApplySize(conditions);
		return count;
	}

	/**
	 * 通过预存款申请ID查询信息
	 * 
	 * @param preApplyId
	 * @return
	 */
	public PartnerPreApply findPartnerPreApplyByPreApplyId(long preApplyId) {
		return partnerAccountMapper.findPartnerPreApplyByPreApplyId(preApplyId);
	}

	/**
	 * 更新预存款状态
	 * 
	 * @param preApplyId
	 * @param status
	 * @return
	 */
	public int auditPartnerPreApply(long preApplyId, int status) {
		return partnerAccountMapper.auditPartnerPreApply(preApplyId, status);
	}

	/**
	 * 统计渠道商充值金额
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return 渠道商及金额
	 */
	public Map<String, Long> countRecharge(String beginTime, String endTime) {
		List<StatisticResult> list = partnerAccountMapper.countRecharge(
				beginTime, endTime);
		Map<String, Long> resultMap = new HashMap<String, Long>();
		for (StatisticResult result : list) {
			resultMap.put(result.getPartnerId(), result.getTotalAmount());
		}
		return resultMap;
	}

	public long totalPaylogNum(String beginTime, String endTime) {
		long payNum = 0, refundNum = 0;
		List<Pair<Integer, Long>> numList = partnerAccountMapper
				.totalPaylogNum(beginTime, endTime);
		for (Pair<Integer, Long> pair : numList) {
			if (pair.first() == PartnerAccountLogType.PAYMENT.getValue()) {
				payNum = pair.second();
			} else {
				refundNum = pair.second();
			}
		}

		return payNum - refundNum;
	}

	public int clearAccountLog(String time) {
		return partnerAccountMapper.clearAccountLog(time);
	}

	public int updatePartnerAccountCreditLimit(String partnerId,
			long creditLimit) {
		return partnerAccountMapper.updatePartnerAccountCreditLimit(partnerId, creditLimit);
	}
}
