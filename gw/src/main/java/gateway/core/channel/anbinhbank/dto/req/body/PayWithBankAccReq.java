package gateway.core.channel.anbinhbank.dto.req.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

public class PayWithBankAccReq extends BaseRequest {

	private String billId;

	private String accountNumber;

	private String accountName;

	private String identityNumber;

	private String customerPhoneNo;

	private String amount;

	private String transactionTime;

	private String currency;

	private String description;

	private String ipAddress;

	@JsonProperty("BillId")
	public String getBillId() {
		return billId;
	}

	@JsonProperty("bill_no")
	public void setBillId(String billId) {
		this.billId = billId;
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

	@JsonProperty("IdentityNumber")
	public String getIdentityNumber() {
		return identityNumber;
	}

	@JsonProperty("customer_id_no")
	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	@JsonProperty("PhoneNumber")
	public String getCustomerPhoneNo() {
		return customerPhoneNo;
	}

	@JsonProperty("customer_phone_no")
	public void setCustomerPhoneNo(String customerPhoneNo) {
		this.customerPhoneNo = customerPhoneNo;
	}

	@JsonProperty("TransactionAmount")
	public String getAmount() {
		return amount;
	}

	@JsonProperty("amount")
	public void setAmount(Double amount) {
		this.amount = df.format(amount);
	}

	@JsonProperty("TransactionTime")
	public String getTransactionTime() {
		return transactionTime;
	}

	@JsonProperty("trans_time")
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	@JsonProperty("Currency")
	public String getCurrency() {
		return currency;
	}

	@JsonProperty("currency_code")
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@JsonProperty("Description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("IpAddress")
	public String getIpAddress() {
		return ipAddress;
	}

	@JsonProperty("client_ip")
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Override
	public String rawData() {
		return CHARACTER_SEPERATE + billId + CHARACTER_SEPERATE + accountNumber + CHARACTER_SEPERATE + accountName
				+ CHARACTER_SEPERATE + identityNumber + CHARACTER_SEPERATE + customerPhoneNo + CHARACTER_SEPERATE
				+ amount + CHARACTER_SEPERATE + currency + CHARACTER_SEPERATE
				+ StringUtils.defaultString(description, "") + CHARACTER_SEPERATE + merchantId + CHARACTER_SEPERATE
				+ merchantName + CHARACTER_SEPERATE + partnerId + CHARACTER_SEPERATE + ipAddress + CHARACTER_SEPERATE
				+ transactionTime + CHARACTER_SEPERATE;
	}
}
