package gateway.core.channel.vnpost.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseVnpostApiRes {

	@JsonProperty("error_code")
	private String errorCode;

	@JsonProperty("verify_code")
	private String verifyCode;

	@JsonProperty("view_info")
	private String viewInfo;

	@JsonProperty("user_info")
	private UserInfoRes userInfoRes;

	@JsonProperty("transaction_info")
	private TransactionInfoRes transactionInfoRes;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
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
