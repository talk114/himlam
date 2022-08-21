package gateway.core.channel.bidv.ws.message_confirm_wallet;


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
 *         &lt;element name="MESSAGE_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PAYER_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TRAN_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TRAN_STATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AMOUNT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ACCOUNT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TRAN_DATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SERVICE" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="SERVICE_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="SERVICE_PROFILE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="CHANNEL_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PROVIDER_INFO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="SERVICE_INFO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ERRCODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ERRDESC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="URL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "messagecode",
        "payerid",
        "tranid",
        "transtate",
        "amount",
        "account",
        "trandate",
        "service",
        "errcode",
        "errdesc",
        "url"
})
@XmlRootElement(name = "MSG")
public class MSG {

    @XmlElement(name = "MESSAGE_CODE", required = true)
    protected String messagecode;
    @XmlElement(name = "PAYER_ID")
    protected String payerid;
    @XmlElement(name = "TRAN_ID")
    protected String tranid;
    @XmlElement(name = "TRAN_STATE")
    protected String transtate;
    @XmlElement(name = "AMOUNT")
    protected String amount;
    @XmlElement(name = "ACCOUNT")
    protected String account;
    @XmlElement(name = "TRAN_DATE")
    protected String trandate;
    @XmlElement(name = "SERVICE")
    protected MSG.SERVICE service;
    @XmlElement(name = "ERRCODE")
    protected String errcode;
    @XmlElement(name = "ERRDESC")
    protected String errdesc;
    @XmlElement(name = "URL")
    protected String url;

    /**
     * Gets the value of the messagecode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMESSAGECODE() {
        return messagecode;
    }

    /**
     * Sets the value of the messagecode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMESSAGECODE(String value) {
        this.messagecode = value;
    }

    /**
     * Gets the value of the payerid property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPAYERID() {
        return payerid;
    }

    /**
     * Sets the value of the payerid property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPAYERID(String value) {
        this.payerid = value;
    }

    /**
     * Gets the value of the tranid property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTRANID() {
        return tranid;
    }

    /**
     * Sets the value of the tranid property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTRANID(String value) {
        this.tranid = value;
    }

    /**
     * Gets the value of the transtate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTRANSTATE() {
        return transtate;
    }

    /**
     * Sets the value of the transtate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTRANSTATE(String value) {
        this.transtate = value;
    }

    /**
     * Gets the value of the amount property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAMOUNT() {
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
    public void setAMOUNT(String value) {
        this.amount = value;
    }

    /**
     * Gets the value of the account property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getACCOUNT() {
        return account;
    }

    /**
     * Sets the value of the account property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setACCOUNT(String value) {
        this.account = value;
    }

    /**
     * Gets the value of the trandate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTRANDATE() {
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
    public void setTRANDATE(String value) {
        this.trandate = value;
    }

    /**
     * Gets the value of the service property.
     *
     * @return
     *     possible object is
     *     {@link MSG.SERVICE }
     *
     */
    public MSG.SERVICE getSERVICE() {
        return service;
    }

    /**
     * Sets the value of the service property.
     *
     * @param value
     *     allowed object is
     *     {@link MSG.SERVICE }
     *
     */
    public void setSERVICE(MSG.SERVICE value) {
        this.service = value;
    }

    /**
     * Gets the value of the errcode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getERRCODE() {
        return errcode;
    }

    /**
     * Sets the value of the errcode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setERRCODE(String value) {
        this.errcode = value;
    }

    /**
     * Gets the value of the errdesc property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getERRDESC() {
        return errdesc;
    }

    /**
     * Sets the value of the errdesc property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setERRDESC(String value) {
        this.errdesc = value;
    }

    /**
     * Gets the value of the url property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getURL() {
        return url;
    }

    /**
     * Sets the value of the url property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setURL(String value) {
        this.url = value;
    }


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
     *         &lt;element name="SERVICE_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="SERVICE_PROFILE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="CHANNEL_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PROVIDER_INFO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="SERVICE_INFO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
            "serviceid",
            "serviceprofile",
            "channelid",
            "providerinfo",
            "serviceinfo"
    })
    public static class SERVICE {

        @XmlElement(name = "SERVICE_ID")
        protected String serviceid;
        @XmlElement(name = "SERVICE_PROFILE")
        protected String serviceprofile;
        @XmlElement(name = "CHANNEL_ID")
        protected String channelid;
        @XmlElement(name = "PROVIDER_INFO")
        protected String providerinfo;
        @XmlElement(name = "SERVICE_INFO")
        protected String serviceinfo;

        /**
         * Gets the value of the serviceid property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getSERVICEID() {
            return serviceid;
        }

        /**
         * Sets the value of the serviceid property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setSERVICEID(String value) {
            this.serviceid = value;
        }

        /**
         * Gets the value of the serviceprofile property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getSERVICEPROFILE() {
            return serviceprofile;
        }

        /**
         * Sets the value of the serviceprofile property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setSERVICEPROFILE(String value) {
            this.serviceprofile = value;
        }

        /**
         * Gets the value of the channelid property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getCHANNELID() {
            return channelid;
        }

        /**
         * Sets the value of the channelid property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setCHANNELID(String value) {
            this.channelid = value;
        }

        /**
         * Gets the value of the providerinfo property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getPROVIDERINFO() {
            return providerinfo;
        }

        /**
         * Sets the value of the providerinfo property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setPROVIDERINFO(String value) {
            this.providerinfo = value;
        }

        /**
         * Gets the value of the serviceinfo property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getSERVICEINFO() {
            return serviceinfo;
        }

        /**
         * Sets the value of the serviceinfo property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setSERVICEINFO(String value) {
            this.serviceinfo = value;
        }

    }

}
