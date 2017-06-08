package com.cqfc.common.zookeeper.route;

import java.util.List;
import java.util.Random;

import com.cqfc.common.zookeeper.Server;

/**
 * 随机路由选择服务器
 * @author HowKeyond
 *
 */
public class RandomLoadBalance implements LoadBalance{

	@Override
	public Server select(List<Server> serverList) {
		Random random = new Random();
		if (serverList.size() >= 1) {
			return serverList.get(random.nextInt(serverList.size()));
		} else {
			return null;
		}
	}

}
