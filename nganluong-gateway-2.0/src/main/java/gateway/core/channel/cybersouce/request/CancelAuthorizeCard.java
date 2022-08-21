package gateway.core.channel.cybersouce.request;

import java.io.Serializable;

/**
 *
 * @author TaiND
 */
public class CancelAuthorizeCard implements Serializable {

    private String transactionReferenceId;
    private String subscriptionId;
    private int gatewayMID;

    public String getTransactionReferenceId() {
        return transactionReferenceId;
    }

    public void setTransactionReferenceId(String transactionReferenceId) {
        this.transactionReferenceId = transactionReferenceId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public int getGatewayMID() {
        return gatewayMID;
    }

    public void setGatewayMID(int gatewayMID) {
        this.gatewayMID = gatewayMID;
    }

    @Override
    public String toString() {
        return "CancelAuthorizeCard{" + "transactionReferenceId=" + transactionReferenceId + ", subscriptionId=" + subscriptionId + ", gatewayMID=" + gatewayMID + '}';
    }
}
