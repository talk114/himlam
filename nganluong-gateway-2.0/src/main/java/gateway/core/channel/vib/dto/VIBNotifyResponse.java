package gateway.core.channel.vib.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class VIBNotifyResponse {
    @JsonProperty(value = "STATUSCODE")
    private String statusCode;

    @JsonProperty(value = "MESSAGE")
    private String message;

    @JsonProperty(value = "DATA")
    private Object data;
}
