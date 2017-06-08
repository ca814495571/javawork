package com.cqfc.lottery.schedule.issuestate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.common.zookeeper.ServerCache;
import com.cqfc.lottery.service.ILotteryIssueService;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.ParameterUtils;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.util.SwitchUtil;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.ScanLog;

/**
 * 期号状态扭转：已算奖审核中-->算奖已审核
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class IssueHasCalAuditJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0/30 * * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------stateTask8(已算奖审核中-->算奖已审核)开始------------");
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
						List<LotteryTaskComplete> taskCompleteList = lotteryIssueService.getLotteryTaskCompleteByType(
								lotteryId, issueNo, IssueConstant.TASK_COMPLETE_CAN_SEND);

						if (null != taskCompleteList && taskCompleteList.size() == machineNum) {
							int issueId = lotteryIssue.getIssueId();

							boolean taskFlag = true;
							List<String> setNoList = ServerCache.getSetNos(ConstantsUtil.MODULENAME_PARTNER_ORDER,
									"partnerIssueSaleCount");
							ScanLog.scan.info("partnerOrder setNoList=" + setNoList.toString() + "setNoList.size="
									+ setNoList.size());
							if (null != setNoList && setNoList.size() > 0) {
								for (String setNo : setNoList) {
									LotteryTaskComplete lotteryTaskComplete = new LotteryTaskComplete();
									lotteryTaskComplete.setLotteryId(lotteryId);
									lotteryTaskComplete.setIssueNo(issueNo);
									lotteryTaskComplete.setTaskType(IssueConstant.TASK_COMPLETE_PARTNER_WINNINGCAL);
									lotteryTaskComplete.setSetNo(setNo);
									int isTaskSuccess = lotteryIssueService
											.createLotteryTaskComplete(lotteryTaskComplete);
									ScanLog.scan.info("触发统计合作商中奖信息,写入任务记录,lotteryId=%s,issueNo=%s,returnValue=%d",
											lotteryId, issueNo, isTaskSuccess);
									if (isTaskSuccess <= 0
											&& isTaskSuccess != ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST) {
										taskFlag = false;
										break;
									}
								}

								if (taskFlag) {
									int isSuccess = lotteryIssueService.updateLotteryIssueState(issueId,
											IssueConstant.ISSUE_STATUS_CAN_SEND);
									ScanLog.scan.info(
											"stateTask8,期状态扭转：已算奖审核中-->算奖已审核,lotteryId=%s,issueNo=%s,returnValue=%d",
											lotteryId, issueNo, isSuccess);
									if (isSuccess > 0) {
										ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
										ActivemqProducer activemqProducer = ctx.getBean("activemqProducer",
												ActivemqProducer.class);
										ActivemqSendObject sendObject = new ActivemqSendObject();
										sendObject.setLotteryId(lotteryId);
										sendObject.setIssueNo(issueNo);
										activemqProducer.send(sendObject,
												ActivemqMethodUtil.MQ_PARTNER_CHANGEPRIZE_METHODID);
										ScanLog.scan.info(
												"stateTask8,触发统计合作商中奖信息操作,放入ACTIVEMQ队列,lotteryId=%s,issueNo=%s",
												lotteryId, issueNo);
									}
								}
							}
						}
					}
				} else {
					break;
				}
				currentPage++;
			}
		} catch (Exception e) {
			ScanLog.scan.error("stateTask8(已算奖审核中-->算奖已审核)发生异常", e);
		}
		ScanLog.scan.info("--------stateTask8(已算奖审核中-->算奖已审核)结束------------");
	}
}
