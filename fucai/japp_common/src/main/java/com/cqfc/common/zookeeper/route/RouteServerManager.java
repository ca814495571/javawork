package com.cqfc.common.zookeeper.route;

import java.util.List;

import com.cqfc.common.zookeeper.Server;
import com.cqfc.common.zookeeper.ServerCache;

/**
 * 服务器路由管理器
 * 默认构造方法，使用RandomLoadBalance算法
 * @author HowKeyond
 *
 */
public class RouteServerManager {
	
	// 路由算法接口类
	private static RandomLoadBalance loadBalance = new RandomLoadBalance();
	private static RoundRobinBalance robinBalance = new RoundRobinBalance();
	
	private RouteServerManager(){
	}
	

	static Server getServer(String serverKey, int balance){
		List<Server> serverList = ServerCache.getServerList(serverKey);
		if(serverList == null || serverList.size() ==0){
			return null;
		}
		if(balance==1){
			return loadBalance.select(serverList);
		}else{
			return robinBalance.select(serverList);
		}
	}
	
	public static Server getServerRandom(String serverKey){
		return getServer(serverKey, 1);
	}
	
	public static Server getServerRoundRobin(String serverKey){
		return getServer(serverKey, 0);
	}
}
