package com.cqfc.riskcontrol.jms;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.accessback.TicknumRecord;
import com.cqfc.protocol.partnerorder.IssueRiskCount;
import com.cqfc.protocol.riskcontrol.StatisticDataByGame;
import com.cqfc.protocol.ticketissue.FucaiCount;
import com.cqfc.riskcontrol.dao.RiskControlDao;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

public class JmsTask implements Runnable {
	public static final int DO_COUNT_TICKET = 1;
	public static final int DO_COUNT_WINNING = 2;

	// 等待5分钟后才去取数据，以防止福彩中心的数据获取不到
	public static final int WAIT_TIME = 300000;

	private RiskControlDao riskControlDao;

	private int type;
	private String lotteryId;
	private String issueNo;
	private Map<String, Boolean> map;
	private boolean need2wait;
	private ScheduledExecutorService threadPool;

	public JmsTask(ScheduledExecutorService threadPoolTaskExecutor,
			RiskControlDao riskControlDao, int type, String lotteryId,
			String issueNo, Map<String, Boolean> statisticsThreadMap,
			boolean need2wait) {
		this.threadPool = threadPoolTaskExecutor;
		this.riskControlDao = riskControlDao;
		this.type = type;
		this.lotteryId = lotteryId;
		this.issueNo = issueNo;
		this.map = statisticsThreadMap;
		this.need2wait = need2wait;
	}

	@Override
	public void run() {
		try {
			Log.run.debug(
					"start to update statistic data, type=%d, lotteryId=%s, issue=%s",
					type, lotteryId, issueNo);
			Thread.sleep(10000);// 睡10秒再去获取数据，防止主从还没同步好就去取数据
			StatisticDataByGame dataByGame = riskControlDao.getStatisticByGame(
					lotteryId, issueNo);
			if (dataByGame == null) {
				dataByGame = new StatisticDataByGame();
				dataByGame.setGameId(lotteryId);
				dataByGame.setIssue(issueNo);
				try {
					riskControlDao
							.createTickStatisticByGame(lotteryId, issueNo);
				} catch (Exception e) {
					Log.run.warn("create risk data failed.");
				}
			}
			if (type == DO_COUNT_WINNING) {
				ReturnMessage message = TransactionProcessor.dynamicInvoke(
						ConstantsUtil.MODULENAME_TICKET_WINNING,
						"getTotalWinningMoneyByGame", lotteryId, issueNo);
				if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
						.getStatusCode())) {
					Long money = (Long) message.getObj();
					if (money < 0) {
						Log.run.error("riskcontrol receive winning money failed.");
					} else {
						dataByGame.setTotalWinningMoney(money);
					}
					Log.run.debug("riskcontrol receive winning money=%d", money);
				} else {
					Log.run.error("get statistic data from titcket winning failed.");
				}// 取不到销量统计数据时，重试三次，以防止福彩中心未完成统计
				message = TransactionProcessor.dynamicInvoke(
						ConstantsUtil.MODULENAME_TICKET_ISSUE, "getFucaiCount",
						lotteryId, issueNo);
				if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
						.getStatusCode())) {
					FucaiCount statinfo = (FucaiCount) message.getObj();
					dataByGame.setFucaiTotalBuy(statinfo.getTotalBuy());
					dataByGame.setFucaiTotalWinning(statinfo.getTotalWinning());
					dataByGame.setFucaiTicketNum(statinfo.getTicketNum());
					if (statinfo.getTotalBuy() == ServiceStatusCodeUtil.STATUS_LIST_IS_NULL) {
						dataByGame.setFucaiTotalBuy(0);
						String threadKey = FucaiIssueCountTask.getThreadKey(
								lotteryId, issueNo);
						if (!map.containsKey(threadKey) || !map.get(threadKey)) {
							map.put(threadKey, true);
							try {
								threadPool.schedule(new FucaiIssueCountTask(
										threadPool, riskControlDao, map,
										lotteryId, issueNo, 1), 5,
										TimeUnit.MINUTES);
							} catch (Exception e) {
								map.remove(threadKey);
								Log.run.error("too many thread in pool", e);
							}
						}
					}
				} else {
					Log.run.error("get statistic data from ticket issue failed.");
				}	
				Log.run.debug(
						"before update statistic, winning money=%d, fucaiTicket=%d, fucaiMoney=%d, fucaiWinning=%d",
						dataByGame.getOutTicketSuccessNum(),
						dataByGame.getSuccessTicketNum(),
						dataByGame.getCancelAndSuccessNum(),
						dataByGame.getSuccessTotalMoney(),
						dataByGame.getFucaiTicketNum(),
						dataByGame.getFucaiTotalBuy(),
						dataByGame.getFucaiTotalWinning());
				riskControlDao.updateStatisticWinningByGame(dataByGame);
				return;
			}

			if (type != DO_COUNT_TICKET) {
				Log.run.error("error task type.");
				return;
			}
			ReturnMessage message = TransactionProcessor.dynamicInvoke(
					ConstantsUtil.MODULENAME_ACCESS_BACK, "getTicknumRecord",
					lotteryId, issueNo);
			if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
					.getStatusCode())) {
				TicknumRecord ticknumRecord = (TicknumRecord) message.getObj();
				dataByGame
						.setOutTicketSuccessNum(ticknumRecord.getSuccessNum());
			} else {
				Log.run.error("get statistic data from access back failed.");
			}

			message = TransactionProcessor.dynamicInvoke(
					ConstantsUtil.MODULENAME_PARTNER_ORDER,
					"getIssueRiskCount", lotteryId, issueNo);
			if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
					.getStatusCode())) {
				IssueRiskCount dailyCount = (IssueRiskCount) message.getObj();
				dataByGame.setSuccessTicketNum(dailyCount.getSuccessNum());
				dataByGame.setSuccessTotalMoney(dailyCount.getSuccessMoney());
			} else {
				Log.run.error("get statistic data from partner order failed.");
			}

			message = TransactionProcessor.dynamicInvoke(
					ConstantsUtil.MODULENAME_CANCEL_ORDER,
					"getSuccessTicketCancelOrder", lotteryId, issueNo);
			if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
					.getStatusCode())) {
				Long cancelAndSuccessNum = (Long) message.getObj();
				dataByGame.setCancelAndSuccessNum(cancelAndSuccessNum);
			} else {
				Log.run.error("get statistic data from cancel order failed.");
			}

			Log.run.debug(
					"before update statistic, outTicket=%d, successTicket=%d, cancelAndSuccessTicket=%d, successMoney=%d, fucaiTicket=%d, fucaiMoney=%d, fucaiWinning=%d",
					dataByGame.getOutTicketSuccessNum(),
					dataByGame.getSuccessTicketNum(),
					dataByGame.getCancelAndSuccessNum(),
					dataByGame.getSuccessTotalMoney(),
					dataByGame.getFucaiTicketNum(),
					dataByGame.getFucaiTotalBuy(),
					dataByGame.getFucaiTotalWinning());
			riskControlDao.updateStatisticTicketNumByGame(dataByGame);
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error("get risk data failed.");
		} finally {
			map.remove(getThreadKey(type, lotteryId, issueNo));
		}
	}

	public static String getThreadKey(int type, String lotteryId, String issueNo) {
		return type + "#" + lotteryId + "#" + issueNo;
	}
}
