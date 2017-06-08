package com.cqfc.statistics;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author: giantspider@126.com
 */

@EnableScheduling

public class StatisticsScheduler {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("/spring.xml", StatisticsScheduler.class);
    }

}
