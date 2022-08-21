package gateway.core.channel.napas.dto.response;

import gateway.core.channel.napas.dto.obj.*;

public class RefundDomesticRes extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RefundAuthorizationResponse authorizationResponse;
	
	private MerchantOrderInfo order;
	
	private MerchantOrderResponse response;
	
	private SourceOfFund sourceOfFunds;
	
	private String timeOfRecord;
	
	private NapasTransInfo transaction;
	
	private String channel;
	
	private String version;

	public RefundAuthorizationResponse getAuthorizationResponse() {
		return authorizationResponse;
	}

	public void setAuthorizationResponse(RefundAuthorizationResponse authorizationResponse) {
		this.authorizationResponse = authorizationResponse;
	}

	public MerchantOrderInfo getOrder() {
		return order;
	}

	public void setOrder(MerchantOrderInfo order) {
		this.order = order;
	}

	public MerchantOrderResponse getResponse() {
		return response;
	}

	public void setResponse(MerchantOrderResponse response) {
		this.response = response;
	}

	public SourceOfFund getSourceOfFunds() {
		return sourceOfFunds;
	}

	public void setSourceOfFunds(SourceOfFund sourceOfFunds) {
		this.sourceOfFunds = sourceOfFunds;
	}

	public String getTimeOfRecord() {
		return timeOfRecord;
	}

	public void setTimeOfRecord(String timeOfRecord) {
		this.timeOfRecord = timeOfRecord;
	}

	public NapasTransInfo getTransaction() {
		return transaction;
	}

	public void setTransaction(NapasTransInfo transaction) {
		this.transaction = transaction;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
	
}
