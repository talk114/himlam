package gateway.core.channel.anbinhbank.report.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrxNglAbbInfo {

	private String amount;

	@JsonProperty("nl_trans_id")
	private String nlTransId;

	@JsonProperty("bank_trans_id")
	private String bankTransId;

//	@JsonProperty("response_code")
//	private String responseCode;

//	@JsonProperty("trans_type")
	private String transType = "01";

	@JsonProperty("date_time")
	private String dateTime;

	private String currencyCode = "VND";

	private String recordType = "";

	private String bankResponseCode = "";

	private String merchantId = "";

	private String checksum = "";

	private String description = "";

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getNlTransId() {
		return nlTransId;
	}

	public void setNlTransId(String nlTransId) {
		this.nlTransId = nlTransId;
	}

	public String getBankTransId() {
		return bankTransId;
	}

	public void setBankTransId(String bankTransId) {
		this.bankTransId = bankTransId;
	}

//	public String getResponseCode() {
//		return responseCode;
//	}
//
//	public void setResponseCode(String responseCode) {
//		this.responseCode = responseCode;
//	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getBankResponseCode() {
		return bankResponseCode;
	}

	public void setBankResponseCode(String bankResponseCode) {
		this.bankResponseCode = bankResponseCode;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += ((nlTransId + description) != null ? (nlTransId + description).hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TrxNglAbbInfo)) {
			return false;
		}
		TrxNglAbbInfo other = (TrxNglAbbInfo) object;
		return !(((this.nlTransId + this.description) == null && (other.nlTransId + other.description) != null)
				|| ((this.nlTransId + this.description) != null
						&& !(this.nlTransId + this.description).equals(other.nlTransId + other.description)));
	}

	public String rawData() {
		return recordType + transType + currencyCode + amount + nlTransId + dateTime + bankTransId + merchantId
				+ StringUtils.defaultIfEmpty(bankResponseCode, "");
	}
}
