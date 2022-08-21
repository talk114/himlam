package gateway.core.channel.onepay.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 
 * @author anhnq
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyAuthenRes extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("vpc_SecureHash")
	private String vpcSecureHash;

	@JsonProperty("vpc_SecureHashCompare")
	private String vpcSecureHashCompare;

	@JsonProperty("vpc_AuthURL")
	private String vpcAuthURL;

	@JsonProperty("vpc_TicketNo")
	private String vpcTicketNo;

	@JsonProperty("vpc_BankId")
	private String vpcBankId;

	@JsonProperty("vpc_Otp")
	private String vpcOtp;

	@JsonProperty("vpc_ReturnURL")
	private String vpcReturnURL;

	@JsonProperty("vpc_CustomerUserAgent")
	private String vpcCustomerUserAgent;

	@JsonProperty("vpc_CurrencyCode")
	private String vpcCurrencyCode;

	private String amount;

	@JsonProperty("merchant_name")
	private String merchantName;

	private String language;

	@JsonProperty("merchant_id")
	private String merchantId;

	@JsonProperty("order_info")
	private String orderInfo;

	@JsonProperty("vpc_AdditionData")
	private String vpcAdditionData;

	public String getVpcSecureHash() {
		return vpcSecureHash;
	}

	public void setVpcSecureHash(String vpcSecureHash) {
		this.vpcSecureHash = vpcSecureHash;
	}

	public String getVpcSecureHashCompare() {
		return vpcSecureHashCompare;
	}

	public void setVpcSecureHashCompare(String vpcSecureHashCompare) {
		this.vpcSecureHashCompare = vpcSecureHashCompare;
	}

	public String getVpcAuthURL() {
		return vpcAuthURL;
	}

	public void setVpcAuthURL(String vpcAuthURL) {
		this.vpcAuthURL = vpcAuthURL;
	}

	public String getVpcTicketNo() {
		return vpcTicketNo;
	}

	public void setVpcTicketNo(String vpcTicketNo) {
		this.vpcTicketNo = vpcTicketNo;
	}

	public String getVpcBankId() {
		return vpcBankId;
	}

	public void setVpcBankId(String vpcBankId) {
		this.vpcBankId = vpcBankId;
	}

	public String getVpcOtp() {
		return vpcOtp;
	}

	public void setVpcOtp(String vpcOtp) {
		this.vpcOtp = vpcOtp;
	}

	public String getVpcReturnURL() {
		return vpcReturnURL;
	}

	public void setVpcReturnURL(String vpcReturnURL) {
		this.vpcReturnURL = vpcReturnURL;
	}

	public String getVpcCustomerUserAgent() {
		return vpcCustomerUserAgent;
	}

	public void setVpcCustomerUserAgent(String vpcCustomerUserAgent) {
		this.vpcCustomerUserAgent = vpcCustomerUserAgent;
	}

	public String getVpcCurrencyCode() {
		return vpcCurrencyCode;
	}

	public void setVpcCurrencyCode(String vpcCurrencyCode) {
		this.vpcCurrencyCode = vpcCurrencyCode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}

	public String getVpcAdditionData() {
		return vpcAdditionData;
	}

	public void setVpcAdditionData(String vpcAdditionData) {
		this.vpcAdditionData = vpcAdditionData;
	}


}
