package com.cqfc.queue.factory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: giantspider@126.com
 */

public class ThreadPoolFactory {

    private static BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<Runnable>(4);

    private static ThreadPoolExecutor taskPoolExecutor = new ThreadPoolExecutor(4, 10, 120, TimeUnit.SECONDS, taskQueue,
            new ThreadPoolExecutor.AbortPolicy());

    private ThreadPoolFactory() {}

    public static ThreadPoolExecutor getTaskPoolExecutor() {
        return taskPoolExecutor;
    }

}
