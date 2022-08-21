package gateway.core.channel.smart_pay.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RootResponse implements Serializable {

    @JsonProperty(value = "code")
    private String code;

    @JsonProperty(value = "requestId")
    private String requestId;

    @JsonProperty(value = "data")
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
