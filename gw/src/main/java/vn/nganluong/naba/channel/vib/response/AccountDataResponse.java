package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class AccountDataResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 843355342846499361L;

	@JsonProperty(value = "CCY")
	private String ccy;

	@JsonProperty(value = "ACCTDESC")
	private String acct_desc;

	@JsonProperty(value = "ACCTNAME")
	private String acct_name;

	@JsonProperty(value = "STATUS")
	private String status;

	public String getCcy() {
		return ccy;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy;
	}

	public String getAcct_desc() {
		return acct_desc;
	}

	public void setAcct_desc(String acct_desc) {
		this.acct_desc = acct_desc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAcct_name() {
		return acct_name;
	}

	public void setAcct_name(String acct_name) {
		this.acct_name = acct_name;
	}

}
