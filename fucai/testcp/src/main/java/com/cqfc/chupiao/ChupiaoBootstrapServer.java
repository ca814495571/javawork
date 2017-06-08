package com.cqfc.chupiao;

import javax.annotation.Resource;

import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.cqfc.protocol.chupiao.ChupiaoService;


@Component
public class ChupiaoBootstrapServer {
	@Resource
	ChupiaoService.Iface chupiaoService;
//	@Resource
//	ChupiaoReasultService.Iface chupiaoReasultService;

    private static ApplicationContext applicationContext = null;

    public static void main(String[] args) {
        applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        ChupiaoBootstrapServer server = (ChupiaoBootstrapServer)applicationContext.getBean("chupiaoBootstrapServer");
        server.start();
    }

    private void start() {
        System.out.println("starting ChupiaoBootstrapServer ...");
        ChupiaoService.Processor p = new ChupiaoService.Processor(this.chupiaoService);
        try {
            TServerTransport transport = new TServerSocket(10666);
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
