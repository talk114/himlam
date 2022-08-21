package gateway.core.channel.migs.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment3DS1Req {
    private String apiOperation;
    @JsonProperty("3DSecureId")
    private String _3DSecureId;
    private String orderId;
    private String orderReference;
    private String sessionId;
    private String transId;
    private String transReference;
}
