//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.11 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2014.11.06 时间 06:24:26 PM CST 
//


package com.cqfc.xmlparser.transactionmsg741content;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="partnerId" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="gameId" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="statTime" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="saleAccount" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="prizeAccount" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="encachMoney" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="chargeMoney" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="proxyAccount" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "partnerdatebean")
public class Partnerdatebean {

    @XmlAttribute(name = "partnerId")
    protected String partnerId;
    @XmlAttribute(name = "gameId")
    protected String gameId;
    @XmlAttribute(name = "statTime")
    protected String statTime;
    @XmlAttribute(name = "saleAccount")
    protected String saleAccount;
    @XmlAttribute(name = "prizeAccount")
    protected String prizeAccount;
    @XmlAttribute(name = "encachMoney")
    protected String encachMoney;
    @XmlAttribute(name = "chargeMoney")
    protected String chargeMoney;
    @XmlAttribute(name = "proxyAccount")
    protected String proxyAccount;

    /**
     * 获取partnerId属性的值。
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
     * 设置partnerId属性的值。
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
     * 获取gameId属性的值。
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
     * 设置gameId属性的值。
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
     * 获取statTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatTime() {
        return statTime;
    }

    /**
     * 设置statTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatTime(String value) {
        this.statTime = value;
    }

    /**
     * 获取saleAccount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaleAccount() {
        return saleAccount;
    }

    /**
     * 设置saleAccount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaleAccount(String value) {
        this.saleAccount = value;
    }

    /**
     * 获取prizeAccount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrizeAccount() {
        return prizeAccount;
    }

    /**
     * 设置prizeAccount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrizeAccount(String value) {
        this.prizeAccount = value;
    }

    /**
     * 获取encachMoney属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncachMoney() {
        return encachMoney;
    }

    /**
     * 设置encachMoney属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncachMoney(String value) {
        this.encachMoney = value;
    }

    /**
     * 获取chargeMoney属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChargeMoney() {
        return chargeMoney;
    }

    /**
     * 设置chargeMoney属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChargeMoney(String value) {
        this.chargeMoney = value;
    }

    /**
     * 获取proxyAccount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProxyAccount() {
        return proxyAccount;
    }

    /**
     * 设置proxyAccount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProxyAccount(String value) {
        this.proxyAccount = value;
    }

}
