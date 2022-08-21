package gateway.core.channel.napas.dto.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantOrderResponse implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private String acquirerCode;
	private String gatewayCode;
	private String message;

	@JsonProperty("acquirer_code")
	public String getAcquirerCode() {
		return acquirerCode;
	}

	@JsonProperty("acquirerCode")
	public void setAcquirerCode(String acquirerCode) {
		this.acquirerCode = acquirerCode;
	}

	@JsonProperty("trans_status")
	public String getGatewayCode() {
		return gatewayCode;
	}

	@JsonProperty("gatewayCode")
	public void setGatewayCode(String gatewayCode) {
		this.gatewayCode = gatewayCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
