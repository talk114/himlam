package gateway.core.channel.vnpost.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseVnpostApiReq {

	private String func;

	@JsonProperty("access_code")
	private String accessCode;

	@JsonProperty("cashin_id")
	private String cashinId;

	@JsonProperty("cashout_id")
	private String cashoutId;

	@JsonProperty("vnpost_service_code")
	private String vnpostServiceCode;

	@JsonProperty("request_id")
	private String requestId;

	@JsonProperty("date_time")
	private String dateTime;

	private String checksum;

	@JsonProperty("verify_code")
	private String verifyCode;

	private String amount;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCashoutId() {
		return cashoutId;
	}

	public void setCashoutId(String cashoutId) {
		this.cashoutId = cashoutId;
	}

	public String getFunc() {
		return func;
	}

	public void setFunc(String func) {
		this.func = func;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public String getCashinId() {
		return cashinId;
	}

	public void setCashinId(String cashinId) {
		this.cashinId = cashinId;
	}

	public String getVnpostServiceCode() {
		return vnpostServiceCode;
	}

	public void setVnpostServiceCode(String vnpostServiceCode) {
		this.vnpostServiceCode = vnpostServiceCode;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestd) {
		this.requestId = requestd;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

}
