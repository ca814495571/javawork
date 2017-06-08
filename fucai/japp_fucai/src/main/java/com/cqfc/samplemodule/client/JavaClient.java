package com.cqfc.samplemodule.client;

import com.cqfc.samplemodule.protocol.SampleService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;


public class JavaClient {
    public static void main(String[] args) {
        try {
            TTransport transport;
            transport = new TSocket("localhost",10010);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            SampleService.Client javaClient = new SampleService.Client(protocol);
            System.out.println(javaClient.getUserList());
            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
