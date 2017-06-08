package com.cqfc.appendtask;

import java.util.List;

import javax.annotation.Resource;

import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import com.cqfc.appendtask.service.IAppendTaskService;
import com.cqfc.protocol.appendtask.AppendTask;
import com.cqfc.protocol.appendtask.AppendTaskDetail;
import com.cqfc.protocol.appendtask.AppendTaskIndex;
import com.cqfc.protocol.appendtask.AppendTaskService;
import com.cqfc.util.AppendTaskConstant;
import com.cqfc.util.DbGenerator;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Service
public class AppendTaskHandler implements AppendTaskService.Iface {

	@Resource(name = "appendTaskServiceImpl")
	private IAppendTaskService appendTaskService;

	/**
	 * 创建追号任务
	 * 
	 * @param appendTask
	 * @return
	 * @throws TException
	 */
	@Override
	public int addAppendTask(AppendTask appendTask) throws TException {
		int isSuccess = 0;
		try {
			String userId = String.valueOf(appendTask.getUserId());
			String appendTaskId = appendTask.getAppendTaskId();
			String tradeId = appendTaskId.split("#")[1];
			String partnerId = appendTask.getPartnerId();

			DbGenerator.setDynamicDataSource(DbGenerator.MASTER, tradeId);
			Log.run.debug("createAppendTask index dbName：" + DbGenerator.getDbName(userId));

			AppendTaskIndex appendTaskIndex = new AppendTaskIndex();
			appendTaskIndex.setPartnerId(partnerId);
			appendTaskIndex.setPartnerTradeId(tradeId);
			appendTaskIndex.setAppendTaskId(appendTaskId);
			appendTaskIndex.setUserId(appendTask.getUserId());
			isSuccess = appendTaskService.createAppendTaskIndex(appendTaskIndex);

			if (isSuccess > 0) {
				DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userId);
				Log.run.debug("createAppendTask task and detail dbName：" + DbGenerator.getDbName(userId));
				isSuccess = appendTaskService.addAppendTask(appendTask);

				String logStr = "";
				if (isSuccess <= 0) {
					logStr = "fail";
					DbGenerator.setDynamicDataSource(DbGenerator.MASTER, tradeId);
					int indexSuccess = appendTaskService.deleteAppendTaskIndex(partnerId, tradeId);
					String indexStr = indexSuccess > 0 ? "success" : "fail";
					Log.run.debug("createAppendTask fail,delete index " + indexStr + ",appendTaskId=" + appendTaskId);
				} else {
					logStr = "success";
				}
				Log.run.debug("createAppendTask " + logStr + ",appendTaskId=" + appendTaskId);
			} else {
				Log.run.error("createAppendTask fail,tradeId:" + tradeId);
			}
		} catch (Exception e) {
			Log.run.error("addAppendTask error：", e);
			return 0;
		}
		return isSuccess;
	}

	/**
	 * 查询追号任务信息
	 * 
	 * @param appendTaskId
	 *            追号任务ID
	 * @return
	 * @throws TException
	 */
	@Override
	public AppendTask findAppendTaskById(String appendTaskId) throws TException {
		AppendTask appendTask = null;
		try {
			String[] arr = appendTaskId.split("#");
			String userId = getAppendTaskUserId(arr[0], arr[1]);
			if (null != userId && !"".equals(userId)) {
				DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
				appendTask = appendTaskService.findAppendTaskById(appendTaskId, userId);
			}
		} catch (Exception e) {
			Log.run.error("findAppendTaskById error,appendTaskId=" + appendTaskId, e);
			return null;
		}
		return appendTask;
	}

	/**
	 * 在追号索引表中查询追号任务ID
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param partnerTradeId
	 *            合作商交易ID
	 * @return
	 * @throws TException
	 */
	/*
	 * @Override public String findAppendTaskIndexByParam(String partnerId,
	 * String partnerTradeId) throws TException {
	 * DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, partnerTradeId);
	 * String appendTaskId = ""; AppendTaskIndex appendTaskIndex =
	 * appendTaskService.findAppendTaskIndex(partnerId, partnerTradeId); if
	 * (null != appendTaskIndex && !"".equals(appendTaskIndex)) { appendTaskId =
	 * appendTaskIndex.getAppendTaskId(); } return appendTaskId; }
	 */

	/**
	 * 查询最小期号追号任务明细
	 * 
	 * @param partnerId
	 * @param partnerTradeId
	 * @return
	 * @throws TException
	 */
	@Override
	public AppendTaskDetail findMinAppendTaskDetail(String partnerId, String partnerTradeId) throws TException {
		AppendTaskDetail appendTaskDetail = null;
		try {
			String userId = getAppendTaskUserId(partnerId, partnerTradeId);
			if (null != userId && !"".equals(userId)) {
				// 设置追号任务数据源
				DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
				String appendTaskId = partnerId + "#" + partnerTradeId;
				appendTaskDetail = appendTaskService.findMinAppendTaskDetail(partnerId, appendTaskId, userId);
			}
		} catch (Exception e) {
			Log.run.error("findMinAppendTaskDetail error,partnerId=" + partnerId + ",partnerTradeId=" + partnerTradeId,
					e);
			return null;
		}
		return appendTaskDetail;
	}

	/**
	 * 停止追号任务
	 * 
	 * @param appendTaskId
	 *            追号任务ID
	 * @param issueNoList
	 *            停追期号列表
	 * @return
	 * @throws TException
	 */
	@Override
	public List<AppendTaskDetail> stopAppendTask(String appendTaskId, List<String> issueNoList) throws TException {
		List<AppendTaskDetail> appendTaskDetailList = null;
		try {
			String[] arr = appendTaskId.split("#");
			String userId = getAppendTaskUserId(arr[0], arr[1]);
			if (null != userId && !"".equals(userId)) {
				// 设置追号任务数据源
				DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userId);
				appendTaskDetailList = appendTaskService.stopAppendTask(appendTaskId, issueNoList, userId);
			}
		} catch (Exception e) {
			Log.run.error("stopAppendTask error,appendTaskId=" + appendTaskId, e);
			return null;
		}
		return appendTaskDetailList;
	}

	/**
	 * 获取追号任务状态
	 * 
	 * @param appendTaskId
	 *            追号任务ID
	 * @return
	 * @throws TException
	 */
	@Override
	public int getAppendTaskStatus(String appendTaskId) throws TException {
		int status = 0;
		try {
			String[] arr = appendTaskId.split("#");
			String userId = getAppendTaskUserId(arr[0], arr[1]);
			if (null != userId && !"".equals(userId)) {
				// 设置追号任务数据源
				DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
				status = appendTaskService.getAppendTaskStatus(appendTaskId, userId);
			}
		} catch (Exception e) {
			Log.run.error("getAppendTaskStatus error,appendTaskId=" + appendTaskId, e);
			return 0;
		}
		return status;
	}

	/**
	 * 追号明细生成订单后修改追号任务信息
	 * 
	 * @param appendTaskDetailId
	 *            追号明细ID
	 * @param orderNo
	 *            订单编号
	 * @return
	 * @throws TException
	 */
	@Override
	public int updateAppendAfterOrder(long appendTaskDetailId, String orderNo) throws TException {
		int isSuccess = 0;
		try {
			String[] arr = orderNo.split("#");
			String partnerId = arr[1];
			String tradeId = arr[2];
			String userId = getAppendTaskUserId(partnerId, tradeId);
			if (null != userId && !"".equals(userId)) {
				// 设置追号任务数据源
				DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userId);
				isSuccess = appendTaskService.updateAppendAfterOrder(appendTaskDetailId, orderNo, userId);
			}
			Log.run.debug("update append after create order,orderNo=%s,userId=%s,returnValue=%d", orderNo, userId,
					isSuccess);
		} catch (Exception e) {
			Log.run.error("updateAppendAfterOrder error,appendTaskDetailId=%d,orderNo=%s,errorMsg=%s",
					appendTaskDetailId, orderNo, e);
			return 0;
		}
		return isSuccess;
	}

	/**
	 * 获取追号任务用户ID
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param tradeId
	 *            交易ID
	 * @return
	 */
	private String getAppendTaskUserId(String partnerId, String tradeId) {
		String userId = "";
		try {
			// 设置追号任务索引数据源
			DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, tradeId);
			AppendTaskIndex appendTaskIndex = appendTaskService.findAppendTaskIndex(partnerId, tradeId);
			if (null != appendTaskIndex && !"".equals(appendTaskIndex)) {
				userId = String.valueOf(appendTaskIndex.getUserId());
			}
			Log.run.debug("获取追号任务用户ID,tradeId=%s,partnerId=%s,userId=%s", partnerId, tradeId, userId);
		} catch (Exception e) {
			Log.run.error("getAppendTaskUserId error,partnerId=" + partnerId + ",tradeId=" + tradeId, e);
			return "";
		}
		return userId;
	}

	/**
	 * 通过订单编号获取冻结金额序列号（userId用于分库分表）
	 * 
	 * @param orderNo
	 * @param userId
	 * @return
	 * @throws TException
	 */
	@Override
	public String getRefundSerialNumberByOrderNo(String orderNo, String userId) throws TException {
		String freezeSerial = "";
		try {
			DbGenerator.setDynamicDataSource(DbGenerator.SLAVE, userId);
			freezeSerial = appendTaskService.getRefundSerialNumberByOrderNo(orderNo, userId);
		} catch (Exception e) {
			Log.run.error("getRefundSerialNumberByOrderNo error,orderNo=" + orderNo + ",userId=" + userId, e);
			return "";
		}
		return freezeSerial;
	}

	/**
	 * 取消追号任务（冻结金额发生金额不足时使用）
	 * 
	 * @param appendTaskId
	 * @return
	 * @throws TException
	 */
	@Override
	public int cancelAppendTask(String appendTaskId) throws TException {
		int isSuccess = 0;
		try {
			String[] arr = appendTaskId.split("#");
			String userId = getAppendTaskUserId(arr[0], arr[1]);
			if (null != userId && !"".equals(userId)) {
				// 设置追号任务数据源
				DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userId);
				isSuccess = appendTaskService.cancelAppendTask(appendTaskId, userId);
			}
		} catch (Exception e) {
			Log.run.error("cancelAppendTask error,appendTaskId=" + appendTaskId, e);
			return 0;
		}
		return isSuccess;
	}

	/**
	 * 追号明细订单出票回调后,更新追号明细状态
	 * 
	 * @param orderNo
	 * @param isPrintSuccess
	 * @return
	 * @throws TException
	 */
	@Override
	public int updateDetailAfterPrint(String orderNo, boolean isPrintSuccess) throws TException {
		int isSuccess = 0;
		try {
			String[] arr = orderNo.split("#");
			String partnerId = arr[1];
			String tradeId = arr[2];
			long detailId = Long.valueOf(arr[3]);
			String userId = getAppendTaskUserId(partnerId, tradeId);
			int status = isPrintSuccess ? AppendTaskConstant.DetailStatus.TRADE_SUCCESS.getValue()
					: AppendTaskConstant.DetailStatus.TRADE_FAILURE.getValue();
			if (null != userId && !"".equals(userId)) {
				// 设置追号任务数据源
				DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userId);
				isSuccess = appendTaskService.updateDetailAfterPrint(detailId, orderNo, status, userId);
			}
			Log.run.debug("追号明细订单出票回调后,更新追号明细状态,orderNo=%s,detailId=%d,status=%d,userId=%s,returnValue=%d", orderNo,
					detailId, status, userId, isSuccess);
		} catch (Exception e) {
			Log.run.error("updateDetailAfterPrint error,orderNo=" + orderNo + ",isPrintSuccess=" + isPrintSuccess, e);
			return 0;
		}
		return isSuccess;
	}

	/**
	 * 中奖后更新追号任务
	 * 
	 * @param orderNo
	 * @param prizeLevel
	 * @param userId
	 * @return
	 * @throws TException
	 */
	@Override
	public int updateAppendAfterOrderPrize(String orderNo, int prizeLevel, long userId) throws TException {
		int isSuccess = 0;
		try {
			String userIdStr = String.valueOf(userId);
			DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userIdStr);
			AppendTask appendTask = appendTaskService.getStopFlagByOrderNo(orderNo, userIdStr);
			if (null != appendTask && !"".equals(appendTask)) {
				int stopFlag = appendTask.getStopFlag();
				if (prizeLevel == stopFlag
						&& (stopFlag == AppendTaskConstant.StopAppendType.ANYPRIZE_STOP.getValue() || stopFlag == AppendTaskConstant.StopAppendType.BIGPRIZE_STOP
								.getValue())) {
					// 设置追号任务数据源
					DbGenerator.setDynamicDataSource(DbGenerator.MASTER, userIdStr);
					String appendTaskId = appendTask.getAppendTaskId();
					List<AppendTaskDetail> appendTaskDetailList = appendTaskService.stopAppendTask(appendTaskId, null,
							userIdStr);
					if (null == appendTaskDetailList || appendTaskDetailList.size() >= 0) {
						isSuccess = 1;
					}
				}
			}
		} catch (Exception e) {
			Log.run.error("中奖后更新追号任务发生异常", e);
			return 0;
		}
		return isSuccess;
	}

}
