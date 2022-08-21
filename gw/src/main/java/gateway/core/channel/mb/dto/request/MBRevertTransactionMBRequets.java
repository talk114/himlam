package gateway.core.channel.mb.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MBRevertTransactionMBRequets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5164659578743697279L;
	private String client_transaction_id;
	private String amount;

	public String getClient_transaction_id() {
		return client_transaction_id;
	}

	public void setClient_transaction_id(String client_transaction_id) {
		this.client_transaction_id = client_transaction_id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
