package com.cqfc.queue.model;

import com.cqfc.queue.model.Message;

/**
 * @author: giantspider@126.com
 */

public class QueueTask implements Runnable {

    private Message message;

    public QueueTask(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " processing msg(id=" + message.getId() + ")");
            Thread.sleep(100);   //可以调整sleep的大小来模拟实际处理需要的时间
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }


}
