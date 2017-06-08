package com.cqfc.xmlparser;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.cqfc.xmlparser.transactionmsg722.Msg;

public class TransactionMsgLoader722 {

	private static final JAXBContext jaxbContext = initContext();

	private static JAXBContext initContext() {
 		try {
			return JAXBContext.newInstance("com.cqfc.xmlparser.transactionmsg722");
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
    
    public static String msgToXml(Msg msg) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            StringWriter sw = new StringWriter();

            DocumentBuilderFactory docBuilderFactory =
                    DocumentBuilderFactory.newInstance();
            Document document = docBuilderFactory.newDocumentBuilder().newDocument();
            marshaller.marshal(msg, document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer nullTransformer = transformerFactory.newTransformer();
            nullTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
            nullTransformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS,
                    "url");
            nullTransformer.transform(new DOMSource(document), new StreamResult(sw));
            return sw.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
}
