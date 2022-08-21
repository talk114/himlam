package gateway.core.channel.anbinhbank.report.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrxVimoAbbInfo {

	private String amount;

	@JsonProperty("vimo_trans_id")
	private String vimoTransId;

	@JsonProperty("bank_trans_id")
	private String bankTransId;

	@JsonProperty("response_code")
	private String responseCode;

	@JsonProperty("currency_code")
	private String currencyCode;

	@JsonProperty("trans_type")
	private String transType;

	@JsonProperty("customer_id")
	private String customerId;

	@JsonProperty("date_time")
	private String dateTime;

	private String recordType = "";

	private String bankResponseCode = "";

	private String checksum = "";

	private String description = "";

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getVimoTransId() {
		return vimoTransId;
	}

	public void setVimoTransId(String vimoTransId) {
		this.vimoTransId = vimoTransId;
	}

	public String getBankTransId() {
		return bankTransId;
	}

	public void setBankTransId(String bankTransId) {
		this.bankTransId = bankTransId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
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
		hash += ((vimoTransId + description) != null ? (vimoTransId + description).hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TrxVimoAbbInfo)) {
			return false;
		}
		TrxVimoAbbInfo other = (TrxVimoAbbInfo) object;
		return !(((this.vimoTransId + this.description) == null && (other.vimoTransId + other.description) != null)
				|| ((this.vimoTransId + this.description) != null
						&& !(this.vimoTransId + this.description).equals(other.vimoTransId + other.description)));
	}

	public String rawData() {
		return recordType + transType + customerId + currencyCode + amount + vimoTransId + dateTime + bankTransId
				+ StringUtils.defaultIfEmpty(bankResponseCode, "");
	}
}
