package com.cqfc.partner.order.task;

import java.util.concurrent.ConcurrentMap;

import org.springframework.context.ApplicationContext;

import com.cqfc.partner.order.service.IPartnerOrderService;
import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.ParameterUtils;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.CountLog;

/**
 * @author liwh
 */
public class JmsTask implements Runnable {

	private int type;
	private String lotteryId;
	private String issueNo;
	private ConcurrentMap<String, Boolean> statisticsThreadMap;

	public JmsTask(int type, String lotteryId, String issueNo, ConcurrentMap<String, Boolean> statisticsThreadMap) {
		this.type = type;
		this.lotteryId = lotteryId;
		this.issueNo = issueNo;
		this.statisticsThreadMap = statisticsThreadMap;
	}

	@Override
	public void run() {
		try {
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			IPartnerOrderService partnerOrderService = ctx.getBean("partnerOrderServiceImpl",
					IPartnerOrderService.class);
			Thread.sleep(150000);
			int flag = type == 1 ? partnerOrderService.partnerIssueSaleCount(lotteryId, issueNo, true)
					: (type == 2 ? partnerOrderService.partnerIssueRewardCount(lotteryId, issueNo) : 0);

			CountLog.count.debug("partner order count,type=%d,returnValue=%d", type, flag);
			if (flag == 1) {
				// 销量统计完成
				LotteryTaskComplete taskComplete = new LotteryTaskComplete();
				taskComplete.setLotteryId(lotteryId);
				taskComplete.setIssueNo(issueNo);
				int taskType = type == 1 ? IssueConstant.TASK_COMPLETE_PARTNER_USER_STATISTICS
						: (type == 2 ? IssueConstant.TASK_COMPLETE_PARTNER_WINNINGCAL : 0);
				taskComplete.setTaskType(taskType);
				taskComplete.setSetNo(ParameterUtils.getParameterValue("setNo"));

				ActivemqProducer activemqProducer = ctx.getBean("activemqProducer", ActivemqProducer.class);
				ActivemqSendObject sendObject = new ActivemqSendObject();
				sendObject.setLotteryId(lotteryId);
				sendObject.setIssueNo(issueNo);
				sendObject.setObj(taskComplete);
				activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_TASKCOMPLETE_METHODID);
				CountLog.count.debug("partner count send mq complete,taskType=%d,lotteryId=%s,issueNo=%s", taskType,
						lotteryId, issueNo);
			}
		} catch (Exception e) {
			CountLog.count.error("partnerOrder统计数据发生异常,lotteryId=" + lotteryId + ",issueNo=" + issueNo, e);
		} finally {
			String threadKey = type + "#" + lotteryId + "#" + issueNo;
			statisticsThreadMap.remove(threadKey);
		}
	}
}
