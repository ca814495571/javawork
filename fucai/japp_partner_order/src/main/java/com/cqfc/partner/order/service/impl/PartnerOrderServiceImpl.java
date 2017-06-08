package com.cqfc.partner.order.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cqfc.partner.order.dao.DailyAwardCountDao;
import com.cqfc.partner.order.dao.DailyChargeCountDao;
import com.cqfc.partner.order.dao.DailyEncashCountDao;
import com.cqfc.partner.order.dao.DailyReportCountDao;
import com.cqfc.partner.order.dao.DailySaleCountDao;
import com.cqfc.partner.order.dao.IssueRewardCountDao;
import com.cqfc.partner.order.dao.IssueSaleCountDao;
import com.cqfc.partner.order.dao.PartnerOrderDao;
import com.cqfc.partner.order.dao.RecoveryIndexDao;
import com.cqfc.partner.order.dao.TempOrderDao;
import com.cqfc.partner.order.dataCache.PartnerOrderBuffer;
import com.cqfc.partner.order.service.IPartnerOrderService;
import com.cqfc.partner.order.task.AddOrderExcSqlTask;
import com.cqfc.partner.order.task.AddOrderToQueueTask;
import com.cqfc.partner.order.task.UpdateJcOrderWinResultTask;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.partner.ReturnData;
import com.cqfc.protocol.partnerorder.DailyAwardCount;
import com.cqfc.protocol.partnerorder.DailyChargeCount;
import com.cqfc.protocol.partnerorder.DailyEncashCount;
import com.cqfc.protocol.partnerorder.DailyRiskCount;
import com.cqfc.protocol.partnerorder.DailySaleAndCharge;
import com.cqfc.protocol.partnerorder.DailySaleCount;
import com.cqfc.protocol.partnerorder.IssueRewardCount;
import com.cqfc.protocol.partnerorder.IssueRiskCount;
import com.cqfc.protocol.partnerorder.IssueSaleAndReward;
import com.cqfc.protocol.partnerorder.IssueSaleCount;
import com.cqfc.protocol.partnerorder.LotteryDaySale;
import com.cqfc.protocol.partnerorder.LotteryIssueSale;
import com.cqfc.protocol.partnerorder.Order;
import com.cqfc.protocol.partnerorder.OrderDetail;
import com.cqfc.protocol.partnerorder.PcDailyReport;
import com.cqfc.protocol.partnerorder.PcDaySaleDetails;
import com.cqfc.protocol.partnerorder.PcLotteryIssueSale;
import com.cqfc.protocol.partnerorder.PcPartnerOrder;
import com.cqfc.protocol.partnerorder.RecoverOrderIndex;
import com.cqfc.protocol.ticketwinning.WinningNumStat;
import com.cqfc.protocol.ticketwinning.WinningNumStatData;
import com.cqfc.protocol.ticketwinning.WinningOrderInfo;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.DateUtil;
import com.cqfc.util.LotteryType;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.OrderStatus.OrderType;
import com.cqfc.util.OrderUtil;
import com.cqfc.util.Pair;
import com.jami.common.ActivemqProducer;
import com.jami.util.CountLog;
import com.jami.util.DataSourceContextHolder;
import com.jami.util.DataSourceUtil;
import com.jami.util.Log;
import com.jami.util.OrderLogger;
import com.jami.util.PartnerOrderLogUtil;

@Service
public class PartnerOrderServiceImpl implements IPartnerOrderService {

	@Autowired
	PartnerOrderDao partnerOrderDao;

	@Autowired
	IssueSaleCountDao issueSaleCountDao;

	@Autowired
	IssueRewardCountDao issueRewardCountDao;

	@Autowired
	DailySaleCountDao dailySaleCountDao;

	@Autowired
	DailyAwardCountDao dailyAwardCountDao;

	@Autowired
	DailyChargeCountDao dailyChargeCountDao;

	@Autowired
	DailyEncashCountDao dailyEncashCountDao;
	
	@Autowired
	DailyReportCountDao dailyReportCountDao;

	@Autowired
	RecoveryIndexDao recoveryIndexDao;

	@Autowired
	TempOrderDao tempOrderDao;

	@Autowired
	ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Autowired
	ThreadPoolTaskExecutor addOrderTaskExecutor;

	@Autowired
	private MemcachedClient memcachedClient;

	@Autowired
	ActivemqProducer activemqProducer;
	
	/**
	 * 添加合作商的一个订单，返回值 -1 数据库异常 1 添加成功 -7 订单号不存在 -100 违反唯一性约束
	 * 
	 */
	public int addPartnerOrder(Order partnerOrder) {

		return PartnerOrderBuffer.addOrder(partnerOrder);

	}
	
	@Override
	public PcPartnerOrder getPartnerOrderByWhere(Order order, int pageNum, int pageSize){
		
		String tableName = "";
		String dbName = "";
		PcPartnerOrder pcPartnerOrder = new PcPartnerOrder();
		try {
			if (pageNum < 1) {
				pageNum = 1;
			}
		
			if (pageSize > 50) {
				pageSize = 50;
			}
			if(StringUtils.isNotBlank(order.getTradeId())){
				tableName = DataSourceUtil.getTableName(DataSourceUtil.PARTNERORDER_TABLENAME,order
						.getTradeId());
				dbName = DataSourceUtil.SLAVE
						+ DataSourceUtil.getDbName(order.getTradeId());
			}else if(StringUtils.isNotBlank(order.getOrderNo())){
				
				tableName = DataSourceUtil.getTableNameByOrderNo(Long.parseLong(order.getOrderNo()));
				dbName = DataSourceUtil.SLAVE
						+ DataSourceUtil.getDbNameByOrderNo(Long.parseLong(order.getOrderNo()));
			}
			
			if("".equals(dbName)||"".equals(tableName)){
				return pcPartnerOrder;
			}
			
			DataSourceContextHolder.setDataSourceType(dbName);
		} catch (Exception e) {
			Log.run.error("查询订单编号格式出错,订单编号orderNo:" +order.getOrderNo());
			
			return pcPartnerOrder;
			
		}	
		return partnerOrderDao.getPartnerOrderByWhere(order, tableName, pageNum, pageSize);
			
	}
	/**
	 * 根据条件查询合作商彩种期号销售中奖情况(分订单类型)
	 */
	@Override
	public List<IssueSaleAndReward> getIssueSaleAndReward(String partnerId,
			String lotteryId, String issueNo) {
		Log.run.debug("根据条件查询合作商彩种期号销售中奖情况,参数partnerId=" + partnerId
				+ ",lotteryId=" + lotteryId + ",issueNo=" + issueNo);

		List<IssueSaleAndReward> issueSaleAndRewards = new ArrayList<IssueSaleAndReward>();

		try {
			String key = "GISAR_" + partnerId + lotteryId + issueNo;
			issueSaleAndRewards = memcachedClient.get(key);

			if (issueSaleAndRewards != null && issueSaleAndRewards.size() > 0) {
				return issueSaleAndRewards;
			} else {

				DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE
						+ DataSourceUtil.COUNT_DB);
				issueSaleAndRewards = partnerOrderDao.getIssueSaleAndReward(
						partnerId, lotteryId, issueNo);
				memcachedClient.set(key, 0, issueSaleAndRewards);
			}

		} catch (TimeoutException e) {
			Log.run.error(e);
		} catch (InterruptedException e) {
			Log.run.error(e);
		} catch (MemcachedException e) {
			Log.run.error(e);
		}

		Log.run.debug("根据条件查询合作商彩种期号销售中奖完毕...");
		return issueSaleAndRewards;
	}

	@Override
	public List<LotteryIssueSale> getIssueSaleAndRewardByGroup(
			String partnerId, String lotteryId, String issueNo) {
		Log.run.debug("根据条件查询合作商彩种期号销售中奖情况,参数partnerId=" + partnerId
				+ ",lotteryId=" + lotteryId + ",issueNo=" + issueNo);

		List<LotteryIssueSale> lotteryIssueSales = new ArrayList<LotteryIssueSale>();

		try {
			String key = "GUSARBG_" + partnerId + lotteryId + issueNo;
			lotteryIssueSales = memcachedClient.get(key);

			if (lotteryIssueSales != null && lotteryIssueSales.size() > 0) {

				return lotteryIssueSales;
			} else {

				DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE
						+ DataSourceUtil.COUNT_DB);
				lotteryIssueSales = partnerOrderDao
						.getIssueSaleAndRewardByGroup(partnerId, lotteryId,
								issueNo);
				memcachedClient.set(key, 0, lotteryIssueSales);
			}

		} catch (TimeoutException e) {
			Log.run.error(e);
		} catch (InterruptedException e) {
			Log.run.error(e);
		} catch (MemcachedException e) {
			Log.run.error(e);
		}

		Log.run.debug("根据条件查询合作商彩种期号销售中奖完毕...");
		return lotteryIssueSales;
	}

	@Override
	public List<LotteryIssueSale> getAllLotteryIssueSale(String lotteryId,
			String issueNo) {
		Log.run.debug("根据彩种期号查询所有合作商彩种期号销售情况,参数lotteryId=" + lotteryId
				+ ",issueNo=" + issueNo);
		List<LotteryIssueSale> lotteryIssueSales = new ArrayList<LotteryIssueSale>();

		try {
			String key = "GALIS_" + lotteryId + issueNo;
			lotteryIssueSales = memcachedClient.get(key);

			if (lotteryIssueSales != null && lotteryIssueSales.size() > 0) {

				return lotteryIssueSales;
			} else {

				DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE
						+ DataSourceUtil.COUNT_DB);

				lotteryIssueSales = partnerOrderDao.getAllLotteryIssueSale(
						lotteryId, issueNo);
				memcachedClient.set(key, 0, lotteryIssueSales);
			}

		} catch (Exception e) {
			Log.run.error(e);
		} 

		Log.run.debug("根据彩种期号查询所有合作商彩种期号销售完毕...");
		return lotteryIssueSales;
	}

	// GDSACB_ GALIS_ GUSARBG_ GISAR_
	/**
	 * 根据条件查询合作商每天的销售中奖充值情况
	 */
	@Override
	public List<DailySaleAndCharge> getDailySaleAndChargeByWhere(
			String partnerId, String countTime) {

		Log.run.debug("根据条件查询合作商每天的销售中奖充值情况，参数partnerId" + partnerId
				+ ",countTime =" + countTime);

		List<DailySaleAndCharge> dailySaleAndCharges = new ArrayList<DailySaleAndCharge>();
		try {

				DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE
						+ DataSourceUtil.COUNT_DB);
				dailySaleAndCharges = partnerOrderDao.getDailySaleAndCharge(
						partnerId, countTime);

		} catch (Exception e) {
			Log.run.error(e);
		}

		Log.run.debug("根据条件查询合作商每天的销售中奖充值完毕...");
		return dailySaleAndCharges;
	}

	public List<LotteryDaySale> getLotteryDaySaleByWhere(
			LotteryDaySale lotteryDaySale, int pageNum, int pageSize) {

		DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE
				+ DataSourceUtil.COUNT_DB);
		return partnerOrderDao.getLotteryDaySaleByWhere(lotteryDaySale,
				pageNum, pageSize);
	}

	public PcLotteryIssueSale getLotteryIssueSaleByWhere(
			LotteryIssueSale lotteryIssueSale, int pageNum, int pageSize) {
		PcLotteryIssueSale pcLotteryIssueSale = new PcLotteryIssueSale();

		DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE
				+ DataSourceUtil.COUNT_DB);
		pcLotteryIssueSale = partnerOrderDao.getLotteryIssueSaleByWhere(
				lotteryIssueSale, pageNum, pageSize);
		return pcLotteryIssueSale;
	}

	/**
	 * //期销售结束后，未中奖前统计所有合作商的期销售情况
	 * 
	 * -9 全局数据库查询列表结果为空 -1 数据库错误统计失败 1 统计成功
	 */
	@Override
	public int partnerIssueSaleCount(String lotteryId, String issueNo,
			boolean byMQ) {
		
		Log.run.debug(lotteryId + issueNo + "期销售结束后，未中奖前统计所有面向用户合作商的期销售情况...延迟2分30秒");
		IssueSaleCount issueSaleCount = null;
		Map<String, IssueSaleCount> countMap = new HashMap<String, IssueSaleCount>();
		String key = "";
		int flag = 0;
		try {
			
			LotteryPartner lotteryPartner = new LotteryPartner();
			com.cqfc.processor.ReturnMessage message = TransactionProcessor
					.dynamicInvoke("partner", "getLotteryPartnerList",
							lotteryPartner, 1, 10000);
			if (message.getObj() == null) {
				Log.run.error("IDL接口调用异常");
				return -1;
			}

			ReturnData returnData = (ReturnData) message.getObj();
			List<LotteryPartner> lotteryPartners = returnData.getResultList();
			if (lotteryPartners != null && lotteryPartners.size() > 0) {

				LotteryPartner partner = new LotteryPartner();
				for (int i = 0; i < lotteryPartners.size(); i++) {

					partner = lotteryPartners.get(i);
					for (OrderType orderType :OrderType.values()) {
						
						issueSaleCount = new IssueSaleCount();
						issueSaleCount.setLotteryId(lotteryId);
						issueSaleCount.setIssueNo(issueNo);
						issueSaleCount.setOrderType(orderType.getKey());
						issueSaleCount.setPartnerId(partner.getPartnerId());
						key = partner.getPartnerId() + lotteryId + issueNo + orderType.getKey();
						countMap.put(key, issueSaleCount);
					}
				}
			} else {
				Log.run.debug("无合作商信息,统计无效");
				return 1;
			}

			DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE
					+ DataSourceUtil.TempOrderDbName);

			List<IssueSaleCount> issueSales = issueSaleCountDao
					.getIssueSaleCount(lotteryId, issueNo,
							DataSourceUtil.TempOrderTableName);

			for (IssueSaleCount issueSale : issueSales) {

				key = issueSale.getPartnerId() + issueSale.getLotteryId()
						+ issueSale.getIssueNo() + issueSale.getOrderType();

				if (countMap.containsKey(key)) {

					countMap.put(key, issueSale);
				}
			}

			// 对所有的合作商销售进行insert操作
			DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER
					+ DataSourceUtil.COUNT_DB);

			flag = addIssueSaleCount(countMap);
			
			if (!byMQ) {
				
				for (LotteryPartner partner :lotteryPartners) {
					
					memcachedClient.delete("GISAR_"+partner.getPartnerId()+lotteryId+issueNo);
					memcachedClient.delete("GUSARBG_"+partner.getPartnerId()+lotteryId+issueNo);
					
				}
				memcachedClient.delete("GALIS_"+lotteryId+issueNo);
				
				
				ActivemqSendObject sendObject = new ActivemqSendObject();
				sendObject.setLotteryId(lotteryId);
				sendObject.setIssueNo(issueNo);
				activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_SALECOUNT_COMPLETED_METHODID);
			}
		} catch (Exception e) {

			Log.run.error("期销量统计有异常",e);
		}
		Log.run.debug(lotteryId + issueNo + "期销售结束后，未中奖前统计所有面向合作商的期销售情况成功！！！");
		return flag;
	}

	@Override
	public int partnerIssueSaleCount(String lotteryId, String issueNo) {
		return partnerIssueSaleCount(lotteryId, issueNo, false);
	}
	/**
	 * 期中奖后,统计所有合作商中奖信息
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 * @throws TException
	 */
	public int partnerIssueRewardCount(String lotteryId, String issueNo) {
		int flag = 0;
		WinningNumStat winningNumStat = new WinningNumStat();
		IssueRewardCount issueRewardCount = null;
		Log.run.debug("彩种:" + lotteryId + "期号：" + issueNo
				+ "期中奖审核后,统计所有合作商中奖情况..延迟2分30秒");

		String key = "";
		String keyTemp = "";
		try {
		Map<String, IssueRewardCount> countMap = new HashMap<String, IssueRewardCount>();
		com.cqfc.processor.ReturnMessage message = TransactionProcessor
				.dynamicInvoke("partner", "getLotteryPartnerList",
						new LotteryPartner(), 1, 10000);
		if (message.getObj() == null) {

			Log.run.error("partner模块getLotteryPartnerList方法调用失败");
			return -1;
		}
		ReturnData returnData = (ReturnData) message.getObj();
		List<LotteryPartner> lotteryPartners = returnData.getResultList();
		if (lotteryPartners != null && lotteryPartners.size() > 0) {

			for (LotteryPartner partner :lotteryPartners) {
				
				for (OrderType orderType :OrderType.values()) {
					
					issueRewardCount = new IssueRewardCount();
					issueRewardCount.setPartnerId(partner.getPartnerId());
					issueRewardCount.setLotteryId(lotteryId);
					issueRewardCount.setIssueNo(issueNo);
					issueRewardCount.setOrderType(orderType.getKey());
					key = partner.getPartnerId() + lotteryId + issueNo + orderType.getKey();
					countMap.put(key, issueRewardCount);
				}
			}
			
		} else {
			Log.run.debug("无合作商信息,统计无效");
			return -1;
		}

		winningNumStat.setLotteryId(lotteryId);
		winningNumStat.setIssueNo(issueNo);

		message = TransactionProcessor.dynamicInvoke("ticketWinning",
				"getWinningNumStat", winningNumStat, 1, 1000);
		if (message.getObj() == null) {
			Log.run.error("调用ticketWinning模块中的getWinningNumStat方法,结果为NULL");
			return -1;
		} else {

			WinningNumStatData winningNumStatData = (WinningNumStatData) message
					.getObj();
			List<WinningNumStat> winningNumStats = winningNumStatData
					.getResultList();

			for (int i = 0; i < winningNumStats.size(); i++) {

				winningNumStat = winningNumStats.get(i);
				keyTemp = winningNumStat.getPartnerId()
						+ winningNumStat.getLotteryId()
						+ winningNumStat.getIssueNo()
						+ winningNumStat.getOrderType();

				if (countMap.containsKey(keyTemp)) {

					issueRewardCount = countMap.get(keyTemp);
					issueRewardCount.setSmallPrizeMoney(issueRewardCount
							.getSmallPrizeMoney()
							+ winningNumStat.getSmallPrizeMoney());
					issueRewardCount.setSmallPrizeNum(issueRewardCount
							.getSmallPrizeNum()
							+ winningNumStat.getSmallPrizeNum());
					issueRewardCount.setBigPrizeMoney(issueRewardCount
							.getBigPrizeMoney()
							+ winningNumStat.getBigPrizeMoney());
					issueRewardCount
							.setBigPrizeNum(issueRewardCount.getBigPrizeNum()
									+ winningNumStat.getBigPrizeNum());
				}
			}

		}

		DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER
				+ DataSourceUtil.COUNT_DB);
		flag = addIssueRewardCount(countMap);
		
		for (LotteryPartner partner :lotteryPartners) {
			
			memcachedClient.delete("GISAR_"+partner.getPartnerId()+lotteryId+issueNo);
			memcachedClient.delete("GUSARBG_"+partner.getPartnerId()+lotteryId+issueNo);
			
		}
		memcachedClient.delete("GALIS_"+lotteryId+issueNo);
		} catch (Exception e) {
			Log.run.error("期中奖统计有异常", e);
			
		} 
		if(flag == 1){
			Log.run.debug("彩种:" + lotteryId + "期号：" + issueNo
					+ "期中奖审核后,统计所有合作商中奖信息成功!!！");
		}else{
			Log.run.debug("彩种:" + lotteryId + "期号：" + issueNo
					+ "期中奖审核后,统计所有合作商中奖信息失败");
		}
		
		return flag;
	}

	/**
	 * 根据时间（yyyy-MM-dd）统计合作商销售总额,中奖总额,充值,提现(暂没该业务) 1 操作成功 -9 全局数据库查询记录结果为空 -1
	 * 数据库操作失败或者传入日期参数格式错误 导致统计失败
	 */
	public int partnerDailySaleCount(String countTime) {

		DailySaleCount dailySaleCount = null;
		String key = "";
		int flag = 1;

		try {
			Map<String, DailySaleCount> countMap = new HashMap<String, DailySaleCount>();
			LotteryPartner lotteryPartner = new LotteryPartner();
			Log.run.debug("初始化面向用户合作商的日销量数据....");
			com.cqfc.processor.ReturnMessage message = TransactionProcessor
					.dynamicInvoke("partner", "getLotteryPartnerList",
							lotteryPartner, 1, 10000);
			if (message.getObj() == null) {
				Log.run.error("调用IDL接口partner模块方法getLotteryPartnerList失败");
				return -1;
			}
			ReturnData returnData = (ReturnData) message.getObj();
			List<LotteryPartner> lotteryPartners = returnData.getResultList();
			if (lotteryPartners != null && lotteryPartners.size() > 0) {

				LotteryPartner partner = new LotteryPartner();
				for (int i = 0; i < lotteryPartners.size(); i++) {

					partner = lotteryPartners.get(i);
					for (LotteryType lotteryType :LotteryType.values()) {
						
						dailySaleCount = new DailySaleCount();
						
						dailySaleCount.setPartnerId(partner.getPartnerId());
						dailySaleCount.setLotteryId(lotteryType.getText());
						dailySaleCount.setCountTime(countTime);
						key = partner.getPartnerId()
								+ lotteryType.getText() + countTime;
						countMap.put(key, dailySaleCount);
					}
				}
			} else {
				Log.run.debug("无合作商记录，统计无效");
				return -1;
			}


			DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE
					+ DataSourceUtil.TempOrderDbName);

			List<DailySaleCount> dailySaleCounts = dailySaleCountDao
					.getDailySaleByCountTime(countTime,
							DataSourceUtil.TempOrderTableName);

			for (DailySaleCount dailySaleCountTemp : dailySaleCounts) {

				key = dailySaleCountTemp.getPartnerId()
						+ dailySaleCountTemp.getLotteryId() + countTime;
				if (countMap.containsKey(key)) {
					
					dailySaleCount = countMap.get(key);
					dailySaleCount.setTotalMoney(dailySaleCountTemp.getTotalMoney());
				}

			}

			DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER
					+ DataSourceUtil.COUNT_DB);
			flag = addDailySaleCount(countMap);

		} catch (Exception e) {
			Log.run.error(e);
		}
		return flag;
	}

	@Override
	public int partnerDailyAwardCount(String countTime) {
		
		DailyAwardCount dailyAwardCount = null;
		String key = "";
		int flag = -1;

		try {
			Map<String, DailyAwardCount> countMap = new HashMap<String, DailyAwardCount>();
			LotteryPartner lotteryPartner = new LotteryPartner();
			com.cqfc.processor.ReturnMessage message = TransactionProcessor
					.dynamicInvoke("partner", "getLotteryPartnerList",
							lotteryPartner, 1, 10000);
			if (message.getObj() == null) {
				Log.run.error("调用IDL接口partner模块方法getLotteryPartnerList失败");
				return -1;
			}
			ReturnData returnData = (ReturnData) message.getObj();
			List<LotteryPartner> lotteryPartners = returnData.getResultList();
			if (lotteryPartners != null && lotteryPartners.size() > 0) {

				LotteryPartner partner = new LotteryPartner();
				for (int i = 0; i < lotteryPartners.size(); i++) {

					partner = lotteryPartners.get(i);
					for (LotteryType lotteryType :LotteryType.values()) {
						
						dailyAwardCount = new DailyAwardCount();
						
						dailyAwardCount.setPartnerId(partner.getPartnerId());
						dailyAwardCount.setLotteryId(lotteryType.getText());
						dailyAwardCount.setCountTime(countTime);
						key = partner.getPartnerId()
								+ lotteryType.getText() + countTime;
						countMap.put(key, dailyAwardCount);
					}
				}
			} else {
				Log.run.debug("无合作商记录，统计无效");
				return -1;
			}

			DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE
					+ DataSourceUtil.TempOrderDbName);

			List<DailyAwardCount> dailyAwardCounts = dailyAwardCountDao
					.getDailyAwardByCountTime(countTime,
							DataSourceUtil.WinningResultTableName);

			for (DailyAwardCount dailyAwardCountTemp : dailyAwardCounts) {

				key = dailyAwardCountTemp.getPartnerId()
						+ dailyAwardCountTemp.getLotteryId() + countTime;
				if (countMap.containsKey(key)) {
					
					dailyAwardCount = countMap.get(key);
					dailyAwardCount.setAwardPrizeMoney(dailyAwardCountTemp.getAwardPrizeMoney());
				}

			}

			DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER
					+ DataSourceUtil.COUNT_DB);
			flag = addDailyAwardCount(countMap);

		} catch (Exception e) {
			Log.run.error(e);
		}
		return flag;
	}

	public int partnerDailyChargeCount(String countTime) {
		int flag = 0;
		com.cqfc.processor.ReturnMessage message = TransactionProcessor
				.dynamicInvoke("partner", "getLotteryPartnerList",
						new LotteryPartner(), 1, 10000);
		if (message.getObj() == null) {

			Log.run.error("partner模块getLotteryPartnerList方法调用失败");
			return -1;
		}
		ReturnData returnData = (ReturnData) message.getObj();
		List<LotteryPartner> lotteryPartners = returnData.getResultList();

		LotteryPartner lotteryPartner = null;
		DailyChargeCount dailyChargeCount = null;
		long chargeMoney = 0;
		Map<String, DailyChargeCount> mapCount = new HashMap<String, DailyChargeCount>();
		if (lotteryPartners != null && lotteryPartners.size() > 0) {

			for (int i = 0; i < lotteryPartners.size(); i++) {

				lotteryPartner = lotteryPartners.get(i);

				dailyChargeCount = new DailyChargeCount();
				dailyChargeCount.setPartnerId(lotteryPartner.getPartnerId());
				dailyChargeCount.setCountTime(countTime);
				mapCount.put(lotteryPartner.getPartnerId() + countTime,
						dailyChargeCount);
			}
		} else {
			Log.run.debug("无合作商信息,统计无效");
			return 1;
		}

		com.cqfc.processor.ReturnMessage msg = TransactionProcessor
				.dynamicInvoke("partnerAccount", "statisticRecharge", countTime);
		Iterator<String> iterator = null;
		String keyTmp = "";
		String partnerId = "";
		if (msg.getObj() != null) {

			Map<String, Long> map = (Map<String, Long>) msg.getObj();

			iterator = map.keySet().iterator();

			while (iterator.hasNext()) {

				partnerId = iterator.next();
				keyTmp = partnerId + countTime;
				chargeMoney = map.get(partnerId);

				if (mapCount.containsKey(keyTmp)) {

					dailyChargeCount = mapCount.get(keyTmp);
					dailyChargeCount.setChargeTotalMoney(dailyChargeCount
							.getChargeTotalMoney() + chargeMoney);
				}

			}
		} else {

			Log.run.error("调用IDL接口partnerAccount模块方法statisticRecharge失败");
			return -1;
		}
		Log.run.debug("统计1,2类型合作商日充值完毕");
		msg = TransactionProcessor.dynamicInvoke("userAccount",
				"statisticRecharge", countTime);

		if (msg.getObj() != null) {

			Map<String, Long> map = (Map<String, Long>) msg.getObj();
			iterator = map.keySet().iterator();

			while (iterator.hasNext()) {

				partnerId = iterator.next();
				keyTmp = partnerId + countTime;
				chargeMoney = map.get(partnerId);

				if (mapCount.containsKey(keyTmp)) {

					dailyChargeCount = mapCount.get(keyTmp);
					dailyChargeCount.setChargeTotalMoney(dailyChargeCount
							.getChargeTotalMoney() + chargeMoney);
				}

			}
		} else {

			Log.run.error("调用IDL接口userAccount模块方法statisticRecharge失败");
			return -1;
		}

		Log.run.debug("统计3类型合作商日充值完毕");

		DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER
				+ DataSourceUtil.COUNT_DB);

		flag = addDailyChargeCount(mapCount);

		return flag;
	}

	public int partnerDailyEncashCount(String countTime) {
		int flag = 0;
		com.cqfc.processor.ReturnMessage message = TransactionProcessor
				.dynamicInvoke("partner", "getLotteryPartnerList",
						new LotteryPartner(), 1, 10000);
		if (message.getObj() == null) {

			Log.run.error("partner模块getLotteryPartnerList方法调用失败");
			return -1;
		}
		ReturnData returnData = (ReturnData) message.getObj();
		List<LotteryPartner> lotteryPartners = returnData.getResultList();
		LotteryPartner lotteryPartner = null;
		DailyEncashCount dailyEncashCount = null;
		Map<String, DailyEncashCount> mapCount = new HashMap<String, DailyEncashCount>();
		long encashMoney = 0;
		if (lotteryPartners != null && lotteryPartners.size() > 0) {

			for (int i = 0; i < lotteryPartners.size(); i++) {

				lotteryPartner = lotteryPartners.get(i);

				dailyEncashCount = new DailyEncashCount();
				dailyEncashCount.setPartnerId(lotteryPartner.getPartnerId());
				dailyEncashCount.setCountTime(countTime);

				mapCount.put(lotteryPartner.getPartnerId() + countTime,
						dailyEncashCount);

			}
		} else {

			Log.run.debug("无合作商信息,统计无效");
			return 1;
		}

		com.cqfc.processor.ReturnMessage msg = TransactionProcessor
				.dynamicInvoke("userAccount", "statisticWithdraw", countTime);
		Iterator<String> iterator = null;
		String partnerId = "";
		if (msg.getObj() != null) {

			Map<String, Long> map = (Map<String, Long>) msg.getObj();

			iterator = map.keySet().iterator();
			String keyTmp = "";
			while (iterator.hasNext()) {

				partnerId = iterator.next();
				keyTmp = partnerId + countTime;
				encashMoney = map.get(partnerId);

				if (mapCount.containsKey(keyTmp)) {

					dailyEncashCount = mapCount.get(keyTmp);
					dailyEncashCount.setEncashTotalMoney(dailyEncashCount
							.getEncashTotalMoney() + encashMoney);
				}

			}
		} else {

			Log.run.error("调用IDL接口userAccount模块方法statisticWithdraw失败");
			return -1;
		}

		DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER
				+ DataSourceUtil.COUNT_DB);
		flag = addDailyEncashCount(mapCount);
		return flag;
	}

	public int addIssueSaleCount(Map<String, IssueSaleCount> map) {
		IssueSaleCount issueSaleCount = null;
		int flag = 1;
		try {

			Iterator<String> ites = map.keySet().iterator();

			String key_ = "";
			while (ites.hasNext()) {

				key_ = ites.next();
				issueSaleCount = map.get(key_);
				issueSaleCountDao.insert(issueSaleCount);
			}

		} catch (Exception e) {
			Log.run.error("添加销量信息异常", e);
			flag = -1;
		}
		return flag;
	}

	public int addIssueRewardCount(Map<String, IssueRewardCount> map) {

		IssueRewardCount issueRewardCount = null;
		int flag = 1;
		try {

			Iterator<String> iterator = map.keySet().iterator();
			String _key = "";
			while (iterator.hasNext()) {

				_key = iterator.next();
				issueRewardCount = map.get(_key);
				issueRewardCountDao.insert(issueRewardCount);
			}

		} catch (Exception e) {
			Log.run.error("添加期中奖信息异常", e);
			flag = -1;
		}
		return flag;
	}

	public int addDailySaleCount(Map<String, DailySaleCount> map) {

		DailySaleCount dailySaleCount = null;
		int flag = 1;
		try {

			Iterator<String> ites = map.keySet().iterator();
			String key_ = "";
			while (ites.hasNext()) {

				key_ = ites.next();
				dailySaleCount = map.get(key_);
				dailySaleCountDao.insert(dailySaleCount);
			}

		} catch (Exception e) {
			Log.run.error("添加销量信息异常", e);
			flag = -1;
		}
		return flag;
	}

	public int addDailyAwardCount(Map<String, DailyAwardCount> map) {

		DailyAwardCount dailyAwardCount = null;
		int flag = 1;
		try {

			Iterator<String> ites = map.keySet().iterator();
			String key_ = "";
			while (ites.hasNext()) {

				key_ = ites.next();
				dailyAwardCount = map.get(key_);
				dailyAwardCountDao.insert(dailyAwardCount);
			}

		} catch (Exception e) {
			Log.run.error("添加日中奖信息异常", e);
			flag = -1;
		}
		return flag;
	}

	public int addDailyChargeCount(Map<String, DailyChargeCount> map) {

		DailyChargeCount dailyChargeCount = null;
		int flag = 1;
		try {

			Iterator<String> iterator = map.keySet().iterator();

			while (iterator.hasNext()) {
				dailyChargeCount = map.get(iterator.next());
				dailyChargeCountDao.insert(dailyChargeCount);
			}

		} catch (Exception e) {
			Log.run.error("添加销量信息异常", e);
			flag = -1;
		}
		return flag;
	}

	public int addDailyEncashCount(Map<String, DailyEncashCount> map) {

		DailyEncashCount dailyEncashCount = null;
		int flag = 1;
		try {

			Iterator<String> iterator = map.keySet().iterator();

			while (iterator.hasNext()) {

				dailyEncashCount = map.get(iterator.next());
				dailyEncashCountDao.insert(dailyEncashCount);
			}

		} catch (Exception e) {
			Log.run.error("添加销量信息异常", e);
			flag = -1;
		}
		return flag;
	}

	
	public int addDailyReport(Map<String, DailySaleAndCharge> map) {

		DailySaleAndCharge dailySaleAndCharge = null;
		int flag = 1;
		try {

			Iterator<String> iterator = map.keySet().iterator();

			while (iterator.hasNext()) {

				dailySaleAndCharge = map.get(iterator.next());
				dailyReportCountDao.insert(dailySaleAndCharge);
			}

		} catch (Exception e) {
			Log.run.error("添加销量信息异常", e);
			flag = -1;
		}
		return flag;
	}
	
	
	
	@Override
	public void initAddOrderTask(ApplicationContext applicationContext) {

		AtomicBoolean running = new AtomicBoolean(true);
	
		for (int i = 0; i < DataSourceUtil.DB_NUM; i++) {

			addOrderTaskExecutor.submit(new AddOrderExcSqlTask(
					applicationContext, running, i));
		}

	}
	
	
	@Override
	public void initUpdateWinResultTask(ApplicationContext applicationContext) {

		AtomicBoolean running = new AtomicBoolean(true);
	
		for (int i = 0; i < DataSourceUtil.DB_NUM; i++) {

			threadPoolTaskExecutor.submit(new UpdateJcOrderWinResultTask(
					applicationContext, running, i));
		}

	}

	@Override
	public int logOrderToQueue(List<Pair<File, BlockingQueue>> pairs) {
		try {
			
			PartnerOrderBuffer.initListQueue();
			List<BlockingQueue<Order>> queue = PartnerOrderBuffer.getListQueue();
			File file = null;
			Pair<File, BlockingQueue> pair = null;
			for (int i = 0; i < DataSourceUtil.DB_NUM; i++) {

				file = new File(OrderLogger.BACKFILE_PREFIX
						+ DataSourceUtil.getDateSourceName(i)
						+ OrderLogger.LOG_FILE_PATH_SUBBIX);
				Log.run.info("日志文件路径:"+file.getAbsolutePath());
				if (!file.exists() || file.isDirectory()) {
					continue;
				}
				// 每个日志文件对应一个队列
				pair = new Pair<File, BlockingQueue>(file, queue.get(i));
				pairs.add(pair);
			}

			// true 标示正常
			AtomicBoolean atomicBoolean = new AtomicBoolean(true);
			CountDownLatch countDownLatch = new CountDownLatch(pairs.size());
			for (Pair<File, BlockingQueue> p : pairs) {
				threadPoolTaskExecutor.submit(new AddOrderToQueueTask(p,
						countDownLatch, atomicBoolean));

			}
			countDownLatch.await();

			if (!atomicBoolean.get()) {

				CountLog.run.fatal("恢复订单日志将订单放入队列中失败...");
				return -1;
			}
		} catch (Exception e) {
			CountLog.run.error("系统异常", e);
			return -1;
		}

		return 1;
	}

	@Override
	public int queueToDb(List<Pair<File, BlockingQueue>> pairs) {

		AtomicBoolean atomicBoolean = new AtomicBoolean(true);
		CountDownLatch countDownLatch = new CountDownLatch(pairs.size());
		for (Pair p : pairs) {

//			threadPoolTaskExecutor.submit(new RecoverOrderFromQueueTask(p,
//					countDownLatch, atomicBoolean));
		}

		try {
			countDownLatch.await();
			if (!atomicBoolean.get()) {
				Log.run.error("恢复订单日志,将队列中的订单批量入库失败..");
				return -1;
			}
		} catch (InterruptedException e) {
			CountLog.run.error("计数器调用await()异常", e);
			return -1;
		}

		return 1;
	}

	@Override
	public RecoverOrderIndex getIndexByDbName(String dbName,String setNo) {

		RecoverOrderIndex recoverOrderIndex = null;
		try {

			recoverOrderIndex = recoveryIndexDao.getIndexByDbName(dbName,setNo);
		} catch (Exception e) {
			Log.run.error("数据库异常", e);
		}
		return recoverOrderIndex;
	}

	@Override
	public int updateFlag(String dbName, int flag) {

		int b = -1;
		try {
			b = recoveryIndexDao.updateFlag(dbName, flag);
		} catch (Exception e) {
			CountLog.run.error("数据库异常", e);
		}
		return b;
	}

	@Override
	public int addOrderToQueue(Pair<File, BlockingQueue> pair, String orderNo) {
		File file = pair.first();
		BlockingQueue queue = pair.second();

		// 标示是否从日志中找到订单号
		boolean b = false;
		b = backOrderByOrderNo(file, queue, orderNo);

		if (!b) {

			String fileName = file.getName();
			file = getLastUpdateFile(fileName);

			// 找到.log.1 .log.2......文件中最后一个被修改的文件进行恢复
			b = backOrderByOrderNo(file, queue, orderNo);
		}

		if (b) {
			return 1;
		} else {
			Log.run.error(file.getName() + "没有找到orderNo:" + orderNo);
			return -1;
		}
	}

	/**
	 * 找到最后修改的文件
	 * 
	 * @param fileName
	 * @return
	 */
	public File getLastUpdateFile(String fileName) {
		File file = null;
		try {
			file = new File(OrderLogger.BACKFILE_PREFIX);
			long lastTime = 0;
			long lastTimeTemp = 0;
			File[] files = file.listFiles();

			List<File> filesTocompare = new ArrayList<File>();
			for (File f : files) {

				if (f.getName().startsWith(fileName)
						&& f.getName().length() > fileName.length()) {
					filesTocompare.add(f);
				}
			}
			if (filesTocompare.size() > 0) {
				file = filesTocompare.get(0);
				for (File f : filesTocompare) {
					lastTimeTemp = f.lastModified();
					if (lastTimeTemp > lastTime) {
						lastTime = lastTimeTemp;
						file = f;
					}
				}

			}

		} catch (Exception e) {
			CountLog.run.error("寻找最后修改的日志文件出现异常" + fileName);
		}

		return file;
	}

	public boolean backOrderByOrderNo(File file, BlockingQueue queue,
			String orderNo) {
		RandomAccessFile rf = null;
		Order order = null;
		List<OrderDetail> orderDetails = null;
		boolean b = false;
		boolean flag = false;//订单详情标示是否new
		try {
			rf = new RandomAccessFile(file, "r");
			long len = rf.length();

			if (len == 0) {
				return true;
			}
			long start = rf.getFilePointer();
			long end = start + len - 2;
			String line = "";
			rf.seek(end);
			int c = -1;
			String orderNoInLog;
			String jcFlag ;
			List<String> list = new ArrayList<String>();
			while (end > start) {

				c = rf.read();
				if (c == '\n' || end == 1) {

					line = rf.readLine();
					if (line != null && !"".equals(line)) {

						line = new String(line.getBytes("ISO-8859-1"), "utf-8");

						list = Arrays.asList(line
								.split(PartnerOrderLogUtil.SEPERATOR));
						
						if(list.size() == PartnerOrderLogUtil.ORDER_FIELDS_NUM){
							
							orderNoInLog = list.get(5);
							if (orderNoInLog != null
									&& orderNoInLog.equals(orderNo)) {
								b = true;
								Log.run.info(file.getName()
										+ "滚动的日志文件中找到orderNo:" + orderNo);
								break;
							}
							
							order = PartnerOrderLogUtil.str2Order(line);
							
							if(OrderUtil.getLotteryCategory(order.getLotteryId()) == OrderStatus.LotteryType.SPORTS_GAME.type){
								
								order.setOrderDetails(orderDetails);
								orderDetails.clear();
								flag = false;
							}
							
							queue.offer(order);
						}
						if(list.size() == PartnerOrderLogUtil.ORDERDETAL_FIELDS_NUM){
							
							jcFlag = list.get(PartnerOrderLogUtil.ORDERDETAL_FIELDS_NUM-1);
							if(!flag){
								orderDetails = new ArrayList<OrderDetail>();
								flag = true;
							}
							if(OrderStatus.JC_CODE.equals(jcFlag)){
								orderDetails.add(PartnerOrderLogUtil.str2OrderDetail(line));
							}
						}
					}
				}
				end--;
				rf.seek(end);
			}
		} catch (Exception e) {
			Log.run.error("订单日志恢复时出异常,文件名称" + file.getName(), e);
			return false;
		} finally {
			try {
				rf.close();
			} catch (IOException e) {
				Log.run.error("订单日志恢复时关闭IO流出异常,文件名称" + file.getName(), e);
			}
		}

		return b;
	}

	@Override
	public int cleanTempOrder(String time,int type) {
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER
				+ DataSourceUtil.TempOrderDbName);
		return tempOrderDao.cleanTempOrder(time,type);
	}

	@Override
	public int cleanTempJcOrderDetail(String time) {
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER
				+ DataSourceUtil.TempOrderDbName);
		return tempOrderDao.cleanTempJcOrderDetail(time);
	}

	
	@Override
	public DailyRiskCount getDailyRiskCount(String day) {
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE
				+ DataSourceUtil.COUNT_DB);
		DailyRiskCount dailyCount = new DailyRiskCount();
		long totalRecharge = dailyChargeCountDao.getTotalRecharge(day);
		dailyCount.setRechargeTotalMoney(totalRecharge);
		long totalWithdraw = dailyEncashCountDao.getTotalWithdraw(day);
		dailyCount.setWithdrawTotalMoney(totalWithdraw);
		long totalSale = dailySaleCountDao.getTotalSale(day);
		dailyCount.setSaleTotalMoney(totalSale);
		long totalAwardPrize = dailyAwardCountDao.getTotalAwardPrize(day);
		dailyCount.setAwardPrizeMoney(totalAwardPrize);
		return dailyCount;
	}

	@Override
	public IssueRiskCount getIssueRiskCount(String lotteryId, String issueNo) {
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE
				+ DataSourceUtil.COUNT_DB);
		return issueSaleCountDao.getIssueRiskCount(lotteryId, issueNo);
	}

	@Override
	public long getTotalTicknumByDay(String date) {
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE
				+ DataSourceUtil.TempOrderDbName);
		String beginTime = date + " 00:00:00";
		String endTime = date + " 23:59:59";
		return tempOrderDao.getTotalTicknumByDay(beginTime, endTime);
	}

	@Override
	public List<LotteryIssueSale> getCurrentIssueSaleByWhere(
			LotteryIssueSale lotteryIssueSale, String fromTime, String toTime) {
		
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE
				+ DataSourceUtil.TempOrderDbName);
		
		return tempOrderDao.getCurrentIssueSale(lotteryIssueSale, fromTime, toTime);
	}

	@Override
	public int partnerDailyReport(String countTime) {
		String endTime;
		try {
			
			endTime = DateUtil.getDateTime("yyyy-MM-dd", DateUtil.getOffsetDate(DateUtil.convertStringToDate("yyyy-MM-dd", countTime), 1))+" 06:00:00";
		} catch (ParseException e1) {
			Log.run.error("时间格式不正确"+countTime);
			return -1;
		}
		String startTime = countTime+" 06:00:00";
		DailySaleAndCharge dailySaleCount = null;
		String key = "";
		int flag = 1;

		try {
			Map<String, DailySaleAndCharge> countMap = new HashMap<String, DailySaleAndCharge>();
			LotteryPartner lotteryPartner = new LotteryPartner();
			Log.run.debug("初始化面向用户合作商的日报....");
			com.cqfc.processor.ReturnMessage message = TransactionProcessor
					.dynamicInvoke("partner", "getLotteryPartnerList",
							lotteryPartner, 1, 10000);
			if (message.getObj() == null) {
				Log.run.error("调用IDL接口partner模块方法getLotteryPartnerList失败");
				return -1;
			}
			ReturnData returnData = (ReturnData) message.getObj();
			List<LotteryPartner> lotteryPartners = returnData.getResultList();
			if (lotteryPartners != null && lotteryPartners.size() > 0) {

				LotteryPartner partner = new LotteryPartner();
				for (int i = 0; i < lotteryPartners.size(); i++) {

					partner = lotteryPartners.get(i);
					
					for (LotteryType lotteryType :LotteryType.values()) {
						
						dailySaleCount = new DailySaleAndCharge();
						
						dailySaleCount.setPartnerId(partner.getPartnerId());
						dailySaleCount.setLotteryId(lotteryType.getText());
						dailySaleCount.setCountTime(countTime);
						key = partner.getPartnerId()
								+ lotteryType.getText() + countTime;
						countMap.put(key, dailySaleCount);
					}
				}
			} else {
				Log.run.debug("无合作商记录，统计无效");
				return -1;
			}

		//	DailySaleAndCharge
			DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE
					+ DataSourceUtil.TempOrderDbName);

			List<DailySaleCount> dailySaleCounts = tempOrderDao
					.getDailyReportSale(countTime, startTime, endTime);

			for (DailySaleCount dailySaleCountTemp : dailySaleCounts) {

				key = dailySaleCountTemp.getPartnerId()
						+ dailySaleCountTemp.getLotteryId() + countTime;
				if (countMap.containsKey(key)) {
					
					dailySaleCount = countMap.get(key);
					dailySaleCount.setTotalMoney(dailySaleCountTemp.getTotalMoney());
				}

			}
			
			List<DailyAwardCount> dailyAwardCounts = tempOrderDao
					.getDailyReportAward(countTime, startTime, endTime);

			for (DailyAwardCount dailyAwardCountTemp : dailyAwardCounts) {

				key = dailyAwardCountTemp.getPartnerId()
						+ dailyAwardCountTemp.getLotteryId() + countTime;
				if (countMap.containsKey(key)) {
					
					dailySaleCount = countMap.get(key);
					dailySaleCount.setAwardPrizeMoney(dailyAwardCountTemp.getAwardPrizeMoney());
					dailySaleCount.setChargeTotalMoney(dailyAwardCountTemp.getAfterPrizeMoney());
				}
			}
			
			DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER
					+ DataSourceUtil.COUNT_DB);
			flag = addDailyReport(countMap);

		} catch (Exception e) {
			Log.run.error(e);
		}
		return flag;
		
	}

	@Override
	public PcDailyReport getDailyReportByWhere(
			DailySaleAndCharge dailySaleAndCharge, int pageNum, int pageSize) {
		
		if (pageNum < 1) {
			pageNum = 1;
		}

		if (pageSize > 500) {
			pageSize = 50;
		}
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE
				+ DataSourceUtil.COUNT_DB);
		return dailyReportCountDao.getDailyReportByWhere(dailySaleAndCharge,pageNum,pageSize);
		
	}

	@Override
	public int updateWinResultInfo(List<WinningOrderInfo> winningOrderInfos) {
		return PartnerOrderBuffer.updateBatchWinResult(winningOrderInfos);
	}

	@Override
	public PcDaySaleDetails getDailySaleDetails(DailySaleAndCharge dailySaleAndCharge,int pageNum,int pageSize) {
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE
				+ DataSourceUtil.COUNT_DB);
		return partnerOrderDao.getDailySaleByWhere( dailySaleAndCharge,pageNum,pageSize);
	}



}
