package gateway.core.channel.stb_ecom.dto.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author vinhnt
 *
 */


@SuppressWarnings("serial")
public class BaseRequest implements Serializable {

	@JsonProperty(value = "Data")
	protected String data;
	
	@JsonProperty(value = "FunctionName")
	protected String functionName;
	
	@JsonProperty(value = "RequestDateTime")
	protected String requestDateTime;
	
	@JsonProperty(value = "RequestID")
	protected String requestID;
	
	@JsonIgnore
	private String transId;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

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

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}
	
	
}
