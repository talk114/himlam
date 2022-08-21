package gateway.core.channel.viettelpay.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseResponse {
	
	@JsonProperty("check_sum")
	protected String checkSum;

	public String getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}

	public String rawData(String accessCode) {
		return "";
	}
}
