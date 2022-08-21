package gateway.core.channel.onepay.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 
 * @author anhnq
 * <b>Request Check Card/Bank Acc before Transfer</b>
 */
@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryReq implements Serializable{
	
	@JsonProperty("vpc_Command")
	private Long vpcCommand;
	
	@JsonProperty("vpc_Version")
	private String vpcVersion;
	
	@JsonProperty("vpc_MerchTxnRef")
	private String vpcMerchTxnRef;
	
	@JsonProperty("vpc_Merchant")
	private String vpcMerchant;
	
	@JsonProperty("vpc_AccessCode")
	private String vpcAccessCode;
		
	@JsonProperty("vpc_User")
	private String vpcUser;
	
	@JsonProperty("vpc_Password")
	private String vpcPassword;

	public Long getVpcCommand() {
		return vpcCommand;
	}

	public void setVpcCommand(Long vpcCommand) {
		this.vpcCommand = vpcCommand;
	}

	public String getVpcVersion() {
		return vpcVersion;
	}

	public void setVpcVersion(String vpcVersion) {
		this.vpcVersion = vpcVersion;
	}

	public String getVpcMerchTxnRef() {
		return vpcMerchTxnRef;
	}

	public void setVpcMerchTxnRef(String vpcMerchTxnRef) {
		this.vpcMerchTxnRef = vpcMerchTxnRef;
	}

	public String getVpcMerchant() {
		return vpcMerchant;
	}

	public void setVpcMerchant(String vpcMerchant) {
		this.vpcMerchant = vpcMerchant;
	}

	public String getVpcAccessCode() {
		return vpcAccessCode;
	}

	public void setVpcAccessCode(String vpcAccessCode) {
		this.vpcAccessCode = vpcAccessCode;
	}

	public String getVpcUser() {
		return vpcUser;
	}

	public void setVpcUser(String vpcUser) {
		this.vpcUser = vpcUser;
	}

	public String getVpcPassword() {
		return vpcPassword;
	}

	public void setVpcPassword(String vpcPassword) {
		this.vpcPassword = vpcPassword;
	}

	
	
}
