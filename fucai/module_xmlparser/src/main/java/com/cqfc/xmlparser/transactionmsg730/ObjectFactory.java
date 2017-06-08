//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.08.26 at 06:40:30 PM CST 
//


package com.cqfc.xmlparser.transactionmsg730;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.cqfc.xmlparser.transactionmsg730 package. 
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

    private final static QName _Unionorder_QNAME = new QName("", "unionorder");
    private final static QName _Orders_QNAME = new QName("", "orders");
    private final static QName _Head_QNAME = new QName("", "head");
    private final static QName _Userorder_QNAME = new QName("", "userorder");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.cqfc.xmlparser.transactionmsg730
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Bookorder }
     * 
     */
    public Bookorder createBookorder() {
        return new Bookorder();
    }

    /**
     * Create an instance of {@link Joinorders }
     * 
     */
    public Joinorders createJoinorders() {
        return new Joinorders();
    }

    /**
     * Create an instance of {@link Msg }
     * 
     */
    public Msg createMsg() {
        return new Msg();
    }

    /**
     * Create an instance of {@link Userordertype }
     * 
     */
    public Userordertype createUserordertype() {
        return new Userordertype();
    }

    /**
     * Create an instance of {@link Bookorders }
     * 
     */
    public Bookorders createBookorders() {
        return new Bookorders();
    }

    /**
     * Create an instance of {@link Userorders }
     * 
     */
    public Userorders createUserorders() {
        return new Userorders();
    }

    /**
     * Create an instance of {@link Unionorders }
     * 
     */
    public Unionorders createUnionorders() {
        return new Unionorders();
    }

    /**
     * Create an instance of {@link Querytype }
     * 
     */
    public Querytype createQuerytype() {
        return new Querytype();
    }

    /**
     * Create an instance of {@link Body }
     * 
     */
    public Body createBody() {
        return new Body();
    }

    /**
     * Create an instance of {@link Headtype }
     * 
     */
    public Headtype createHeadtype() {
        return new Headtype();
    }

    /**
     * Create an instance of {@link Unionjoinorder }
     * 
     */
    public Unionjoinorder createUnionjoinorder() {
        return new Unionjoinorder();
    }

    /**
     * Create an instance of {@link Unionordertype }
     * 
     */
    public Unionordertype createUnionordertype() {
        return new Unionordertype();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Unionordertype }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "unionorder")
    public JAXBElement<Unionordertype> createUnionorder(Unionordertype value) {
        return new JAXBElement<Unionordertype>(_Unionorder_QNAME, Unionordertype.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Querytype }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "orders")
    public JAXBElement<Querytype> createOrders(Querytype value) {
        return new JAXBElement<Querytype>(_Orders_QNAME, Querytype.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link Userordertype }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "userorder")
    public JAXBElement<Userordertype> createUserorder(Userordertype value) {
        return new JAXBElement<Userordertype>(_Userorder_QNAME, Userordertype.class, null, value);
    }

}