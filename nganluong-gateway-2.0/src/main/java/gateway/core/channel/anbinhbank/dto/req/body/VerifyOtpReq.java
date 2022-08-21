package gateway.core.channel.anbinhbank.dto.req.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

public class VerifyOtpReq extends BaseRequest {

	private String customerId;

	private String type;

	private String channel;

	private String otp;

	private String refNumber;

	private String transactionTime;

	public VerifyOtpReq() {
	}

	public VerifyOtpReq(String partnerId, String customerId, String type, String channel, String otp, String refNumber,
                        String transactionTime) {
		super();
		this.partnerId = partnerId;
		this.customerId = customerId;
		this.type = type;
		this.channel = channel;
		this.otp = otp;
		this.refNumber = refNumber;
		this.transactionTime = transactionTime;
	}

	@JsonProperty("CustomerId")
	public String getCustomerId() {
		return customerId;
	}

	@JsonProperty("user_id")
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@JsonProperty("TransactionType")
	public String getType() {
		return type;
	}

	@JsonProperty("verify_type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("Chanel")
	public String getChannel() {
		return channel;
	}

	@JsonProperty("channel")
	public void setChannel(String channel) {
		this.channel = channel;
	}

	@JsonProperty("OTP")
	public String getOtp() {
		return otp;
	}

	@JsonProperty("otp")
	public void setOtp(String otp) {
		this.otp = otp;
	}

	@JsonProperty("RefNumber")
	public String getRefNumber() {
		return refNumber;
	}

	@JsonProperty("bank_trans_id")
	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	@JsonProperty("TransactionTime")
	public String getTransactionTime() {
		return transactionTime;
	}

	@JsonProperty("trans_time")
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	@Override
	public String rawData() {
		return CHARACTER_SEPERATE + partnerId + CHARACTER_SEPERATE + StringUtils.defaultString(customerId, "")
				+ CHARACTER_SEPERATE + type + CHARACTER_SEPERATE + channel + CHARACTER_SEPERATE + otp
				+ CHARACTER_SEPERATE + refNumber + CHARACTER_SEPERATE + transactionTime + CHARACTER_SEPERATE;
	}
}
