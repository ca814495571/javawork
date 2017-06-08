package com.cqfc.samplemodule.client;

import net.rubyeye.xmemcached.exception.MemcachedException;
import org.springframework.beans.factory.annotation.Autowired;
import net.rubyeye.xmemcached.MemcachedClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

/**
 * @author: giantspider@126.com
 */

@Component
public class MemcachedDemo {

    @Autowired
    private MemcachedClient memcachedClient;
    private static ApplicationContext applicationContext = null;

    public void demo() throws InterruptedException, MemcachedException, TimeoutException {
        memcachedClient.set("hello", 0, "Hello, xmemcached!");
        String value = (String)memcachedClient.get("hello");
        System.out.println("hello=" + value);
        System.out.println("delete key: hello");
        memcachedClient.delete("hello");
        value = (String) memcachedClient.get("hello");
        System.out.println("hello=" + value);
    }

    public static void main(String[] args) throws InterruptedException, MemcachedException, TimeoutException {
        applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        MemcachedDemo memcachedDemo = (MemcachedDemo)applicationContext.getBean("memcachedDemo");
        memcachedDemo.demo();
    }

}
