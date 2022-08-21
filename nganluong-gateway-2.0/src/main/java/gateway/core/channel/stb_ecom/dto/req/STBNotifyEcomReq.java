package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class STBNotifyEcomReq {
    @JsonProperty("TransactionID")
    private String transactionID;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("Token")
    private String token;

    @JsonProperty("CardNumber")
    private String cardNumber;

    @JsonProperty("AccountNo")
    private String accountNo;

    @JsonProperty("ExpiryDate")
    private String expiryDate;

    @JsonProperty("SubscriptionSource")
    private String subscriptionSource;

    @JsonProperty("Signature")
    private String signature;

    @JsonProperty("SubscriptionType")
    private String subscriptionType;

    @JsonProperty("CardType")
    private String cardType;







}
