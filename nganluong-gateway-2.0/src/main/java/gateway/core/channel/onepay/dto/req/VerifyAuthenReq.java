package gateway.core.channel.onepay.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 
 * @author anhnq
 * <b>Request Check Card/Bank Acc before Transfer</b>
 */
@SuppressWarnings("serial")
public class VerifyAuthenReq implements Serializable{
	
	@JsonProperty("vpc_TicketNo")
	private Long vpcTicketNo;
	
	@JsonProperty("vpc_CustomerUserAgent")
	private String vpcCustomerUserAgent;
	
	@JsonProperty("vpc_BankId")
	private String vpcBankId;
	
	@JsonProperty("vpc_AuthURL")
	private String vpcAuthURL;
	
	@JsonProperty("vpc_Otp")
	private String vpcOtp;
		
	@JsonProperty("vpc_ReturnURL")
	private String vpcReturnURL;

	public Long getVpcTicketNo() {
		return vpcTicketNo;
	}

	public void setVpcTicketNo(Long vpcTicketNo) {
		this.vpcTicketNo = vpcTicketNo;
	}

	public String getVpcCustomerUserAgent() {
		return vpcCustomerUserAgent;
	}

	public void setVpcCustomerUserAgent(String vpcCustomerUserAgent) {
		this.vpcCustomerUserAgent = vpcCustomerUserAgent;
	}

	public String getVpcBankId() {
		return vpcBankId;
	}

	public void setVpcBankId(String vpcBankId) {
		this.vpcBankId = vpcBankId;
	}

	public String getVpcAuthURL() {
		return vpcAuthURL;
	}

	public void setVpcAuthURL(String vpcAuthURL) {
		this.vpcAuthURL = vpcAuthURL;
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
	

	
}
