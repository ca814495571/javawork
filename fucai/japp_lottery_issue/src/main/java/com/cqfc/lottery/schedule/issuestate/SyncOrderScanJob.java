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
 * 触发同步订单操作 (job条件： 1.期号状态为已撤单 2.扫描任务完成表并没有完成同步订单记录)
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class SyncOrderScanJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0/30 * * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------扫描同步订单开始------------");
		try {
			int currentPage = 1;
			int pageSize = 2000;
			while (true) {
				List<LotteryIssue> lotteryIssueList = lotteryIssueService.getLotteryIssueListByState(
						IssueConstant.ISSUE_STATUS_CANCEL, currentPage, pageSize);

				if (lotteryIssueList != null && lotteryIssueList.size() > 0) {
					int machineNum = Integer.valueOf(ParameterUtils.getParameterValue("TASK_COMPLETE_SYNC_ORDER"));

					for (LotteryIssue lotteryIssue : lotteryIssueList) {
						String lotteryId = lotteryIssue.getLotteryId();
						String issueNo = lotteryIssue.getIssueNo();

						List<LotteryTaskComplete> taskCompleteList = lotteryIssueService.getLotteryTaskCompleteByType(
								lotteryId, issueNo, IssueConstant.TASK_COMPLETE_SYNC_ORDER);

						if (taskCompleteList == null || taskCompleteList.size() != machineNum) {
							ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
							ActivemqProducer activemqProducer = ctx.getBean("activemqProducer", ActivemqProducer.class);

							ActivemqSendObject sendObject = new ActivemqSendObject();
							sendObject.setLotteryId(lotteryId);
							sendObject.setIssueNo(issueNo);
							activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_ORDERSYNC_METHODID);
							ScanLog.scan.info("scan sycnOrder put ACTIVEMQ queue,lotteryId=%s,issueNo=%s", lotteryId,
									issueNo);
						}
					}
				} else {
					break;
				}
				currentPage++;
			}
		} catch (Exception e) {
			ScanLog.scan.error("扫描同步订单发生异常", e);
		}
		ScanLog.scan.info("--------扫描同步订单结束------------");
	}
}
