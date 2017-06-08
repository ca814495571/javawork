package com.cqfc.xmlparser;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.cqfc.xmlparser.transactionmsg122.Msg;


public class TransactionMsgLoader122 {

	private static final JAXBContext jaxbContext = initContext();

	private static JAXBContext initContext() {
 		try {
			return JAXBContext.newInstance("com.cqfc.xmlparser.transactionmsg122");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
        return null;
	}

    public static Msg xmlToMsg(String xmlstr) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (Msg) unmarshaller.unmarshal(new StringReader(xmlstr));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String msgToXml(Msg Msg) {
        try {
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
}
