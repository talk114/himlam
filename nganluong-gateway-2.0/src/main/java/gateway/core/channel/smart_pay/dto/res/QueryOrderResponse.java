package gateway.core.channel.smart_pay.dto.res;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

public class QueryOrderResponse extends BaseResponse implements Serializable {

    @JsonProperty(value = "transId")
    private String transId;
    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "amount")
    private int amount;
    @JsonProperty(value = "created")
    private String created;
    @JsonProperty(value = "orderNo")
    private String orderNo;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
