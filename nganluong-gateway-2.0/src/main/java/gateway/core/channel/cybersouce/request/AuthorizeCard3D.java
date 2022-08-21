package gateway.core.channel.cybersouce.request;

import java.io.Serializable;

/**
 *
 * @author TaiND
 */
public class AuthorizeCard3D implements Serializable {

    private String transactionReferenceId;
    private String orderCurrency;
    private Card card;
    private BillAddress billAddress;
    private String signedPARes;
    private int gatewayMID;

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

    @Override
    public String toString() {
        return "AuthorizeCard3D{" + "transactionReferenceId=" + transactionReferenceId + ", orderCurrency=" + orderCurrency + ", card=" + card + ", billAddress=" + billAddress.toString() + ", signedPARes=" + signedPARes + ", gatewayMID=" + gatewayMID + '}';
    }
}