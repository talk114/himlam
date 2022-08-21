package gateway.core.channel.vcb_ecom_atm.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse {

	@JsonProperty(access = Access.WRITE_ONLY)
	protected String errCode;

	@JsonProperty( access = Access.WRITE_ONLY)
	protected String errDesc;

	@JsonProperty("trans_id")
	protected String transId;

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrDesc() {
		return errDesc;
	}

	public void setErrDesc(String errDesc) {
		this.errDesc = errDesc;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

}
