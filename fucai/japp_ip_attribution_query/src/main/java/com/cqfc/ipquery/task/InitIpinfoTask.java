package com.cqfc.ipquery.task;

import java.util.List;

import com.cqfc.ipquery.dao.IpQueryDao;
import com.cqfc.ipquery.datacenter.IpInfoBuffer;

public class InitIpinfoTask implements Runnable {

	private IpQueryDao ipDao;

	public InitIpinfoTask(IpQueryDao ipDao) {
		this.ipDao = ipDao;
	}

	@Override
	public void run() {
		List<Long> allStartIndex = ipDao.getAllStartIndex();
		if (!allStartIndex.isEmpty()) {
			IpInfoBuffer.getInstance().initDatas(allStartIndex);
		}
	}
}
