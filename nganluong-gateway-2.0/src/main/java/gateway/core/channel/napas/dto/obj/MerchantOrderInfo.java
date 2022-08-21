package gateway.core.channel.napas.dto.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MerchantOrderInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String amount;
	private String creationTime;
	private String currency;
	private String id;
	private String reference;
	private String totalAuthorizedAmount;
	private String totalCapturedAmount;
	private String totalRefundedAmount;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@JsonProperty("trans_time")
	public String getCreationTime() {
		return creationTime;
	}

	@JsonProperty("creationTime")
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@JsonProperty("trans_id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getTotalAuthorizedAmount() {
		return totalAuthorizedAmount;
	}

	public void setTotalAuthorizedAmount(String totalAuthorizedAmount) {
		this.totalAuthorizedAmount = totalAuthorizedAmount;
	}

	public String getTotalCapturedAmount() {
		return totalCapturedAmount;
	}

	public void setTotalCapturedAmount(String totalCapturedAmount) {
		this.totalCapturedAmount = totalCapturedAmount;
	}

	public String getTotalRefundedAmount() {
		return totalRefundedAmount;
	}

	public void setTotalRefundedAmount(String totalRefundedAmount) {
		this.totalRefundedAmount = totalRefundedAmount;
	}

	
}
