package com.cqfc.statistics.common;

import java.util.List;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class DataSyncConfig {
	
	
	private List<DataSyncBean> dataSyncBeans;

	private ThreadPoolTaskExecutor threadPool;
	
	public List<DataSyncBean> getDataSyncBeans() {
		return dataSyncBeans;
	}

	public void setDataSyncBeans(List<DataSyncBean> dataSyncBeans) {
		this.dataSyncBeans = dataSyncBeans;
	}

	public ThreadPoolTaskExecutor getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ThreadPoolTaskExecutor threadPool) {
		this.threadPool = threadPool;
	}
	
	
}
