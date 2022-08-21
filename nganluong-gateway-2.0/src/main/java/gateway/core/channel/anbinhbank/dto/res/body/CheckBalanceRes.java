package gateway.core.channel.anbinhbank.dto.res.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckBalanceRes extends BaseResponse {

	private String balance;

	@JsonProperty("balance")
	public String getBalance() {
		return balance;
	}

	@JsonProperty("AvailableBalance")
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	@Override
	public String rawData(){
		return super.rawData() + balance + CHARACTER_SEPERATE;
	}
}
