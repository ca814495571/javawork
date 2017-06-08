package com.cqfc.order.task;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import com.jami.util.ScanLog;

/**
 * @author liwh
 */
public class QueueTask implements Runnable {

	private CountDownLatch latch;
	private int type;
	private String dbName;
	private String lotteryId;
	private String issueNo;
	private AtomicBoolean atomicBoolean;

	public QueueTask(CountDownLatch latch, int type, String dbName, String lotteryId, String issueNo,
			AtomicBoolean atomicBoolean) {
		this.latch = latch;
		this.type = type;
		this.dbName = dbName;
		this.lotteryId = lotteryId;
		this.issueNo = issueNo;
		this.atomicBoolean = atomicBoolean;
	}

	@Override
	public void run() {
		try {
			ScanLog.scan.debug("扫描线程START,type=%d,lotteryId=%s,issueNo=%s,dbName=%s", type, lotteryId, issueNo, dbName);
			OrderStatusTask.taskProcess(dbName, type, lotteryId, issueNo, atomicBoolean);
			ScanLog.scan.debug("扫描线程FINISHED,type=%d,lotteryId=%s,issueNo=%s,dbName=%s", type, lotteryId, issueNo,
					dbName);
		} catch (Exception e) {
			ScanLog.scan.error("issue_scan期扫描发生异常,dbName=" + dbName + ",type=" + type + ",lotteryId=" + lotteryId
					+ ",issueNo=" + issueNo, e);
		} finally {
			latch.countDown();
		}
	}

}
