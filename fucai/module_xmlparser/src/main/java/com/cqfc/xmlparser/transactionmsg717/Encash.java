//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.07.22 at 06:18:27 PM CST 
//


package com.cqfc.xmlparser.transactionmsg717;

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
 *       &lt;attribute name="encashid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="userid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="money" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="createtime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="username" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="bankName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="bankCardno" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="processtime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="processnote" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="partnerEncashid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="eiid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "encash")
public class Encash {

    @XmlAttribute
    protected String encashid;
    @XmlAttribute
    protected String userid;
    @XmlAttribute
    protected String money;
    @XmlAttribute
    protected String createtime;
    @XmlAttribute
    protected String status;
    @XmlAttribute
    protected String username;
    @XmlAttribute
    protected String bankName;
    @XmlAttribute
    protected String bankCardno;
    @XmlAttribute
    protected String processtime;
    @XmlAttribute
    protected String processnote;
    @XmlAttribute
    protected String partnerEncashid;
    @XmlAttribute
    protected String eiid;

    /**
     * Gets the value of the encashid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncashid() {
        return encashid;
    }

    /**
     * Sets the value of the encashid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncashid(String value) {
        this.encashid = value;
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
     * Gets the value of the createtime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreatetime() {
        return createtime;
    }

    /**
     * Sets the value of the createtime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatetime(String value) {
        this.createtime = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the bankName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * Sets the value of the bankName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankName(String value) {
        this.bankName = value;
    }

    /**
     * Gets the value of the bankCardno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankCardno() {
        return bankCardno;
    }

    /**
     * Sets the value of the bankCardno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankCardno(String value) {
        this.bankCardno = value;
    }

    /**
     * Gets the value of the processtime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcesstime() {
        return processtime;
    }

    /**
     * Sets the value of the processtime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcesstime(String value) {
        this.processtime = value;
    }

    /**
     * Gets the value of the processnote property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessnote() {
        return processnote;
    }

    /**
     * Sets the value of the processnote property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessnote(String value) {
        this.processnote = value;
    }

    /**
     * Gets the value of the partnerEncashid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartnerEncashid() {
        return partnerEncashid;
    }

    /**
     * Sets the value of the partnerEncashid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartnerEncashid(String value) {
        this.partnerEncashid = value;
    }

    /**
     * Gets the value of the eiid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEiid() {
        return eiid;
    }

    /**
     * Sets the value of the eiid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEiid(String value) {
        this.eiid = value;
    }

}
