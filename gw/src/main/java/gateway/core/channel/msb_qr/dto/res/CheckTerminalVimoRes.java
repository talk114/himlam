package gateway.core.channel.msb_qr.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckTerminalVimoRes {

    // {"errorCode":"SUCCESS","errorDescription":"Th\u00e0nh
    // c\u00f4ng","data":{"terminalId":"vimo2018","name":"vimo2018","merchant_id":"1100000","supplier_code":"MARITIMEBANK","supplier_name":"Maritime
    // Bank"}}
    // {"errorCode":"ERROR","errorDescription":"L\u1ed7i kh\u00f4ng x\u00e1c \u0111\u1ecbnh","data":[]}
    private String errorCode;

    private String errorDescription;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

}
