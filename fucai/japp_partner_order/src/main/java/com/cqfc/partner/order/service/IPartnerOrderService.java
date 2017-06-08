package com.cqfc.partner.order.service;

import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.thrift.TException;
import org.springframework.context.ApplicationContext;

import com.cqfc.protocol.partnerorder.DailyRiskCount;
import com.cqfc.protocol.partnerorder.DailySaleAndCharge;
import com.cqfc.protocol.partnerorder.IssueRiskCount;
import com.cqfc.protocol.partnerorder.IssueSaleAndReward;
import com.cqfc.protocol.partnerorder.LotteryDaySale;
import com.cqfc.protocol.partnerorder.LotteryIssueSale;
import com.cqfc.protocol.partnerorder.Order;
import com.cqfc.protocol.partnerorder.PcDailyReport;
import com.cqfc.protocol.partnerorder.PcDaySaleDetails;
import com.cqfc.protocol.partnerorder.PcLotteryIssueSale;
import com.cqfc.protocol.partnerorder.PcPartnerOrder;
import com.cqfc.protocol.partnerorder.RecoverOrderIndex;
import com.cqfc.protocol.ticketwinning.WinningOrderInfo;
import com.cqfc.util.Configuration;
import com.cqfc.util.Pair;

public interface IPartnerOrderService {

	
	//public static final String BACKFILE_PREFIX = "backLog/";
	public static final String BACKFILE_PREFIX = Configuration.getConfigValue("backOrder_filePath").trim();
	/**
	 * 添加合作商订单
	 * @param order
	 * @return
	 */
	public int addPartnerOrder(Order order) ;

//	/**
//	 * 修改合作商订单
//	 * @param order
//	 * @return
//	 */
//	public int updatePartnerOrder(Order order) ;

	/**
	 * 根据条件获取合作商订单
	 * @param orderNo
	 * @return
	 */
	public PcPartnerOrder getPartnerOrderByWhere(Order order, int pageNum, int pageSize);

	/**
	 * 根据条件查询期号销售兑奖信息(文档接口)
	 * @param issueSaleCount
	 * @return
	 */
	public List<IssueSaleAndReward> getIssueSaleAndReward(String partnerId,
			String lotteryId, String issueNo) ;

	
	
	/**
	 *  根据条件分组查询期号销售兑奖信息(文档接口)
	 * @param partnerId
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */

	public List<LotteryIssueSale> getIssueSaleAndRewardByGroup(
			String partnerId, String lotteryId, String issueNo);
	
	/**
	 * 根据条件查询日销售兑奖充值信息（文档借口）
	 * @param dailySaleCount
	 * @return
	 */
	public List<DailySaleAndCharge> getDailySaleAndChargeByWhere(String partnerId,
			String countTime) ;
	
	
	/**
	 * 根据彩种id 期号查询所有合作商的销售量
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public List<LotteryIssueSale> getAllLotteryIssueSale(String lotteryId,
			String issueNo);
	
	
	
	
	
	/**
	 * 根据条件查询日销售量(后台调用)
	 * @param lotteryDaySale
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<LotteryDaySale> getLotteryDaySaleByWhere(
			LotteryDaySale lotteryDaySale, int pageNum, int pageSize);
	
	/**
	 * 根据条件查询期号销售统计表(后台调用)
	 * @param lotteryIssueSale
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PcLotteryIssueSale getLotteryIssueSaleByWhere(
			LotteryIssueSale lotteryIssueSale, int pageNum, int pageSize);



	/**
	 * 期销售结束后，未兑奖前统计所有合作商的期销售情况 (期销售截止触发)
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public int partnerIssueSaleCount(String lotteryId, String issueNo);

	/**
	 * 期销售结束后，未兑奖前统计所有合作商的期销售情况 (期销售截止触发)
	 * @param lotteryId
	 * @param issueNo
	 * @param byMQ 是否通过Mq触发
	 * @return
	 */
	public int partnerIssueSaleCount(String lotteryId, String issueNo, boolean byMQ);
	
	/**
	 * 期兑奖后,统计所有合作商兑奖信息(兑奖完毕后触发)
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 * @throws TException
	 */
	public int partnerIssueRewardCount(String lotteryId, String issueNo);
	
	
	
	
	/**
	 * 	每天定时统计所有合作商的日销售情况 countTime格式yyyy/MM/dd
	 * @param countTime(本模块自己定时触发)
	 * @return
	 */
	public int partnerDailySaleCount(String countTime);
	
	
	
	/**
	 * 	每天定时统计所有合作商的兑奖情况 countTime格式yyyy/MM/dd
	 * @param countTime(本模块自己定时触发)
	 * @return
	 */
	public int partnerDailyAwardCount(String countTime);
	
	
	/**
	 * 每天定时统计所有合作商的日充值情况 countTime格式yyyy/MM/dd
	 * @param countTime(本模块自己定时触发)
	 * @return
	 */
	public int partnerDailyChargeCount(String countTime);
	
	/**
	 * 每天定时统计所有合作商的日 提现情况 countTime格式yyyy/MM/dd
	 * @param countTime(本模块自己定时触发)
	 * @return
	 */
	public int partnerDailyEncashCount(String countTime);
	
	/**
	 * 初始化添加同步订单线程任务
	 */
	public void initAddOrderTask(ApplicationContext applicationContext);
	
	
	/**
	 * 重启应用start（）,之前将日志中的order放入队列中
	 * @return
	 */
	public int logOrderToQueue(List<Pair<File, BlockingQueue>> pairs);
	/**
	 * 重启应用start（）,之后从队列中中恢复订单数据到数据库
	 * @return
	 */
	public int queueToDb(List<Pair<File, BlockingQueue>> pairs);
	
	
	/**
	 * 根据库名查找日志恢复表中的最后一条入库数据
	 * @param dbName
	 * @return
	 */
	public RecoverOrderIndex getIndexByDbName(String dbName,String setNo);
	
	/**
	 * 修改日志恢复表中的标示符
	 * @param dbName
	 * @param flag
	 * @return
	 */
	public int updateFlag(String dbName,int flag);
	
	
	/**
	 * 根据订单号和日志文件恢复将需要恢复的数据放入队列中
	 */
	public int addOrderToQueue(Pair<File, BlockingQueue> pair,String orderNo);
	
	/**
	 * 根据类型清理临时库订单
	 * @param time
	 * @return
	 */
	public int cleanTempOrder(String time,int type);
	
	/**
	 * 清理临时表5天前的所有竞技彩订单详情
	 * @param time
	 * @return
	 */
	public int cleanTempJcOrderDetail(String time);


	/**
	 * 获取日统计数据供风控模块使用
	 * @param day
	 * @return
	 */
	public DailyRiskCount getDailyRiskCount(String day);

	/**
	 * 获取期统计数据供风控模块使用
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public IssueRiskCount getIssueRiskCount(String lotteryId, String issueNo);

	/**
	 * 统计一天销售成功的订单数
	 * @param date
	 * @return
	 */
	public long getTotalTicknumByDay(String date);
	
	
	/**
	 * 供合作商查询当前期销量
	 * @param lotteryIssueSale
	 * @param fromTime
	 * @param toTime
	 * @return
	 */
	public List<LotteryIssueSale> getCurrentIssueSaleByWhere(
			LotteryIssueSale lotteryIssueSale, String fromTime, String toTime);


	/**
	 * 供合作商使用的日报统计
	 * @param countTime
	 * @return
	 */
	public int partnerDailyReport(String countTime);
	
	/**
	 * 供合作商使用的日报查询
	 * @param dailySaleAndCharge
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PcDailyReport getDailyReportByWhere(DailySaleAndCharge dailySaleAndCharge, int pageNum, int pageSize);

	/**
	 * 修改竞彩中奖订单状态
	 * @param winningOrderInfos
	 * @return
	 */
	public int updateWinResultInfo(List<WinningOrderInfo> winningOrderInfos);

	/**
	 * 初始化竞彩中奖订单状态金额更新任务
	 * @param applicationContext
	 */
	public void initUpdateWinResultTask(ApplicationContext applicationContext);


	/**
	 * 日销售详情列表
	 * @param partnerId
	 * @param countTime
	 * @return
	 */
	public PcDaySaleDetails getDailySaleDetails(DailySaleAndCharge dailySaleAndCharge ,int pageNum,int pageSize) ;



}
