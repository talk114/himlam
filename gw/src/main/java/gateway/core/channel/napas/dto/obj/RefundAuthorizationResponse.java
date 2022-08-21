package gateway.core.channel.napas.dto.obj;

public class RefundAuthorizationResponse {

	private String commercialCardIndicator;
	private String date;
	private String financialNetworkDate;
	private String processingCode;
	private String responseCode;
	private String stan;
	private String time;
	private String transactionIdentifier;

	public String getCommercialCardIndicator() {
		return commercialCardIndicator;
	}

	public void setCommercialCardIndicator(String commercialCardIndicator) {
		this.commercialCardIndicator = commercialCardIndicator;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFinancialNetworkDate() {
		return financialNetworkDate;
	}

	public void setFinancialNetworkDate(String financialNetworkDate) {
		this.financialNetworkDate = financialNetworkDate;
	}

	public String getProcessingCode() {
		return processingCode;
	}

	public void setProcessingCode(String processingCode) {
		this.processingCode = processingCode;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getStan() {
		return stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTransactionIdentifier() {
		return transactionIdentifier;
	}

	public void setTransactionIdentifier(String transactionIdentifier) {
		this.transactionIdentifier = transactionIdentifier;
	}

}
