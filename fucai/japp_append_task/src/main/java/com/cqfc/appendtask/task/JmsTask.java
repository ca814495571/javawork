package com.cqfc.appendtask.task;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.cqfc.util.AppendTaskConstant;
import com.cqfc.util.TaskDbGenerator;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.ScanLog;

/**
 * @author liwh
 */
public class JmsTask implements Runnable {
	private String lotteryId;
	private String issueNo;
	private ConcurrentMap<String, Boolean> appendThreadMap;

	public JmsTask(String lotteryId, String issueNo, ConcurrentMap<String, Boolean> appendThreadMap) {
		this.lotteryId = lotteryId;
		this.issueNo = issueNo;
		this.appendThreadMap = appendThreadMap;
	}

	@Override
	public void run() {
		try {
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			ThreadPoolTaskExecutor threadPoolTaskExecutor = ctx.getBean("threadPoolTaskExecutor",
					ThreadPoolTaskExecutor.class);

			CountDownLatch latch = new CountDownLatch(AppendTaskConstant.DATASOURCE_NUM);
			for (int i = 0; i < AppendTaskConstant.DATASOURCE_NUM; i++) {
				String dbName = TaskDbGenerator.getDbName(i);
				threadPoolTaskExecutor.submit(new QueueTask(latch, dbName, lotteryId, issueNo));
			}
			ScanLog.scan.info("appendDetail createOrder,lotteryId=%s,issueNo=%s(start)", lotteryId, issueNo);
			latch.await();
			ScanLog.scan.info("appendDetail createOrder,lotteryId=%s,issueNo=%s(finish)", lotteryId, issueNo);
		} catch (Exception e) {
			ScanLog.scan.error("处理追号任务创建订单扫描发生错误,lotteryId=" + lotteryId + ",issueNo=" + issueNo, e);
		} finally {
			String threadKey = lotteryId + "#" + issueNo;
			appendThreadMap.remove(threadKey);
		}
	}

}
