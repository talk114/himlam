package gateway.core.channel.anbinhbank.dto.req.body;


import com.fasterxml.jackson.annotation.JsonProperty;

public class PayWithTokenReq extends BaseRequest {

	private String customerId;

	private String amount;

	private String description;

	private String transactionTime;

	public PayWithTokenReq() {

	}

	public PayWithTokenReq(String partnerId, String customerId, Double amount, String description,
                           String transactionTime) {
		super();
		this.partnerId = partnerId;
		this.customerId = customerId;
		this.amount = df.format(amount);
		this.description = description;
		this.transactionTime = transactionTime;
	}

	@JsonProperty("CustomerId")
	public String getCustomerId() {
		return customerId;
	}

	@JsonProperty("user_id")
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@JsonProperty("TransactionAmount")
	public String getAmount() {
		return amount;
	}

	@JsonProperty("amount")
	public void setAmount(Double amount) {
		this.amount = df.format(amount);
	}

	@JsonProperty("Description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("TransactionTime")
	public String getTransactionTime() {
		return transactionTime;
	}

	@JsonProperty("trans_time")
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	@Override
	public String rawData() {
		return CHARACTER_SEPERATE + partnerId + CHARACTER_SEPERATE + customerId + CHARACTER_SEPERATE + amount
				+ CHARACTER_SEPERATE + description + CHARACTER_SEPERATE + transactionTime + CHARACTER_SEPERATE;
	}
}
