package com.cqfc.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author: giantspider@126.com
 */

@EnableScheduling
@Service
public class ScheduledTask {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public void reportCurrentTime(String scheduledType, String taskName) {
        System.out.println("[" + scheduledType + "] " + taskName + " got executed at " + dateFormat.format(new Date()));
    }

    @Scheduled(fixedRate = 5000)
    public void task1() {
        reportCurrentTime("fixedRate", "task1");
    }


    @Scheduled(fixedDelay = 6000)
    public void task2() {
        reportCurrentTime("fixedDelay", "task2");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "*/5 * * * * MON-FRI")
    public void task3() {
        reportCurrentTime("cron", "task3");
    }

}
