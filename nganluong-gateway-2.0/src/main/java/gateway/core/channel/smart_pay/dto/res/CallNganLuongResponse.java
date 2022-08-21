package gateway.core.channel.smart_pay.dto.res;

import java.io.Serializable;

public class CallNganLuongResponse implements Serializable {

    private String message;
    private String errorCode;

    public CallNganLuongResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
