//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.07.03 at 11:14:23 AM CST 
//


package com.cqfc.xmlparser.transactionmsg711;

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
 *       &lt;attribute name="userid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="giftmoney" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="activityid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="giftid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="palmgiftid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="statuscode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="msg" type="{http://www.w3.org/2001/XMLSchema}string" />
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
    protected String userid;
    @XmlAttribute
    protected String giftmoney;
    @XmlAttribute
    protected String activityid;
    @XmlAttribute
    protected String giftid;
    @XmlAttribute
    protected String palmgiftid;
    @XmlAttribute
    protected String statuscode;
    @XmlAttribute
    protected String msg;

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
     * Gets the value of the giftmoney property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGiftmoney() {
        return giftmoney;
    }

    /**
     * Sets the value of the giftmoney property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGiftmoney(String value) {
        this.giftmoney = value;
    }

    /**
     * Gets the value of the activityid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivityid() {
        return activityid;
    }

    /**
     * Sets the value of the activityid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivityid(String value) {
        this.activityid = value;
    }

    /**
     * Gets the value of the giftid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGiftid() {
        return giftid;
    }

    /**
     * Sets the value of the giftid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGiftid(String value) {
        this.giftid = value;
    }

    /**
     * Gets the value of the palmgiftid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPalmgiftid() {
        return palmgiftid;
    }

    /**
     * Sets the value of the palmgiftid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPalmgiftid(String value) {
        this.palmgiftid = value;
    }

    /**
     * Gets the value of the statuscode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatuscode() {
        return statuscode;
    }

    /**
     * Sets the value of the statuscode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatuscode(String value) {
        this.statuscode = value;
    }

    /**
     * Gets the value of the msg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Sets the value of the msg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsg(String value) {
        this.msg = value;
    }

}
