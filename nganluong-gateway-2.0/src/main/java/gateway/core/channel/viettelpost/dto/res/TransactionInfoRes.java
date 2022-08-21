package gateway.core.channel.viettelpost.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionInfoRes {

	@JsonProperty("payment_id")
	private String paymentId;

	private String amount;

	@JsonProperty("time_created")
	private String timeCreated;

	public TransactionInfoRes() {
	}

	public TransactionInfoRes(String paymentId, String amount, String timeCreated) {
		this.paymentId = paymentId;
		this.amount = amount;
		this.timeCreated = timeCreated;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(String timeCreated) {
		this.timeCreated = timeCreated;
	}

}
