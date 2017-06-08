package com.cqfc.common.zookeeper.route;

import java.util.List;

import com.cqfc.common.zookeeper.Server;

/**
 * 循环路由选择服务器
 * @author HowKeyond
 *
 */
public class RoundRobinBalance implements LoadBalance{

	private int round = 0;
	
	@Override
	public Server select(List<Server> serverList) {
		if (serverList.size() >= 1) {
			if(round >= 99){
				round = 0;
			}else{
				round++;
			}
			return serverList.get(round%serverList.size());
		} else {
			return null;
		}
	}
	
}
