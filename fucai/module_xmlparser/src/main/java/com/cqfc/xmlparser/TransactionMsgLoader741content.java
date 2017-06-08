package com.cqfc.xmlparser;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.cqfc.xmlparser.transactionmsg741content.Partnerdatebeans;



public class TransactionMsgLoader741content {

	private static final JAXBContext jaxbContext = initContext();

	private static JAXBContext initContext() {
 		try {
			return JAXBContext.newInstance("com.cqfc.xmlparser.transactionmsg741content");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
        return null;
	}

    public static Partnerdatebeans xmlToMsg(String xmlstr) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (Partnerdatebeans) unmarshaller.unmarshal(new StringReader(xmlstr));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String msgToXml(Partnerdatebeans msg) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            marshaller.marshal(msg, sw);
            return sw.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
