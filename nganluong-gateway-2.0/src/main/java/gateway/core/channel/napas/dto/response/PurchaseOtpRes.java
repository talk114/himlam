package gateway.core.channel.napas.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import gateway.core.channel.napas.dto.obj.Error;
import gateway.core.channel.napas.dto.obj.PaymentResultPO;
import gateway.core.channel.napas.dto.obj.TokenResultPO;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseOtpRes extends VerifyOTPTokenRes {
    @JsonProperty("tokenResult")
    private TokenResultPO tokenResult;

    @JsonProperty("paymentResult")
    private PaymentResultPO paymentResult;

    @JsonProperty("error")
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public TokenResultPO getTokenResult() {
        return tokenResult;
    }

    public void setTokenResult(TokenResultPO tokenResult) {
        this.tokenResult = tokenResult;
    }

    public PaymentResultPO getPaymentResult() {
        return paymentResult;
    }

    public void setPaymentResult(PaymentResultPO paymentResult) {
        this.paymentResult = paymentResult;
    }
}
