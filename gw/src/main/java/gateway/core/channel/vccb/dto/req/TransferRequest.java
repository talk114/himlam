package gateway.core.channel.vccb.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferRequest {
	
	@JsonProperty("accountNo")
	private String accountNo;
	
	@JsonProperty("cardNo")
	private String cardNo;
	
	@JsonProperty("bankCode")
	private String bankCode;
	
	@JsonProperty("onus")
	private String onus;
	
	@JsonProperty("amount")
	private String amount;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("feeModel")
	private String feeModel;

	public TransferRequest() {
		super();
	}
	

	public String getFeeModel() {
		return feeModel;
	}



	public void setFeeModel(String feeModel) {
		this.feeModel = feeModel;
	}



	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getOnus() {
		return onus;
	}

	public void setOnus(String onus) {
		this.onus = onus;
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
	
	
	
}
