//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.13 at 06:28:28 PM CST 
//


package com.cqfc.xmlparser.transactionmsg605;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.cqfc.xmlparser.transactionmsg605 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Head_QNAME = new QName("", "head");
    private final static QName _Ticketresult_QNAME = new QName("", "ticketresult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.cqfc.xmlparser.transactionmsg605
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Msg }
     * 
     */
    public Msg createMsg() {
        return new Msg();
    }

    /**
     * Create an instance of {@link Headtype }
     * 
     */
    public Headtype createHeadtype() {
        return new Headtype();
    }

    /**
     * Create an instance of {@link Body }
     * 
     */
    public Body createBody() {
        return new Body();
    }

    /**
     * Create an instance of {@link Querytype }
     * 
     */
    public Querytype createQuerytype() {
        return new Querytype();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Headtype }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "head")
    public JAXBElement<Headtype> createHead(Headtype value) {
        return new JAXBElement<Headtype>(_Head_QNAME, Headtype.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Querytype }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ticketresult")
    public JAXBElement<Querytype> createTicketresult(Querytype value) {
        return new JAXBElement<Querytype>(_Ticketresult_QNAME, Querytype.class, null, value);
    }

}
