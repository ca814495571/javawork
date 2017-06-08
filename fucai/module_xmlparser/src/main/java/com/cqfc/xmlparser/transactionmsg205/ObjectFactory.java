//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.12 at 11:58:41 AM CST 
//


package com.cqfc.xmlparser.transactionmsg205;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.cqfc.xmlparser.transactionmsg205 package. 
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
    private final static QName _Ticketorder_QNAME = new QName("", "ticketorder");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.cqfc.xmlparser.transactionmsg205
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
     * Create an instance of {@link Tickets }
     * 
     */
    public Tickets createTickets() {
        return new Tickets();
    }

    /**
     * Create an instance of {@link Ticket }
     * 
     */
    public Ticket createTicket() {
        return new Ticket();
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
    @XmlElementDecl(namespace = "", name = "ticketorder")
    public JAXBElement<Querytype> createTicketorder(Querytype value) {
        return new JAXBElement<Querytype>(_Ticketorder_QNAME, Querytype.class, null, value);
    }

}
