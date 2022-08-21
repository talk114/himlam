package gateway.core.channel.bidv.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vinhnt
 *
 */
@XmlRootElement(name = "ISO8583")
@XmlType(propOrder = { "mti", "customerId", "serviceCode", "amount", "requestDateTime", "auditNumber", "payDateTime",
        "channelCode", "functionCode", "senderCode", "areaCode", "responseCode", "acceptorTerminalId",
        "moreResponseData", "customerInfo", "currencyCode", "securityCode", "transactionOldInfo",
        "reservedForPrivateUser", "billTransId", "signature" })
public class Message8583 {

    private String mti;
    private String customerId = "";
    private String serviceCode = "";
    private String amount = "";
    private String requestDateTime = "";
    private String auditNumber = "";
    private String payDateTime = ""; // YYMMDD Ngày thanh toán
    private String channelCode = "";
    private String functionCode = "";
    private String senderCode = "";
    private String areaCode = "";
    private String responseCode = "";
    private String acceptorTerminalId = "";
    private String moreResponseData = "";
    private String customerInfo = "";
    private String currencyCode = "";
    private String securityCode = "";
    private String transactionOldInfo = "";
    private String reservedForPrivateUser = "";
    private String billTransId = "";
    private String signature;

    public String getMti() {
        return mti;
    }

    @XmlElement(name = "MTI")
    public void setMti(String mti) {
        this.mti = mti;
    }

    public String getCustomerId() {
        return customerId;
    }

    @XmlElement(name = "f2")
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    @XmlElement(name = "f3")
    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getAmount() {
        return amount;
    }

    @XmlElement(name = "f4")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRequestDateTime() {
        return requestDateTime;
    }

    @XmlElement(name = "f7")
    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public String getAuditNumber() {
        return auditNumber;
    }

    @XmlElement(name = "f11")
    public void setAuditNumber(String auditNumber) {
        this.auditNumber = auditNumber;
    }

    public String getPayDateTime() {
        return payDateTime;
    }

    @XmlElement(name = "f12")
    public void setPayDateTime(String payDateTime) {
        this.payDateTime = payDateTime;
    }

    public String getChannelCode() {
        return channelCode;
    }

    @XmlElement(name = "f22")
    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    @XmlElement(name = "f24")
    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    @XmlElement(name = "f34")
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getResponseCode() {
        return responseCode;
    }

    @XmlElement(name = "f39")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getAcceptorTerminalId() {
        return acceptorTerminalId;
    }

    @XmlElement(name = "f41")
    public void setAcceptorTerminalId(String acceptorTerminalId) {
        this.acceptorTerminalId = acceptorTerminalId;
    }

    public String getMoreResponseData() {
        return moreResponseData;
    }

    @XmlElement(name = "f44")
    public void setMoreResponseData(String moreResponseData) {
        this.moreResponseData = moreResponseData;
    }

    public String getCustomerInfo() {
        return customerInfo;
    }

    @XmlElement(name = "f48")
    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getSenderCode() {
        return senderCode;
    }

    @XmlElement(name = "f32")
    public void setSenderCode(String senderCode) {
        this.senderCode = senderCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    @XmlElement(name = "f49")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    @XmlElement(name = "f53")
    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getTransactionOldInfo() {
        return transactionOldInfo;
    }

    @XmlElement(name = "f56")
    public void setTransactionOldInfo(String transactionOldInfo) {
        this.transactionOldInfo = transactionOldInfo;
    }

    public String getReservedForPrivateUser() {
        return reservedForPrivateUser;
    }

    @XmlElement(name = "f61")
    public void setReservedForPrivateUser(String reservedForPrivateUser) {
        this.reservedForPrivateUser = reservedForPrivateUser;
    }

    public String getBillTransId() {
        return billTransId;
    }

    @XmlElement(name = "f62")
    public void setBillTransId(String billTransId) {
        this.billTransId = billTransId;
    }

    public String getSignature() {
        return signature;
    }

    @XmlElement(name = "f63")
    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String dataBeforeSignature() {
        return customerId + "|" + serviceCode + "|" + amount + "|" + requestDateTime + "|" + auditNumber + "|" + payDateTime + "|" + channelCode
                + "|" + functionCode + "|" + senderCode + "|" + areaCode + "|" + responseCode + "|" + acceptorTerminalId + "|" + moreResponseData
                + "|" + customerInfo + "|" + currencyCode + "|" + securityCode + "|" + transactionOldInfo + "|" + reservedForPrivateUser + "|" + billTransId;
    }
}
