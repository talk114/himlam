package gateway.core.channel.msb_qr.dto.req;

import java.io.Serializable;

public class CreateQrCodeReq extends BaseRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String appId;

    private String merchantName;

    private String serviceCode;

    private String countryCode;

    private String masterMerCode;

    private String merchantCode;

    private String terminalId;

    private String payType;

    private String productId;

    private String txnId;

    private String amount;

    private String tipAndFee;

    private String ccy;

    private String expDate;

    private String desc;

    private String checksum;

    private String billNumber;

    private String creator;

    private String merchantType;

    private String consumerID;

    private String purpose;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMasterMerCode() {
        return masterMerCode;
    }

    public void setMasterMerCode(String masterMerCode) {
        this.masterMerCode = masterMerCode;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTipAndFee() {
        return tipAndFee;
    }

    public void setTipAndFee(String tipAndFee) {
        this.tipAndFee = tipAndFee;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getConsumerID() {
        return consumerID;
    }

    public void setConsumerID(String consumerID) {
        this.consumerID = consumerID;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    private static final String CHARACTER_SUM = "|";

    public String rawData(String secretKey) {
        return appId + CHARACTER_SUM + merchantName + CHARACTER_SUM + serviceCode + CHARACTER_SUM + countryCode
                + CHARACTER_SUM + masterMerCode + CHARACTER_SUM + merchantType + CHARACTER_SUM + merchantCode
                + CHARACTER_SUM + terminalId + CHARACTER_SUM + payType + CHARACTER_SUM + productId + CHARACTER_SUM
                + txnId + CHARACTER_SUM + amount + CHARACTER_SUM + tipAndFee + CHARACTER_SUM + ccy + CHARACTER_SUM
                + expDate + CHARACTER_SUM + secretKey;
    }
}
