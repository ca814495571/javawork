//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.10 at 06:11:11 PM CST 
//


package com.cqfc.xmlparser.transactionmsg703;

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
 *         &lt;element ref="{}statinfo"/>
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
    "statinfo"
})
@XmlRootElement(name = "body")
public class Body {

    @XmlElement(required = true)
    protected Querytype statinfo;

    /**
     * Gets the value of the statinfo property.
     * 
     * @return
     *     possible object is
     *     {@link Querytype }
     *     
     */
    public Querytype getStatinfo() {
        return statinfo;
    }

    /**
     * Sets the value of the statinfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Querytype }
     *     
     */
    public void setStatinfo(Querytype value) {
        this.statinfo = value;
    }

}
