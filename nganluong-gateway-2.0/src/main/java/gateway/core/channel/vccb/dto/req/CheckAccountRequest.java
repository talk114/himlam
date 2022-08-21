package gateway.core.channel.vccb.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckAccountRequest {
	
	@JsonProperty("accountNo")
	private String accountNo;
	
	@JsonProperty("cardNo")
	private String cardNo;
	
	@JsonProperty("bankCode")
	private String bankCode;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("onus")
	private String onus;

	public CheckAccountRequest() {
		super();
	}
	
	public String getOnus() {
		return onus;
	}

	public void setOnus(String onus) {
		this.onus = onus;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
