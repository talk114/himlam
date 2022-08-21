package gateway.core.channel.vccb.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CheckBalanceRequest {
	@JsonProperty("accountNo")
	private String accountNo;
	
	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
}
