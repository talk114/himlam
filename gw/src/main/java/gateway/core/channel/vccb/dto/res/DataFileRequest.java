package gateway.core.channel.vccb.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataFileRequest {
	@JsonProperty("merchant_id")
	private String clientCode;
	
	@JsonProperty("trans_id")
	private String transactionID;
	
	@JsonProperty("trans_time")
	private String transactionDate;
	
	@JsonProperty("card_number")
	private String cardNo;
	
	@JsonProperty("card_ind")
	private String cardInd;
	
	@JsonProperty("business_type")
	private String businessType;
	
	@JsonProperty("amount")
	private String amount;
	
	@JsonProperty("currency_code")
	private String currencyCode;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("bank_trans_id")
	private String bankTransactionID;
	
	@JsonProperty("reversal_ind")
	private String reversalIndicator;
	
	@JsonProperty("trans_status")
	private String transactionStatus;
	
	@JsonProperty("trans_type")
	private String transactionType;
	
	private String checksum;
	
	
	public DataFileRequest() {
		super();
	}
	
	
	public String getChecksum() {
		return checksum;
	}


	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}


	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCardInd() {
		return cardInd;
	}
	public void setCardInd(String cardInd) {
		this.cardInd = cardInd;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBankTransactionID() {
		return bankTransactionID;
	}
	public void setBankTransactionID(String bankTransactionID) {
		this.bankTransactionID = bankTransactionID;
	}
	public String getReversalIndicator() {
		return reversalIndicator;
	}
	public void setReversalIndicator(String reversalIndicator) {
		this.reversalIndicator = reversalIndicator;
	}
	public String getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public String dataToString() {
		return this.clientCode + this.transactionID + this.transactionDate + this.cardNo + this.cardInd + 
				this.businessType + this.amount + this.currencyCode + this.description + this.bankTransactionID + 
				this.reversalIndicator + this.transactionStatus + this.transactionType;
	}
	
}
