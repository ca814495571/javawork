package com.cqfc.lottery.jms;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.cqfc.lottery.service.ILotteryIssueService;
import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.SwitchUtil;
import com.jami.util.Log;
import com.jami.util.LotteryIssueConstant;
import com.jami.util.ScanLog;

/**
 * @author liwh
 */
public class ActivemqConsumer implements MessageListener {

	@Resource(name = "lotteryIssueServiceImpl")
	private ILotteryIssueService lotteryIssueService;

	@Override
	public void onMessage(Message message) {
		if (!SwitchUtil.mqConsumerIsOpen()) {
			return;
		}
		ObjectMessage msg = (ObjectMessage) message;
		try {
			String methodId = msg.getStringProperty("methodId");
			if (ActivemqMethodUtil.MQ_TASKCOMPLETE_METHODID.equals(methodId)) {
				ActivemqSendObject obj = (ActivemqSendObject) msg.getObject();

				String lotteryId = obj.getLotteryId();
				String issueNo = obj.getIssueNo();
				LotteryTaskComplete taskComplete = (LotteryTaskComplete) obj.getObj();

				ScanLog.scan.debug("接收ACTIVEMQ队列成功,lotteryId=%s,issueNo=%s,taskType=%d,setNo=%s,", lotteryId, issueNo,
						taskComplete.getTaskType(), taskComplete.getSetNo());

				int isSuccess = 0;
				if (taskComplete.getTaskType() == IssueConstant.TASK_COMPLETE_PARTNER_WINNINGCAL) {
					taskComplete.setStatus(LotteryIssueConstant.TaskStatus.COMPLETE.getValue());
					isSuccess = lotteryIssueService.updateTaskCompleteStatus(taskComplete);
				} else {
					isSuccess = lotteryIssueService.createLotteryTaskComplete(taskComplete);
				}
				ScanLog.scan.debug("创建扫描完成任务完成,lotteryId=%s,issueNo=%s,taskType=%d,setNo=%s,returnValue=%d", lotteryId,
						issueNo, taskComplete.getTaskType(), taskComplete.getSetNo(), isSuccess);
			}
		} catch (Exception e) {
			Log.error("lotteryIssue消息监听发生异常", e);
			ScanLog.scan.error("lotteryIssue消息监听发生异常", e);
		}
	}
}
