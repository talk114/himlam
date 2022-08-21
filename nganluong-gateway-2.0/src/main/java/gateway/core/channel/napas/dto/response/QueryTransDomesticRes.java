package gateway.core.channel.napas.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import gateway.core.channel.napas.dto.obj.*;
import gateway.core.channel.napas.dto.obj.Error;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryTransDomesticRes extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private String merchantId;
	
	private String version;
	
	private MerchantOrderInfo order;
	
	private MerchantOrderResponse response;
	
	private SourceOfFund sourceOfFunds;
	
	private NapasTransInfo transaction;
	
	private Error error;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@JsonProperty("merchant_order_info")
	public MerchantOrderInfo getOrder() {
		return order;
	}

	@JsonProperty("order")
	public void setOrder(MerchantOrderInfo order) {
		this.order = order;
	}

	@JsonProperty("merchant_order_response")
	public MerchantOrderResponse getResponse() {
		return response;
	}

	@JsonProperty("response")
	public void setResponse(MerchantOrderResponse response) {
		this.response = response;
	}

	public SourceOfFund getSourceOfFunds() {
		return sourceOfFunds;
	}

	public void setSourceOfFunds(SourceOfFund sourceOfFunds) {
		this.sourceOfFunds = sourceOfFunds;
	}

	@JsonProperty("napas_trans_info")
	public NapasTransInfo getTransaction() {
		return transaction;
	}

	@JsonProperty("transaction")
	public void setTransaction(NapasTransInfo transaction) {
		this.transaction = transaction;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}
	
	
}
