package gateway.core.channel.msb_onus.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author sonln
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class NotifyTransStatusReq {
    @JsonProperty("transId")
    String transId;
    @JsonProperty("status")
    boolean success;
}
