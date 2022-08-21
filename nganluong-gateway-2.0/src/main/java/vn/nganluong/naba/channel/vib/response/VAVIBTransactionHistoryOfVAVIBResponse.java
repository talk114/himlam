package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class VAVIBTransactionHistoryOfVAVIBResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -121581294215752189L;
	
	@JsonProperty("Result")
	private VAVIBTransactionHistoryOfVAVIBResultResponse result;

	public VAVIBTransactionHistoryOfVAVIBResultResponse getResult() {
		return result;
	}

	public void setResult(VAVIBTransactionHistoryOfVAVIBResultResponse result) {
		this.result = result;
	}

}
