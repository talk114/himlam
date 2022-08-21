package vn.nganluong.naba.channel.vib.request;

import java.io.Serializable;

public class ValidAccountVIBRequets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5621054491501429914L;

	private String account_no;
	private String account_type;
	
	private String bank_id;
	private String signed_data;

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}

	public String getSigned_data() {
		return signed_data;
	}

	public void setSigned_data(String signed_data) {
		this.signed_data = signed_data;
	}

	public String getBank_id() {
		return bank_id;
	}

	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}

}
