//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.10 at 06:11:11 PM CST 
//


package com.cqfc.xmlparser.transactionmsg703;

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
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="sucnum" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="sucmoney" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failnum" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failmoney" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="smallprizenum" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="smallprize" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="bigprizenum" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="bigprizebonus" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "stat")
public class Stat {

    @XmlAttribute
    protected String type;
    @XmlAttribute
    protected String sucnum;
    @XmlAttribute
    protected String sucmoney;
    @XmlAttribute
    protected String failnum;
    @XmlAttribute
    protected String failmoney;
    @XmlAttribute
    protected String smallprizenum;
    @XmlAttribute
    protected String smallprize;
    @XmlAttribute
    protected String bigprizenum;
    @XmlAttribute
    protected String bigprizebonus;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the sucnum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSucnum() {
        return sucnum;
    }

    /**
     * Sets the value of the sucnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSucnum(String value) {
        this.sucnum = value;
    }

    /**
     * Gets the value of the sucmoney property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSucmoney() {
        return sucmoney;
    }

    /**
     * Sets the value of the sucmoney property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSucmoney(String value) {
        this.sucmoney = value;
    }

    /**
     * Gets the value of the failnum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailnum() {
        return failnum;
    }

    /**
     * Sets the value of the failnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailnum(String value) {
        this.failnum = value;
    }

    /**
     * Gets the value of the failmoney property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailmoney() {
        return failmoney;
    }

    /**
     * Sets the value of the failmoney property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailmoney(String value) {
        this.failmoney = value;
    }

    /**
     * Gets the value of the smallprizenum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmallprizenum() {
        return smallprizenum;
    }

    /**
     * Sets the value of the smallprizenum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmallprizenum(String value) {
        this.smallprizenum = value;
    }

    /**
     * Gets the value of the smallprize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmallprize() {
        return smallprize;
    }

    /**
     * Sets the value of the smallprize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmallprize(String value) {
        this.smallprize = value;
    }

    /**
     * Gets the value of the bigprizenum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBigprizenum() {
        return bigprizenum;
    }

    /**
     * Sets the value of the bigprizenum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBigprizenum(String value) {
        this.bigprizenum = value;
    }

    /**
     * Gets the value of the bigprizebonus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBigprizebonus() {
        return bigprizebonus;
    }

    /**
     * Sets the value of the bigprizebonus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBigprizebonus(String value) {
        this.bigprizebonus = value;
    }

}
