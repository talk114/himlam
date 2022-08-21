package vn.nganluong.naba.channel.vib.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import vn.nganluong.naba.utils.RequestUtil;

import java.util.Date;
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

	private Integer channelId;
	private Integer pgFunctionId;
	private Integer paymentType;
	private String merchantCode;
	private String merchantName;
	private String clientRequestId;
	private String merchantTransactionId;
	private String channelTransactionId;
	private String channelTransactionSeq;
	private String channelStatus;
	private String channelMessage;
	private Integer channelTransactionStatus;
	private Integer merchantTransactionStatus;
	private Integer pgTransactionStatus;
	private Integer channelRevertStatus; // tran_type
	private String accountNo;
	private String cardNo;
	private Integer cardType;
	private String amount;
	private Integer pgErrorId;
	private String description;
	private String rawRequest;
	private String rawResponse;
	private String virtualAccountNo;
	private Date timeCreated;
	private String sourceAccount;
	private String channelTransactionType;
	{
		channelTransactionStatus = 0;
		merchantTransactionStatus = 0;
		pgTransactionStatus = 0;
	}
	public String getVirtualAccountNo() {
		return virtualAccountNo;
	}

	public void setVirtualAccountNo(String virtualAccountNo) {
		this.virtualAccountNo = virtualAccountNo;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Integer getPgFunctionId() {
		return pgFunctionId;
	}

	public void setPgFunctionId(Integer pgFunctionId) {
		this.pgFunctionId = pgFunctionId;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getClientRequestId() {
		return clientRequestId;
	}

	public void setClientRequestId(String clientRequestId) {
		this.clientRequestId = clientRequestId;
	}

	public String getMerchantTransactionId() {
		return merchantTransactionId;
	}

	public void setMerchantTransactionId(String merchantTransactionId) {
		this.merchantTransactionId = merchantTransactionId;
	}

	public String getChannelTransactionId() {
		return channelTransactionId;
	}

	public void setChannelTransactionId(String channelTransactionId) {
		this.channelTransactionId = channelTransactionId;
	}

	public String getChannelStatus() {
		return channelStatus;
	}

	public void setChannelStatus(String channelStatus) {
		this.channelStatus = channelStatus;
	}

	public String getChannelMessage() {
		return channelMessage;
	}

	public void setChannelMessage(String channelMessage) {
		this.channelMessage = channelMessage;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Integer getPgErrorId() {
		return pgErrorId;
	}

	public void setPgErrorId(Integer pgErrorId) {
		this.pgErrorId = pgErrorId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getChannelTransactionStatus() {
		return channelTransactionStatus;
	}

	public void setChannelTransactionStatus(Integer channelTransactionStatus) {
		this.channelTransactionStatus = channelTransactionStatus;
	}

	public Integer getMerchantTransactionStatus() {
		return merchantTransactionStatus;
	}

	public void setMerchantTransactionStatus(Integer merchantTransactionStatus) {
		this.merchantTransactionStatus = merchantTransactionStatus;
	}

	public Integer getPgTransactionStatus() {
		return pgTransactionStatus;
	}

	public void setPgTransactionStatus(Integer pgTransactionStatus) {
		this.pgTransactionStatus = pgTransactionStatus;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getRawRequest() {
		return rawRequest;
	}

	public void setRawRequest(String rawRequest) {
		this.rawRequest = rawRequest;
	}

	public String getRawResponse() {
		return rawResponse;
	}

	public void setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Integer getChannelRevertStatus() {
		return channelRevertStatus;
	}

	public void setChannelRevertStatus(Integer channelRevertStatus) {
		this.channelRevertStatus = channelRevertStatus;
	}

	public String getSourceAccount() {
		return sourceAccount;
	}

	public void setSourceAccount(String sourceAccount) {
		this.sourceAccount = sourceAccount;
	}

	public String getChannelTransactionType() {
		return channelTransactionType;
	}

	public void setChannelTransactionType(String channelTransactionType) {
		this.channelTransactionType = channelTransactionType;
	}

	public String getChannelTransactionSeq() {
		return channelTransactionSeq;
	}

	public void setChannelTransactionSeq(String channelTransactionSeq) {
		this.channelTransactionSeq = channelTransactionSeq;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = RequestUtil.overlayAccountNo(cardNo);
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}
}
