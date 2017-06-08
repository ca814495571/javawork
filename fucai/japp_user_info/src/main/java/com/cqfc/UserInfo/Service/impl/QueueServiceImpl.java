package com.cqfc.UserInfo.Service.impl;

import com.cqfc.UserInfo.Service.QueueService;
import com.cqfc.UserInfo.factory.ThreadPoolFactory;

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
