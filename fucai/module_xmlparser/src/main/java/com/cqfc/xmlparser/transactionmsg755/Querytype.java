//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.24 at 10:20:10 AM CST 
//


package com.cqfc.xmlparser.transactionmsg755;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for querytype complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="querytype">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}tickets"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "querytype", propOrder = {
    "tickets"
})
public class Querytype {

    @XmlElement(required = true)
    protected Tickets tickets;

    /**
     * Gets the value of the tickets property.
     * 
     * @return
     *     possible object is
     *     {@link Tickets }
     *     
     */
    public Tickets getTickets() {
        return tickets;
    }

    /**
     * Sets the value of the tickets property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tickets }
     *     
     */
    public void setTickets(Tickets value) {
        this.tickets = value;
    }

}