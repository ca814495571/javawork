//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.11 at 02:45:48 PM CST 
//


package com.cqfc.xmlparser.transactionmsg741;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element ref="{}partnerdatesbeans"/>
 *       &lt;/sequence>
 *       &lt;attribute name="partnerid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "querytype", propOrder = {
    "partnerdatesbeans"
})
public class Querytype {

    @XmlElement(required = true)
    protected Partnerdatesbeans partnerdatesbeans;
    @XmlAttribute
    protected String partnerid;

    /**
     * Gets the value of the partnerdatesbeans property.
     * 
     * @return
     *     possible object is
     *     {@link Partnerdatesbeans }
     *     
     */
    public Partnerdatesbeans getPartnerdatesbeans() {
        return partnerdatesbeans;
    }

    /**
     * Sets the value of the partnerdatesbeans property.
     * 
     * @param value
     *     allowed object is
     *     {@link Partnerdatesbeans }
     *     
     */
    public void setPartnerdatesbeans(Partnerdatesbeans value) {
        this.partnerdatesbeans = value;
    }

    /**
     * Gets the value of the partnerid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartnerid() {
        return partnerid;
    }

    /**
     * Sets the value of the partnerid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartnerid(String value) {
        this.partnerid = value;
    }

}
