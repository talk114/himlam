package gateway.core.channel.cybersouce.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SubData {
    private String requestToken;
    private String subscriptionId;
    private String accessToken;
    private String resultMessage;
    private String deviceDataCollectionURL;
    private String referenceID;
    private String transactionReferenceId;

    @JsonProperty(value = "3DSTransactionID")
    private String transactionID3ds;
    private String eci;
    private String version;

    @JsonProperty(value = "TransactionID")
    private String transactionID;
    private String acsURL;
    private String paresStatus;
    private String commerceIndicator;

    @JsonProperty(value = "cavv/avv")
    private String cavv;
    private String xid;
    private String veresEnrolled;
    private String paReq;
}
