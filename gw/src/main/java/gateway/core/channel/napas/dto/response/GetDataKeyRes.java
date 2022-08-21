package gateway.core.channel.napas.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import gateway.core.channel.napas.dto.obj.Order;

public class GetDataKeyRes extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Order order;

	private String dataKey;

	private String napasKey;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@JsonProperty("data_key")
	public String getDataKey() {
		return dataKey;
	}

	@JsonProperty("dataKey")
	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	@JsonProperty("napas_key")
	public String getNapasKey() {
		return napasKey;
	}

	@JsonProperty("napasKey")
	public void setNapasKey(String napasKey) {
		this.napasKey = napasKey;
	}

}
