package vn.nganluong.naba.dto;

import java.io.Serializable;
import java.util.Date;

public class PaymentDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4971654055669064005L;
	private Integer id;
	private String channelCode;
	private String channelName;
	private String amount;
	private String merchantName;
	private int channelTransactionStatus;
	private int merchantTransactionStatus;
	private int pgTransactionStatus;

	private Byte revertStatus;

	private String channelTransactionId;
	private String merchantTransactionId;

	private String accountNo;
	private String cardNo;
	private String description;
	private String rawRequest;
	private String rawResponse;

	private String timeCreatedFormat;
	private String timeUpdatedFormat;
	private Date timeCreated;
	private Date timeUpdated;

	private String channelTransactionStatusDisplay;
	private String merchantTransactionStatusDisplay;
	private String pgTransactionStatusDisplay;

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public int getChannelTransactionStatus() {
		return channelTransactionStatus;
	}

	public void setChannelTransactionStatus(int channelTransactionStatus) {
		this.channelTransactionStatus = channelTransactionStatus;
	}

	public int getMerchantTransactionStatus() {
		return merchantTransactionStatus;
	}

	public void setMerchantTransactionStatus(int merchantTransactionStatus) {
		this.merchantTransactionStatus = merchantTransactionStatus;
	}

	public int getPgTransactionStatus() {
		return pgTransactionStatus;
	}

	public void setPgTransactionStatus(int pgTransactionStatus) {
		this.pgTransactionStatus = pgTransactionStatus;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTimeCreatedFormat() {
		return timeCreatedFormat;
	}

	public void setTimeCreatedFormat(String timeCreatedFormat) {
		this.timeCreatedFormat = timeCreatedFormat;
	}

	public String getTimeUpdatedFormat() {
		return timeUpdatedFormat;
	}

	public void setTimeUpdatedFormat(String timeUpdatedFormat) {
		this.timeUpdatedFormat = timeUpdatedFormat;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Date getTimeUpdated() {
		return timeUpdated;
	}

	public void setTimeUpdated(Date timeUpdated) {
		this.timeUpdated = timeUpdated;
	}

	public String getChannelTransactionStatusDisplay() {
		return PaymentConst.EnumBankStatus.SUCCEEDED.statusByCode(channelTransactionStatus);
	}

	public void setChannelTransactionStatusDisplay(String channelTransactionStatusDisplay) {
		this.channelTransactionStatusDisplay = channelTransactionStatusDisplay;
	}

	public String getMerchantTransactionStatusDisplay() {
		return PaymentConst.EnumBankStatus.SUCCEEDED.statusByCode(merchantTransactionStatus);
	}

	public void setMerchantTransactionStatusDisplay(String merchantTransactionStatusDisplay) {
		this.merchantTransactionStatusDisplay = merchantTransactionStatusDisplay;
	}

	public String getPgTransactionStatusDisplay() {
		return PaymentConst.EnumBankStatus.SUCCEEDED.statusByCode(pgTransactionStatus);
	}

	public void setPgTransactionStatusDisplay(String pgTransactionStatusDisplay) {
		this.pgTransactionStatusDisplay = pgTransactionStatusDisplay;
	}

	public String getChannelTransactionId() {
		return channelTransactionId;
	}

	public void setChannelTransactionId(String channelTransactionId) {
		this.channelTransactionId = channelTransactionId;
	}

	public String getMerchantTransactionId() {
		return merchantTransactionId;
	}

	public void setMerchantTransactionId(String merchantTransactionId) {
		this.merchantTransactionId = merchantTransactionId;
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

	public Byte getRevertStatus() {
		return revertStatus;
	}

	public void setRevertStatus(Byte revertStatus) {
		this.revertStatus = revertStatus;
	}
}
