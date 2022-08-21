package gateway.core.channel.msb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import gateway.core.channel.msb_ecom.dto.MSBEcomConstant;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyTransactionRequest extends BaseRequest implements Serializable {

    @JsonProperty(value = "transId")
    private String transId;

    @JsonProperty(value = "merchantID")
    private String merchantID;

    @JsonProperty(value = "otp")
    private String otp;

    @JsonProperty(value = "secureHash")
    private String secureHash;

    // build tham số chuỗi mã hóa
    public String buildDataRaw(String accessCode) {
        return accessCode + this.merchantID + this.transId + this.otp;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getSecureHash() {
        return secureHash;
    }

    public void setSecureHash(String secureHash) {
        this.secureHash = secureHash;
    }
}
