package com.cqfc.lottery.schedule.issuesport;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.lottery.service.ILotteryIssueService;
import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.protocol.lotteryissue.MatchCompetive;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.ParameterUtils;
import com.cqfc.util.SportIssueConstant;
import com.cqfc.util.SwitchUtil;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.ScanLog;

/**
 * 触发竞足竞篮北单 算奖 操作 (job条件：1.赛事状态为已开奖 2.扫描任务完成表没有当前赛事算奖完成记录 并且 赛事最后修改时间超过10分钟)
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class CalSportPrizeScanJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0 0/2 * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------触发竞足竞篮北单算奖操作扫描开始------------");
		try {
			int currentPage = 1;
			int pageSize = 2000;
			while (true) {
				List<MatchCompetive> matchCompetiveList = lotteryIssueService.getMatchCompetiveListForCalPrize(
						currentPage, pageSize);

				ScanLog.scan.debug("sport match cal prize,matchSize=%d", matchCompetiveList.size());
				if (matchCompetiveList != null && matchCompetiveList.size() > 0) {
					int machineNum = Integer.valueOf(ParameterUtils.getParameterValue("TASK_COMPLETE_SPORT_CAL"));

					for (MatchCompetive matchCompetive : matchCompetiveList) {
						String issueNo = matchCompetive.getWareIssue();
						String transferId = matchCompetive.getTransferId(); // 竞足竞篮北单ID相当于数字彩的彩种ID
						int matchType = matchCompetive.getMatchType();

						String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
						String lastUpdateTime = matchCompetive.getLastUpdateTime();
						currentTime = DateUtil.addDateMinut(currentTime, "MINUTE", -5);

						List<LotteryTaskComplete> taskCompleteList = lotteryIssueService.getLotteryTaskCompleteByType(
								transferId, issueNo, IssueConstant.TASK_COMPLETE_HASCAL_WAITAUDIT);

						if ((taskCompleteList == null || taskCompleteList.size() != machineNum)
								&& DateUtil.compareDateTime(currentTime, lastUpdateTime) < 0) {
							// 触发算奖前,先修改赛事状态为算奖中
							int caling = lotteryIssueService.updateMatchCompetiveStatus(transferId,
									SportIssueConstant.CompetiveMatchStatus.MATCH_IN_CALING.getValue());
							if (caling > 0) {
								ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
								ActivemqProducer activemqProducer = ctx.getBean("activemqProducer",
										ActivemqProducer.class);

								ActivemqSendObject sendObject = new ActivemqSendObject();

								String lotteryId = "";
								if (matchType == SportIssueConstant.CompetitionMatchType.BEIDAN.getValue()
										|| matchType == SportIssueConstant.CompetitionMatchType.BEIDAN_SFGG.getValue()) {
									lotteryId = IssueConstant.CAL_PRIZE_BD_LOTTERYID;
								} else if (matchType == SportIssueConstant.CompetitionMatchType.FOOTBALL.getValue()
										|| matchType == SportIssueConstant.CompetitionMatchType.BASKETBALL.getValue()) {
									lotteryId = IssueConstant.CAL_PRIZE_SPORT_LOTTERYID;
								}

								sendObject.setLotteryId(lotteryId);
								sendObject.setIssueNo(issueNo);
								sendObject.setTransferId(transferId);
								activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_CALPRIZESCAN_METHODID);
								ScanLog.scan.info("触发竞足竞篮北单算奖操作,放入ACTIVEMQ队列,transferId=%s,issueNo=%s", transferId,
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
			ScanLog.scan.error("触发竞足竞篮北单算奖操作扫描发生异常", e);
		}
		ScanLog.scan.info("--------触发竞足竞篮北单算奖操作扫描 结束------------");
	}
}
