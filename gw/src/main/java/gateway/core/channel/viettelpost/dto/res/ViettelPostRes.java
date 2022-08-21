package gateway.core.channel.viettelpost.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViettelPostRes {

	@JsonProperty("error_code")
	private String errorCode;

	@JsonProperty("view_info")
	private String viewInfo;

	@JsonProperty("user_info")
	private UserInfoRes userInfoRes;

	@JsonProperty("transaction_info")
	private TransactionInfoRes transactionInfoRes;

	public ViettelPostRes() {
	}

	public ViettelPostRes(String errorCode, String viewInfo, UserInfoRes userInfoRes, TransactionInfoRes transactionInfoRes) {
		this.errorCode = errorCode;
		this.viewInfo = viewInfo;
		this.userInfoRes = userInfoRes;
		this.transactionInfoRes = transactionInfoRes;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getViewInfo() {
		return viewInfo;
	}

	public void setViewInfo(String viewInfo) {
		this.viewInfo = viewInfo;
	}

	public UserInfoRes getUserInfoRes() {
		return userInfoRes;
	}

	public void setUserInfoRes(UserInfoRes userInfoRes) {
		this.userInfoRes = userInfoRes;
	}

	public TransactionInfoRes getTransactionInfoRes() {
		return transactionInfoRes;
	}

	public void setTransactionInfoRes(TransactionInfoRes transactionInfoRes) {
		this.transactionInfoRes = transactionInfoRes;
	}

}
