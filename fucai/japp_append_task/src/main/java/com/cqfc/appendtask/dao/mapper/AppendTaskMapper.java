package com.cqfc.appendtask.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.thrift.TException;

import com.cqfc.protocol.appendtask.AppendTask;
import com.cqfc.protocol.appendtask.AppendTaskDetail;
import com.cqfc.protocol.appendtask.AppendTaskIndex;
import com.jami.common.BaseMapper;

/**
 * @author liwh
 */
public interface AppendTaskMapper extends BaseMapper {

	/**
	 * 创建追号任务
	 * 
	 * @param appendTask
	 * @return
	 */
	@Insert("insert into ${tableName}"
			+ "(appendTaskId,partnerId,lotteryId,ball,beginIssueNo,appendQuantity,remainingQuantity,stopFlag,appendTotalMoney,perNoteNumber,userId,playType,freezeSerialNumber,createTime) "
			+ "values(#{param1.appendTaskId},#{param1.partnerId},#{param1.lotteryId},#{param1.ball},#{param1.beginIssueNo},#{param1.appendQuantity},#{param1.remainingQuantity},#{param1.stopFlag},#{param1.appendTotalMoney},#{param1.perNoteNumber},#{param1.userId},#{param1.playType},#{param1.freezeSerialNumber},now())")
	public int createAppendTask(AppendTask appendTask, @Param("tableName") String tableName);

	/**
	 * 创建追号任务明细
	 * 
	 * @param appendTaskDetail
	 * @return
	 */
	@Insert("insert into ${tableName}"
			+ "(appendTaskId,partnerId,issueNo,lotteryId,userId,playType,totalMoney,multiple,noteNumber,ball,createTime) "
			+ "values(#{param1.appendTaskId},#{param1.partnerId},#{param1.issueNo},#{param1.lotteryId},#{param1.userId},#{param1.playType},#{param1.totalMoney},#{param1.multiple},#{param1.noteNumber},#{param1.ball},now())")
	public int createAppendTaskDetail(AppendTaskDetail appendTaskDetail, @Param("tableName") String tableName);

	/**
	 * 查询追号索引
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param partnerTradeId
	 *            合作商交易ID
	 * @return
	 */
	@Select("select * from ${tableName} where partnerId=#{partnerId} and partnerTradeId=#{partnerTradeId}")
	public AppendTaskIndex findAppendTaskIndexByParam(@Param("partnerId") String partnerId,
			@Param("partnerTradeId") String partnerTradeId, @Param("tableName") String tableName);

	/**
	 * 查询追号任务明细
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param appendTaskId
	 *            追号任务ID
	 * @return
	 */
	@Select("select * from ${tableName} where partnerId=#{partnerId} and appendTaskId=#{appendTaskId} order by issueNo")
	public List<AppendTaskDetail> getAppendTaskDetailList(@Param("partnerId") String partnerId,
			@Param("appendTaskId") String appendTaskId, @Param("tableName") String tableName);

	/**
	 * 创建追号任务索引
	 * 
	 * @param appendTaskIndex
	 * @return
	 */
	@Insert("insert into ${tableName}" + "(partnerId,partnerTradeId,appendTaskId,userId,createTime)"
			+ "values(#{param1.partnerId},#{param1.partnerTradeId},#{param1.appendTaskId},#{param1.userId},now())")
	public int createAppendTaskIndex(AppendTaskIndex appendTaskIndex, @Param("tableName") String tableName);

	/**
	 * 查询追号任务信息
	 * 
	 * @param appendTaskId
	 * @return
	 */
	@Select("select * from ${tableName} where appendTaskId=#{appendTaskId}")
	public AppendTask findAppendTaskById(@Param("appendTaskId") String appendTaskId,
			@Param("tableName") String tableName);

	/**
	 * 停止追号（具体某一些期）
	 * 
	 * @param appendTaskId
	 *            追号任务ID
	 * @param issueNos
	 *            期号集合
	 * @param appendDetailStatus
	 *            追号详情状态
	 * @return
	 */
	@Update("update ${tableName} set appendDetailStatus=#{appendDetailStatus} where appendTaskId=#{appendTaskId} and appendDetailStatus=0 and issueNo in(${issueNos})")
	public int stopAppendTask(@Param("appendTaskId") String appendTaskId, @Param("issueNos") String issueNos,
			@Param("appendDetailStatus") int appendDetailStatus, @Param("tableName") String tableName);

	/**
	 * 停追所有追号任务
	 * 
	 * @param appendTaskId
	 *            追号任务ID
	 * @param appendDetailStatus
	 *            追号任务状态
	 * @return
	 */
	@Update("update ${tableName} set appendDetailStatus=#{appendDetailStatus} where appendTaskId=#{appendTaskId} and appendDetailStatus=0")
	public int stopAllAppendTask(@Param("appendTaskId") String appendTaskId,
			@Param("appendDetailStatus") int appendDetailStatus, @Param("tableName") String tableName);

	/**
	 * 统计某些追号的总金额
	 * 
	 * @param appendTaskId
	 * @param issueNos
	 * @return
	 */
	@Select("select * from ${tableName} where ${conditions}")
	public List<AppendTaskDetail> getStopAppendTask(@Param("conditions") String conditions,
			@Param("tableName") String tableName);

	/**
	 * 修改追号任务剩余期数
	 * 
	 * @param appendTaskId
	 * @param appendEndNum
	 * @param cancelMoney
	 * @param tableName
	 * @return
	 */
	@Update("update ${tableName} set remainingQuantity=remainingQuantity-#{appendEndNum},cancelNum=cancelNum+#{appendEndNum},"
			+ "cancelMoney=cancelMoney+#{cancelMoney}"
			+ " where appendTaskId=#{appendTaskId} and remainingQuantity>=#{appendEndNum}")
	public int modifyAppendTaskRemaining(@Param("appendTaskId") String appendTaskId,
			@Param("appendEndNum") int appendEndNum, @Param("cancelMoney") long cancelMoney,
			@Param("tableName") String tableName);

	/**
	 * 将追号剩余期数为0、状态为'追号正常'的追号任务状态修改为'追号完成'
	 * 
	 * @param appendTaskId
	 * @return
	 */
	@Update("update ${tableName} set appendStatus=2 where appendTaskId=#{appendTaskId} and remainingQuantity=0 and appendStatus=1")
	public int modifyAppendTaskStatus(@Param("appendTaskId") String appendTaskId, @Param("tableName") String tableName);

	/**
	 * 追号明细生成订单后修改追号任务信息
	 * 
	 * @param appendTaskDetailId
	 * @return
	 * @throws TException
	 */
	@Select("select * from ${tableName} where detailId=#{detailId}")
	public AppendTaskDetail findAppendTaskDetailById(@Param("detailId") long appendTaskDetailId,
			@Param("tableName") String tableName);

	/**
	 * 创建追号明细订单后更新追号任务信息
	 * 
	 * @param appendTaskId
	 * @param completeMoney
	 * @param issueNo
	 * @return
	 */
	@Update("update ${tableName} set remainingQuantity=remainingQuantity-1,finishedNum=finishedNum+1,"
			+ "finishedMoney=finishedMoney+#{completeMoney},newAppendIssueNo=#{issueNo}"
			+ " where appendTaskId=#{appendTaskId}")
	public int updateAppendTaskAfterOrder(@Param("appendTaskId") String appendTaskId,
			@Param("completeMoney") long completeMoney, @Param("issueNo") String issueNo,
			@Param("tableName") String tableName);

	/**
	 * 更新追号明细交易信息
	 * 
	 * @param detailId
	 *            追号详情ID
	 * @param orderNo
	 *            订单编号
	 * @param status
	 *            状态
	 * @param tableName
	 *            追号详情表名
	 * @return
	 */
	@Update("update ${tableName} set appendDetailStatus=#{status},orderNo=#{orderNo} where detailId=#{detailId}")
	public int updateAppendDetailStatus(@Param("detailId") long detailId, @Param("orderNo") String orderNo,
			@Param("status") int status, @Param("tableName") String tableName);

	/**
	 * 出票回调后,更新追号明细交易信息
	 * 
	 * @param detailId
	 *            追号详情ID
	 * @param status
	 *            状态
	 * @param tableName
	 *            追号详情表名
	 * @return
	 */
	@Update("update ${tableName} set appendDetailStatus=#{status} where detailId=#{detailId}")
	public int updateAppendDetailAfterPrint(@Param("detailId") long detailId, @Param("status") int status,
			@Param("tableName") String tableName);

	/**
	 * 通过订单编号获取冻结金额序列号
	 * 
	 * @param orderNo
	 *            订单编号
	 * @param taskTableName
	 *            追号任务表名
	 * @param detailTableName
	 *            追号详情表名
	 * @return
	 */
	@Select("select freezeSerialNumber from ${taskTableName} where appendTaskId=(select appendTaskId from ${detailTableName} where orderNo=#{orderNo})")
	public String getRefundSerialNumberByOrderNo(@Param("orderNo") String orderNo,
			@Param("taskTableName") String taskTableName, @Param("detailTableName") String detailTableName);

	/**
	 * 查询追号明细列表（定时扫描任务）
	 * 
	 * @param tableName
	 *            表名
	 * @param conditions
	 *            搜索条件
	 * @return
	 */
	@Select("select * from ${tableName} where ${conditions}")
	public List<AppendTaskDetail> getAppendTaskDetailListByParam(@Param("tableName") String tableName,
			@Param("conditions") String conditions);

	/**
	 * 取消追号任务信息
	 * 
	 * @param appendTaskId
	 *            追号任务ID
	 * @param status
	 *            状态
	 * @param tableName
	 *            表名
	 * @return
	 */
	@Update("update ${tableName} set appendStatus=#{status} where appendTaskId=#{appendTaskId}")
	public int cancelAppendTask(@Param("appendTaskId") String appendTaskId, @Param("status") int status,
			@Param("tableName") String tableName);

	/**
	 * 取消追号任务明细
	 * 
	 * @param appendTaskId
	 *            追号任务ID
	 * @param status
	 *            状态
	 * @param tableName
	 *            表名
	 * @return
	 */
	@Update("update ${tableName} set appendDetailStatus=#{status} where appendTaskId=#{appendTaskId}")
	public int cancelAppendTaskDetail(@Param("appendTaskId") String appendTaskId, @Param("status") int status,
			@Param("tableName") String tableName);

	/**
	 * 删除索引
	 * 
	 * @param partnerId
	 * @param tradeId
	 * @param tableName
	 * @return
	 */
	@Delete("delete from ${tableName} where partnerId=#{partnerId} and partnerTradeId=#{tradeId}")
	public int deleteAppendTaskIndex(@Param("partnerId") String partnerId, @Param("tradeId") String tradeId,
			@Param("tableName") String tableName);

	/**
	 * 根据订单编号查询追号中止条件
	 * 
	 * @param orderNo
	 * @param taskTableName
	 * @param detailTableName
	 * @return
	 */
	@Select("select * from ${taskTableName} where appendTaskId=(select appendTaskId from ${detailTableName} where orderNo=#{orderNo})")
	public AppendTask getStopFlagByOrderNo(@Param("orderNo") String orderNo,
			@Param("taskTableName") String taskTableName, @Param("detailTableName") String detailTableName);
}
