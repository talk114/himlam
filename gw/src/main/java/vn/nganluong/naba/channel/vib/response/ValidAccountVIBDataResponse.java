package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ValidAccountVIBDataResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4490273195868164194L;

	@JsonProperty(value = "DATA")
	private AccountDataResponse data;

	@JsonProperty(value = "STATUSCODE")
	private String status_code;

	public AccountDataResponse getData() {
		return data;
	}

	public void setData(AccountDataResponse data) {
		this.data = data;
	}

	public String getStatus_code() {
		return status_code;
	}

	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}

}
