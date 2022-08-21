package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class TransferToSTBCardReq implements Serializable {

    @JsonProperty("SenderName")
    private String senderName;

    @JsonProperty("RefNumber")
    private String refNumber;

    @JsonProperty("InqRefNumber")
    private String inqRefNumber;

    @JsonProperty("Amount")
    private String amount;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("ToSubscriptionID")
    private String to_subscriptionID;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public String getInqRefNumber() {
        return inqRefNumber;
    }

    public void setInqRefNumber(String inqRefNumber) {
        this.inqRefNumber = inqRefNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTo_subscriptionID() {
        return to_subscriptionID;
    }

    public void setTo_subscriptionID(String to_subscriptionID) {
        this.to_subscriptionID = to_subscriptionID;
    }
}
