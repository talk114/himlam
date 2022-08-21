package gateway.core.channel.napas.dto.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order2 {
    private String amount;
    private String creationTime;
    private String currency;
    private String id;
    private String reference;
    private String totalAuthorizedAmount;
    private String totalCapturedAmount;
    private String totalRefundedAmount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTotalAuthorizedAmount() {
        return totalAuthorizedAmount;
    }

    public void setTotalAuthorizedAmount(String totalAuthorizedAmount) {
        this.totalAuthorizedAmount = totalAuthorizedAmount;
    }

    public String getTotalCapturedAmount() {
        return totalCapturedAmount;
    }

    public void setTotalCapturedAmount(String totalCapturedAmount) {
        this.totalCapturedAmount = totalCapturedAmount;
    }

    public String getTotalRefundedAmount() {
        return totalRefundedAmount;
    }

    public void setTotalRefundedAmount(String totalRefundedAmount) {
        this.totalRefundedAmount = totalRefundedAmount;
    }
}
