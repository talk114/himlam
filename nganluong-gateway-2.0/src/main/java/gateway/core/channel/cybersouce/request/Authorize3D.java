package gateway.core.channel.cybersouce.request;

import java.io.Serializable;

/**
 *
 * @author TaiND
 */
public class Authorize3D implements Serializable {

    private String transactionReferenceId;
    private double paymentAmount;
    private String orderCurrency;
    private Card card;
    private BillAddress billAddress;
    private String signedPARes;
    private int gatewayMID;
    private String merchantDescriptor;
    private String authenCode;
    private String merchantName;
    private String merchantType;
    private long merchantId;
    private String subMtid;
    private String nameMtid;
    private String referenceID;
    private PurchaseTotals purchaseTotals;

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

    public String getSignedPARes() {
        return signedPARes;
    }

    public void setSignedPARes(String signedPARes) {
        this.signedPARes = signedPARes;
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

    public PurchaseTotals getPurchaseTotals() {
        return purchaseTotals;
    }

    public void setPurchaseTotals(PurchaseTotals purchaseTotals) {
        this.purchaseTotals = purchaseTotals;
    }

    public String getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(String referenceID) {
        this.referenceID = referenceID;
    }

    @Override
    public String toString() {
        return "Authorize3D{" + "transactionReferenceId=" + transactionReferenceId + ", paymentAmount=" + paymentAmount + ", orderCurrency=" + orderCurrency + ", card=" + card + ", billAddress=" + billAddress + ", signedPARes=" + signedPARes + ", gatewayMID=" + gatewayMID + ", merchantDescriptor=" + merchantDescriptor + ", authenCode=" + authenCode + ", merchantName=" + merchantName + ", merchantType=" + merchantType + ", merchantId=" + merchantId + '}';
    }
}