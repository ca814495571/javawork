//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.22 at 04:12:16 PM CST 
//


package com.cqfc.xmlparser.transactionmsg763;

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
 *       &lt;attribute name="lotteryid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="result" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dgpv" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="rq" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "result")
public class Result {

    @XmlAttribute
    protected String lotteryid;
    @XmlAttribute
    protected String result;
    @XmlAttribute
    protected String dgpv;
    @XmlAttribute
    protected String rq;

    /**
     * Gets the value of the lotteryid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLotteryid() {
        return lotteryid;
    }

    /**
     * Sets the value of the lotteryid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLotteryid(String value) {
        this.lotteryid = value;
    }

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResult(String value) {
        this.result = value;
    }

    /**
     * Gets the value of the dgpv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDgpv() {
        return dgpv;
    }

    /**
     * Sets the value of the dgpv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDgpv(String value) {
        this.dgpv = value;
    }

    /**
     * Gets the value of the rq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRq() {
        return rq;
    }

    /**
     * Sets the value of the rq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRq(String value) {
        this.rq = value;
    }

}
