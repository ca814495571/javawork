package com.cqfc.ticketissue;

import javax.annotation.Resource;

import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.cqfc.protocol.ticketissue.TicketIssueService;

@Component
public class TicketIssueBootstrapServer {
	@Resource
	TicketIssueService.Iface ticketIssueService;

    private static ApplicationContext applicationContext = null;

    public static void main(String[] args) {
        applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        TicketIssueBootstrapServer server = (TicketIssueBootstrapServer)applicationContext.getBean("ticketIssueBootstrapServer");
        server.start();
    }

    private void start() {
        System.out.println("starting TicketIssueBootstrapServer ...");
        TicketIssueService.Processor p = new TicketIssueService.Processor(this.ticketIssueService);
        try {
            TServerTransport transport = new TServerSocket(10066);
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
