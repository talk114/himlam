package gateway.core.channel.mb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MBPaymentEcomDataResponse {

	@JsonProperty("requestId")
	private String requestId;

	@JsonProperty("ft")
	private String ft;

	@JsonProperty("referenceNumber")
	private String reference_number;

	@JsonProperty("way4ReferenceNumber")
	private String way4_reference_number;

	@JsonProperty("revertSuccess")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Boolean revert_success;

	@JsonProperty("rootID")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String[] root_id;

	public String getFt() {
		return ft;
	}

	public void setFt(String ft) {
		this.ft = ft;
	}

	public String getReference_number() {
		return reference_number;
	}

	public void setReference_number(String reference_number) {
		this.reference_number = reference_number;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Boolean isRevert_success() {
		return revert_success;
	}

	public void setRevert_success(Boolean revert_success) {
		this.revert_success = revert_success;
	}

	public String[] getRoot_id() {
		return root_id;
	}

	public void setRoot_id(String[] root_id) {
		this.root_id = root_id;
	}

	public String getWay4_reference_number() {
		return way4_reference_number;
	}

	public void setWay4_reference_number(String way4_reference_number) {
		this.way4_reference_number = way4_reference_number;
	}
}
