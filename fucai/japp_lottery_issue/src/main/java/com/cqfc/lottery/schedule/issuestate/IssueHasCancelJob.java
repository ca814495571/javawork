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
 * 期号状态扭转：已出票-->已撤单 (job条件：1.查询期状态为已出票列表 2.查询扫描任务完成表撤单类型是否有记录
 * 3.扫描任务表有记录,修改期状态为已撤单 4.期状态修改成功,触发扫描未同步订单、触发销量统计)
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class IssueHasCancelJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0/30 * * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------stateTask4(已出票-->已撤单)开始------------");
		try {
			int currentPage = 1;
			int pageSize = 2000;
			while (true) {
				List<LotteryIssue> lotteryIssueList = lotteryIssueService.getLotteryIssueListByState(
						IssueConstant.ISSUE_STATUS_PRINT, currentPage, pageSize);

				if (lotteryIssueList != null && lotteryIssueList.size() > 0) {

					int machineNum = Integer.valueOf(ParameterUtils.getParameterValue("TASK_COMPLETE_CANCEL"));

					for (LotteryIssue lotteryIssue : lotteryIssueList) {
						String lotteryId = lotteryIssue.getLotteryId();
						String issueNo = lotteryIssue.getIssueNo();
						List<LotteryTaskComplete> taskCompleteList = lotteryIssueService.getLotteryTaskCompleteByType(
								lotteryId, issueNo, IssueConstant.TASK_COMPLETE_CANCEL);
						if (taskCompleteList != null && taskCompleteList.size() == machineNum) {
							int issueId = lotteryIssue.getIssueId();
							int isSuccess = lotteryIssueService.updateLotteryIssueState(issueId,
									IssueConstant.ISSUE_STATUS_CANCEL);
							ScanLog.scan.info("stateTask4,期状态扭转：已出票-->已撤单,lotteryId=%s,issueNo=%s,returnValue=%d",
									lotteryId, issueNo, isSuccess);
							if (isSuccess > 0) {
								ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
								ActivemqProducer activemqProducer = ctx.getBean("activemqProducer",
										ActivemqProducer.class);

								ActivemqSendObject sendObject = new ActivemqSendObject();
								sendObject.setLotteryId(lotteryId);
								sendObject.setIssueNo(issueNo);
								activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_ORDERSYNC_METHODID);
								ScanLog.scan.info("stateTask4,触发未同步订单扫描操作,放入ACTIVEMQ队列,lotteryId=%s,issueNo=%s",
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
			ScanLog.scan.error("stateTask4(已出票-->已撤单)发生异常", e);
		}
		ScanLog.scan.info("--------stateTask4(已出票-->已撤单)结束------------");
	}
}
