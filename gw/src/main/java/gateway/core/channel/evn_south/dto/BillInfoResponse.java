package gateway.core.channel.evn_south.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillInfoResponse {

    @JsonProperty("TransactionCode")
    private String transactionCode;

    @JsonProperty("Status")
    private String status;
}
