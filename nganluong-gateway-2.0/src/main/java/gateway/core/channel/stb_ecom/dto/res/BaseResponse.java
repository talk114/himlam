package gateway.core.channel.stb_ecom.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 
 * @author vinhnt
 *
 */

@SuppressWarnings("serial")
public class BaseResponse implements Serializable {

	@JsonProperty("FunctionName")
	private String functionName;
	
	@JsonProperty("RequestDateTime")
	private String requestDateTime;
	
	@JsonProperty("RequestID")
	private String requestID;
	
	@JsonProperty("Description")
	private String description;
	
	@JsonProperty("RespCode")
	private String respCode;
	
	@JsonProperty("Data")
	private String data;

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getRequestDateTime() {
		return requestDateTime;
	}

	public void setRequestDateTime(String requestDateTime) {
		this.requestDateTime = requestDateTime;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
	
	
}
