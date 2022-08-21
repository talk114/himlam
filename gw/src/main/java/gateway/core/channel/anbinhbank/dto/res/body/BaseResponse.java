package gateway.core.channel.anbinhbank.dto.res.body;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {

	protected static final String CHARACTER_SEPERATE = "|";
	
	protected String code;

	protected String desc;

	private String bankTransId;

	private String bankTransTime;
	
	@JsonProperty("trans_id")
	private String transId;

	@JsonProperty("res_code")
	public String getCode() {
		return code;
	}

	@JsonProperty("code")
	public void setCode(String code) {
		this.code = code;
	}

	@JsonProperty("res_desc")
	public String getDesc() {
		return desc;
	}

	@JsonProperty("errors")
	public void setDesc(String desc) {
		this.desc = desc;
	}

	@JsonProperty("bank_trans_id")
	public String getBankTransId() {
		return bankTransId;
	}

	@JsonProperty("RefNumber")
	public void setBankTransId(String bankTransId) {
		this.bankTransId = bankTransId;
	}

	@JsonProperty("bank_trans_time")
	public String getBankTransTime() {
		return bankTransTime;
	}

	@JsonProperty("TransactionTime")
	public void setBankTransTime(String bankTransTime) {
		this.bankTransTime = bankTransTime;
	}
	
	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String rawData(){
		return CHARACTER_SEPERATE + bankTransId + CHARACTER_SEPERATE + bankTransTime + CHARACTER_SEPERATE;
	}

}
