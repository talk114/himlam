package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CardInquiryReq implements Serializable {
    @JsonProperty("ToSubscriptionID")
    private String to_subscriptionID;

    @JsonProperty("RefNumber")
    private String refNumber;

    @JsonProperty("ToSTBCardNo")
    private String to_StbCardNo;

    public String getTo_subscriptionID() {
        return to_subscriptionID;
    }

    public void setTo_subscriptionID(String to_subscriptionID) {
        this.to_subscriptionID = to_subscriptionID;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public String getTo_StbCardNo() {
        return to_StbCardNo;
    }

    public void setTo_StbCardNo(String to_StbCardNo) {
        this.to_StbCardNo = to_StbCardNo;
    }
}
