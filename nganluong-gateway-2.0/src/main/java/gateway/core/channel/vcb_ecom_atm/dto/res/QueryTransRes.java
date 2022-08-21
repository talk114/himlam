package gateway.core.channel.vcb_ecom_atm.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryTransRes extends BaseResponse {

	private String balance;
	
	private String tranStatus;
	
	private String queryTranID;
	
	private String vcbTranID;

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	@JsonProperty("tranStatus")
	public String getTranStatus() {
		return tranStatus;
	}

	@JsonProperty("TranStatus")
	public void setTranStatus(String tranStatus) {
		this.tranStatus = tranStatus;
	}

	@JsonProperty("queryTranID")
	public String getQueryTranID() {
		return queryTranID;
	}

	@JsonProperty("queryTranID")
	public void setQueryTranID(String queryTranID) {
		this.queryTranID = queryTranID;
	}

	@JsonProperty("vcbTranID")
	public String getVcbTranID() {
		return vcbTranID;
	}

	@JsonProperty("VCBTranID")
	public void setVcbTranID(String vcbTranID) {
		this.vcbTranID = vcbTranID;
	}
	
	
}
