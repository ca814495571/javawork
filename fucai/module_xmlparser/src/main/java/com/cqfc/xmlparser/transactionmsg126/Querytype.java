//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.24 at 10:11:03 AM CST 
//


package com.cqfc.xmlparser.transactionmsg126;

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
 *       &lt;attribute name="gameid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="issue" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ordertype" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="filetype" type="{http://www.w3.org/2001/XMLSchema}string" />
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
    protected String gameid;
    @XmlAttribute
    protected String issue;
    @XmlAttribute
    protected String ordertype;
    @XmlAttribute
    protected String filetype;

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
     * Gets the value of the ordertype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrdertype() {
        return ordertype;
    }

    /**
     * Sets the value of the ordertype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrdertype(String value) {
        this.ordertype = value;
    }

    /**
     * Gets the value of the filetype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFiletype() {
        return filetype;
    }

    /**
     * Sets the value of the filetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFiletype(String value) {
        this.filetype = value;
    }

}
