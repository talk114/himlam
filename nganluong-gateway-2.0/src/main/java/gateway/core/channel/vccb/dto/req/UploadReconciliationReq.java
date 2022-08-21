package gateway.core.channel.vccb.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadReconciliationReq {
	@JsonProperty("processingDate")
	private Integer processingDate;
	
	@JsonProperty("data")
	private String data;
	
	@JsonProperty("description")
	private String description;
	

	public UploadReconciliationReq() {
		super();
	}

	public Integer getProcessingDate() {
		return processingDate;
	}

	public void setProcessingDate(Integer processingDate) {
		this.processingDate = processingDate;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
