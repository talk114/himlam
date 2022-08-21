package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 
 * @author vinhnt
 *
 */

@SuppressWarnings("serial")
public class BaseCardlessReq implements Serializable {

	@JsonProperty("SenderName")
	private String senderName;
	
	@JsonProperty("RecipientMobile")
	private String recipientMobile;
	
	@JsonProperty("Amount")
	private String amount;
	
	@JsonProperty("Description")
	private String description;
	
	@JsonProperty("RefNumber")
	private String refNumber;

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getRecipientMobile() {
		return recipientMobile;
	}

	public void setRecipientMobile(String recipientMobile) {
		this.recipientMobile = recipientMobile;
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
