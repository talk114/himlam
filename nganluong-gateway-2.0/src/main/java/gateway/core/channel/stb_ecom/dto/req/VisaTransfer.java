package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VisaTransfer {
    @JsonProperty("CardToken")
    private String cardToken;

    @JsonProperty("Amount")
    private String amount;

    @JsonProperty("SenderName")
    private String senderName;

    @JsonProperty("RefNumber")
    private String refNumber;

    @JsonProperty("InqRefNumber")
    private String inqRefNumber;

    @JsonProperty("Description")
    private String description;

}
