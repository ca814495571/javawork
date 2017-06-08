package com.cqfc.lottery.schedule.issuestate;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.lottery.service.ILotteryIssueService;
import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.SwitchUtil;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.LotteryIssueConstant;
import com.jami.util.ScanLog;

/**
 * 触发统计合作商中奖信息
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class PartnerWinningCalJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0 0/2 * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------触发统计合作商中奖信息开始------------");
		try {
			int currentPage = 1;
			int pageSize = 2000;
			ConcurrentMap<String, Boolean> mqMap = new ConcurrentHashMap<String, Boolean>();
			while (true) {
				List<LotteryTaskComplete> taskCompleteList = lotteryIssueService.getLotteryTaskCompleteByStatus(
						IssueConstant.TASK_COMPLETE_PARTNER_WINNINGCAL,
						LotteryIssueConstant.TaskStatus.DEALING.getValue(), currentPage, pageSize);

				if (taskCompleteList != null && taskCompleteList.size() > 0) {
					for (LotteryTaskComplete taskComplete : taskCompleteList) {
						String lotteryId = taskComplete.getLotteryId();
						String issueNo = taskComplete.getIssueNo();

						String mapKey = lotteryId + "#" + issueNo;
						if (mqMap.containsKey(mapKey)) {
							continue;
						}
						String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
						String lastUpdateTime = DateUtil.addDateMinut(taskComplete.getLastUpdateTime(), "MINUTE", 1);
						if (DateUtil.compareDateTime(lastUpdateTime, currentTime) > 0) {
							ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
							ActivemqProducer activemqProducer = ctx.getBean("activemqProducer", ActivemqProducer.class);

							ActivemqSendObject sendObject = new ActivemqSendObject();
							sendObject.setLotteryId(lotteryId);
							sendObject.setIssueNo(issueNo);
							activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_PARTNER_CHANGEPRIZE_METHODID);
							mqMap.put(mapKey, true);
							ScanLog.scan.info("scan partnerWinningCal put ACTIVEMQ queue,lotteryId=%s,issueNo=%s",
									lotteryId, issueNo);
						}
					}
				} else {
					break;
				}
				currentPage++;
			}
		} catch (Exception e) {
			ScanLog.scan.error("触发统计合作商中奖信息发生异常", e);
		}
		ScanLog.scan.info("--------触发统计合作商中奖信息结束------------");
	}
}
