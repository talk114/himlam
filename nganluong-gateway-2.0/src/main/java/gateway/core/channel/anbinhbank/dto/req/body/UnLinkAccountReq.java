package gateway.core.channel.anbinhbank.dto.req.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnLinkAccountReq extends BaseRequest {

	private String customerId;

	private String transactionTime;

	public UnLinkAccountReq() {

	}

	public UnLinkAccountReq(String partnerId, String customerId, String identityNumber, String accountNumber,
                            String accountName, String transactionTime) {
		super();
		this.partnerId = partnerId;
		this.customerId = customerId;
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
		return CHARACTER_SEPERATE + partnerId + CHARACTER_SEPERATE + customerId + CHARACTER_SEPERATE
				+ transactionTime + CHARACTER_SEPERATE;
	}
}
