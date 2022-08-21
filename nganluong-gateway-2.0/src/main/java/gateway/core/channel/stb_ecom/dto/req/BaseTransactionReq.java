package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BaseTransactionReq implements Serializable {

    @JsonProperty("IsRequiredOTPBySTB")
    private  String isRequiredOTPBySTB;

    @JsonProperty("IsByPassOTP")
    private String  isByPassOTP;

    @JsonProperty("AuthType")
    private String authType;

    @JsonProperty("OTP")
    private String otp;

    @JsonProperty("CustomerID")
    private String customerId;

    @JsonProperty("SubscriptionID")
    private String subscriptionID;

    @JsonProperty("RefNumber")
    private String refNumber;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("InqRefNumber")
    private String inqRefNumber;

    public String getIsRequiredOTPBySTB() {
        return isRequiredOTPBySTB;
    }

    public void setIsRequiredOTPBySTB(String isRequiredOTPBySTB) {
        this.isRequiredOTPBySTB = isRequiredOTPBySTB;
    }

    public String getIsByPassOTP() {
        return isByPassOTP;
    }

    public void setIsByPassOTP(String isByPassOTP) {
        this.isByPassOTP = isByPassOTP;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInqRefNumber() {
        return inqRefNumber;
    }

    public void setInqRefNumber(String inqRefNumber) {
        this.inqRefNumber = inqRefNumber;
    }
}
