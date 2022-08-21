package gateway.core.channel.vcb_ecom_atm.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RootRequest {

	@JsonProperty("PartnerId")
	private String partnerId;
	
	@JsonProperty("TransactionID")
	private String transactionID;
	
	@JsonProperty("Data")
	private String data;
	
	@JsonProperty("Signature")
	private String signature;

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	
}
