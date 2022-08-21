package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;

import vn.nganluong.naba.dto.ResponseJson;

public class ValidAccountGWResponse extends ResponseJson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6737228227385410967L;

	
	private String account_name;
	
	private String account_status;


	public String getAccount_name() {
		return account_name;
	}


	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}


	public String getAccount_status() {
		return account_status;
	}


	public void setAccount_status(String account_status) {
		this.account_status = account_status;
	}
	
	
	
	
}
