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
public class VerifyCardRes extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("vpc_MerTxnRef")
	private String vpcMerTxnRef;

	@JsonProperty("vpc_AuthSite")
	private String vpcAuthSite;

	@JsonProperty("vpc_AuthURL")
	private String vpcAuthURL;

	@JsonProperty("vpc_Currency")
	private String vpcCurrency;

	@JsonProperty("vpc_CardNo")
	private String vpcCardNo;

	@JsonProperty("Title")
	private String title;

	@JsonProperty("vpc_ReturnURL")
	private String vpcReturnURL;

	@JsonProperty("AgainLink")
	private String againLink;

	@JsonProperty("vpc_CustomerUserAgent")
	private String vpcCustomerUserAgent;

	@JsonProperty("vpc_AccessCode")
	private String vpcAccessCode;

	@JsonProperty("vpc_TicketNo")
	private String vpcTicketNo;

	@JsonProperty("vpc_BankId")
	private String vpcBankId;

	@JsonProperty("vpc_CardName")
	private String vpcCardName;

	@JsonProperty("vpc_CardYear")
	private String vpcCardYear;

	@JsonProperty("vpc_CardMonth")
	private String vpcCardMonth;

	public String getVpcMerTxnRef() {
		return vpcMerTxnRef;
	}

	public void setVpcMerTxnRef(String vpcMerTxnRef) {
		this.vpcMerTxnRef = vpcMerTxnRef;
	}

	public String getVpcAuthSite() {
		return vpcAuthSite;
	}

	public void setVpcAuthSite(String vpcAuthSite) {
		this.vpcAuthSite = vpcAuthSite;
	}

	public String getVpcAuthURL() {
		return vpcAuthURL;
	}

	public void setVpcAuthURL(String vpcAuthURL) {
		this.vpcAuthURL = vpcAuthURL;
	}

	public String getVpcCurrency() {
		return vpcCurrency;
	}

	public void setVpcCurrency(String vpcCurrency) {
		this.vpcCurrency = vpcCurrency;
	}

	public String getVpcCardNo() {
		return vpcCardNo;
	}

	public void setVpcCardNo(String vpcCardNo) {
		this.vpcCardNo = vpcCardNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVpcReturnURL() {
		return vpcReturnURL;
	}

	public void setVpcReturnURL(String vpcReturnURL) {
		this.vpcReturnURL = vpcReturnURL;
	}

	public String getAgainLink() {
		return againLink;
	}

	public void setAgainLink(String againLink) {
		this.againLink = againLink;
	}

	public String getVpcCustomerUserAgent() {
		return vpcCustomerUserAgent;
	}

	public void setVpcCustomerUserAgent(String vpcCustomerUserAgent) {
		this.vpcCustomerUserAgent = vpcCustomerUserAgent;
	}

	public String getVpcAccessCode() {
		return vpcAccessCode;
	}

	public void setVpcAccessCode(String vpcAccessCode) {
		this.vpcAccessCode = vpcAccessCode;
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

	public String getVpcCardName() {
		return vpcCardName;
	}

	public void setVpcCardName(String vpcCardName) {
		this.vpcCardName = vpcCardName;
	}

	public String getVpcCardYear() {
		return vpcCardYear;
	}

	public void setVpcCardYear(String vpcCardYear) {
		this.vpcCardYear = vpcCardYear;
	}

	public String getVpcCardMonth() {
		return vpcCardMonth;
	}

	public void setVpcCardMonth(String vpcCardMonth) {
		this.vpcCardMonth = vpcCardMonth;
	}

}
