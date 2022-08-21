
package gateway.core.channel.bidv.ws.nccwalletinput_schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *       &lt;sequence>
 *         &lt;element name="Service_Id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Merchant_Id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Merchant_Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Trandate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Trans_Id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Trans_Desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Curr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Payer_Id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Payer_Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Payer_Addr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Custmer_Id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Customer_Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IssueDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Channel_Id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Link_Type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Otp_Number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="More_Info" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Secure_Code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FromProcess" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "serviceId",
        "merchantId",
        "merchantName",
        "trandate",
        "transId",
        "transDesc",
        "amount",
        "curr",
        "payerId",
        "payerName",
        "payerAddr",
        "type",
        "custmerId",
        "customerName",
        "issueDate",
        "channelId",
        "linkType",
        "otpNumber",
        "moreInfo",
        "secureCode",
        "fromProcess"
})
@XmlRootElement(name = "root")
public class Root {

    @XmlElement(name = "Service_Id", required = true)
    protected String serviceId;
    @XmlElement(name = "Merchant_Id", required = true)
    protected String merchantId;
    @XmlElement(name = "Merchant_Name")
    protected String merchantName;
    @XmlElement(name = "Trandate")
    protected String trandate;
    @XmlElement(name = "Trans_Id")
    protected String transId;
    @XmlElement(name = "Trans_Desc")
    protected String transDesc;
    @XmlElement(name = "Amount")
    protected String amount;
    @XmlElement(name = "Curr")
    protected String curr;
    @XmlElement(name = "Payer_Id", required = true)
    protected String payerId;
    @XmlElement(name = "Payer_Name")
    protected String payerName;
    @XmlElement(name = "Payer_Addr")
    protected String payerAddr;
    @XmlElement(name = "Type")
    protected String type;
    @XmlElement(name = "Custmer_Id")
    protected String custmerId;
    @XmlElement(name = "Customer_Name")
    protected String customerName;
    @XmlElement(name = "IssueDate")
    protected String issueDate;
    @XmlElement(name = "Channel_Id")
    protected String channelId;
    @XmlElement(name = "Link_Type")
    protected String linkType;
    @XmlElement(name = "Otp_Number")
    protected String otpNumber;
    @XmlElement(name = "More_Info")
    protected String moreInfo;
    @XmlElement(name = "Secure_Code", required = true)
    protected String secureCode;
    @XmlElement(name = "FromProcess")
    protected String fromProcess;

    /**
     * Gets the value of the serviceId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Sets the value of the serviceId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setServiceId(String value) {
        this.serviceId = value;
    }

    /**
     * Gets the value of the merchantId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMerchantId() {
        return merchantId;
    }

    /**
     * Sets the value of the merchantId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMerchantId(String value) {
        this.merchantId = value;
    }

    /**
     * Gets the value of the merchantName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMerchantName() {
        return merchantName;
    }

    /**
     * Sets the value of the merchantName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMerchantName(String value) {
        this.merchantName = value;
    }

    /**
     * Gets the value of the trandate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTrandate() {
        return trandate;
    }

    /**
     * Sets the value of the trandate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTrandate(String value) {
        this.trandate = value;
    }

    /**
     * Gets the value of the transId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTransId() {
        return transId;
    }

    /**
     * Sets the value of the transId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTransId(String value) {
        this.transId = value;
    }

    /**
     * Gets the value of the transDesc property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTransDesc() {
        return transDesc;
    }

    /**
     * Sets the value of the transDesc property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTransDesc(String value) {
        this.transDesc = value;
    }

    /**
     * Gets the value of the amount property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAmount(String value) {
        this.amount = value;
    }

    /**
     * Gets the value of the curr property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCurr() {
        return curr;
    }

    /**
     * Sets the value of the curr property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCurr(String value) {
        this.curr = value;
    }

    /**
     * Gets the value of the payerId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPayerId() {
        return payerId;
    }

    /**
     * Sets the value of the payerId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPayerId(String value) {
        this.payerId = value;
    }

    /**
     * Gets the value of the payerName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPayerName() {
        return payerName;
    }

    /**
     * Sets the value of the payerName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPayerName(String value) {
        this.payerName = value;
    }

    /**
     * Gets the value of the payerAddr property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPayerAddr() {
        return payerAddr;
    }

    /**
     * Sets the value of the payerAddr property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPayerAddr(String value) {
        this.payerAddr = value;
    }

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
     * Gets the value of the custmerId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCustmerId() {
        return custmerId;
    }

    /**
     * Sets the value of the custmerId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCustmerId(String value) {
        this.custmerId = value;
    }

    /**
     * Gets the value of the customerName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the value of the customerName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCustomerName(String value) {
        this.customerName = value;
    }

    /**
     * Gets the value of the issueDate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIssueDate() {
        return issueDate;
    }

    /**
     * Sets the value of the issueDate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIssueDate(String value) {
        this.issueDate = value;
    }

    /**
     * Gets the value of the channelId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * Sets the value of the channelId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setChannelId(String value) {
        this.channelId = value;
    }

    /**
     * Gets the value of the linkType property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLinkType() {
        return linkType;
    }

    /**
     * Sets the value of the linkType property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLinkType(String value) {
        this.linkType = value;
    }

    /**
     * Gets the value of the otpNumber property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOtpNumber() {
        return otpNumber;
    }

    /**
     * Sets the value of the otpNumber property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOtpNumber(String value) {
        this.otpNumber = value;
    }

    /**
     * Gets the value of the moreInfo property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMoreInfo() {
        return moreInfo;
    }

    /**
     * Sets the value of the moreInfo property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMoreInfo(String value) {
        this.moreInfo = value;
    }

    /**
     * Gets the value of the secureCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSecureCode() {
        return secureCode;
    }

    /**
     * Sets the value of the secureCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSecureCode(String value) {
        this.secureCode = value;
    }

    /**
     * Gets the value of the fromProcess property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFromProcess() {
        return fromProcess;
    }

    /**
     * Sets the value of the fromProcess property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFromProcess(String value) {
        this.fromProcess = value;
    }

}
