//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.06.04 at 11:52:20 AM CST 
//


package com.cqfc.xmlparser.transactionmsg762;

import java.util.ArrayList;
import java.util.List;
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
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{}match"/>
 *       &lt;/sequence>
 *       &lt;attribute name="issue" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="lotteryid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="issuestarttime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="issueendtime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="drawtime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "match"
})
@XmlRootElement(name = "matchs")
public class Matchs {

    protected List<Match> match;
    @XmlAttribute
    protected String issue;
    @XmlAttribute
    protected String lotteryid;
    @XmlAttribute
    protected String issuestarttime;
    @XmlAttribute
    protected String issueendtime;
    @XmlAttribute
    protected String drawtime;

    /**
     * Gets the value of the match property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the match property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMatch().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Match }
     * 
     * 
     */
    public List<Match> getMatch() {
        if (match == null) {
            match = new ArrayList<Match>();
        }
        return this.match;
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
     * Gets the value of the lotteryid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLotteryid() {
        return lotteryid;
    }

    /**
     * Sets the value of the lotteryid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLotteryid(String value) {
        this.lotteryid = value;
    }

    /**
     * Gets the value of the issuestarttime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuestarttime() {
        return issuestarttime;
    }

    /**
     * Sets the value of the issuestarttime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuestarttime(String value) {
        this.issuestarttime = value;
    }

    /**
     * Gets the value of the issueendtime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueendtime() {
        return issueendtime;
    }

    /**
     * Sets the value of the issueendtime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueendtime(String value) {
        this.issueendtime = value;
    }

    /**
     * Gets the value of the drawtime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDrawtime() {
        return drawtime;
    }

    /**
     * Sets the value of the drawtime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDrawtime(String value) {
        this.drawtime = value;
    }

}