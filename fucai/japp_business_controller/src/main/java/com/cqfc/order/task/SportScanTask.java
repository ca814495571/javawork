package com.cqfc.order.task;

import java.util.concurrent.CountDownLatch;

import com.jami.util.ScanLog;

/**
 * 竞技彩扫描
 * 
 * @author liwh
 */
public class SportScanTask implements Runnable {

	private int type;
	private String dbName;
	private CountDownLatch latch;

	public SportScanTask(CountDownLatch latch, int type, String dbName) {
		this.latch = latch;
		this.type = type;
		this.dbName = dbName;
	}

	@Override
	public void run() {
		try {
			ScanLog.scan.debug("SPORT SCAN START,type=%d,dbName=%s", type, dbName);
			SportOrderScanTask.taskProcess(dbName, type);
			ScanLog.scan.debug("SPORT SCAN FINISHED,type=%d,dbName=%s", type, dbName);
		} catch (Exception e) {
			ScanLog.scan.error("SPORT SCAN 订单扫描发生异常,dbName=" + dbName + ",type=" + type, e);
		} finally {
			latch.countDown();
		}
	}

}
