package gateway.core.channel.viettelpay.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PayOrderQrcodeRes extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("order_id")
	private String orderId;

	@JsonProperty("billcode")
	private String billcode;

	@JsonProperty("merchant_code")
	private String merchantCode;

	@JsonProperty("error_code")
	private String errorCode;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getBillcode() {
		return billcode;
	}

	public void setBillcode(String billcode) {
		this.billcode = billcode;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String rawData(String accessCode) {
		return new StringBuilder().append(accessCode).append(errorCode).append(merchantCode).append(orderId).toString();
	}
}
