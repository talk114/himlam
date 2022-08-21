package gateway.core.channel.smart_pay.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CreateOrderRequest extends SmartPayBaseRequest implements Serializable {

    @JsonProperty(value = "orderNo")
    private String orderNo;

    @JsonProperty(value = "amount")
    private int amount;

    @JsonProperty(value = "requestType")
    private String requestType;

    @JsonProperty(value = "notifyUrl")
    private String notifyUrl;

    @JsonProperty(value = "created")
    private String created;

    @JsonProperty(value = "desc")
    private String desc;

    public CreateOrderRequest(String channel, String subChannel, String signature) {
        super(channel, subChannel, signature);
    }

    public CreateOrderRequest() {
        super();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
