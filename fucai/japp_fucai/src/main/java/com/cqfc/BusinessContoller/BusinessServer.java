package com.cqfc.BusinessContoller;

import javax.annotation.Resource;

import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.cqfc.BusinessContoller.protocol.BusinessContollerService;

@Component
public class BusinessServer {
	@Resource
    BusinessContollerService.Iface businessService;

    private static ApplicationContext applicationContext = null;

    public static void main(String[] args) {
        applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        BusinessServer server = (BusinessServer)applicationContext.getBean("businessServer");
        server.start();
    }

    private void start() {
        System.out.println("starting BusinessServer ...");
        BusinessContollerService.Processor p = new BusinessContollerService.Processor(this.businessService);
        try {
            TServerTransport transport = new TServerSocket(10010);
            TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(transport);
            serverArgs.processor(p);
            serverArgs.minWorkerThreads(4);
            serverArgs.maxWorkerThreads(400);
            TThreadPoolServer server = new TThreadPoolServer(serverArgs);
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}
