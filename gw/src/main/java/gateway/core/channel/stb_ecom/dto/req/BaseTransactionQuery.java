package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BaseTransactionQuery {
    @JsonProperty("ProfileID")
    private String profileID;

    @JsonProperty("AccessKey")
    private String accessKey;

    @JsonProperty("RequestID")
    private String requestID;

    @JsonProperty("Data")
    private String data;

    @JsonProperty("Signature")
    private String signature;
}
