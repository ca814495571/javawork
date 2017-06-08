package com.cqfc.order.schedule.sport;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.cqfc.order.task.SportScanTask;
import com.cqfc.order.util.TaskDbGenerator;
import com.cqfc.util.OrderConstant;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.ScanLog;

/**
 * @author liwh
 */
public class SportScanInit {

	public static void putScan(ConcurrentMap<Integer, Boolean> scanInitMap, int type) {
		try {
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			ThreadPoolTaskExecutor scanThreadPoolTaskExecutor = ctx.getBean("scanThreadPoolTaskExecutor",
					ThreadPoolTaskExecutor.class);

			CountDownLatch latch = new CountDownLatch(OrderConstant.DATASOURCE_NUM);
			for (int i = 0; i < OrderConstant.DATASOURCE_NUM; i++) {
				String dbName = TaskDbGenerator.getSportDbName(i);
				scanThreadPoolTaskExecutor.submit(new SportScanTask(latch, type, dbName));
			}
			ScanLog.scan.debug("TYPE=%d(1付款 4同步 6退款 ),SPORT SCAN WAITING!!!", type);
			latch.await();
			ScanLog.scan.debug("TYPE=%d(1付款 4同步 6退款 ),SPORT SCAN FINISHED!!!", type);
		} catch (Exception e) {
			ScanLog.scan.error("business扫描订单发生异常,type=" + type, e);
		} finally {
			scanInitMap.remove(type);
		}
	}
}