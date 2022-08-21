package gateway.core.channel.bidv.bidv_transfer_247.dto.req;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

import static gateway.core.channel.bidv.bidv_transfer_247.dto.BIDVTransferConstants.PRIVATE_KEY;

public class GetListBank247Request implements Serializable {
    @XmlElement(name = "Service_Id")
    private String serviceId;
    @XmlElement(name = "Merchant_Id")
    private String merchantId;
    @XmlElement(name = "Merchant_Name")
    private String merchantName;
    @XmlElement(name = "Trandate")
    private String transDate;
    @XmlElement(name = "Trans_Id")
    private String transId;
    @XmlElement(name = "Trans_Desc")
    private String transDesc;
    @XmlElement(name = "Amount")
    private double amount;
    @XmlElement(name = "Curr")
    private String curr;
    @XmlElement(name = "Payer_Id")
    private String payerId;
    @XmlElement(name = "Payer_Name")
    private String payerName;
    @XmlElement(name = "Payer_Addr")
    private String payerAddress;
    @XmlElement(name = "Type")
    private String type;
    @XmlElement(name = "Custmer_Id")
    private String customerId;
    @XmlElement(name = "Customer_Name")
    private String customerName;
    @XmlElement(name = "IssueDate")
    private String issueDate;
    @XmlElement(name = "Channel_Id")
    private String channelId;
    @XmlElement(name = "Link_Type")
    private String linkType;
    @XmlElement(name = "Otp_Number")
    private String otpNumber;
    @XmlElement(name = "More_Info")
    private String moreInfo;
    @XmlElement(name = "Secure_Code")
    private String secureCode;

    public String dataForMD5() {
        return PRIVATE_KEY + "|" + this.serviceId + "|" + this.merchantId + "|" + this.merchantName
                + "|" + this.transDate + "|" + this.transId + "|" + this.transDesc + "|" + this.amount
                + "|" + this.curr + "|" + this.payerId + "|" + this.payerName + "|" + this.payerAddress
                + "|" + this.type + "|" + this.customerId + "|" + this.customerName + "|" + this.issueDate
                + "|" + this.channelId + "|" + this.linkType + "|" + otpNumber + "|" + this.moreInfo;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTransDesc() {
        return transDesc;
    }

    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurr() {
        return curr;
    }

    public void setCurr(String curr) {
        this.curr = curr;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerAddress() {
        return payerAddress;
    }

    public void setPayerAddress(String payerAddress) {
        this.payerAddress = payerAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getOtpNumber() {
        return otpNumber;
    }

    public void setOtpNumber(String otpNumber) {
        this.otpNumber = otpNumber;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getSecureCode() {
        return secureCode;
    }

    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }
}
