package com.cqfc.order.jms;

import java.util.concurrent.RejectedExecutionException;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.cqfc.order.task.JmsTask;
import com.cqfc.order.util.MQUtil;
import com.cqfc.order.util.ScanUtil;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.OrderConstant;
import com.cqfc.util.SwitchUtil;
import com.jami.util.Log;
import com.jami.util.ScanLog;

/**
 * @author liwh
 * 
 */
public class ActivemqConsumer implements MessageListener {

	@Resource(name = "jmsThreadPoolTaskExecutor")
	private ThreadPoolTaskExecutor jmsThreadPoolTaskExecutor;

	@Override
	public void onMessage(Message message) {
		if (!SwitchUtil.mqConsumerIsOpen()) {
			return;
		}
		ObjectMessage msg = (ObjectMessage) message;

		try {
			String methodId = msg.getStringProperty("methodId");

			int type = 0;

			if (ActivemqMethodUtil.MQ_ORDERPRINT_METHODID.equals(methodId)) {
				type = OrderConstant.ORDER_PRINT_CHECK;
			}
			if (ActivemqMethodUtil.MQ_ORDERCANCEL_METHODID.equals(methodId)) {
				type = OrderConstant.ORDER_CANCEL_CHECK;
			}
			if (ActivemqMethodUtil.MQ_ORDERSYNC_METHODID.equals(methodId)) {
				type = OrderConstant.ORDER_SYNC_CHECK;
			}
			if (type > 0) {
				ActivemqSendObject obj = (ActivemqSendObject) msg.getObject();
				String lotteryId = obj.getLotteryId();
				String issueNo = obj.getIssueNo();
				if (MQUtil.orderScanMap.size() <= 15 && !MQUtil.isOrderScanMapExist(type, lotteryId, issueNo)) {
					try {
						jmsThreadPoolTaskExecutor.submit(new JmsTask(type, lotteryId, issueNo));
						if (type == OrderConstant.ORDER_PRINT_CHECK) {
							boolean mapValue = ScanUtil.isPrintScanMapExist(type, lotteryId, issueNo);
							ScanLog.scan.debug("出票扫描放入MAP中,lotteryId=%s,issueNo=%s,returnValue=%b", lotteryId, issueNo,
									mapValue);
						} else if (type == OrderConstant.ORDER_CANCEL_CHECK) {
							String mapKey = ScanUtil.getBreakMapKey(type, lotteryId, issueNo);
							ScanUtil.printScanMap.put(mapKey, true);
							ScanLog.scan.debug("撤单扫描将出票扫描中的MAP修改为true,lotteryId=%s,issueNo=%s", lotteryId, issueNo);
						}
						ScanLog.scan.debug("MQ消息放入JMS线程池成功,type=%d,lotteryId=%s,issueNo=%s", type, lotteryId, issueNo);
					} catch (RejectedExecutionException e) {
						MQUtil.removeOrderScanMap(type, lotteryId, issueNo);
						ScanLog.scan.error("MQ消息放入JMS线程池时,线程池已满,type=" + type + "lotteryId=" + lotteryId + ",issueNo="
								+ issueNo, e);
					} catch (Exception e) {
						MQUtil.removeOrderScanMap(type, lotteryId, issueNo);
						ScanLog.scan.error("MQ消息放入JMS线程池发生异常,type=" + type + "lotteryId=" + lotteryId + ",issueNo="
								+ issueNo, e);
					}
				} else {
					ScanLog.scan.debug("MAP已满或已存在,MQ消息丢弃,type=%d,lotteryId=%s,issueNo=%s,mapSize=%d", type, lotteryId,
							issueNo, MQUtil.orderScanMap.size());
				}
			}
		} catch (JMSException e) {
			Log.error("business JMS消息监听发生异常", e);
			ScanLog.scan.error("business JMS消息监听发生异常", e);
		} catch (Exception e) {
			Log.error("business扫描订单数据发生异常", e);
			ScanLog.scan.error("business扫描订单数据发生异常", e);
		}
	}

}
