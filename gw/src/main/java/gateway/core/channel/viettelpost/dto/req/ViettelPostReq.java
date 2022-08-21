package gateway.core.channel.viettelpost.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViettelPostReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("func")
	private String function;

	@JsonProperty("access_code")
	private String accessCode;

	@JsonProperty("service_code")
	private String serviceCode;

	@JsonProperty("amount")
	private String amount;

	@JsonProperty("payment_id")
	private String paymentId;

	@JsonProperty("request_id")
	private String requestId;

	@JsonProperty("date_time")
	private String dateTime;

	@JsonProperty("checksum")
	private String checksum;

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String dataBeforeChecksum(String key, String func) {
		if(func.equals("inquery")  | func.equals("query")) {
			return function + accessCode + key + serviceCode + paymentId + requestId + dateTime;			
		}else if(func.equals("payment")) {
			return function + accessCode + key + serviceCode + paymentId + amount + requestId + dateTime;
		}
		return null;
		
	}
}
