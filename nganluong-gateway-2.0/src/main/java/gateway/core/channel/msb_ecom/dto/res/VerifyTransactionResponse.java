package gateway.core.channel.msb_ecom.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyTransactionResponse extends BaseResponse implements Serializable {

    @JsonProperty(value = "transactionId")
    private String transactionId;

    @JsonProperty(value = "amount")
    private String amount;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
