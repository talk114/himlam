package gateway.core.channel.anbinhbank.dto.res.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

public class VerifyOtpRes extends BaseResponse {

	private String uniqueIdentifier; // md5 customerId

	@JsonProperty("uniqueIdentifier")
	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	@JsonProperty("UniqueIdentifier")
	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	@Override
	public String rawData() {
		return super.rawData() + StringUtils.defaultString(uniqueIdentifier, "")  + CHARACTER_SEPERATE;
	}
}
