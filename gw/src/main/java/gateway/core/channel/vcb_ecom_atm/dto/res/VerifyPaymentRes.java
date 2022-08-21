package gateway.core.channel.vcb_ecom_atm.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VerifyPaymentRes extends BaseResponse {

	private String otpPhoneNumber;

	private String hashCode;

	@JsonProperty("otp_phone_number")
	public String getOtpPhoneNumber() {
		return otpPhoneNumber;
	}

	@JsonProperty("otpPhoneNumber")
	public void setOtpPhoneNumber(String otpPhoneNumber) {
		this.otpPhoneNumber = otpPhoneNumber;
	}

	@JsonProperty("hash_code")
	public String getHashCode() {
		return hashCode;
	}

	@JsonProperty("HashCode")
	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

}
