package gateway.core.channel.vccb.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckBalanceBankAccIBFTResp extends ApiResponse{
	@JsonProperty("balance")
	private String balance;
	
	@JsonProperty("responseTime")
	private String responseTime;
	
	@JsonProperty("description")
	private String description;

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
