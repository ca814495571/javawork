package com.cqfc.xmlparser;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import com.cqfc.xmlparser.transactionmsg.Body;
import com.cqfc.xmlparser.transactionmsg.Headtype;
import com.cqfc.xmlparser.transactionmsg.Msg;
import com.cqfc.xmlparser.transactionmsg.Querytype;


public class TransactionMsgLoader {

    public static Msg xmlToMsg(String xmlstr) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("com.cqfc.xmlparser.transactionmsg");
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (Msg) unmarshaller.unmarshal(new StringReader(xmlstr));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String msgToXml(Msg Msg) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("com.cqfc.xmlparser.transactionmsg");
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            marshaller.marshal(Msg, sw);
            return sw.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
    

   private static boolean validate(InputStream xsd, String xmlstr) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            StringReader reader = new StringReader(xmlstr);
            validator.validate(new StreamSource(reader));
            return true;
        } catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        String xmlstr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<msg>\n" +
                        "      <head transcode='101' partnerid='00860001' version='1.0' time='200911120000'/>\n" +
                        "      <body>\n" +
                        "          <queryIssue gameid='SSQ' issueno='2009001' province='tj'/>\n" +
                        "      </body>\n" +
                        "</msg>";


        System.out.println("--- xmlToMsg ---");
        Msg msg = xmlToMsg(xmlstr);
        System.out.println(msg.getHead().getPartnerid());

        System.out.println("\r\n\r\n");

        System.out.println("--- msgToXml ---");
        Msg msg1 = new Msg();
        Headtype headtype = new Headtype();
        headtype.setPartnerid("100010");
        headtype.setTranscode("601");
        headtype.setVersion("2.0");
        headtype.setTime("20140610");
        msg1.setHead(headtype);
        Body body = new Body();
        Querytype querytype = new Querytype();
        querytype.setGameid("ssq");
        querytype.setIssueno("2014001");
        querytype.setProvince("chongqing");
        body.setQueryIssue(querytype);
        msg1.setBody(body);
        System.out.println(msgToXml(msg1));

    }
}
