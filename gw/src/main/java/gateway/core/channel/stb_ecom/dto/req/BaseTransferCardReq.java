package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 
 * @author vinhnt
 *
 */


@SuppressWarnings("serial")
public class BaseTransferCardReq implements Serializable{

	
	@JsonProperty("CardNumber")
	private String cardNumber;
	
	@JsonProperty("Amount")
	private String amount;
	
	@JsonProperty("Description")
	private String description;
	
	@JsonProperty("RefNumber")
	private String refNumber;

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
