package vn.nganluong.naba.channel.vib.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class VAVIBGetListVirtualAccountResultVIBResponse implements Serializable {

	@JsonProperty("STATUSCODE")
	private String status_code;

	@JsonProperty("TOTALRECORD")
	private String total_record;

	@JsonProperty("DATA")
	private List<VAVIBVirtualAccountDetailVIBResponse> data;

	public String getStatus_code() {
		return status_code;
	}

	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}

	public String getTotal_record() {
		return total_record;
	}

	public void setTotal_record(String total_record) {
		this.total_record = total_record;
	}

	public List<VAVIBVirtualAccountDetailVIBResponse> getData() {
		return data;
	}

	public void setData(List<VAVIBVirtualAccountDetailVIBResponse> data) {
		this.data = data;
	}
}
