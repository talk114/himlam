package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransactionQuery {
    @JsonProperty("TransactionID")
    private String transactionID;

    @JsonProperty("TransactionDateTime")
    private String transactionDateTime;

    @JsonProperty("IsQuery")
    private String isQuery;


}
