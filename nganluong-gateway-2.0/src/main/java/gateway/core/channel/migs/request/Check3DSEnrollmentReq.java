package gateway.core.channel.migs.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Check3DSEnrollmentReq {
    private String apiOperation;
    @JsonProperty("3DSecureId")
    private String _3DSecureId;
    private String responseUrl;
    private String orderAmount;
    private String orderCurrency;
    private String sessionId;
}
