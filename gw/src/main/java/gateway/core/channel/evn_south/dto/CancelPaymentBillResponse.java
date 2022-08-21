package gateway.core.channel.evn_south.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CancelPaymentBillResponse {

    @JsonProperty("CancelBillFeesByCustomerCodeResult")
    private String resultCode;
}
