package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VisaInquiry {
    @JsonProperty("CardNumber")
    private String cardNumber;

    @JsonProperty("CardToken")
    private String cardToken;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("RefNumber")
    private String refNumber;


}
