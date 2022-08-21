package gateway.core.channel.anbinhbank.dto.res.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

public class CheckTransRes extends BaseResponse {

	private String originTransCode;

	private String originTransDesc;

	@JsonProperty("origin_trans_code")
	public String getOriginTransCode() {
		return originTransCode;
	}

	@JsonProperty("Code")
	public void setOriginTransCode(String originTransCode) {
		this.originTransCode = originTransCode;
	}

	@JsonProperty("origin_trans_desc")
	public String getOriginTransDesc() {
		return originTransDesc;
	}

	@JsonProperty("Desc")
	public void setOriginTransDesc(String originTransDesc) {
		this.originTransDesc = originTransDesc;
	}

	@Override
	public String rawData() {
		return CHARACTER_SEPERATE + originTransCode + CHARACTER_SEPERATE
				+ StringUtils.defaultString(originTransDesc, "") + super.rawData();
	}
}
