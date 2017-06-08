package com.cqfc.appendtask.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.cqfc.appendtask.dao.AppendTaskDao;
import com.cqfc.appendtask.service.IAppendTaskService;
import com.cqfc.protocol.appendtask.AppendTask;
import com.cqfc.protocol.appendtask.AppendTaskDetail;
import com.cqfc.protocol.appendtask.AppendTaskIndex;
import com.cqfc.util.AppendTaskConstant;
import com.cqfc.util.AppendTaskConstant.AppendTaskStatus;
import com.cqfc.util.DaoLevelException;
import com.cqfc.util.DbGenerator;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Service
public class AppendTaskServiceImpl implements IAppendTaskService {

	@Resource
	private AppendTaskDao appendTaskDao;

	/**
	 * 新增追号任务
	 * 
	 * @param appendTask
	 * @return
	 */
	@Override
	@Transactional
	public int addAppendTask(AppendTask appendTask) {
		int returnValue = 0, isAppendTaskSuccess = 0;
		String userId = String.valueOf(appendTask.getUserId());
		String taskTableName = DbGenerator.getAppendTaskTableName(userId);
		String detailTableName = DbGenerator.getAppendTaskDetailTableName(userId);
		try {
			returnValue = appendTaskDao.createAppendTask(appendTask, taskTableName);
			if (returnValue != 1) {
				return returnValue;
			}
			List<AppendTaskDetail> appendTaskDetailList = appendTask.getAppendTaskDetailList();
			for (AppendTaskDetail appendTaskDetail : appendTaskDetailList) {
				isAppendTaskSuccess = appendTaskDao.createAppendTaskDetail(appendTaskDetail, detailTableName);
				if (isAppendTaskSuccess != 1) {
					returnValue = isAppendTaskSuccess;
					break;
				}
			}
			Log.run.info("创建追号任务表名：" + taskTableName);
		} catch (DaoLevelException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		}
		return returnValue;
	}

	@Override
	public AppendTask findAppendTaskById(String appendTaskId, String userId) {
		String tableName = DbGenerator.getAppendTaskTableName(userId);
		AppendTask appendTask = appendTaskDao.findAppendTaskById(appendTaskId, tableName);
		if (null != appendTask) {
			String detailTableName = DbGenerator.getAppendTaskDetailTableName(userId);
			List<AppendTaskDetail> appendTaskDetailList = appendTaskDao.getAppendTaskDetailList(
					appendTask.getPartnerId(), appendTaskId, detailTableName);
			appendTask.setAppendTaskDetailList(appendTaskDetailList);
		}
		return appendTask;
	}

	@Override
	public AppendTaskDetail findMinAppendTaskDetail(String partnerId, String appendTaskId, String userId) {
		String tableName = DbGenerator.getAppendTaskDetailTableName(userId);
		List<AppendTaskDetail> appendTaskDetailList = appendTaskDao.getAppendTaskDetailList(partnerId, appendTaskId,
				tableName);
		return appendTaskDetailList.get(0);
	}

	@Override
	@Transactional
	public List<AppendTaskDetail> stopAppendTask(String appendTaskId, List<String> issueNoList, String userId) {
		int isStopSuccess = 0;
		List<AppendTaskDetail> detailList = null;
		String detailTableName = DbGenerator.getAppendTaskDetailTableName(userId);
		String taskTableName = DbGenerator.getAppendTaskTableName(userId);
		try {
			detailList = appendTaskDao.getStopAppendTask(appendTaskId, issueNoList, detailTableName);
			if (null != detailList && !"".equals(detailList)) {
				int needStopNum = detailList.size();
				isStopSuccess = appendTaskDao.stopAppendTask(appendTaskId, issueNoList, detailTableName);
				if (needStopNum != isStopSuccess) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return null;
				}
				long cancelMoney = 0;
				for (AppendTaskDetail AppendTaskDetail : detailList) {
					cancelMoney += AppendTaskDetail.getTotalMoney();
				}
				// 更新追号任务剩余期数、状态
				AppendTask appendTask = appendTaskDao.findAppendTaskById(appendTaskId, taskTableName);
				int status = AppendTaskStatus.APPEND_NORMAL.getValue();
				if (appendTask.getRemainingQuantity() == needStopNum) {
					status = AppendTaskStatus.APPEND_COMPLETE.getValue();
				}
				appendTaskDao.modifyAppendTask(appendTaskId, needStopNum, status, cancelMoney, taskTableName);
			}
		} catch (DaoLevelException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		}
		return detailList;
	}

	@Override
	public int getAppendTaskStatus(String appendTaskId, String userId) {
		int appendTaskStatus = 1;
		String tableName = DbGenerator.getAppendTaskTableName(userId);
		AppendTask appendTask = appendTaskDao.findAppendTaskById(appendTaskId, tableName);
		if (null != appendTask) {
			appendTaskStatus = appendTask.getAppendStatus();
		}
		return appendTaskStatus;
	}

	@Override
	@Transactional
	public int updateAppendAfterOrder(long appendTaskDetailId, String orderNo, String userId) {
		int isSuccess = 0;
		String detailTableName = DbGenerator.getAppendTaskDetailTableName(userId);
		String taskTableName = DbGenerator.getAppendTaskTableName(userId);
		try {
			AppendTaskDetail appendTaskDetail = appendTaskDao.findAppendTaskDetailById(appendTaskDetailId,
					detailTableName);
			String appendTaskId = appendTaskDetail.getAppendTaskId();
			long completeMoney = appendTaskDetail.getTotalMoney();
			String issueNo = appendTaskDetail.getIssueNo();
			// 更新追号任务
			isSuccess = appendTaskDao.updateAppendTaskAfterOrder(appendTaskId, completeMoney, issueNo, taskTableName);
			Log.run.debug("updateAppendAfterOrder updateTask,orderNo=%s,userId=%s,returnValue=%d,taskTableName=%s",
					orderNo, userId, isSuccess, taskTableName);
			AppendTask appendTask = appendTaskDao.findAppendTaskById(appendTaskId, taskTableName);
			if (appendTask.getRemainingQuantity() == 0) {
				appendTaskDao.updateAppendTaskStatus(appendTaskId, taskTableName);
			}
			// 更新追号明细信息（状态、订单编号）
			int status = AppendTaskConstant.DetailStatus.TRADING.getValue();
			int isDetailSuccess = appendTaskDao.updateAppendDetailStatus(appendTaskDetailId, orderNo, status,
					detailTableName);
			Log.run.debug("updateAppendAfterOrder updateDetail,orderNo=%s,userId=%s,returnValue=%d,detailTableName=%s",
					orderNo, userId, isDetailSuccess, detailTableName);
		} catch (DaoLevelException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		}
		return isSuccess;
	}

	@Override
	public int createAppendTaskIndex(AppendTaskIndex appendTaskIndex) {
		int isSuccess = 0;
		try {
			String tradeId = appendTaskIndex.getPartnerTradeId();
			String tableName = DbGenerator.getAppendTaskIndexTableName(tradeId);
			Log.run.debug("创建追号任务索引表名：" + tableName);
			isSuccess = appendTaskDao.createAppendTaskIndex(appendTaskIndex, tableName);
		} catch (DaoLevelException e) {
			return Integer.valueOf(e.getMessage());
		}
		return isSuccess;
	}

	@Override
	public AppendTaskIndex findAppendTaskIndex(String partnerId, String tradeId) {
		String tableName = DbGenerator.getAppendTaskIndexTableName(tradeId);
		return appendTaskDao.findAppendTaskIndexByParam(partnerId, tradeId, tableName);
	}

	@Override
	public String getRefundSerialNumberByOrderNo(String orderNo, String userId) {
		String detailTableName = DbGenerator.getAppendTaskDetailTableName(userId);
		String taskTableName = DbGenerator.getAppendTaskTableName(userId);
		return appendTaskDao.getRefundSerialNumberByOrderNo(orderNo, taskTableName, detailTableName);
	}

	@Override
	public List<AppendTaskDetail> getAppendTaskDetailListByParam(String lotteryId, String issueNo, String tableName,
			int currentPage, int pageSize) {
		return appendTaskDao.getAppendTaskDetailListByParam(lotteryId, issueNo, tableName, currentPage, pageSize);
	}

	@Override
	@Transactional
	public int cancelAppendTask(String appendTaskId, String userId) {
		String taskTableName = DbGenerator.getAppendTaskTableName(userId);
		String detailTableName = DbGenerator.getAppendTaskDetailTableName(userId);
		int isAppendSuccess = 0, isDetailSuccess = 0;
		try {
			isAppendSuccess = appendTaskDao.cancelAppendTask(appendTaskId,
					AppendTaskConstant.AppendTaskStatus.APPEND_CANCEL.getValue(), taskTableName);
			if (isAppendSuccess > 0) {
				isDetailSuccess = appendTaskDao.cancelAppendTaskDetail(appendTaskId,
						AppendTaskConstant.DetailStatus.TRADE_CANCEL.getValue(), detailTableName);
			}
		} catch (DaoLevelException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		}
		return isDetailSuccess;
	}

	@Override
	public int deleteAppendTaskIndex(String partnerId, String tradeId) {
		int isSuccess = 0;
		try {
			String tableName = DbGenerator.getAppendTaskIndexTableName(tradeId);
			isSuccess = appendTaskDao.deleteAppendTaskIndex(partnerId, tradeId, tableName);
		} catch (DaoLevelException e) {
			return Integer.valueOf(e.getMessage());
		}
		return isSuccess;
	}

	@Override
	public int updateDetailAfterPrint(long detailId, String orderNo, int status, String userId) {
		int isSuccess = 0;
		String detailTableName = DbGenerator.getAppendTaskDetailTableName(userId);
		try {
			isSuccess = appendTaskDao.updateAppendDetailStatus(detailId, orderNo, status, detailTableName);
			Log.run.debug("追号明细订单出票回调后,orderNo=%s,detailId=%d,status=%d,detailTableName=%s,returnValue=%d", orderNo,
					detailId, status, detailTableName, isSuccess);
		} catch (Exception e) {
			return Integer.valueOf(isSuccess);
		}
		return isSuccess;
	}

	@Override
	public AppendTask getStopFlagByOrderNo(String orderNo, String userId) {
		String detailTableName = DbGenerator.getAppendTaskDetailTableName(userId);
		String taskTableName = DbGenerator.getAppendTaskTableName(userId);
		return appendTaskDao.getStopFlagByOrderNo(orderNo, taskTableName, detailTableName);
	}
}
