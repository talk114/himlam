
package gateway.core.channel.bidv_ecom.ws.nccwalletoutput_schema;


import javax.xml.bind.annotation.*;


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
 *         &lt;element name="Trandate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Trans_Id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Response_Code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Response_TxnCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="List" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="More_Info" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Redirect_Url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Secure_Code" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "trandate",
        "transId",
        "responseCode",
        "responseTxnCode",
        "list",
        "moreInfo",
        "redirectUrl",
        "secureCode"
})
@XmlRootElement(name = "root")
public class Root {

    @XmlElement(name = "Service_Id", required = true)
    protected String serviceId;
    @XmlElement(name = "Merchant_Id", required = true)
    protected String merchantId;
    @XmlElement(name = "Trandate", required = true)
    protected String trandate;
    @XmlElement(name = "Trans_Id", required = true)
    protected String transId;
    @XmlElement(name = "Response_Code", required = true)
    protected String responseCode;
    @XmlElement(name = "Response_TxnCode")
    protected String responseTxnCode;
    @XmlElement(name = "List")
    protected String list;
    @XmlElement(name = "More_Info")
    protected String moreInfo;
    @XmlElement(name = "Redirect_Url")
    protected String redirectUrl;
    @XmlElement(name = "Secure_Code", required = true)
    protected String secureCode;

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
     * Gets the value of the responseCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * Sets the value of the responseCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setResponseCode(String value) {
        this.responseCode = value;
    }

    /**
     * Gets the value of the responseTxnCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getResponseTxnCode() {
        return responseTxnCode;
    }

    /**
     * Sets the value of the responseTxnCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setResponseTxnCode(String value) {
        this.responseTxnCode = value;
    }

    /**
     * Gets the value of the list property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getList() {
        return list;
    }

    /**
     * Sets the value of the list property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setList(String value) {
        this.list = value;
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
     * Gets the value of the redirectUrl property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRedirectUrl() {
        return redirectUrl;
    }

    /**
     * Sets the value of the redirectUrl property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRedirectUrl(String value) {
        this.redirectUrl = value;
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

}
