package gateway.core.channel.napas.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.io.Serializable;

public class BaseResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String apiOperation;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	protected String result;
	
	@JsonProperty("merchant_id")
	protected String merchantId;

	@JsonProperty("api_operation")
	public String getApiOperation() {
		return apiOperation;
	}

	@JsonProperty("apiOperation")
	public void setApiOperation(String apiOperation) {
		this.apiOperation = apiOperation;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	
	
}
