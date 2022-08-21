package gateway.core.channel.vccb.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferResponse extends ApiResponse{
	@JsonProperty("settleDate")
	private String settleDate;

	public String getSettleDate() {
		return settleDate;
	}

	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}
	
	
}
