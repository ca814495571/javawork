//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.24 at 10:10:52 AM CST 
//


package com.cqfc.xmlparser.transactionmsg110;

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
 *         &lt;element ref="{}flowissues"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalmoney" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="province" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalissue" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="playtype" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="gameid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="stopflag" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="userid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ball" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "querytype", propOrder = {
    "flowissues"
})
public class Querytype {

    @XmlElement(required = true)
    protected Flowissues flowissues;
    @XmlAttribute
    protected String id;
    @XmlAttribute
    protected String totalmoney;
    @XmlAttribute
    protected String province;
    @XmlAttribute
    protected String totalissue;
    @XmlAttribute
    protected String playtype;
    @XmlAttribute
    protected String gameid;
    @XmlAttribute
    protected String stopflag;
    @XmlAttribute
    protected String userid;
    @XmlAttribute
    protected String ball;

    /**
     * Gets the value of the flowissues property.
     * 
     * @return
     *     possible object is
     *     {@link Flowissues }
     *     
     */
    public Flowissues getFlowissues() {
        return flowissues;
    }

    /**
     * Sets the value of the flowissues property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flowissues }
     *     
     */
    public void setFlowissues(Flowissues value) {
        this.flowissues = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
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
     * Gets the value of the totalissue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalissue() {
        return totalissue;
    }

    /**
     * Sets the value of the totalissue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalissue(String value) {
        this.totalissue = value;
    }

    /**
     * Gets the value of the playtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlaytype() {
        return playtype;
    }

    /**
     * Sets the value of the playtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlaytype(String value) {
        this.playtype = value;
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
     * Gets the value of the stopflag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStopflag() {
        return stopflag;
    }

    /**
     * Sets the value of the stopflag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStopflag(String value) {
        this.stopflag = value;
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
     * Gets the value of the ball property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBall() {
        return ball;
    }

    /**
     * Sets the value of the ball property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBall(String value) {
        this.ball = value;
    }

}
