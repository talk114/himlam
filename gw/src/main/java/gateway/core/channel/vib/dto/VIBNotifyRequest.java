package gateway.core.channel.vib.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VIBNotifyRequest {

    @JsonProperty(value = "seq_no")
    private long sequenceNumber;

    @JsonProperty(value = "virtual_acct")
    private String virtualAccount;

    @JsonProperty(value = "actual_acct")
    private String actualAccount; //Mã số khách hàng


    @JsonProperty(value = "tran_amt")
    private long transactionAmount;

    @JsonProperty(value = "ccy")
    private String currency;

    @JsonProperty(value = "tran_date_time")
    private String transactionDate;

    @JsonProperty(value = "narrative")
    private String transactionDescription;

    @JsonProperty(value = "tran_type")
    private String transactionType;

    @JsonProperty(value = "trace_id")
    private String transactionId;
}
