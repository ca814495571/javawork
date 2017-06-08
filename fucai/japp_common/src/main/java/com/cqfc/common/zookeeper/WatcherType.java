package com.cqfc.common.zookeeper;

/**
 * WatcherType 定义
 * Node 表示监听节点数据的Watcher
 * Exist 表示监听节点是否存在的Watcher 
 * Child 表示监听子节点变化的Watcher
 * @author HowKeyond
 *
 */
public enum WatcherType {

	Node (1),
    Exist (2),
    Child (3);
    
    private final int intValue;     
    
    WatcherType(int intValue) {
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }
}
