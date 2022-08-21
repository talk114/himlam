package gateway.core.channel.cybersouce.request;

import java.io.Serializable;

/**
 *
 * @author TaiND
 */
public class Authorize implements Serializable {

    private String transactionReferenceId;
    private double paymentAmount;
    private String orderCurrency;
    private Card card;
    private BillAddress billAddress;
    private boolean disable3DSecure;
    private int gatewayMID;
    private String merchantDescriptor = "";
    private String authenCode = "";
    private String merchantName = "";
    private String merchantType = "";
    private long merchantId;
    private String subMtid = "";
    private String nameMtid = "";

    public String getTransactionReferenceId() {
        return transactionReferenceId;
    }

    public void setTransactionReferenceId(String transactionReferenceId) {
        this.transactionReferenceId = transactionReferenceId;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getOrderCurrency() {
        return orderCurrency;
    }

    public void setOrderCurrency(String orderCurrency) {
        this.orderCurrency = orderCurrency;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public BillAddress getBillAddress() {
        return billAddress;
    }

    public void setBillAddress(BillAddress billAddress) {
        this.billAddress = billAddress;
    }

    public boolean isDisable3DSecure() {
        return disable3DSecure;
    }

    public void setDisable3DSecure(boolean disable3DSecure) {
        this.disable3DSecure = disable3DSecure;
    }

    public int getGatewayMID() {
        return gatewayMID;
    }

    public void setGatewayMID(int gatewayMID) {
        this.gatewayMID = gatewayMID;
    }

    public String getMerchantDescriptor() {
        return merchantDescriptor;
    }

    public void setMerchantDescriptor(String merchantDescriptor) {
        this.merchantDescriptor = merchantDescriptor;
    }

    public String getAuthenCode() {
        return authenCode;
    }

    public void setAuthenCode(String authenCode) {
        this.authenCode = authenCode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public String getSubMtid() {
        return subMtid;
    }

    public void setSubMtid(String subMtid) {
        this.subMtid = subMtid;
    }

    public String getNameMtid() {
        return nameMtid;
    }

    public void setNameMtid(String nameMtid) {
        this.nameMtid = nameMtid;
    }

    @Override
    public String toString() {
        return "Authorize{" + "transactionReferenceId=" + transactionReferenceId + ", paymentAmount=" + paymentAmount + ", orderCurrency=" + orderCurrency + ", card=" + card + ", billAddress=" + billAddress + ", disable3DSecure=" + disable3DSecure + ", gatewayMID=" + gatewayMID + ", merchantDescriptor=" + merchantDescriptor + ", authenCode=" + authenCode + ", merchantName=" + merchantName + ", merchantType=" + merchantType +  ", merchantId=" + merchantId + '}';
    }
}