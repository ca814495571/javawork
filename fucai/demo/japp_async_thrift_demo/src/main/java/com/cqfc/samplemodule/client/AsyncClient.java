package com.cqfc.samplemodule.client;

import java.io.IOException;
import java.util.Scanner;

import com.cqfc.samplemodule.protocol.SampleService;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;

public class AsyncClient {

    public static final String host = "127.0.0.1";
    public static final int port = 10005;
    public static final int clientTimeout = 30000;

    public static void main(String[] args)  throws Exception {
        try {
            TAsyncClientManager clientManager = new TAsyncClientManager();
            TNonblockingTransport transport = new TNonblockingSocket(host, port, clientTimeout);
            TProtocolFactory protocol = new TCompactProtocol.Factory();
            SampleService.AsyncClient asyncClient = new SampleService.AsyncClient(protocol, clientManager, transport);
            MyCallback callBack = new MyCallback();
            asyncClient.doService("Larry", callBack);
            System.out.println("press any key to exit...");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
