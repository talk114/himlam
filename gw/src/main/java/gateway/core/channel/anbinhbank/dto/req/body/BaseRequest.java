package gateway.core.channel.anbinhbank.dto.req.body;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.text.DecimalFormat;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseRequest {

	protected static final DecimalFormat df = new DecimalFormat("#");
	protected static final String CHARACTER_SEPERATE = "|";

	protected String partnerId;
	
	protected String merchantId;
	
	protected String merchantName;

	@JsonProperty(value = "trans_id", access = Access.WRITE_ONLY)
	protected String transId;

	@JsonProperty("PartnerId")
	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	
	@JsonProperty("MerchantId")
	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	@JsonProperty("MerchantName")
	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String rawData() {
		return "";
	}

}
