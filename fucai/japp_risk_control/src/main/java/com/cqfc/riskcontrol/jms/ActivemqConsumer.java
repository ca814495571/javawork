package com.cqfc.riskcontrol.jms;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.riskcontrol.dao.RiskControlDao;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.SwitchUtil;
import com.jami.util.Log;

/**
 * @author HowKeyond
 * 
 */
public class ActivemqConsumer implements MessageListener {
	@Resource(name = "threadPoolTaskExecutor")
	private ScheduledExecutorService threadPoolTaskExecutor;

	@Resource
	private RiskControlDao riskControlDao;
	
	public static ConcurrentMap<String, Boolean> statisticsThreadMap = new ConcurrentHashMap<String, Boolean>();

	@Override
	public void onMessage(Message message) {
		if (!SwitchUtil.mqConsumerIsOpen()) {
			return;
		}
		ObjectMessage msg = (ObjectMessage) message;

		try {
			String methodId = msg.getStringProperty("methodId");
			int type = 0;
			boolean need2wait = false;
			// 触发销量统计接口
			if (ActivemqMethodUtil.MQ_TASKCOMPLETE_METHODID.equals(methodId)) {
				ActivemqSendObject obj = (ActivemqSendObject) msg.getObject();
				LotteryTaskComplete complete = (LotteryTaskComplete) obj.getObj();
				if(IssueConstant.TASK_COMPLETE_PARTNER_USER_STATISTICS == complete.getTaskType()){
					type = JmsTask.DO_COUNT_TICKET;
					need2wait = true;
				}else{
					return;
				}
			} else
			// 触发销量统计接口
			if (ActivemqMethodUtil.MQ_SALECOUNT_COMPLETED_METHODID
					.equals(methodId)) {
				type = JmsTask.DO_COUNT_TICKET;
			}
			// 触发统计算奖信息
			else if (ActivemqMethodUtil.MQ_WINNING_COUNT_COMPLETED_METHODID
					.equals(methodId)) {
				type = JmsTask.DO_COUNT_WINNING;
			} else {
				return;
			}
			ActivemqSendObject obj = (ActivemqSendObject) msg.getObject();

			String lotteryId = obj.getLotteryId();
			String issueNo = obj.getIssueNo();

			String threadKey = JmsTask.getThreadKey(type, lotteryId, issueNo);
			if (!statisticsThreadMap.containsKey(threadKey)
					|| !statisticsThreadMap.get(threadKey)) {
				statisticsThreadMap.put(threadKey, true);
				try {
					threadPoolTaskExecutor.submit(new JmsTask(threadPoolTaskExecutor, riskControlDao, type, lotteryId,
							issueNo, statisticsThreadMap, need2wait));
				} catch (Exception e) {
					statisticsThreadMap.remove(threadKey);
					Log.run.error("too many thread in pool", e);
				}
			}
		} catch (JMSException e) {
			Log.run.error("risk control消息监听发生错误,错误信息：", e);
		} catch (Exception e) {
			Log.run.error("risk control发生错误,错误信息：", e);
		}
	}

}
