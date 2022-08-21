package gateway.core.channel.cybersouce.request;

import java.io.Serializable;

/**
 *
 * @author Zon
 */
public class AuthorizeCard implements Serializable {

    private String transactionReferenceId;
    private String orderCurrency;
    private long amount;
    private Card card;
    private BillAddress billAddress;
    private boolean disable3DSecure;
    private int gatewayMID;
    private String merchantCode;

    public String getTransactionReferenceId() {
        return transactionReferenceId;
    }

    public void setTransactionReferenceId(String transactionReferenceId) {
        this.transactionReferenceId = transactionReferenceId;
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

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
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

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    @Override
    public String toString() {
        return "AuthorizeCard{" + "transactionReferenceId=" + transactionReferenceId + ", orderCurrency=" + orderCurrency + ", amount=" + amount + ", card=" + card + ", billAddress=" + billAddress.toString() + ", disable3DSecure=" + disable3DSecure + ", gatewayMID=" + gatewayMID +  ", merchantCode=" + merchantCode + '}';
    }
}