package vn.nganluong.naba.channel.vib.request;

import java.io.Serializable;

public class GetTransactionStatusVIBRequets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6652549413849934543L;

	private String signed_data;
	private String client_transaction_id;
	private String service_type;
	private String transaction_type;

	private String from_date;
	private String to_date;

	public String getSigned_data() {
		return signed_data;
	}

	public void setSigned_data(String signed_data) {
		this.signed_data = signed_data;
	}

	public String getClient_transaction_id() {
		return client_transaction_id;
	}

	public void setClient_transaction_id(String client_transaction_id) {
		this.client_transaction_id = client_transaction_id;
	}

	public String getService_type() {
		return service_type;
	}

	public void setService_type(String service_type) {
		this.service_type = service_type;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public String getFrom_date() {
		return from_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}

	public String getTo_date() {
		return to_date;
	}

	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}

}
