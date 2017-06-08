package com.cqfc.ticketwinning.jms;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RejectedExecutionException;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.cqfc.ticketwinning.task.JmsTask;
import com.cqfc.ticketwinning.util.JmsUtils;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.SwitchUtil;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.ScanLog;

/**
 * @author liwh
 */
public class ActivemqConsumer implements MessageListener {

	public static ConcurrentMap<String, Boolean> winningThreadMap = new ConcurrentHashMap<String, Boolean>();

	@Override
	public void onMessage(Message message) {
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		ThreadPoolTaskExecutor jmsThreadPoolTaskExecutor = applicationContext.getBean("jmsThreadPoolTaskExecutor",
				ThreadPoolTaskExecutor.class);
		if (!SwitchUtil.mqConsumerIsOpen()) {
			return;
		}
		ObjectMessage msg = (ObjectMessage) message;
		try {
			String methodId = msg.getStringProperty("methodId");
			int type = 0;

			// 触发算奖接口
			if (ActivemqMethodUtil.MQ_CALPRIZESCAN_METHODID.equals(methodId)) {
				type = JmsUtils.OPERATE_CALPRIZE;
			}
			// 触发将算奖结果更新到订单表前,先修改期号状态为已算奖审核中
			if (ActivemqMethodUtil.MQ_CALAFTERUPDATEORDER_METHODID.equals(methodId)) {
				type = JmsUtils.OPERATE_UPDATEORDER;
			}
			// 触发派奖接口
			if (ActivemqMethodUtil.MQ_SENDPRIZE_METHODID.equals(methodId)) {
				type = JmsUtils.OPERATE_SENDPRIZE;
			}

			if (type > 0) {
				ActivemqSendObject obj = (ActivemqSendObject) msg.getObject();

				String lotteryId = obj.getLotteryId();
				String issueNo = obj.getIssueNo();
				String transferId = obj.getTransferId();

				ScanLog.scan.debug("receive MQ consumer,type=%d,lotteryId=%s,issueNo=%s", type, lotteryId, issueNo);
				String threadKey = type + "#" + lotteryId + "#" + issueNo + "#" + transferId;
				if (winningThreadMap.size() < 20
						&& (!winningThreadMap.containsKey(threadKey) || !winningThreadMap.get(threadKey))) {
					try {
						winningThreadMap.put(threadKey, true);
						jmsThreadPoolTaskExecutor.submit(new JmsTask(type, lotteryId, issueNo, transferId,
								winningThreadMap));
						ScanLog.scan.debug("MQ queue put jms threadPool success,type=%d,lotteryId=%s,issueNo=%s", type,
								lotteryId, issueNo);
					} catch (RejectedExecutionException e) {
						winningThreadMap.remove(threadKey);
						ScanLog.scan.error("MQ消息放入线程池时,线程池已满", e);
					} catch (Exception e) {
						winningThreadMap.remove(threadKey);
						ScanLog.scan.error("MQ消息放入线程池发生异常", e);
					}
				}
			}
		} catch (Exception e) {
			ScanLog.scan.error("ticketWinning消息监听发生异常", e);
		}
	}
}
