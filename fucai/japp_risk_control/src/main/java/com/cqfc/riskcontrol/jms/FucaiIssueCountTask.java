package com.cqfc.riskcontrol.jms;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.riskcontrol.StatisticDataByGame;
import com.cqfc.protocol.ticketissue.FucaiCount;
import com.cqfc.riskcontrol.dao.RiskControlDao;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

public class FucaiIssueCountTask  implements Runnable{

	private ScheduledExecutorService threadPool;
	private RiskControlDao riskControlDao;
	private String lotteryId;
	private String issueNo;
	private int retryTime;
	private Map<String, Boolean> map;

	public FucaiIssueCountTask(ScheduledExecutorService threadPool,
			RiskControlDao riskControlDao, Map<String, Boolean> map, String lotteryId, String issueNo, int retryTime) {

		this.threadPool = threadPool;
		this.riskControlDao = riskControlDao;
		this.map = map;
		this.lotteryId = lotteryId;
		this.issueNo = issueNo;
		this.retryTime = retryTime;
	}

	@Override
	public void run() {
		try {
			StatisticDataByGame dataByGame = new StatisticDataByGame();
			dataByGame.setGameId(lotteryId);
			dataByGame.setIssue(issueNo);
			ReturnMessage message = TransactionProcessor.dynamicInvoke(
					ConstantsUtil.MODULENAME_TICKET_ISSUE, "getFucaiCount",
					lotteryId, issueNo);
			if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
					.getStatusCode())) {
				FucaiCount statinfo = (FucaiCount) message.getObj();
				dataByGame.setFucaiTotalBuy(statinfo.getTotalBuy());
				dataByGame.setFucaiTotalWinning(statinfo.getTotalWinning());
				dataByGame.setFucaiTicketNum(statinfo.getTicketNum());
				if (statinfo.getTotalBuy() == ServiceStatusCodeUtil.STATUS_LIST_IS_NULL) {
					if(retryTime > 60){
						Log.run.error("retry 60 times and cannot get fucai count, lotteryId=%s, issueNo= %s",lotteryId, issueNo);
						map.remove(getThreadKey(lotteryId, issueNo));
						return;
					}
					threadPool.schedule(new FucaiIssueCountTask(threadPool, riskControlDao, map, lotteryId, issueNo, retryTime + 1), 5, TimeUnit.MINUTES);
					return;
				}
				riskControlDao.updateFucaiCountByGame(dataByGame);
				map.remove(getThreadKey(lotteryId, issueNo));
			} else {
				Log.run.error("get statistic data from ticket issue failed.");
				map.remove(getThreadKey(lotteryId, issueNo));
			}
		} catch (Exception e) {
			Log.run.error("get statistic data from ticket issue failed.%s", e.toString());
			map.remove(getThreadKey(lotteryId, issueNo));
		}
	}

	public static String getThreadKey( String lotteryId,
			String issueNo) {
		return lotteryId + "#" + issueNo + "#fucaiCount";
	}

}
