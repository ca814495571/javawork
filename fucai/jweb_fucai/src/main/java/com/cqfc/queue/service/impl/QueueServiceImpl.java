package com.cqfc.queue.service.impl;

import com.cqfc.queue.factory.ThreadPoolFactory;
import com.cqfc.queue.model.QueueTask;
import com.cqfc.queue.service.QueueService;
import org.springframework.stereotype.Service;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;


@Service
public class QueueServiceImpl implements QueueService {

    private static ThreadPoolExecutor threadPoolExecutor = ThreadPoolFactory.getTaskPoolExecutor();

    @Override
    public void submit(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

}
