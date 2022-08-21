package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class VAVIBTransactionHistoryOfVAVIBResultResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5564529526607973013L;

	@JsonProperty("STATUSCODE")
	private String status_code;

	@JsonProperty("TOTALRECORD")
	private String total_record;

	@JsonProperty("SYNCTIME")
	private String sync_time;

	@JsonProperty("DATA")
	private List<VAVIBTransactionHistoryOfVAVIBDetailResponse> data;

	public String getSync_time() {
		return sync_time;
	}

	public void setSync_time(String sync_time) {
		this.sync_time = sync_time;
	}

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

	public List<VAVIBTransactionHistoryOfVAVIBDetailResponse> getData() {
		return data;
	}

	public void setData(List<VAVIBTransactionHistoryOfVAVIBDetailResponse> data) {
		this.data = data;
	}

}
