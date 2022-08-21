package gateway.core.channel.napas.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gateway.core.channel.napas.dto.obj.PaymentResult;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NapasNotifyResultReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String data;

	private String checksum;

	private PaymentResult paymentResult;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public PaymentResult getPaymentResult() {
		return paymentResult;
	}

	public void setPaymentResult(PaymentResult paymentResult) {
		this.paymentResult = paymentResult;
	}

}
