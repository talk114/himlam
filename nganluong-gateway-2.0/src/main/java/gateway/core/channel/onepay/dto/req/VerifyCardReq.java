package gateway.core.channel.onepay.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 
 * @author anhnq
 * 
 */
@SuppressWarnings("serial")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyCardReq implements Serializable{
	
	@JsonProperty("vpc_Version")
	private Long vpcVersion;
	
	@JsonProperty("vpc_Command")
	private String vpcCommand;
	
	@JsonProperty("vpc_AccessCode")
	private String vpcAccessCode;
	
	@JsonProperty("vpc_Merchant")
	private String vpcMerchant;
	
	@JsonProperty("vpc_MerchTxnRef")
	private String vpcMerchTxnRef;
		
	@JsonProperty("vpc_OrderInfo")
	private String vpcOrderInfo;	
	
	@JsonProperty("vpc_Amount")
	private String vpcAmount;
	
	@JsonProperty("vpc_Locale")
	private String vpcLocale;
	
	@JsonProperty("vpc_Currency")
	private String vpcCurrency;
	
	@JsonProperty("vpc_TicketNo")
	private String vpcTicketNo;
	
	@JsonProperty("vpc_ReturnURL")
	private String vpcReturnURL;

	@JsonProperty("vpc_SecureHash")
	private String vpcSecureHash;
	
	@JsonProperty("AgainLink")
	private String againLink;
	
	@JsonProperty("Title")
	private String title;
	
	@JsonProperty("vpc_CustomerUserAgent")
	private String vpcCustomerUserAgent;
	
	@JsonProperty("vpc_BankId")
	private Long vpcBankId;
	
	@JsonProperty("vpc_CardNo")
	private Long vpcCardNo;
	
	@JsonProperty("vpc_CardName")
	private String vpcCardName;
	
	@JsonProperty("vpc_CardMonth")
	private String vpcCardMonth;
	
	@JsonProperty("vpc_CardYear")
	private String vpcCardYear;
	
	@JsonProperty("vpc_CardType")
	private String vpcCardType;
	
	@JsonProperty("vpc_AuthMethod")
	private String vpcAuthMethod;
	
	@JsonProperty("vpc_MobileNumber")
	private String vpcMobileNumber;

	
	public VerifyCardReq(){
		
	}


	public Long getVpcVersion() {
		return vpcVersion;
	}


	public void setVpcVersion(Long vpcVersion) {
		this.vpcVersion = vpcVersion;
	}


	public String getVpcCommand() {
		return vpcCommand;
	}


	public void setVpcCommand(String vpcCommand) {
		this.vpcCommand = vpcCommand;
	}


	public String getVpcAccessCode() {
		return vpcAccessCode;
	}


	public void setVpcAccessCode(String vpcAccessCode) {
		this.vpcAccessCode = vpcAccessCode;
	}


	public String getVpcMerchant() {
		return vpcMerchant;
	}


	public void setVpcMerchant(String vpcMerchant) {
		this.vpcMerchant = vpcMerchant;
	}


	public String getVpcMerchTxnRef() {
		return vpcMerchTxnRef;
	}


	public void setVpcMerchTxnRef(String vpcMerchTxnRef) {
		this.vpcMerchTxnRef = vpcMerchTxnRef;
	}


	public String getVpcOrderInfo() {
		return vpcOrderInfo;
	}


	public void setVpcOrderInfo(String vpcOrderInfo) {
		this.vpcOrderInfo = vpcOrderInfo;
	}


	public String getVpcAmount() {
		return vpcAmount;
	}


	public void setVpcAmount(String vpcAmount) {
		this.vpcAmount = vpcAmount;
	}


	public String getVpcLocale() {
		return vpcLocale;
	}


	public void setVpcLocale(String vpcLocale) {
		this.vpcLocale = vpcLocale;
	}


	public String getVpcCurrency() {
		return vpcCurrency;
	}


	public void setVpcCurrency(String vpcCurrency) {
		this.vpcCurrency = vpcCurrency;
	}


	public String getVpcTicketNo() {
		return vpcTicketNo;
	}


	public void setVpcTicketNo(String vpcTicketNo) {
		this.vpcTicketNo = vpcTicketNo;
	}


	public String getVpcReturnURL() {
		return vpcReturnURL;
	}


	public void setVpcReturnURL(String vpcReturnURL) {
		this.vpcReturnURL = vpcReturnURL;
	}


	public String getVpcSecureHash() {
		return vpcSecureHash;
	}


	public void setVpcSecureHash(String vpcSecureHash) {
		this.vpcSecureHash = vpcSecureHash;
	}


	public String getAgainLink() {
		return againLink;
	}


	public void setAgainLink(String againLink) {
		this.againLink = againLink;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getVpcCustomerUserAgent() {
		return vpcCustomerUserAgent;
	}


	public void setVpcCustomerUserAgent(String vpcCustomerUserAgent) {
		this.vpcCustomerUserAgent = vpcCustomerUserAgent;
	}


	public Long getVpcBankId() {
		return vpcBankId;
	}


	public void setVpcBankId(Long vpcBankId) {
		this.vpcBankId = vpcBankId;
	}


	public Long getVpcCardNo() {
		return vpcCardNo;
	}


	public void setVpcCardNo(Long vpcCardNo) {
		this.vpcCardNo = vpcCardNo;
	}


	public String getVpcCardName() {
		return vpcCardName;
	}


	public void setVpcCardName(String vpcCardName) {
		this.vpcCardName = vpcCardName;
	}


	public String getVpcCardMonth() {
		return vpcCardMonth;
	}


	public void setVpcCardMonth(String vpcCardMonth) {
		this.vpcCardMonth = vpcCardMonth;
	}


	public String getVpcCardYear() {
		return vpcCardYear;
	}


	public void setVpcCardYear(String vpcCardYear) {
		this.vpcCardYear = vpcCardYear;
	}


	public String getVpcCardType() {
		return vpcCardType;
	}


	public void setVpcCardType(String vpcCardType) {
		this.vpcCardType = vpcCardType;
	}


	public String getVpcAuthMethod() {
		return vpcAuthMethod;
	}


	public void setVpcAuthMethod(String vpcAuthMethod) {
		this.vpcAuthMethod = vpcAuthMethod;
	}


	public String getVpcMobileNumber() {
		return vpcMobileNumber;
	}


	public void setVpcMobileNumber(String vpcMobileNumber) {
		this.vpcMobileNumber = vpcMobileNumber;
	}
	
	
	
	
}
