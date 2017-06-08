package com.cqfc.common.zookeeper.watcher;


/**
 * 实现了该接口，ZooKeeperManager 会在KeeperState.Expired事件
 * 重新注册Watcher时，一并调用WatcherOpeartion接口的firstInvoker()方法;
 * @author HowKeyond
 *
 */
public interface WatcherOpeartion {

	public void firstInvoker();
}
