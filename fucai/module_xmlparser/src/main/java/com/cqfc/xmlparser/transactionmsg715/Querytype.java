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
 *         &lt;element ref="{}unionorders"/>
 *       &lt;/sequence>
 *       &lt;attribute name="gameid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="issue" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalcount" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="pagesize" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="currentpage" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "querytype", propOrder = {
    "unionorders"
})
public class Querytype {

    @XmlElement(required = true)
    protected Unionorders unionorders;
    @XmlAttribute
    protected String gameid;
    @XmlAttribute
    protected String issue;
    @XmlAttribute
    protected String totalcount;
    @XmlAttribute
    protected String pagesize;
    @XmlAttribute
    protected String currentpage;

    /**
     * Gets the value of the unionorders property.
     * 
     * @return
     *     possible object is
     *     {@link Unionorders }
     *     
     */
    public Unionorders getUnionorders() {
        return unionorders;
    }

    /**
     * Sets the value of the unionorders property.
     * 
     * @param value
     *     allowed object is
     *     {@link Unionorders }
     *     
     */
    public void setUnionorders(Unionorders value) {
        this.unionorders = value;
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
     * Gets the value of the totalcount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalcount() {
        return totalcount;
    }

    /**
     * Sets the value of the totalcount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalcount(String value) {
        this.totalcount = value;
    }

    /**
     * Gets the value of the pagesize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPagesize() {
        return pagesize;
    }

    /**
     * Sets the value of the pagesize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPagesize(String value) {
        this.pagesize = value;
    }

    /**
     * Gets the value of the currentpage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentpage() {
        return currentpage;
    }

    /**
     * Sets the value of the currentpage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentpage(String value) {
        this.currentpage = value;
    }

}