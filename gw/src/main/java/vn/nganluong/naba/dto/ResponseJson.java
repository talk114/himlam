package vn.nganluong.naba.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ResponseJson {

//	private int result;

//	private String status;

	@JsonProperty(value = "gateway_error_code")
	private String error_code;

	@JsonProperty(value = "gateway_message")
	private String message;

	@JsonProperty(value = "bank_error_code")
	private String bank_error_code;

	@JsonProperty(value = "bank_message")
	private String bank_message;

	public ResponseJson() {
		super();
	}

//	public ResponseJson(int result, String status, String error_code, String message) {
//		super();
//		this.result = result;
//		this.status = status;
//		this.error_code = error_code;
//		this.message = message;
//	}

	public ResponseJson(String error_code, String message) {
		super();
		this.error_code = error_code;
		this.message = message;
	}

//	public int getResult() {
//		return result;
//	}
//
//	public void setResult(int result) {
//		this.result = result;
//	}
//
//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getBank_error_code() {
		return bank_error_code;
	}

	public void setBank_error_code(String bank_error_code) {
		this.bank_error_code = bank_error_code;
	}

	public String getBank_message() {
		return bank_message;
	}

	public void setBank_message(String bank_message) {
		this.bank_message = bank_message;
	}
}
