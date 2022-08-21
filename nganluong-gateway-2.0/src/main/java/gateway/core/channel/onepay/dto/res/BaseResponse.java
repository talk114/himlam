package gateway.core.channel.onepay.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BaseResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("vpc_Command")
	protected String vpcCommand;
	
	@JsonProperty("vpc_TransactionNo")
	protected String vpcTransactionNo;
	
	@JsonProperty("vpc_Message")
	protected String vpcMessage;
	
	@JsonProperty("vpc_TxnResponseCode")
	protected String vpcTxnResponseCode;
	
	@JsonProperty("vpc_Merchant")
	protected String vpcMerchant;
	
	@JsonProperty("vpc_MerchTxnRef")
	protected String vpcMerchTxnRef;
	
	@JsonProperty("vpc_Amount")
	protected String vpcAmount;
	
	@JsonProperty("vpc_Locale")
	protected String vpcLocale;
	
	@JsonProperty("vpc_Version")
	protected String vpcVersion;
	
	@JsonProperty("vpc_OrderInfo")
	protected String vpcOrderInfo;

	public String getVpcCommand() {
		return vpcCommand;
	}

	public void setVpcCommand(String vpcCommand) {
		this.vpcCommand = vpcCommand;
	}

	public String getVpcTransactionNo() {
		return vpcTransactionNo;
	}

	public void setVpcTransactionNo(String vpcTransactionNo) {
		this.vpcTransactionNo = vpcTransactionNo;
	}

	public String getVpcMessage() {
		return vpcMessage;
	}

	public void setVpcMessage(String vpcMessage) {
		this.vpcMessage = vpcMessage;
	}

	public String getVpcTxnResponseCode() {
		return vpcTxnResponseCode;
	}

	public void setVpcTxnResponseCode(String vpcTxnResponseCode) {
		this.vpcTxnResponseCode = vpcTxnResponseCode;
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

	public String getVpcVersion() {
		return vpcVersion;
	}

	public void setVpcVersion(String vpcVersion) {
		this.vpcVersion = vpcVersion;
	}

	public String getVpcOrderInfo() {
		return vpcOrderInfo;
	}

	public void setVpcOrderInfo(String vpcOrderInfo) {
		this.vpcOrderInfo = vpcOrderInfo;
	}
	
	
}
