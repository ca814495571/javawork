//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.26 at 11:44:32 AM CST 
//


package com.cqfc.xmlparser.transactionmsg602;

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
 *         &lt;element ref="{}levelinfos"/>
 *         &lt;element ref="{}saleinfos"/>
 *       &lt;/sequence>
 *       &lt;attribute name="gameid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="issue" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="province" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="prizeball" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="pricepool" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "querytype", propOrder = {
    "levelinfos",
    "saleinfos"
})
public class Querytype {

    @XmlElement(required = true)
    protected Levelinfos levelinfos;
    @XmlElement(required = true)
    protected Saleinfos saleinfos;
    @XmlAttribute
    protected String gameid;
    @XmlAttribute
    protected String issue;
    @XmlAttribute
    protected String province;
    @XmlAttribute
    protected String prizeball;
    @XmlAttribute
    protected String status;
    @XmlAttribute
    protected String pricepool;

    /**
     * Gets the value of the levelinfos property.
     * 
     * @return
     *     possible object is
     *     {@link Levelinfos }
     *     
     */
    public Levelinfos getLevelinfos() {
        return levelinfos;
    }

    /**
     * Sets the value of the levelinfos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Levelinfos }
     *     
     */
    public void setLevelinfos(Levelinfos value) {
        this.levelinfos = value;
    }

    /**
     * Gets the value of the saleinfos property.
     * 
     * @return
     *     possible object is
     *     {@link Saleinfos }
     *     
     */
    public Saleinfos getSaleinfos() {
        return saleinfos;
    }

    /**
     * Sets the value of the saleinfos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Saleinfos }
     *     
     */
    public void setSaleinfos(Saleinfos value) {
        this.saleinfos = value;
    }

    /**
     * Gets the value of the gameid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGameid() {
        return gameid;
    }

    /**
     * Sets the value of the gameid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGameid(String value) {
        this.gameid = value;
    }

    /**
     * Gets the value of the issue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssue() {
        return issue;
    }

    /**
     * Sets the value of the issue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssue(String value) {
        this.issue = value;
    }

    /**
     * Gets the value of the province property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvince() {
        return province;
    }

    /**
     * Sets the value of the province property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvince(String value) {
        this.province = value;
    }

    /**
     * Gets the value of the prizeball property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrizeball() {
        return prizeball;
    }

    /**
     * Sets the value of the prizeball property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrizeball(String value) {
        this.prizeball = value;
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
     * Gets the value of the pricepool property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPricepool() {
        return pricepool;
    }

    /**
     * Sets the value of the pricepool property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPricepool(String value) {
        this.pricepool = value;
    }

}
