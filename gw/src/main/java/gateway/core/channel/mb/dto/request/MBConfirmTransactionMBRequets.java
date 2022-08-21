package gateway.core.channel.mb.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MBConfirmTransactionMBRequets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4914540885669014105L;
	private String client_transaction_id;
	private String otp;
	private String request_id;

	public String getClient_transaction_id() {
		return client_transaction_id;
	}

	public void setClient_transaction_id(String client_transaction_id) {
		this.client_transaction_id = client_transaction_id;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getRequest_id() {
		return request_id;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}

}
