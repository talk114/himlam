package gateway.core.channel.smart_pay.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class NotificationRequest implements Serializable {

    @JsonProperty(value = "requestId")
    private String requestId;
    @JsonProperty(value = "transId")
    private String transId;
    @JsonProperty(value = "orderNo")
    private String orderNo;
    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "amount")
    private int amount;
    @JsonProperty(value = "created")
    private String created;
    @JsonProperty(value = "signature")
    private String signature;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
