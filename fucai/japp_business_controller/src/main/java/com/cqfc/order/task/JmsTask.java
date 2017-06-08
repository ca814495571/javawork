package com.cqfc.order.task;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.cqfc.order.util.MQUtil;
import com.cqfc.order.util.ScanUtil;
import com.cqfc.order.util.TaskDbGenerator;
import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.OrderConstant;
import com.cqfc.util.ParameterUtils;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.ScanLog;

/**
 * @author liwh
 */
public class JmsTask implements Runnable {

	private int type;
	private String lotteryId;
	private String issueNo;

	public JmsTask(int type, String lotteryId, String issueNo) {
		this.type = type;
		this.lotteryId = lotteryId;
		this.issueNo = issueNo;
	}

	@Override
	public void run() {
		try {
			orderStatusTask(type, lotteryId, issueNo);
		} catch (Exception e) {
			ScanLog.scan.error("处理扫描任务发生异常,type=" + type + ",lotteryId=" + lotteryId + ",issueNo=" + issueNo, e);
		} finally {
			MQUtil.removeOrderScanMap(type, lotteryId, issueNo);
		}
	}

	private void orderStatusTask(int type, String lotteryId, String issueNo) {
		try {
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			ThreadPoolTaskExecutor scanThreadPoolTaskExecutor = ctx.getBean("scanThreadPoolTaskExecutor",
					ThreadPoolTaskExecutor.class);

			ScanLog.scan.debug("期扫描任务START,准备按库创建线程,type=%d,lotteryId=%s,issueNo=%s", type, lotteryId, issueNo);
			CountDownLatch latch = new CountDownLatch(OrderConstant.DATASOURCE_NUM);
			AtomicBoolean atomicBoolean = new AtomicBoolean(true);
			for (int i = 0; i < OrderConstant.DATASOURCE_NUM; i++) {
				String dbName = TaskDbGenerator.getDbName(i);
				QueueTask queueTask = new QueueTask(latch, type, dbName, lotteryId, issueNo, atomicBoolean);
				scanThreadPoolTaskExecutor.submit(queueTask);
			}
			latch.await();
			ScanLog.scan.debug("期扫描任务FINISHED,type=%d,lotteryId=%s,issueNo=%s", type, lotteryId, issueNo);

			if ((type == OrderConstant.ORDER_CANCEL_CHECK || type == OrderConstant.ORDER_SYNC_CHECK)
					&& atomicBoolean.get()) {
				LotteryTaskComplete taskComplete = new LotteryTaskComplete();
				String setNo = ParameterUtils.getParameterValue("setNo");
				int taskType = type == OrderConstant.ORDER_CANCEL_CHECK ? IssueConstant.TASK_COMPLETE_CANCEL
						: IssueConstant.TASK_COMPLETE_SYNC_ORDER;
				taskComplete.setLotteryId(lotteryId);
				taskComplete.setIssueNo(issueNo);
				taskComplete.setTaskType(taskType);
				taskComplete.setSetNo(setNo);

				ActivemqProducer activemqProducer = ctx.getBean("activemqProducer", ActivemqProducer.class);

				ActivemqSendObject sendObject = new ActivemqSendObject();
				sendObject.setLotteryId(lotteryId);
				sendObject.setIssueNo(issueNo);
				sendObject.setObj(taskComplete);

				ScanLog.scan.debug("期扫描任务完成,发送MQ消息写完成记录,lotteryId=%s,issueNo=%s,taskType=%d,setNo=%s", lotteryId,
						issueNo, taskType, setNo);

				activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_TASKCOMPLETE_METHODID);
			}
		} catch (Exception e) {
			ScanLog.scan.error("期扫描任务发生异常,type=" + type + ",lotteryId=" + lotteryId + ",issueNo=" + issueNo, e);
		} finally {
			if (type == OrderConstant.ORDER_CANCEL_CHECK) {
				String mapKey = ScanUtil.getBreakMapKey(type, lotteryId, issueNo);
				ScanUtil.printScanMap.remove(mapKey);
			}
		}
	}

}
