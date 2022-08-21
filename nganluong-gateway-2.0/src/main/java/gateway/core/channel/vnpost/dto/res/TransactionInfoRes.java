package gateway.core.channel.vnpost.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionInfoRes {

	@JsonProperty("cashout_id")
	private String cashoutId;
	
	@JsonProperty("cashin_id")
	private String cashinId;

	private String amount;

	@JsonProperty("time_created")
	private String timeCreated;

	private int status;

	public String getCashoutId() {
		return cashoutId;
	}

	public void setCashoutId(String cashoutId) {
		this.cashoutId = cashoutId;
	}

	public String getCashinId() {
		return cashinId;
	}

	public void setCashinId(String cashinId) {
		this.cashinId = cashinId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(String timeCreated) {
		this.timeCreated = timeCreated;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
