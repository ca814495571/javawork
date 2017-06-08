package com.jami;

import javax.annotation.Resource;

import com.cqfc.samplemodule.protocol.SampleService;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class JappBootstrapServer {

    @Resource
    SampleService.Iface sampleService;

    private static ApplicationContext applicationContext = null;

    public static void main(String[] args) {
        applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        JappBootstrapServer server = (JappBootstrapServer)applicationContext.getBean("jappBootstrapServer");
        server.start();
    }

    private void start() {
        try {
            System.out.println("Start server on port 10010 ...");
            TServerTransport transport = new TServerSocket(10010);
            TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(transport);
            SampleService.Processor p = new SampleService.Processor(this.sampleService);
            serverArgs.processor(p);
            serverArgs.minWorkerThreads(4);
            serverArgs.maxWorkerThreads(400);
            TThreadPoolServer server = new TThreadPoolServer(serverArgs);
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
