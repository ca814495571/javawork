//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.24 at 10:20:03 AM CST 
//


package com.cqfc.xmlparser.transactionmsg743;

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
 *       &lt;attribute name="money" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="seriesno" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="cashid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="statuscode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="tradetime" type="{http://www.w3.org/2001/XMLSchema}string" />
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
    protected String money;
    @XmlAttribute
    protected String seriesno;
    @XmlAttribute
    protected String cashid;
    @XmlAttribute
    protected String statuscode;
    @XmlAttribute
    protected String tradetime;
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
     * Gets the value of the money property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoney() {
        return money;
    }

    /**
     * Sets the value of the money property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoney(String value) {
        this.money = value;
    }

    /**
     * Gets the value of the seriesno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeriesno() {
        return seriesno;
    }

    /**
     * Sets the value of the seriesno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeriesno(String value) {
        this.seriesno = value;
    }

    /**
     * Gets the value of the cashid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCashid() {
        return cashid;
    }

    /**
     * Sets the value of the cashid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCashid(String value) {
        this.cashid = value;
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
     * Gets the value of the tradetime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTradetime() {
        return tradetime;
    }

    /**
     * Sets the value of the tradetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTradetime(String value) {
        this.tradetime = value;
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
