package gateway.core.channel.msb_ecom.dto.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InquiryTransactionResponse extends BaseResponse implements Serializable {

    @JsonProperty(value = "transactionId")
    private String transactionId;

    @JsonProperty(value = "paymentType")
    private String paymentType;

    @JsonProperty(value = "merchTxnRef")
    private String merchTxnRef;

    @JsonProperty(value = "orderInfo")
    private String orderInfo;

    @JsonProperty(value = "amount")
    private String amount;


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getMerchTxnRef() {
        return merchTxnRef;
    }

    public void setMerchTxnRef(String merchTxnRef) {
        this.merchTxnRef = merchTxnRef;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
