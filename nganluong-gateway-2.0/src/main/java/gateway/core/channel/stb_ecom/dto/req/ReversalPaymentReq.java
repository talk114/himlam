package gateway.core.channel.stb_ecom.dto.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author vinhnt
 *
 */

@SuppressWarnings("serial")
public class ReversalPaymentReq implements Serializable {

	@JsonProperty("SSN")
	private String ssn;

	@JsonProperty("FullName")
	private String fullName;

	@JsonProperty("CardNumber")
	private String cardNumber;

	@JsonProperty("Amount")
	private String amount;

	@JsonProperty("Description")
	private String description;

	@JsonProperty("RefNumber")
	private String refNumber;

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
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
