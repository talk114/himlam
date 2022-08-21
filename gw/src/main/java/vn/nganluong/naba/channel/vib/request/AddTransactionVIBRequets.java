package vn.nganluong.naba.channel.vib.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class AddTransactionVIBRequets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1098301832961809551L;

	private String amount;
	private String ccy;
	private String client_no; // cif
	private String narrative;
	private String service_id;
	private String transaction_type;
	private String user_id; // user_name

	private String from_account;
	private String to_account;

	private String account_name;
	private String client_transaction_id;
	private String feeside;

	private String ben_name;
	private String ben_bank_id;
	private String ben_bank_name;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCcy() {
		return ccy;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy;
	}

	public String getClient_no() {
		return client_no;
	}

	public void setClient_no(String client_no) {
		this.client_no = client_no;
	}

	public String getNarrative() {
		return narrative;
	}

	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getFrom_account() {
		return from_account;
	}

	public void setFrom_account(String from_account) {
		this.from_account = from_account;
	}

	public String getTo_account() {
		return to_account;
	}

	public void setTo_account(String to_account) {
		this.to_account = to_account;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getClient_transaction_id() {
		return client_transaction_id;
	}

	public void setClient_transaction_id(String client_transaction_id) {
		this.client_transaction_id = client_transaction_id;
	}

	public String getFeeside() {
		return feeside;
	}

	public void setFeeside(String feeside) {
		this.feeside = feeside;
	}

	public String getBen_name() {
		return ben_name;
	}

	public void setBen_name(String ben_name) {
		this.ben_name = ben_name;
	}

	public String getBen_bank_id() {
		return ben_bank_id;
	}

	public void setBen_bank_id(String ben_bank_id) {
		this.ben_bank_id = ben_bank_id;
	}

	public String getBen_bank_name() {
		return ben_bank_name;
	}

	public void setBen_bank_name(String ben_bank_name) {
		this.ben_bank_name = ben_bank_name;
	}

}
