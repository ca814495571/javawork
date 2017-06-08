package com.cqfc.common.zookeeper.route;

import java.util.List;

import com.cqfc.common.zookeeper.Server;
/**
 * 路由选择服务器
 * @author HowKeyond
 *
 */
public interface LoadBalance {

	public Server select(List<Server> serverList);
}
