package gateway.core.channel.vccb_va.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"bankTime", "data", "msgId", "responseCode", "responseMsg", "requestId", "to"})
public class VCCBResponse {
    private long bankTime;
    private Object data;
    private String msgId;

    @JsonProperty(value= "rCode")
    private String responseCode;

    @JsonProperty(value = "rMsg")
    private String responseMsg;

    private String requestId;
    private String to;
}
