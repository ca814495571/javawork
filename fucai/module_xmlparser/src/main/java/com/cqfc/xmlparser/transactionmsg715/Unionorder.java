//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.24 at 10:19:18 AM CST 
//


package com.cqfc.xmlparser.transactionmsg715;

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
 *       &lt;attribute name="unionid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="creatoruserid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="sharenum" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalmoney" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="persharemoney" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="rate" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "unionorder")
public class Unionorder {

    @XmlAttribute
    protected String unionid;
    @XmlAttribute
    protected String creatoruserid;
    @XmlAttribute
    protected String sharenum;
    @XmlAttribute
    protected String totalmoney;
    @XmlAttribute
    protected String persharemoney;
    @XmlAttribute
    protected String rate;

    /**
     * Gets the value of the unionid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnionid() {
        return unionid;
    }

    /**
     * Sets the value of the unionid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnionid(String value) {
        this.unionid = value;
    }

    /**
     * Gets the value of the creatoruserid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreatoruserid() {
        return creatoruserid;
    }

    /**
     * Sets the value of the creatoruserid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatoruserid(String value) {
        this.creatoruserid = value;
    }

    /**
     * Gets the value of the sharenum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSharenum() {
        return sharenum;
    }

    /**
     * Sets the value of the sharenum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSharenum(String value) {
        this.sharenum = value;
    }

    /**
     * Gets the value of the totalmoney property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalmoney() {
        return totalmoney;
    }

    /**
     * Sets the value of the totalmoney property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalmoney(String value) {
        this.totalmoney = value;
    }

    /**
     * Gets the value of the persharemoney property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersharemoney() {
        return persharemoney;
    }

    /**
     * Sets the value of the persharemoney property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersharemoney(String value) {
        this.persharemoney = value;
    }

    /**
     * Gets the value of the rate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRate() {
        return rate;
    }

    /**
     * Sets the value of the rate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRate(String value) {
        this.rate = value;
    }

}
