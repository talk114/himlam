package gateway.core.channel.viettelpay.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PayOrderQrcodeReq extends BaseRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("cust_msisdn")
	private String custMsisdn;

	@JsonProperty("error_code")
	private String errorCode;

	@JsonProperty("merchant_code")
	private String merchantCode;

	@JsonProperty("payment_status")
	private int paymentStatus;

	@JsonProperty("trans_amount")
	private int transAmount;

	@JsonProperty("vt_transaction_id")
	private String vtTransactionId;

	public String getCustMsisdn() {
		return custMsisdn;
	}

	public void setCustMsisdn(String custMsisdn) {
		this.custMsisdn = custMsisdn;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public int getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public int getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(int transAmount) {
		this.transAmount = transAmount;
	}

	public String getVtTransactionId() {
		return vtTransactionId;
	}

	public void setVtTransactionId(String vtTransactionId) {
		this.vtTransactionId = vtTransactionId;
	}

	@Override
	public String rawData(String accessCode) {
		return new StringBuilder().append(accessCode).append(billcode).append(StringUtils.defaultString(custMsisdn, ""))
				.append(errorCode).append(merchantCode).append(orderId).append(paymentStatus).append(transAmount)
				.append(vtTransactionId).toString();
	}

}
