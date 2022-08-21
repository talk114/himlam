package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import vn.nganluong.naba.dto.ResponseJson;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class VAVIBTransactionHistoryOfVAGWResponse extends ResponseJson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5430209950487442035L;

	private String sync_time;

	private String total_record;

	@JsonProperty("history_list")
	private List<VAVIBTransactionHistoryOfVAVIBDetailResponse> data;

	public String getSync_time() {
		return sync_time;
	}

	public void setSync_time(String sync_time) {
		this.sync_time = sync_time;
	}

	public List<VAVIBTransactionHistoryOfVAVIBDetailResponse> getData() {
		return data;
	}

	public void setData(List<VAVIBTransactionHistoryOfVAVIBDetailResponse> data) {
		this.data = data;
	}

	public String getTotal_record() {
		return total_record;
	}

	public void setTotal_record(String total_record) {
		this.total_record = total_record;
	}
}
