package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BaseWithdrawReq  implements Serializable {
    @JsonProperty("CustomerID")
    private String customerId;

    @JsonProperty("SubscriptionID")
    private String subscriptionID;

    @JsonProperty("RefNumber")
    private String refNumber;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }
}
