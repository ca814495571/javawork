package com.cqfc.appendtask.dao;

import java.util.List;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.cqfc.appendtask.dao.mapper.AppendTaskMapper;
import com.cqfc.protocol.appendtask.AppendTask;
import com.cqfc.protocol.appendtask.AppendTaskDetail;
import com.cqfc.protocol.appendtask.AppendTaskIndex;
import com.cqfc.util.AppendTaskConstant;
import com.cqfc.util.DaoLevelException;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Repository
public class AppendTaskDao {

	@Autowired
	private AppendTaskMapper appendTaskMapper;

	/**
	 * 创建追号任务
	 * 
	 * @param appendTask
	 * @return
	 */
	public int createAppendTask(AppendTask appendTask, String tableName) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = appendTaskMapper.createAppendTask(appendTask, tableName);
			Log.run.debug("创建追号任务,appendTaskId=%s,tableName=%s,returnValue=%d", appendTask.getAppendTaskId(),
					tableName, returnValue);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (DuplicateKeyException e) {
			Log.run.error("创建追号任务异常", e);
			throw new DaoLevelException(String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.error("创建追号任务异常", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 创建追号任务明细
	 * 
	 * @param appendTaskDetail
	 * @return
	 */
	public int createAppendTaskDetail(AppendTaskDetail appendTaskDetail, String tableName) throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = appendTaskMapper.createAppendTaskDetail(appendTaskDetail, tableName);
			Log.run.debug("创建追号任务明细,detailId=%s,tableName=%s,returnValue=%d", appendTaskDetail.getDetailId(),
					tableName, returnValue);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (DuplicateKeyException e) {
			Log.run.error("创建追号任务明细异常", e);
			throw new DaoLevelException(String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.error("创建追号任务明细异常", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 查询追号索引
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param partnerTradeId
	 *            合作商交易ID
	 * @return
	 */
	public AppendTaskIndex findAppendTaskIndexByParam(String partnerId, String partnerTradeId, String tableName) {
		AppendTaskIndex appendTaskIndex = null;
		try {
			appendTaskIndex = appendTaskMapper.findAppendTaskIndexByParam(partnerId, partnerTradeId, tableName);
		} catch (Exception e) {
			Log.run.error("查询追号索引异常", e);
			return null;
		}
		return appendTaskIndex;
	}

	/**
	 * 查询追号任务明细
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param appendTaskId
	 *            追号任务ID
	 * @return
	 */
	public List<AppendTaskDetail> getAppendTaskDetailList(String partnerId, String appendTaskId, String tableName) {
		List<AppendTaskDetail> appendTaskDetailList = null;
		try {
			appendTaskDetailList = appendTaskMapper.getAppendTaskDetailList(partnerId, appendTaskId, tableName);
		} catch (Exception e) {
			Log.run.error("查询追号任务明细异常", e);
			return null;
		}
		return appendTaskDetailList;
	}

	/**
	 * 创建追号订单索引
	 * 
	 * @param appendTaskIndex
	 * @return
	 */
	public int createAppendTaskIndex(AppendTaskIndex appendTaskIndex, String tableName) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = appendTaskMapper.createAppendTaskIndex(appendTaskIndex, tableName);
			Log.run.debug("创建追号订单索引,appendTaskId=%s,userId=%d,tableName=%s,returnValue=%d",
					appendTaskIndex.getAppendTaskId(), appendTaskIndex.getUserId(), tableName, returnValue);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (DuplicateKeyException e) {
			Log.run.error("创建追号订单索引异常", e);
			throw new DaoLevelException(String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.error("创建追号订单索引异常", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 查询追号任务信息
	 * 
	 * @param appendTaskId
	 * @return
	 */
	public AppendTask findAppendTaskById(String appendTaskId, String tableName) {
		AppendTask appendTask = null;
		try {
			appendTask = appendTaskMapper.findAppendTaskById(appendTaskId, tableName);
		} catch (Exception e) {
			Log.run.error("查询追号任务信息异常", e);
			return null;
		}
		return appendTask;
	}

	/**
	 * 停止追号
	 * 
	 * @param appendTaskId
	 * @param issueNo
	 * @return
	 * @throws DaoLevelException
	 */
	public int stopAppendTask(String appendTaskId, List<String> issueNoList, String tableName) throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			String issueNos = "";
			if (null != issueNoList && !issueNoList.isEmpty() && issueNoList.size() > 0) {
				for (String issueNo : issueNoList) {
					issueNos += "'" + issueNo + "',";
				}
				issueNos = issueNos.substring(0, issueNos.length() - 1);
				int status = AppendTaskConstant.DetailStatus.TRADE_FAILURE.getValue();
				returnValue = appendTaskMapper.stopAppendTask(appendTaskId, issueNos, status, tableName);
				if (returnValue != issueNoList.size()) {
					throw new DaoLevelException(String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR));
				}
			} else {
				int status = AppendTaskConstant.DetailStatus.TRADE_FAILURE.getValue();
				returnValue = appendTaskMapper.stopAllAppendTask(appendTaskId, status, tableName);
			}
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			Log.run.error("停止追号异常", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 获取停追列表
	 * 
	 * @param appendTaskId
	 * @param issueNoList
	 * @return
	 */
	public List<AppendTaskDetail> getStopAppendTask(String appendTaskId, List<String> issueNoList, String tableName) {
		List<AppendTaskDetail> detailList = null;
		try {
			String issueNos = "", sql = "";
			if (null != issueNoList && !issueNoList.isEmpty() && issueNoList.size() > 0) {
				for (String issueNo : issueNoList) {
					issueNos += "'" + issueNo + "',";
				}
				issueNos = issueNos.substring(0, issueNos.length() - 1);
				sql = "appendTaskId='" + appendTaskId + "' and appendDetailStatus=0 and issueNo in(" + issueNos + ")";
			} else {
				sql = "appendTaskId='" + appendTaskId + "' and appendDetailStatus=0";
			}
			detailList = appendTaskMapper.getStopAppendTask(sql, tableName);
		} catch (Exception e) {
			Log.run.error("获取停追列表异常", e);
			return null;
		}
		return detailList;
	}

	/**
	 * 更新追号任务剩余期数
	 * 
	 * @param sppendTaskId
	 *            追号任务ID
	 * @param appendEndNum
	 *            追号结束期数
	 * @param status
	 *            追号任务状态 1追号正常 2追号结束
	 * @return
	 * @throws DaoLevelException
	 */
	public int modifyAppendTask(String appendTaskId, int appendEndNum, int status, long cancelMoney, String tableName)
			throws DaoLevelException {
		int returnValue = 0;
		try {
			returnValue = appendTaskMapper
					.modifyAppendTaskRemaining(appendTaskId, appendEndNum, cancelMoney, tableName);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
			if (status == AppendTaskConstant.AppendTaskStatus.APPEND_COMPLETE.getValue()) {
				appendTaskMapper.modifyAppendTaskStatus(appendTaskId, tableName);
			}
		} catch (Exception e) {
			Log.run.error("更新追号任务剩余期数异常", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 追号明细生成订单后修改追号任务信息
	 * 
	 * @param appendTaskDetailId
	 * @return
	 * @throws TException
	 */
	public AppendTaskDetail findAppendTaskDetailById(long appendTaskDetailId, String tableName) {
		AppendTaskDetail appendTaskDetail = null;
		try {
			appendTaskDetail = appendTaskMapper.findAppendTaskDetailById(appendTaskDetailId, tableName);
		} catch (Exception e) {
			Log.run.error("根据detailId查询追号明细异常", e);
			return null;
		}
		return appendTaskDetail;
	}

	/**
	 * 追号明细创建订单后更新追号任务信息（剩余期数、完成期数、完成金额、最新追过的期号）
	 * 
	 * @param appendTaskId
	 * @param completeMoney
	 * @param issueNo
	 * @return
	 */
	public int updateAppendTaskAfterOrder(String appendTaskId, long completeMoney, String issueNo, String tableName) {
		int returnValue = 0;
		try {
			returnValue = appendTaskMapper.updateAppendTaskAfterOrder(appendTaskId, completeMoney, issueNo, tableName);
			Log.run.debug("追号明细创建订单后更新明细信息,appendTaskId=%s,issueNo=%s,tableName=%,completeMoney=%d(分),returnValue=%d",
					appendTaskId, issueNo, tableName, completeMoney, returnValue);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			Log.run.error("追号明细创建订单后更新追号任务信息异常", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 追号剩余期数为0、将状态为'追号正常'的追号任务状态修改为'追号完成'
	 * 
	 * @param appendTaskId
	 * @return
	 */
	public int updateAppendTaskStatus(String appendTaskId, String tableName) {
		int isSuccess = 0;
		try {
			isSuccess = appendTaskMapper.modifyAppendTaskStatus(appendTaskId, tableName);
		} catch (Exception e) {
			Log.run.error("更新追号任务剩余期数异常,appendTaskId=%s,tableName=%s,errorMsg=%s", appendTaskId, tableName, e);
			return 0;
		}
		return isSuccess;
	}

	/**
	 * 更新追号明细交易信息
	 * 
	 * @param detailId
	 * @param orderNo
	 * @param status
	 * @param tableName
	 * @return
	 */
	public int updateAppendDetailStatus(long detailId, String orderNo, int status, String tableName) {
		int isSuccess = 0;
		try {
			isSuccess = appendTaskMapper.updateAppendDetailStatus(detailId, orderNo, status, tableName);
			Log.run.error("更新追号明细交易信息,detailId=%d,tableName=%s,orderNo=%s,status=%d,returnValue=%d", detailId,
					tableName, orderNo, status, isSuccess);
		} catch (Exception e) {
			Log.run.error("更新追号明细交易信息异常,detailId=%d,tableName=%s,orderNo=%s,status=%d,errorMsg=%s", detailId,
					tableName, orderNo, status, e);
			return 0;
		}
		return isSuccess;
	}

	/**
	 * 通过订单编号获取冻结金额序列号
	 * 
	 * @param orderNo
	 * @param taskTableName
	 * @param detailTableName
	 * @return
	 */
	public String getRefundSerialNumberByOrderNo(String orderNo, String taskTableName, String detailTableName) {
		String freezeSerialNumber = "";
		try {
			freezeSerialNumber = appendTaskMapper.getRefundSerialNumberByOrderNo(orderNo, taskTableName,
					detailTableName);
		} catch (Exception e) {
			Log.run.error("通过订单编号获取冻结金额序列号异常,orderNo=%s,taskTableName=%s,detailTableName=%s,errorMsg=%s", orderNo,
					taskTableName, detailTableName, e);
		}
		return freezeSerialNumber;
	}

	/**
	 * 查询追号明细列表（定时扫描任务）
	 * 
	 * @param lotteryId
	 *            彩种ID
	 * @param issueNo
	 *            期号
	 * @param tableName
	 *            表名
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * @return
	 */
	public List<AppendTaskDetail> getAppendTaskDetailListByParam(String lotteryId, String issueNo, String tableName,
			int currentPage, int pageSize) {
		List<AppendTaskDetail> detailList = null;
		try {
			if (null != lotteryId && !"".equals(lotteryId) && null != issueNo && !"".equals(issueNo)) {
				StringBuffer conditions = new StringBuffer();
				conditions.append(" appendDetailStatus=" + AppendTaskConstant.DetailStatus.WAIT_TRADE.getValue());
				conditions.append(" and lotteryId='" + lotteryId + "'");
				conditions.append(" and issueNo='" + issueNo + "'");
				conditions.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
				detailList = appendTaskMapper.getAppendTaskDetailListByParam(tableName, conditions.toString());
			}
		} catch (Exception e) {
			Log.run.error("查询追号明细列表(定时扫描任务)发生异常,lotteryId=%s,issueNo=%s,tableName=%s,errorMsg=%s", lotteryId, issueNo,
					tableName, e);
			return null;
		}
		return detailList;
	}

	/**
	 * 取消追号任务信息
	 * 
	 * @param appendTaskId
	 * @param status
	 * @param tableName
	 * @return
	 */
	public int cancelAppendTask(String appendTaskId, int status, String tableName) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = appendTaskMapper.cancelAppendTask(appendTaskId, status, tableName);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			Log.run.error("取消追号任务信息发生异常,appendTaskId=%s,status=%d,tableName=%s,errorMsg=%s", appendTaskId, status,
					tableName, e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 取消追号任务明细
	 * 
	 * @param appendTaskId
	 * @param status
	 * @param tableName
	 * @return
	 */
	public int cancelAppendTaskDetail(String appendTaskId, int status, String tableName) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = appendTaskMapper.cancelAppendTaskDetail(appendTaskId, status, tableName);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			Log.run.error("取消追号任务明细发生异常,appendTaskId=%s,status=%d,tableName=%s,errorMsg=%s", appendTaskId, status,
					tableName, e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 删除索引
	 * 
	 * @param partnerId
	 * @param tradeId
	 * @param tableName
	 * @return
	 */
	public int deleteAppendTaskIndex(String partnerId, String tradeId, String tableName) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = appendTaskMapper.deleteAppendTaskIndex(partnerId, tradeId, tableName);
		} catch (Exception e) {
			Log.run.error("取消追号任务明细发生异常,partnerId=%s,tradeId=%s,tableName=%s,returnValue=%d,errorMsg=%s", partnerId,
					tradeId, tableName, returnValue, e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 根据订单编号查询追号任务中止条件
	 * 
	 * @param orderNo
	 * @param taskTableName
	 * @param detailTableName
	 * @return
	 */
	public AppendTask getStopFlagByOrderNo(String orderNo, String taskTableName, String detailTableName) {
		AppendTask appendTask = null;
		try {
			appendTask = appendTaskMapper.getStopFlagByOrderNo(orderNo, taskTableName, detailTableName);
		} catch (Exception e) {
			return null;
		}
		return appendTask;
	}

	/**
	 * 出票回调后,更新追号明细交易信息
	 * 
	 * @param detailId
	 * @param orderNo
	 * @param status
	 * @param tableName
	 * @return
	 */
	public int updateAppendDetailAfterPrint(long detailId, int status, String tableName) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = appendTaskMapper.updateAppendDetailAfterPrint(detailId, status, tableName);
		} catch (Exception e) {
			Log.run.error("出票回调后,更新追号明细交易信息发生异常,detailId=%d,status=%d,tableName=%s,errorMsg=%s", detailId, status,
					tableName, e);
			return 0;
		}
		return returnValue;
	}
}
