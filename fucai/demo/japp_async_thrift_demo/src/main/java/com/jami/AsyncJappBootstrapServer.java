package com.jami;

import javax.annotation.Resource;

import com.cqfc.samplemodule.protocol.SampleService;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AsyncJappBootstrapServer {

    @Resource
    SampleService.Iface sampleService;

    private static ApplicationContext applicationContext = null;

    public static void main(String[] args) {
        applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        AsyncJappBootstrapServer server = (AsyncJappBootstrapServer)applicationContext.getBean("asyncJappBootstrapServer");
        server.start();
    }

    private void start() {
        try {
            System.out.println("Start server on port 10005 ...");
            TNonblockingServerSocket socket = new TNonblockingServerSocket(10005);
            SampleService.Processor processor = new SampleService.Processor(sampleService);
            THsHaServer.Args arg = new THsHaServer.Args(socket);
            // 高效率的、密集的二进制编码格式进行数据传输
            // 使用非阻塞方式，按块的大小进行传输，类似于 Java 中的 NIO
            arg.protocolFactory(new TCompactProtocol.Factory());
            arg.transportFactory(new TFramedTransport.Factory());
            arg.processorFactory(new TProcessorFactory(processor));
            TServer server = new THsHaServer(arg);
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
