//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.13 at 06:28:28 PM CST 
//


package com.cqfc.xmlparser.transactionmsg605;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}ticketresult"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "ticketresult"
})
@XmlRootElement(name = "body")
public class Body {

    @XmlElement(required = true)
    protected Querytype ticketresult;

    /**
     * Gets the value of the ticketresult property.
     * 
     * @return
     *     possible object is
     *     {@link Querytype }
     *     
     */
    public Querytype getTicketresult() {
        return ticketresult;
    }

    /**
     * Sets the value of the ticketresult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Querytype }
     *     
     */
    public void setTicketresult(Querytype value) {
        this.ticketresult = value;
    }

}
