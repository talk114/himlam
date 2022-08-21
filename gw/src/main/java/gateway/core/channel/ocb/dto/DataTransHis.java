package gateway.core.channel.ocb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataTransHis {
    private Conditions conditions;
}

@Data
class Conditions {
    private TransactionTime transactionTime;

    @JsonProperty("AccountNumber")
    private String accountNumber;
}

@Data
class TransactionTime {
    private String fromDate;
    private String toDate;
}