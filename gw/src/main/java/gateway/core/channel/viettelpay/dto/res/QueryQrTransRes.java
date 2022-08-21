package gateway.core.channel.viettelpay.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryQrTransRes extends BaseResponse{

	@JsonProperty("merchant_code")
	private String merchantCode;
	
	@JsonProperty("order_id")
	private String orderId;
	
	@JsonProperty("payment_status")
	private String paymentStatus;
	
	private String version;
	
	@JsonProperty("secure_hash")
	private String checkSum;
	
	@JsonProperty("error_code")
	private String errorCode;
	
	@JsonProperty("vt_transaction_id")
	private String vtTransactionId;

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getVtTransactionId() {
		return vtTransactionId;
	}

	public void setVtTransactionId(String vtTransactionId) {
		this.vtTransactionId = vtTransactionId;
	}
	
	@Override
	public String rawData(String accessCode) {
		return new StringBuilder().append(accessCode).append(merchantCode).append(orderId).append(paymentStatus).append(version).toString();
	}
}
