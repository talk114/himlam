package vn.nganluong.naba.channel.vib.request;

import java.io.Serializable;

public class GetAccountBalanceVIBRequets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6652549413849934546L;

	// private String client_no;
	private String account_no;
	private String signed_data;

	// private String cif;
	// private String username;
	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getSigned_data() {
		return signed_data;
	}

	public void setSigned_data(String signed_data) {
		this.signed_data = signed_data;
	}

}
