package com.cqfc.appendtask.service;

import java.util.List;

import com.cqfc.protocol.appendtask.AppendTask;
import com.cqfc.protocol.appendtask.AppendTaskDetail;
import com.cqfc.protocol.appendtask.AppendTaskIndex;

/**
 * @author liwh
 */
public interface IAppendTaskService {

	/**
	 * 新增追号任务
	 * 
	 * @param appendTask
	 * @return
	 */
	public int addAppendTask(AppendTask appendTask);

	/**
	 * 查询追号任务信息
	 * 
	 * @param appendTaskId
	 *            追号任务ID
	 * @param userId
	 *            用户ID用于分表
	 * @return
	 */
	public AppendTask findAppendTaskById(String appendTaskId, String userId);

	/**
	 * 查询最小期号追号任务明细
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param appendTaskId
	 *            追号任务ID
	 * @param userId
	 *            用于分表
	 * @return
	 */
	public AppendTaskDetail findMinAppendTaskDetail(String partnerId, String appendTaskId, String userId);

	/**
	 * 停止追号任务
	 * 
	 * @param appendTaskId
	 *            追号任务ID
	 * @param issueNoList
	 *            停追期号列表
	 * @param userId
	 *            用于分表
	 * @return
	 */
	public List<AppendTaskDetail> stopAppendTask(String appendTaskId, List<String> issueNoList, String userId);

	/**
	 * 获取追号任务状态
	 * 
	 * @param appendTaskId
	 *            追号任务ID
	 * @param userId
	 *            用于分表
	 * @return
	 */
	public int getAppendTaskStatus(String appendTaskId, String userId);

	/**
	 * 追号明细生成订单后修改追号任务信息
	 * 
	 * @param appendTaskDetailId
	 *            追号明细ID
	 * @param orderNo
	 *            订单编号
	 * @param userId
	 *            用于分表
	 * @return
	 */
	public int updateAppendAfterOrder(long appendTaskDetailId, String orderNo, String userId);

	/**
	 * 创建追号任务索引
	 * 
	 * @param appendTaskIndex
	 * @return
	 */
	public int createAppendTaskIndex(AppendTaskIndex appendTaskIndex);

	/**
	 * 通过合作商交易ID查询追号任务索引
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param tradeId
	 *            合作商唯一交易ID
	 * @return
	 */
	public AppendTaskIndex findAppendTaskIndex(String partnerId, String tradeId);

	/**
	 * 通过订单编号获取冻结金额序列号（userId用于分库分表）
	 * 
	 * @param orderNo
	 * @param userId
	 * @return
	 */
	public String getRefundSerialNumberByOrderNo(String orderNo, String userId);

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
			int currentPage, int pageSize);

	/**
	 * 取消追号任务（冻结金额发生金额不足时使用）
	 * 
	 * @param appendTaskId
	 *            追号任务ID
	 * @param userId
	 *            用户ID用于分表
	 * @return
	 */
	public int cancelAppendTask(String appendTaskId, String userId);

	/**
	 * 删除追号索引
	 * 
	 * @param partnerId
	 * @param tradeId
	 * @return
	 */
	public int deleteAppendTaskIndex(String partnerId, String tradeId);

	/**
	 * 追号明细订单出票回调后,更新追号明细状态
	 * 
	 * @param detailId
	 * @param orderNo
	 * @param status
	 * @param userId
	 * @return
	 */
	public int updateDetailAfterPrint(long detailId, String orderNo, int status, String userId);

	/**
	 * 根据订单编号查询追号任务中止条件
	 * 
	 * @param orderNo
	 * @param userId
	 * @return
	 */
	public AppendTask getStopFlagByOrderNo(String orderNo, String userId);

}
