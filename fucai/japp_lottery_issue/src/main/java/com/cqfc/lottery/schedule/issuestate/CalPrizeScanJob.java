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
 * 触发 算奖 操作 (job条件：1.期号状态为算奖中 2.扫描任务完成表没有当前期算奖完成记录 并且 期号最后修改时间超过10分钟)
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class CalPrizeScanJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0 0/2 * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------触发算奖操作扫描开始------------");
		try {
			int currentPage = 1;
			int pageSize = 2000;
			while (true) {
				List<LotteryIssue> lotteryIssueList = lotteryIssueService.getLotteryIssueListByState(
						IssueConstant.ISSUE_STATUS_CALING, currentPage, pageSize);

				if (lotteryIssueList != null && lotteryIssueList.size() > 0) {
					int machineNum = Integer
							.valueOf(ParameterUtils.getParameterValue("TASK_COMPLETE_HASCAL_WAITAUDIT"));

					for (LotteryIssue lotteryIssue : lotteryIssueList) {
						String lotteryId = lotteryIssue.getLotteryId();
						String issueNo = lotteryIssue.getIssueNo();
						int issueId = lotteryIssue.getIssueId();

						String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
						String lastUpdateTime = lotteryIssue.getLastUpdateTime();
						currentTime = DateUtil.addDateMinut(currentTime, "MINUTE", -5);

						List<LotteryTaskComplete> taskCompleteList = lotteryIssueService.getLotteryTaskCompleteByType(
								lotteryId, issueNo, IssueConstant.TASK_COMPLETE_HASCAL_WAITAUDIT);

						if ((taskCompleteList == null || taskCompleteList.size() != machineNum)
								&& DateUtil.compareDateTime(currentTime, lastUpdateTime) < 0) {
							// 触发算奖前,先修改期号状态为算奖中
							int caling = lotteryIssueService.updateLotteryIssueState(issueId,
									IssueConstant.ISSUE_STATUS_CALING);
							if (caling > 0) {
								ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
								ActivemqProducer activemqProducer = ctx.getBean("activemqProducer",
										ActivemqProducer.class);

								ActivemqSendObject sendObject = new ActivemqSendObject();
								sendObject.setLotteryId(lotteryId);
								sendObject.setIssueNo(issueNo);
								activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_CALPRIZESCAN_METHODID);
								ScanLog.scan.info("触发算奖操作,放入ACTIVEMQ队列,lotteryId=%s,issueNo=%s", lotteryId, issueNo);
							}
						}
					}
				} else {
					break;
				}
				currentPage++;
			}
		} catch (Exception e) {
			ScanLog.scan.error("触发算奖操作扫描发生异常,异常信息：", e);
		}
		ScanLog.scan.info("--------触发算奖操作扫描 结束------------");
	}
}
