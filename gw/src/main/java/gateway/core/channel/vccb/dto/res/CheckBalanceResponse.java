package gateway.core.channel.vccb.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckBalanceResponse extends ApiResponse{

	@JsonProperty("balance")
	private String balance;

	public CheckBalanceResponse() {
	}

	public CheckBalanceResponse(String balance) {
		this.balance = balance;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}
}
