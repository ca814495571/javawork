package com.cqfc.BusinessContoller.Service.impl;

import com.cqfc.BusinessContoller.factory.ThreadPoolFactory;
import com.cqfc.BusinessContoller.Service.QueueService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;


@Service
public class QueueServiceImpl implements QueueService {

    private static ThreadPoolExecutor threadPoolExecutor = ThreadPoolFactory.getTaskPoolExecutor();

    @Override
    public void submit(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

}
