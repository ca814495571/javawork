package com.cqfc.order.task;

import java.util.concurrent.CountDownLatch;

import com.jami.util.ScanLog;

/**
 * @author liwh
 */
public class ScanTask implements Runnable {

	private int type;
	private String dbName;
	private CountDownLatch latch;

	public ScanTask(CountDownLatch latch, int type, String dbName) {
		this.latch = latch;
		this.type = type;
		this.dbName = dbName;
	}

	@Override
	public void run() {
		try {
			ScanLog.scan.debug("business扫描START,type=%d,dbName=%s", type, dbName);
			OrderScanTask.taskProcess(dbName, type);
			ScanLog.scan.debug("business扫描FINISHED,type=%d,dbName=%s", type, dbName);
		} catch (Exception e) {
			ScanLog.scan.error("business_scan订单扫描发生异常,dbName=" + dbName + ",type=" + type, e);
		} finally {
			latch.countDown();
		}
	}

}
