package gateway.core.channel.anbinhbank.dto.req.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LinkAccountReq extends BaseRequest {

	private String customerId;

	private String identityNumber;

	private String accountNumber;

	private String accountName;

	private String simType = "";

	private String transactionTime;

	public LinkAccountReq() {

	}

	public LinkAccountReq(String partnerId, String customerId, String identityNumber, String accountNumber,
                          String accountName, String transactionTime) {
		super();
		this.partnerId = partnerId;
		this.customerId = customerId;
		this.identityNumber = identityNumber;
		this.accountNumber = accountNumber;
		this.accountName = accountName;
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

	@JsonProperty("IdentityNumber")
	public String getIdentityNumber() {
		return identityNumber;
	}

	@JsonProperty("customer_id_no")
	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	@JsonProperty("AccountNumber")
	public String getAccountNumber() {
		return accountNumber;
	}

	@JsonProperty("account_number")
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@JsonProperty("AccountName")
	public String getAccountName() {
		return accountName;
	}

	@JsonProperty("account_holder_name")
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@JsonProperty("SimType")
	public String getSimType() {
		return simType;
	}

	public void setSimType(String simType) {
		this.simType = simType;
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
		return CHARACTER_SEPERATE + partnerId + CHARACTER_SEPERATE + customerId + CHARACTER_SEPERATE + identityNumber + CHARACTER_SEPERATE
				+ accountNumber + CHARACTER_SEPERATE + accountName + CHARACTER_SEPERATE + simType + CHARACTER_SEPERATE
				+ transactionTime + CHARACTER_SEPERATE;
	}
}
