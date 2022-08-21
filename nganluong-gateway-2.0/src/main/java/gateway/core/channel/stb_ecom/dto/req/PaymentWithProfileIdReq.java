package gateway.core.channel.stb_ecom.dto.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author vinhnt
 *
 */


@SuppressWarnings("serial")
public class PaymentWithProfileIdReq implements Serializable {

	@JsonProperty("IsAuthentication")
	private boolean authentication;

	@JsonProperty("IsEnrollment")
	private boolean enrollment;

	@JsonProperty("IsValidation")
	private boolean validation;

	@JsonProperty("OTP")
	private String otp;

	@JsonProperty("CustomerID")
	private String customerID;

	@JsonProperty("SubscriptionID")
	private String SubscriptionID;

	@JsonProperty("Amount")
	private String amount;

	@JsonProperty("Description")
	private String description;

	@JsonProperty("RefNumber")
	private String refNumber;

	public boolean isAuthentication() {
		return authentication;
	}

	public void setAuthentication(boolean authentication) {
		this.authentication = authentication;
	}

	public boolean isEnrollment() {
		return enrollment;
	}

	public void setEnrollment(boolean enrollment) {
		this.enrollment = enrollment;
	}

	public boolean isValidation() {
		return validation;
	}

	public void setValidation(boolean validation) {
		this.validation = validation;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getSubscriptionID() {
		return SubscriptionID;
	}

	public void setSubscriptionID(String subscriptionID) {
		SubscriptionID = subscriptionID;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}
	
	
}
