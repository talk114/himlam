package gateway.core.channel.dong_a_bank.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DABNotifyEcomRes {

    @JsonProperty("ErrorCode")
    private String errorCode;

    @JsonProperty("Url")
    private String url;

    @JsonProperty("Sign")
    private String sign;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
