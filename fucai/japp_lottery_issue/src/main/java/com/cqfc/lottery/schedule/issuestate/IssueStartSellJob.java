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
 * 期号状态扭转：未销售-->销售中
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class IssueStartSellJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0/30 * * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------stateTask1(未销售-->销售中)开始------------");
		try {
			int currentPage = 1;
			int pageSize = 2000;
			while (true) {
				List<LotteryIssue> lotteryIssueList = lotteryIssueService.getLotteryIssueListByState(
						IssueConstant.ISSUE_STATUS_NOT_SELL, currentPage, pageSize);

				if (lotteryIssueList != null && lotteryIssueList.size() > 0) {
					for (LotteryIssue lotteryIssue : lotteryIssueList) {
						String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
						String beginTime = lotteryIssue.getBeginTime();
						String compoundEndTime = lotteryIssue.getCompoundEndTime();

						if (DateUtil.compareDateTime(beginTime, currentTime) >= 0
								&& DateUtil.compareDateTime(currentTime, compoundEndTime) > 0) {
							int issueId = lotteryIssue.getIssueId();
							int isSuccess = lotteryIssueService.updateLotteryIssueState(issueId,
									IssueConstant.ISSUE_STATUS_SELLING);
							String lotteryId = lotteryIssue.getLotteryId();
							String issueNo = lotteryIssue.getIssueNo();
							ScanLog.scan.info("stateTask1,期状态扭转：未销售-->销售中,lotteryId=%s,issueNo=%s,returnValue=%d",
									lotteryId, issueNo, isSuccess);
						} else if (DateUtil.compareDateTime(currentTime, compoundEndTime) < 0) {
							int issueId = lotteryIssue.getIssueId();
							int isSuccess = lotteryIssueService.updateLotteryIssueState(issueId,
									IssueConstant.ISSUE_STATUS_END_SELL);
							ScanLog.scan.info("期号销售已截止,直接由未销售修改成销售截止,lotteryId=%s,issueNo=%s,returnValue=%d",
									lotteryIssue.getLotteryId(), lotteryIssue.getIssueNo(), isSuccess);

						}
					}
				} else {
					break;
				}
				currentPage++;
			}
		} catch (Exception e) {
			ScanLog.scan.error("stateTask1(未销售-->销售中)发生异常,异常信息：", e);
		}
		ScanLog.scan.info("--------stateTask1(未销售-->销售中)结束------------");
	}
}
