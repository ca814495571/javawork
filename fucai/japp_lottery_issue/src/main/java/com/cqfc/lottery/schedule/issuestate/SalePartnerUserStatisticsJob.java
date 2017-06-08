package com.cqfc.lottery.schedule.issuestate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.lottery.service.ILotteryIssueService;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.ParameterUtils;
import com.cqfc.util.SwitchUtil;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.ScanLog;

/**
 * 触发渠道销量统计事件
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class SalePartnerUserStatisticsJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0 0/2 * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------触发渠道商、用户销量统计开始------------");
		try {
			int currentPage = 1;
			int pageSize = 2000;
			while (true) {
				List<LotteryIssue> lotteryIssueList = lotteryIssueService.getLotteryIssueListByState(
						IssueConstant.ISSUE_STATUS_TRANSFER, currentPage, pageSize);

				if (lotteryIssueList != null && lotteryIssueList.size() > 0) {
					int machineNum = Integer.valueOf(ParameterUtils
							.getParameterValue("TASK_COMPLETE_PARTNER_USER_STATISTICS"));

					for (LotteryIssue lotteryIssue : lotteryIssueList) {
						String lotteryId = lotteryIssue.getLotteryId();
						String issueNo = lotteryIssue.getIssueNo();

						List<LotteryTaskComplete> partnerTaskCompleteList = lotteryIssueService
								.getLotteryTaskCompleteByType(lotteryId, issueNo,
										IssueConstant.TASK_COMPLETE_PARTNER_USER_STATISTICS);

						if (partnerTaskCompleteList == null || partnerTaskCompleteList.size() != machineNum) {
							ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
							ActivemqProducer activemqProducer = ctx.getBean("activemqProducer", ActivemqProducer.class);

							ActivemqSendObject sendObject = new ActivemqSendObject();
							sendObject.setLotteryId(lotteryId);
							sendObject.setIssueNo(issueNo);
							activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_PARTNERANDUSER_SALCOUNT_METHODID);
							ScanLog.scan.info("scan partnerStatistics put ACTIVEMQ queue,lotteryId=%s,issueNo=%s",
									lotteryId, issueNo);
						}
					}
				} else {
					break;
				}
				currentPage++;
			}
		} catch (Exception e) {
			ScanLog.scan.error("触发渠道商、用户销量统计发生异常", e);
		}
		ScanLog.scan.info("--------触发渠道商、用户销量统计结束------------");
	}
}
