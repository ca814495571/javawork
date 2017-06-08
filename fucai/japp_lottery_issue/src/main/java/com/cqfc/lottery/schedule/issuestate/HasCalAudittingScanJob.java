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
import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.ParameterUtils;
import com.cqfc.util.SwitchUtil;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.ScanLog;

/**
 * 触发 将算奖结果更新到订单表 操作 (job条件： 1.期号状态为已算奖审核中 2.扫描任务完成表没有完成算奖已审核记录)
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class HasCalAudittingScanJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0 0/1 * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------更新算奖结果到订单表操作开始------------");
		try {
			int currentPage = 1;
			int pageSize = 2000;
			while (true) {
				List<LotteryIssue> lotteryIssueList = lotteryIssueService.getLotteryIssueListByState(
						IssueConstant.ISSUE_STATUS_AUDITTING, currentPage, pageSize);

				if (lotteryIssueList != null && lotteryIssueList.size() > 0) {

					int machineNum = Integer.valueOf(ParameterUtils.getParameterValue("TASK_COMPLETE_CAN_SEND"));

					for (LotteryIssue lotteryIssue : lotteryIssueList) {
						String lotteryId = lotteryIssue.getLotteryId();
						String issueNo = lotteryIssue.getIssueNo();
						int issueId = lotteryIssue.getIssueId();

						String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
						String lastUpdateTime = lotteryIssue.getLastUpdateTime();
						currentTime = DateUtil.addDateMinut(currentTime, "MINUTE", -5);

						List<LotteryTaskComplete> taskCompleteList = lotteryIssueService.getLotteryTaskCompleteByType(
								lotteryId, issueNo, IssueConstant.TASK_COMPLETE_CAN_SEND);

						if ((taskCompleteList == null || taskCompleteList.size() != machineNum)
								&& DateUtil.compareDateTime(currentTime, lastUpdateTime) <= 0) {
							// 触发将算奖结果更新到订单表前,先修改期号状态为已算奖审核中
							int caling = lotteryIssueService.updateLotteryIssueState(issueId,
									IssueConstant.ISSUE_STATUS_AUDITTING);
							if (caling > 0) {
								ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
								ActivemqProducer activemqProducer = ctx.getBean("activemqProducer",
										ActivemqProducer.class);

								ActivemqSendObject sendObject = new ActivemqSendObject();
								sendObject.setLotteryId(lotteryId);
								sendObject.setIssueNo(issueNo);
								activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_CALAFTERUPDATEORDER_METHODID);
								ScanLog.scan.info("触发将算奖结果更新到订单表操作,放入ACTIVEMQ队列,lotteryId=%s,issueNo=%s", lotteryId,
										issueNo);
							}
						}
					}
				} else {
					break;
				}
				currentPage++;
			}
		} catch (Exception e) {
			ScanLog.scan.error("更新算奖结果到订单表操作发生异常", e);
		}
		ScanLog.scan.info("--------更新算奖结果到订单表操作结束------------");
	}
}
