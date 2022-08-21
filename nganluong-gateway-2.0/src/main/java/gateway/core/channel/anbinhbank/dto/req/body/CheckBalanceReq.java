package gateway.core.channel.anbinhbank.dto.req.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckBalanceReq extends BaseRequest {

	private String accountNumber;
	
	public CheckBalanceReq(){
		
	}

	public CheckBalanceReq(String partnerId, String accountNumber) {
		super();
		this.partnerId = partnerId;
		this.accountNumber = accountNumber;
	}

	@JsonProperty("AccountNumber")
	public String getAccountNumber() {
		return accountNumber;
	}

	@JsonProperty("account_number")
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public String rawData(){
		return CHARACTER_SEPERATE + partnerId + CHARACTER_SEPERATE + accountNumber + CHARACTER_SEPERATE;
	}
}
