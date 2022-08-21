package gateway.core.channel.vcb_ecom_atm.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VerifyOtpReq extends BaseRequest {

	@JsonProperty("HashCode")
	private String hashCode;
	
	private String cardNumber;
	
	private String cardHolderName;
	
	private String cardDate;
	
	@JsonProperty("OTP")
	private String otp;

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getCardDate() {
		return cardDate;
	}

	public void setCardDate(String cardDate) {
		this.cardDate = cardDate;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}
	
	
}
