//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.07.22 at 03:32:13 PM CST 
//


package com.cqfc.xmlparser.transactionmsg740;

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
 *       &lt;attribute name="partnerId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="gameId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="issueNo" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="statType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalSale" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalBonus" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalSucStakes" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalFailStakes" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="bigPrizeStakes" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="createTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalBigBonus" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="smallPrizeStakes" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalSmallBonus" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalSucSale" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalFailSale" type="{http://www.w3.org/2001/XMLSchema}string" />
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
    protected String partnerId;
    @XmlAttribute
    protected String gameId;
    @XmlAttribute
    protected String issueNo;
    @XmlAttribute
    protected String statType;
    @XmlAttribute
    protected String totalSale;
    @XmlAttribute
    protected String totalBonus;
    @XmlAttribute
    protected String totalSucStakes;
    @XmlAttribute
    protected String totalFailStakes;
    @XmlAttribute
    protected String bigPrizeStakes;
    @XmlAttribute
    protected String createTime;
    @XmlAttribute
    protected String totalBigBonus;
    @XmlAttribute
    protected String smallPrizeStakes;
    @XmlAttribute
    protected String totalSmallBonus;
    @XmlAttribute
    protected String totalSucSale;
    @XmlAttribute
    protected String totalFailSale;

    /**
     * Gets the value of the partnerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartnerId() {
        return partnerId;
    }

    /**
     * Sets the value of the partnerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartnerId(String value) {
        this.partnerId = value;
    }

    /**
     * Gets the value of the gameId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * Sets the value of the gameId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGameId(String value) {
        this.gameId = value;
    }

    /**
     * Gets the value of the issueNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueNo() {
        return issueNo;
    }

    /**
     * Sets the value of the issueNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueNo(String value) {
        this.issueNo = value;
    }

    /**
     * Gets the value of the statType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatType() {
        return statType;
    }

    /**
     * Sets the value of the statType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatType(String value) {
        this.statType = value;
    }

    /**
     * Gets the value of the totalSale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalSale() {
        return totalSale;
    }

    /**
     * Sets the value of the totalSale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalSale(String value) {
        this.totalSale = value;
    }

    /**
     * Gets the value of the totalBonus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalBonus() {
        return totalBonus;
    }

    /**
     * Sets the value of the totalBonus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalBonus(String value) {
        this.totalBonus = value;
    }

    /**
     * Gets the value of the totalSucStakes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalSucStakes() {
        return totalSucStakes;
    }

    /**
     * Sets the value of the totalSucStakes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalSucStakes(String value) {
        this.totalSucStakes = value;
    }

    /**
     * Gets the value of the totalFailStakes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalFailStakes() {
        return totalFailStakes;
    }

    /**
     * Sets the value of the totalFailStakes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalFailStakes(String value) {
        this.totalFailStakes = value;
    }

    /**
     * Gets the value of the bigPrizeStakes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBigPrizeStakes() {
        return bigPrizeStakes;
    }

    /**
     * Sets the value of the bigPrizeStakes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBigPrizeStakes(String value) {
        this.bigPrizeStakes = value;
    }

    /**
     * Gets the value of the createTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * Sets the value of the createTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateTime(String value) {
        this.createTime = value;
    }

    /**
     * Gets the value of the totalBigBonus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalBigBonus() {
        return totalBigBonus;
    }

    /**
     * Sets the value of the totalBigBonus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalBigBonus(String value) {
        this.totalBigBonus = value;
    }

    /**
     * Gets the value of the smallPrizeStakes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmallPrizeStakes() {
        return smallPrizeStakes;
    }

    /**
     * Sets the value of the smallPrizeStakes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmallPrizeStakes(String value) {
        this.smallPrizeStakes = value;
    }

    /**
     * Gets the value of the totalSmallBonus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalSmallBonus() {
        return totalSmallBonus;
    }

    /**
     * Sets the value of the totalSmallBonus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalSmallBonus(String value) {
        this.totalSmallBonus = value;
    }

    /**
     * Gets the value of the totalSucSale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalSucSale() {
        return totalSucSale;
    }

    /**
     * Sets the value of the totalSucSale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalSucSale(String value) {
        this.totalSucSale = value;
    }

    /**
     * Gets the value of the totalFailSale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalFailSale() {
        return totalFailSale;
    }

    /**
     * Sets the value of the totalFailSale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalFailSale(String value) {
        this.totalFailSale = value;
    }

}
