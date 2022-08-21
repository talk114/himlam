package gateway.core.channel.mb.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MBStatusTransactionMBRequets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2286589913906562072L;
	private String client_transaction_id;

	public String getClient_transaction_id() {
		return client_transaction_id;
	}

	public void setClient_transaction_id(String client_transaction_id) {
		this.client_transaction_id = client_transaction_id;
	}

}
