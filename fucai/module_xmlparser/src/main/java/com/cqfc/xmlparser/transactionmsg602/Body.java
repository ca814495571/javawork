//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.26 at 11:44:32 AM CST 
//


package com.cqfc.xmlparser.transactionmsg602;

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
 *         &lt;element ref="{}prizeinfo"/>
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
    "prizeinfo"
})
@XmlRootElement(name = "body")
public class Body {

    @XmlElement(required = true)
    protected Querytype prizeinfo;

    /**
     * Gets the value of the prizeinfo property.
     * 
     * @return
     *     possible object is
     *     {@link Querytype }
     *     
     */
    public Querytype getPrizeinfo() {
        return prizeinfo;
    }

    /**
     * Sets the value of the prizeinfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Querytype }
     *     
     */
    public void setPrizeinfo(Querytype value) {
        this.prizeinfo = value;
    }

}
