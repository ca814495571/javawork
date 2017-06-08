package com.cqfc.lottery.schedule.issuestate;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.lottery.service.ILotteryIssueService;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.SwitchUtil;
import com.jami.util.ScanLog;

/**
 * 期号状态扭转：销售中-->销售截止
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class IssueEndSellJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0/30 * * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------stateTask2(销售中-->销售截止)开始------------");
		try {
			int currentPage = 1;
			int pageSize = 2000;
			while (true) {
				List<LotteryIssue> lotteryIssueList = lotteryIssueService.getLotteryIssueListByState(
						IssueConstant.ISSUE_STATUS_SELLING, currentPage, pageSize);

				if (lotteryIssueList != null && lotteryIssueList.size() > 0) {
					for (LotteryIssue lotteryIssue : lotteryIssueList) {
						String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
						String compoundEndTime = lotteryIssue.getCompoundEndTime();
						if (DateUtil.compareDateTime(compoundEndTime, currentTime) >= 0) {
							int issueId = lotteryIssue.getIssueId();
							int isSuccess = lotteryIssueService.updateLotteryIssueState(issueId,
									IssueConstant.ISSUE_STATUS_END_SELL);
							String lotteryId = lotteryIssue.getLotteryId();
							String issueNo = lotteryIssue.getIssueNo();
							ScanLog.scan.info("stateTask2,期状态扭转：销售中-->销售截止,lotteryId=%s,issueNo=%s,returnValue=%d",
									lotteryId, issueNo, isSuccess);
						}
					}
				} else {
					break;
				}
				currentPage++;
			}
		} catch (Exception e) {
			ScanLog.scan.error("stateTask2(销售中-->销售截止)发生异常", e);
		}
		ScanLog.scan.info("--------stateTask2(销售中-->销售截止)结束------------");
	}
}
