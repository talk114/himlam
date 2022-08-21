package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DomesticFundTransfer {
    @JsonProperty("SenderName")
    private String senderName;

    @JsonProperty("Amount")
    private String amount;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("RefNumber")
    private String refNumber;

    @JsonProperty("InqRefNumber")
    private String inqRefNumber;

    @JsonProperty("CardNumber")
    private String cardNumber;





}
