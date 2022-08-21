package gateway.core.channel.vccb.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponse {
	
	@JsonProperty("bank_trans_id")
	protected String bankTrasId;

	public String getBankTrasId() {
		return bankTrasId;
	}

	public void setBankTrasId(String bankTrasId) {
		this.bankTrasId = bankTrasId;
	}
	
}
