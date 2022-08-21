package gateway.core.channel.vcb_ib.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VerifyPaymentResponse extends BaseResponse {

    @JsonProperty(value = "redirectUrl")
    private String redirectUrl;

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
