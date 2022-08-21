package gateway.core.channel.stb_ecom.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseDataRes {

	@JsonProperty("RefNumber")
	private String transId;
	
	@JsonProperty("RequestID")
	private String requestId;

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	
}
