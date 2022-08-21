package gateway.core.channel.stb_ecom.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionQueryRes{
    @JsonProperty("RequestID")
    private String requestID;
    @JsonProperty("Success")
    private boolean success;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("IsSubscribeOnly")
    private boolean isSubscribeOnly;
    @JsonProperty("IsTokenRequest")
    private boolean isTokenRequest;
}
