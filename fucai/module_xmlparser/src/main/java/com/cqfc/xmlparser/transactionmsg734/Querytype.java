//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.24 at 10:20:01 AM CST 
//


package com.cqfc.xmlparser.transactionmsg734;

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
 *       &lt;attribute name="prizestatus" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="gameid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="issueno" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="seriesno" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="prizemoney" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="balls" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="pid" type="{http://www.w3.org/2001/XMLSchema}string" />
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
    protected String prizestatus;
    @XmlAttribute
    protected String gameid;
    @XmlAttribute
    protected String issueno;
    @XmlAttribute
    protected String seriesno;
    @XmlAttribute
    protected String prizemoney;
    @XmlAttribute
    protected String balls;
    @XmlAttribute
    protected String pid;

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
     * Gets the value of the prizestatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrizestatus() {
        return prizestatus;
    }

    /**
     * Sets the value of the prizestatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrizestatus(String value) {
        this.prizestatus = value;
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
     * Gets the value of the issueno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueno() {
        return issueno;
    }

    /**
     * Sets the value of the issueno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueno(String value) {
        this.issueno = value;
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
     * Gets the value of the prizemoney property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrizemoney() {
        return prizemoney;
    }

    /**
     * Sets the value of the prizemoney property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrizemoney(String value) {
        this.prizemoney = value;
    }

    /**
     * Gets the value of the balls property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBalls() {
        return balls;
    }

    /**
     * Sets the value of the balls property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBalls(String value) {
        this.balls = value;
    }

    /**
     * Gets the value of the pid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPid() {
        return pid;
    }

    /**
     * Sets the value of the pid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPid(String value) {
        this.pid = value;
    }

}
