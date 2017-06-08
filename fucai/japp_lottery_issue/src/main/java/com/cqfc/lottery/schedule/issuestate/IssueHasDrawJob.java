package com.cqfc.lottery.schedule.issuestate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.lottery.service.ILotteryIssueService;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.SwitchUtil;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.ScanLog;

/**
 * 期号状态扭转：已转移-->已开奖
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class IssueHasDrawJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0/30 * * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------stateTask6(已转移-->已开奖)开始------------");
		try {
			int currentPage = 1;
			int pageSize = 2000;
			while (true) {
				List<LotteryIssue> lotteryIssueList = lotteryIssueService.getLotteryIssueListByState(
						IssueConstant.ISSUE_STATUS_TRANSFER, currentPage, pageSize);

				if (lotteryIssueList != null && lotteryIssueList.size() > 0) {
					for (LotteryIssue lotteryIssue : lotteryIssueList) {
						String lotteryId = lotteryIssue.getLotteryId();
						String issueNo = lotteryIssue.getIssueNo();
						String drawResult = lotteryIssue.getDrawResult();

						if (null != drawResult && !"".equals(drawResult)) {
							int issueId = lotteryIssue.getIssueId();
							int isSuccess = lotteryIssueService.updateLotteryIssueState(issueId,
									IssueConstant.ISSUE_STATUS_DRAW);
							ScanLog.scan.info("stateTask6,期状态扭转：已转移-->已开奖,lotteryId=%s,issueNo=%s,returnValue=%d",
									lotteryId, issueNo, isSuccess);
							if (isSuccess > 0) {
								int caling = lotteryIssueService.updateLotteryIssueState(issueId,
										IssueConstant.ISSUE_STATUS_CALING);
								if (caling > 0) {
									ScanLog.scan.info("stateTask6期状态更新算奖中,lotteryId=%s,issueNo=%s", lotteryId, issueNo);
									ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
									ActivemqProducer activemqProducer = ctx.getBean("activemqProducer",
											ActivemqProducer.class);
									// 触发算奖机制
									ScanLog.scan.info("stateTask6触发算奖操作扫描,放入ACTIVEMQ队列,lotteryId=%s,issueNo=%s",
											lotteryId, issueNo);
									ActivemqSendObject sendObject = new ActivemqSendObject();
									sendObject.setLotteryId(lotteryId);
									sendObject.setIssueNo(issueNo);
									activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_CALPRIZESCAN_METHODID);
								}
							}
						}
					}
				} else {
					break;
				}
				currentPage++;
			}
		} catch (Exception e) {
			ScanLog.scan.error("stateTask6(已转移-->已开奖)发生异常", e);
		}
		ScanLog.scan.info("--------stateTask6(已转移-->已开奖)结束------------");
	}
}
