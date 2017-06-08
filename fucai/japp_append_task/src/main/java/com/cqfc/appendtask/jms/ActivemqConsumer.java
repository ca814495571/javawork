package com.cqfc.appendtask.jms;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RejectedExecutionException;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.cqfc.appendtask.task.JmsTask;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.SwitchUtil;
import com.jami.util.ScanLog;

/**
 * @author liwh
 */
public class ActivemqConsumer implements MessageListener {

	@Resource(name = "jmsThreadPoolTaskExecutor")
	private ThreadPoolTaskExecutor jmsThreadPoolTaskExecutor;

	public static ConcurrentMap<String, Boolean> appendThreadMap = new ConcurrentHashMap<String, Boolean>();

	@Override
	public void onMessage(Message message) {
		if (!SwitchUtil.mqConsumerIsOpen()) {
			return;
		}
		ObjectMessage msg = (ObjectMessage) message;
		try {
			String methodId = msg.getStringProperty("methodId");
			if (ActivemqMethodUtil.MQ_APPENDSCANORDER_METHODID.equals(methodId)) {
				ActivemqSendObject obj = (ActivemqSendObject) msg.getObject();

				String lotteryId = obj.getLotteryId();
				String issueNo = obj.getIssueNo();

				String threadKey = lotteryId + "#" + issueNo;
				ScanLog.scan.debug("receive activeMQ consumer,lotteryId=%s,issueNo=%s", lotteryId, issueNo);
				if (appendThreadMap.size() <= 20
						&& (!appendThreadMap.containsKey(threadKey) || !appendThreadMap.get(threadKey))) {
					try {
						appendThreadMap.put(threadKey, true);
						jmsThreadPoolTaskExecutor.submit(new JmsTask(lotteryId, issueNo, appendThreadMap));
						ScanLog.scan.debug("activeMQ queue put jms threadPool success,lotteryId=%s,issueNo=%s",
								lotteryId, issueNo);
					} catch (RejectedExecutionException e) {
						appendThreadMap.remove(threadKey);
						ScanLog.scan.error("MQ消息放入线程池时,线程池已满", e);
					} catch (Exception e) {
						appendThreadMap.remove(threadKey);
						ScanLog.scan.error("MQ消息放入线程池发生异常", e);
					}
				}
			}
		} catch (JMSException e) {
			ScanLog.scan.error("appendTask消息接收监听发生异常", e);
		}
	}
}
