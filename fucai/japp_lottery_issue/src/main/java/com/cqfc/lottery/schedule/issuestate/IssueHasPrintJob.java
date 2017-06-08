package com.cqfc.lottery.schedule.issuestate;

import java.util.Date;
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
import com.cqfc.util.DateUtil;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.SwitchUtil;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.ScanLog;

/**
 * 期号状态扭转：销售截止-->已出票
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class IssueHasPrintJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0/30 * * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------stateTask3(销售截止-->已出票)开始------------");
		try {
			int currentPage = 1;
			int pageSize = 2000;
			while (true) {
				List<LotteryIssue> lotteryIssueList = lotteryIssueService.getLotteryIssueListByState(
						IssueConstant.ISSUE_STATUS_END_SELL, currentPage, pageSize);

				if (lotteryIssueList != null && lotteryIssueList.size() > 0) {
					for (LotteryIssue lotteryIssue : lotteryIssueList) {
						String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
						String printEndTime = lotteryIssue.getPrintEndTime();
						String officialEndTime = lotteryIssue.getOfficialEndTime();

						if (DateUtil.compareDateTime(printEndTime, currentTime) >= 0
								&& DateUtil.compareDateTime(officialEndTime, currentTime) >= 0) {
							int issueId = lotteryIssue.getIssueId();
							int isSuccess = lotteryIssueService.updateLotteryIssueState(issueId,
									IssueConstant.ISSUE_STATUS_PRINT);
							String lotteryId = lotteryIssue.getLotteryId();
							String issueNo = lotteryIssue.getIssueNo();
							ScanLog.scan.info("stateTask3,期状态扭转：销售截止-->已出票,lotteryId=%s,issueNo=%s,returnValue=%d",
									lotteryId, issueNo, isSuccess);
							if (isSuccess > 0) {
								ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
								ActivemqProducer activemqProducer = ctx.getBean("activemqProducer",
										ActivemqProducer.class);

								ActivemqSendObject sendObject = new ActivemqSendObject();
								sendObject.setLotteryId(lotteryId);
								sendObject.setIssueNo(issueNo);
								activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_ORDERCANCEL_METHODID);
								ScanLog.scan.info("stateTask3,触发撤单扫描操作,放入ACTIVEMQ队列,lotteryId=%s,issueNo=%s",
										lotteryId, issueNo);
							}
						}
					}
				} else {
					break;
				}
				currentPage++;
			}
		} catch (Exception e) {
			ScanLog.scan.error("stateTask3(销售截止-->已出票)发生异常", e);
		}
		ScanLog.scan.info("--------stateTask3(销售截止-->已出票)结束------------");
	}
}
