package vn.nganluong.naba.channel.vib.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class VAVIBGetListVirtualAccountVIBResponse implements Serializable {

	@JsonProperty("Result")
	private VAVIBGetListVirtualAccountResultVIBResponse result;

	public VAVIBGetListVirtualAccountResultVIBResponse getResult() {
		return result;
	}

	public void setResult(VAVIBGetListVirtualAccountResultVIBResponse result) {
		this.result = result;
	}
}
