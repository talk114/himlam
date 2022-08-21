package gateway.core.channel.smart_pay.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SendOTPResponse extends BaseResponse implements Serializable {
    @JsonProperty(value = "transId")
    private String transId;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }
}
