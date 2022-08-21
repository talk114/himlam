package gateway.core.channel.smart_pay.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CallNganLuongRequest {

    @JsonProperty(value = "error_code")
    private String errorCode;
    @JsonProperty(value = "error_message")
    private String message;
    @JsonProperty(value = "data")
    private Object data;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
