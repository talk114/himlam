package gateway.core.channel.vcb_ecom_atm.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefundReq extends BaseRequest {

	@JsonProperty("RefundSources")
	private String refundSources;

	@JsonProperty("RefundTranID")
	private String refundTranId;

	@JsonProperty("TransactionID")
	private String transactionId;

	@JsonProperty("MerchantID")
	private String merchantID; // ma doi tac

	@JsonProperty("MID")
	private String mid;

	@JsonProperty("RefundAmount")
	private String refundAmount; //

	@JsonProperty("CurrencyCode")
	public String currencyCode; // loai tien giao dich

	@JsonProperty("PartnerID")
	public String partnerID; // ma partner

	@JsonProperty("PartnerPassword")
	public String partnerPassword; // mat khau partner

	public String getRefundSources() {
		return refundSources;
	}

	public void setRefundSources(String refundSources) {
		this.refundSources = refundSources;
	}

	public String getRefundTranId() {
		return refundTranId;
	}

	public void setRefundTranId(String refundTranId) {
		this.refundTranId = refundTranId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getPartnerID() {
		return partnerID;
	}

	public void setPartnerID(String partnerID) {
		this.partnerID = partnerID;
	}

	public String getPartnerPassword() {
		return partnerPassword;
	}

	public void setPartnerPassword(String partnerPassword) {
		this.partnerPassword = partnerPassword;
	}

}
