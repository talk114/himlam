package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ValidAccountVIBResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2011031739020374580L;
	
	@JsonProperty("Result")
	private ValidAccountVIBDataResponse result;

	public ValidAccountVIBDataResponse getResult() {
		return result;
	}

	public void setResult(ValidAccountVIBDataResponse result) {
		this.result = result;
	}

	
}
