package gateway.core.channel.smart_pay.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class VerifyOTPRequest extends SmartPayBaseRequest implements Serializable {

    @JsonProperty(value = "phone")
    private String phone;
    @JsonProperty(value = "otp")
    private String otp;
    @JsonProperty(value = "transId")
    private String transId;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }
}
