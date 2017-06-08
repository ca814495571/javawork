package com.cqfc.lottery.schedule.issuestate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.lottery.service.ILotteryIssueService;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.ParameterUtils;
import com.cqfc.util.SwitchUtil;
import com.jami.util.ScanLog;

/**
 * 期号状态扭转：算奖中-->已算奖待审核
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class IssueCanSendJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0/30 * * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------stateTask7(算奖中-->已算奖待审核)开始------------");
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
						List<LotteryTaskComplete> taskCompleteList = lotteryIssueService.getLotteryTaskCompleteByType(
								lotteryId, issueNo, IssueConstant.TASK_COMPLETE_HASCAL_WAITAUDIT);

						if (null != taskCompleteList && taskCompleteList.size() == machineNum) {
							int issueId = lotteryIssue.getIssueId();
							int isSuccess = lotteryIssueService.updateLotteryIssueState(issueId,
									IssueConstant.ISSUE_STATUS_HASCAL_WAITAUDIT);
							ScanLog.scan.info("stateTask7,期状态扭转：算奖中-->已算奖待审核,lotteryId=%s,issueNo=%s,returnValue=%d",
									lotteryId, issueNo, isSuccess);
						}
					}
				} else {
					break;
				}
				currentPage++;
			}
		} catch (Exception e) {
			ScanLog.scan.error("stateTask7(算奖中-->已算奖待审核)发生异常", e);
		}
		ScanLog.scan.info("--------stateTask7(算奖中-->已算奖待审核)结束------------");
	}
}
