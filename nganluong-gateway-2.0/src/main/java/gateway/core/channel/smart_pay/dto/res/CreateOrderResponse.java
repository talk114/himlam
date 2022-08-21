package gateway.core.channel.smart_pay.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CreateOrderResponse extends BaseResponse implements Serializable {

    @JsonProperty(value = "payload")
    private String payload;
    @JsonProperty(value = "prepayId")
    private String prepayId;
    @JsonProperty(value = "orderNo")
    private String orderNo;

    public CreateOrderResponse() {
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
