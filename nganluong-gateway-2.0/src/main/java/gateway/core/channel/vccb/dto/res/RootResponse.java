package gateway.core.channel.vccb.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class RootResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("responseID")
	private String responseID;

	@JsonProperty("status")
	private String status;

	@JsonProperty("errorCode")
	private String errorCode;

	@JsonProperty("data")
	private String data;

	@JsonProperty("errorMessage")
	private String errorMessage;

	@JsonProperty("sig")
	private String sig;

	public RootResponse() {
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSig() {
		return sig;
	}

	public void setSig(String sig) {
		this.sig = sig;
	}

	public String getResponseID() {
		return responseID;
	}

	public void setResponseID(String responseID) {
		this.responseID = responseID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String rawData() {
		return StringUtils.defaultString(responseID, "null") + "|" + StringUtils.defaultString(status, "null") + "|"
				+ StringUtils.defaultString(errorCode, "null") + "|" + StringUtils.defaultString(errorMessage, "null")
				+ "|" + StringUtils.defaultString(data, "null");
	}
}
