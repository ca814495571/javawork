package com.cqfc.partner.order.jms;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RejectedExecutionException;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.cqfc.partner.order.task.JmsTask;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.SwitchUtil;
import com.jami.util.CountLog;

/**
 * @author liwh
 * 
 */
public class ActivemqConsumer implements MessageListener {

	@Resource(name = "jmsThreadPoolTaskExecutor")
	private ThreadPoolTaskExecutor jmsThreadPoolTaskExecutor;

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
			// 触发销量统计接口
			if (ActivemqMethodUtil.MQ_PARTNERANDUSER_SALCOUNT_METHODID.equals(methodId)) {
				type = 1;
			}
			// 触发统计合作商兑奖信息
			if (ActivemqMethodUtil.MQ_PARTNER_CHANGEPRIZE_METHODID.equals(methodId)) {
				type = 2;
			}
			if (type > 0) {
				ActivemqSendObject obj = (ActivemqSendObject) msg.getObject();

				String lotteryId = obj.getLotteryId();
				String issueNo = obj.getIssueNo();

				CountLog.count.debug("receive MQ consumer,type=%d,lotteryId=%s,issueNo=%s", type, lotteryId, issueNo);
				String threadKey = type + "#" + lotteryId + "#" + issueNo;
				if (statisticsThreadMap.size() <= 30
						&& (!statisticsThreadMap.containsKey(threadKey) || !statisticsThreadMap.get(threadKey))) {
					try {
						statisticsThreadMap.put(threadKey, true);
						jmsThreadPoolTaskExecutor.submit(new JmsTask(type, lotteryId, issueNo, statisticsThreadMap));
						CountLog.count.debug("MQ queue put jms threadPool success,type=%d,lotteryId=%s,issueNo=%s",
								type, lotteryId, issueNo);
					} catch (RejectedExecutionException e) {
						statisticsThreadMap.remove(threadKey);
						CountLog.count.error("MQ消息放入线程池时,线程池已满", e);
					} catch (Exception e) {
						statisticsThreadMap.remove(threadKey);
						CountLog.count.error("MQ消息放入线程池发生异常", e);
					}
				}
			}
		} catch (JMSException e) {
			CountLog.count.error("partnerOrder统计数据消息监听发生异常", e);
		} catch (Exception e) {
			CountLog.count.error("partnerOrder统计数据数据发生异常", e);
		}
	}

}
