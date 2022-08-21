package gateway.core.channel.stb_ecom.dto.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author vinhnt
 *
 */


@SuppressWarnings("serial")
public class UnLinkCardReq implements Serializable{

	@JsonProperty("CustomerID")
	private String customerID;
	
	@JsonProperty("SubscriptionID")
	private String subscriptionID;
	
	@JsonProperty("Description")
	private String description;
	
	@JsonProperty("RefNumber")
	private String refNumber;

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getSubscriptionID() {
		return subscriptionID;
	}

	public void setSubscriptionID(String subscriptionID) {
		this.subscriptionID = subscriptionID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}
	
	
}
