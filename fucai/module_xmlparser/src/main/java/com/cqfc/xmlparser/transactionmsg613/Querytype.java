//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.24 at 10:18:44 AM CST 
//


package com.cqfc.xmlparser.transactionmsg613;

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
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="palmid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="joinid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="realnumber" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="palmjoinid" type="{http://www.w3.org/2001/XMLSchema}string" />
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
    protected String id;
    @XmlAttribute
    protected String palmid;
    @XmlAttribute
    protected String joinid;
    @XmlAttribute
    protected String realnumber;
    @XmlAttribute
    protected String palmjoinid;
    @XmlAttribute
    protected String statuscode;
    @XmlAttribute
    protected String msg;

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
     * Gets the value of the palmid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPalmid() {
        return palmid;
    }

    /**
     * Sets the value of the palmid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPalmid(String value) {
        this.palmid = value;
    }

    /**
     * Gets the value of the joinid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJoinid() {
        return joinid;
    }

    /**
     * Sets the value of the joinid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJoinid(String value) {
        this.joinid = value;
    }

    /**
     * Gets the value of the realnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRealnumber() {
        return realnumber;
    }

    /**
     * Sets the value of the realnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRealnumber(String value) {
        this.realnumber = value;
    }

    /**
     * Gets the value of the palmjoinid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPalmjoinid() {
        return palmjoinid;
    }

    /**
     * Sets the value of the palmjoinid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPalmjoinid(String value) {
        this.palmjoinid = value;
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
