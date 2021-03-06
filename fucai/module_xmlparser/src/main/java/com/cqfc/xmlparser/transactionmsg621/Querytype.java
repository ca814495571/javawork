//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.07.16 at 02:56:41 PM CST 
//


package com.cqfc.xmlparser.transactionmsg621;

import java.util.ArrayList;
import java.util.List;
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
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element ref="{}flowissues"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="cancelmoney" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="gameid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="flowstatus" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="flowMsg" type="{http://www.w3.org/2001/XMLSchema}string" />
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
    protected List<Flowissues> flowissues;
    @XmlAttribute
    protected String id;
    @XmlAttribute
    protected String cancelmoney;
    @XmlAttribute
    protected String gameid;
    @XmlAttribute
    protected String flowstatus;
    @XmlAttribute
    protected String flowMsg;

    /**
     * Gets the value of the flowissues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the flowissues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFlowissues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Flowissues }
     * 
     * 
     */
    public List<Flowissues> getFlowissues() {
        if (flowissues == null) {
            flowissues = new ArrayList<Flowissues>();
        }
        return this.flowissues;
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
     * Gets the value of the cancelmoney property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCancelmoney() {
        return cancelmoney;
    }

    /**
     * Sets the value of the cancelmoney property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCancelmoney(String value) {
        this.cancelmoney = value;
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
     * Gets the value of the flowstatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlowstatus() {
        return flowstatus;
    }

    /**
     * Sets the value of the flowstatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlowstatus(String value) {
        this.flowstatus = value;
    }

    /**
     * Gets the value of the flowMsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlowMsg() {
        return flowMsg;
    }

    /**
     * Sets the value of the flowMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlowMsg(String value) {
        this.flowMsg = value;
    }

}
