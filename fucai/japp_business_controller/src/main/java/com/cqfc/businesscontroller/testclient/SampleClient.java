package com.cqfc.businesscontroller.testclient;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.protocol.businesscontroller.BusinessControllerService;
import com.cqfc.protocol.businesscontroller.Message;

/**
 * @author: giantspider@126.com
 */

public class SampleClient {

    public static void main(String[] args) {

        String xmlstr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<msg>\n" +
                        "      <head transcode='101' partnerid='00860001' version='1.0' time='200911120000'/>\n" +
                        "      <body>\n" +
                        "          <queryIssue gameid='SSQ' issueno='2009001' province='tj'/>\n" +
                        "      </body>\n" +
                        "</msg>";

        String asyncXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<msg>\n" +
                "      <head transcode='104' partnerid='00860001' version='1.0' time='200911120000'/>\n" +
                "      <body>\n" +
                "          <queryIssue gameid='SSQ' issueno='2009001' province='tj'/>\n" +
                "      </body>\n" +
                "</msg>";

        try {
            Message message = new Message();
            message.setTransCode(101);
            message.setTransMsg(xmlstr);
            message.setPartnerId("00860001");


            Message asyncMessage = new Message();
            asyncMessage.setTransCode(104);
            asyncMessage.setTransMsg(xmlstr);
            asyncMessage.setPartnerId("00860001");

            TTransport transport;
            transport = new TSocket("localhost",10010);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            BusinessControllerService.Client businessControllerService = new BusinessControllerService.Client(protocol);
            System.out.println(businessControllerService.ProcessMessage(message));
            System.out.println(businessControllerService.ProcessMessage(asyncMessage));
            transport.close();

        } catch (TTransportException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (TException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
