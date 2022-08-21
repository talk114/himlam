package gateway.core.channel.migs.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrowserPayment {
    private String returnUrl;
    private String paymentOperation;
    private String paymentConfirmation;

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getPaymentOperation() {
        return paymentOperation;
    }

    public void setPaymentOperation(String paymentOperation) {
        this.paymentOperation = paymentOperation;
    }

    public String getPaymentConfirmation() {
        return paymentConfirmation;
    }

    public void setPaymentConfirmation(String paymentConfirmation) {
        this.paymentConfirmation = paymentConfirmation;
    }
}
