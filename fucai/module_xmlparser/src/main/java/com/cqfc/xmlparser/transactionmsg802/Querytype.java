//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.24 at 10:20:13 AM CST 
//


package com.cqfc.xmlparser.transactionmsg802;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute name="partnerid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="transcode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="retime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="userid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="returnmsg" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "querytype")
public class Querytype {

    @XmlAttribute
    protected String partnerid;
    @XmlAttribute
    protected String transcode;
    @XmlAttribute
    protected String retime;
    @XmlAttribute
    protected String userid;
    @XmlAttribute
    protected String key;
    @XmlAttribute
    protected String returnmsg;

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

    /**
     * Gets the value of the transcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranscode() {
        return transcode;
    }

    /**
     * Sets the value of the transcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranscode(String value) {
        this.transcode = value;
    }

    /**
     * Gets the value of the retime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetime() {
        return retime;
    }

    /**
     * Sets the value of the retime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetime(String value) {
        this.retime = value;
    }

    /**
     * Gets the value of the userid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserid() {
        return userid;
    }

    /**
     * Sets the value of the userid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserid(String value) {
        this.userid = value;
    }

    /**
     * Gets the value of the key property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the value of the key property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKey(String value) {
        this.key = value;
    }

    /**
     * Gets the value of the returnmsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnmsg() {
        return returnmsg;
    }

    /**
     * Sets the value of the returnmsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnmsg(String value) {
        this.returnmsg = value;
    }

}
