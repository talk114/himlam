package gateway.core.channel.evn_south.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckBillResponse {
    @JsonProperty("CUSTOMERCODE")
    private String customerCode;

    @JsonProperty("HOADONID")
    private String receiptID;

    @JsonProperty("AMOUNT")
    private String amount;

    @JsonProperty("PAYDATE")
    private String payDate;

    @JsonProperty("POSID")
    private String posID;
}
