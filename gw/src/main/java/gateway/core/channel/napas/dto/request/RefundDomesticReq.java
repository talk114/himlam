package gateway.core.channel.napas.dto.request;


import gateway.core.channel.napas.dto.obj.MerchantOrderInfo;

public class RefundDomesticReq {

	private String apiOperation;
	private MerchantOrderInfo transaction;
	private String channel;

	public String getApiOperation() {
		return apiOperation;
	}

	public void setApiOperation(String apiOperation) {
		this.apiOperation = apiOperation;
	}

	public MerchantOrderInfo getTransaction() {
		return transaction;
	}

	public void setTransaction(MerchantOrderInfo transaction) {
		this.transaction = transaction;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

}
