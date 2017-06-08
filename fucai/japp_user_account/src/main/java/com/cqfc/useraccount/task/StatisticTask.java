package com.cqfc.useraccount.task;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

import org.springframework.context.ApplicationContext;

import com.cqfc.useraccount.service.IUserAccountService;
import com.cqfc.useraccount.util.DataSourceContextHolder;
import com.cqfc.useraccount.util.DbGenerator;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.Log;

public class StatisticTask implements Runnable {
	private CountDownLatch latch;
	private String dbName;
	private Queue<Map<String, Long>> resultQueue;
	private String date;
	private IUserAccountService userAccountService;
	public StatisticTask(CountDownLatch latch, String dbName, Queue<Map<String, Long>> resultQueue, String date, IUserAccountService userAccountService) {
		this.latch = latch;
		this.dbName = dbName;
		this.resultQueue = resultQueue;
		this.date = date;
		this.userAccountService = userAccountService;
	}
	
	@Override
	public void run() {
		try {
			Log.run.info("StatisticTask, dbName=&s, date=%s", dbName, date);
			DataSourceContextHolder.setDataSourceType(DbGenerator.SLAVE + dbName);
			Map<String, Long> map = userAccountService.statisticRecharge(date);
			resultQueue.add(map);
		} catch (Exception e) {
			Log.run.debug("", e);
		}finally{
			latch.countDown();
		}
	}

}
