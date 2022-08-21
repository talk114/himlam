package gateway.core.channel.napas.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class QueryTransDomesticReq implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "trans_id")
    protected String transId;
    @JsonProperty(value = "order_id")
    private String orderId;
    @JsonProperty(value = "merchantCode")
    private String merchantCode;

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}
