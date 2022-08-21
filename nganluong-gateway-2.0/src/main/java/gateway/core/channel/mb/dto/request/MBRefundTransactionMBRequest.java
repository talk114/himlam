package gateway.core.channel.mb.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MBRefundTransactionMBRequest implements Serializable {

    private static final long serialVersionUID = 32201439434L;

    private long amount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long feeAmount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ftNumber;
    private String transactionId;
}
