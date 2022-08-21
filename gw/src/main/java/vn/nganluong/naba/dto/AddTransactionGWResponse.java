package vn.nganluong.naba.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class AddTransactionGWResponse extends ResponseJson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -917422271004806961L;

	private String transaction_id;

	private String request_id;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String ft;

	public String getFt() {
		return ft;
	}

	public void setFt(String ft) {
		this.ft = ft;
	}

	public String getRequest_id() {
		return request_id;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

}
