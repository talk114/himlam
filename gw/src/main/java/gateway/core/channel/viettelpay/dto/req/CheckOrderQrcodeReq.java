package gateway.core.channel.viettelpay.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CheckOrderQrcodeReq extends BaseRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// @JsonProperty("order_id")
	// private String orderId;
	//
	// @JsonProperty("billcode")
	// private String billcode;

	@JsonProperty("merchant_code")
	private String merchantCode;

	@JsonProperty("trans_amount")
	private String transAmount;

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}

	@Override
	public String rawData(String accessCode) {
		return new StringBuilder().append(accessCode).append(billcode).append(merchantCode).append(orderId)
				.append(transAmount).toString();
	}
}
