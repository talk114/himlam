package gateway.core.channel.napas.dto.request;


import gateway.core.channel.napas.dto.obj.InputParam;
import gateway.core.channel.napas.dto.obj.Order;

import java.io.Serializable;

public class GetDataKeyReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String apiOperation;

	private InputParam inputParameters;

	private Order order;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getApiOperation() {
		return apiOperation;
	}

	public void setApiOperation(String apiOperation) {
		this.apiOperation = apiOperation;
	}

	public InputParam getInputParameters() {
		return inputParameters;
	}

	public void setInputParameters(InputParam inputParameters) {
		this.inputParameters = inputParameters;
	}

	
}
