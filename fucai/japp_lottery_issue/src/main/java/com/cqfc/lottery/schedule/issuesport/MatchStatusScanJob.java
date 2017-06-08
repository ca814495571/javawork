package com.cqfc.lottery.schedule.issuesport;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.lottery.service.ILotteryIssueService;
import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.protocol.lotteryissue.MatchCompetive;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.ParameterUtils;
import com.cqfc.util.SportIssueConstant;
import com.cqfc.util.SwitchUtil;
import com.jami.util.ScanLog;

/**
 * 触发赛事状态变更为‘算奖已完成’操作 (job条件：1.赛事状态为算奖中 2.扫描任务完成表有当前赛事算奖完成记录,则赛事状态修改成算奖已完成)
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class MatchStatusScanJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0 0/5 * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------触发竞足竞篮北单算奖完成状态修改扫描开始------------");
		try {
			int currentPage = 1;
			int pageSize = 2000;
			while (true) {
				List<MatchCompetive> matchCompetiveList = lotteryIssueService.getMatchCompetiveListForCalPrize(
						currentPage, pageSize);

				if (matchCompetiveList != null && matchCompetiveList.size() > 0) {
					int machineNum = Integer.valueOf(ParameterUtils.getParameterValue("TASK_COMPLETE_SPORT_CAL"));

					for (MatchCompetive matchCompetive : matchCompetiveList) {
						String issueNo = matchCompetive.getWareIssue();
						String transferId = matchCompetive.getTransferId();

						List<LotteryTaskComplete> taskCompleteList = lotteryIssueService.getLotteryTaskCompleteByType(
								transferId, issueNo, IssueConstant.TASK_COMPLETE_HASCAL_WAITAUDIT);

						if (taskCompleteList.size() == machineNum) {
							// 算奖任务完成表中有该赛事完成记录,则将赛事状态修改成赛事已算奖
							int updateValue = lotteryIssueService.updateMatchCompetiveStatus(transferId,
									SportIssueConstant.CompetiveMatchStatus.MATCH_HAS_CAL.getValue());
							ScanLog.scan.debug("sport match status update,wareIssue=%s,transferId=%s,updateValue=%d",
									issueNo, transferId, updateValue);
						}
					}
				} else {
					break;
				}
				currentPage++;
			}
		} catch (Exception e) {
			ScanLog.scan.error("触发竞足竞篮北单算奖完成状态修改扫描发生异常", e);
		}
		ScanLog.scan.info("--------触发竞足竞篮北单算奖完成状态修改扫描 结束------------");
	}
}
