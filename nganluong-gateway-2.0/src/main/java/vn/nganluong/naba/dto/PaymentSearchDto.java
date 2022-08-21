package vn.nganluong.naba.dto;

import java.io.Serializable;

public class PaymentSearchDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4971654055669064125L;
	private String channelCode;
	private String accountNo;
	private String clientTransactionId;
	private String merchantName;
	private String cardNo;
	private String description;
	private String fromDate;
	private String toDate;

	private Integer pageOfList;

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getClientTransactionId() {
		return clientTransactionId;
	}

	public void setClientTransactionId(String clientTransactionId) {
		this.clientTransactionId = clientTransactionId;
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

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Integer getPageOfList() {
		return pageOfList;
	}

	public void setPageOfList(Integer pageOfList) {
		this.pageOfList = pageOfList;
	}

}
