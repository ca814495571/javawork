//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.13 at 03:11:06 PM CST 
//


package com.cqfc.xmlparser.transactionmsg706;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute name="cash" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="giftcash" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="forzencash" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "account")
public class Account {

    @XmlAttribute
    protected String cash;
    @XmlAttribute
    protected String giftcash;
    @XmlAttribute
    protected String forzencash;

    /**
     * Gets the value of the cash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCash() {
        return cash;
    }

    /**
     * Sets the value of the cash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCash(String value) {
        this.cash = value;
    }

    /**
     * Gets the value of the giftcash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGiftcash() {
        return giftcash;
    }

    /**
     * Sets the value of the giftcash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGiftcash(String value) {
        this.giftcash = value;
    }

    /**
     * Gets the value of the forzencash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForzencash() {
        return forzencash;
    }

    /**
     * Sets the value of the forzencash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForzencash(String value) {
        this.forzencash = value;
    }

}
