package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;

import vn.nganluong.naba.dto.ResponseJson;

public class VirtualAccountCreateGWResponse extends ResponseJson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2938005163239360598L;

	private String virtual_account_code;

	public String getVirtual_account_code() {
		return virtual_account_code;
	}

	public void setVirtual_account_code(String virtual_account_code) {
		this.virtual_account_code = virtual_account_code;
	}

}
