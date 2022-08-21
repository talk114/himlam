package gateway.core.channel.anbinhbank.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import gateway.core.channel.anbinhbank.dto.req.body.BaseRequest;

public class GetAccessTokenReq extends BaseRequest {

	@JsonProperty("scope")
	private String scope;
	
	@JsonProperty("grant_type")
	private String grantType;
	
	@JsonProperty("client_id")
	private String clientId;
	
	@JsonProperty("client_secret")
	private String clientSecret;

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	
	
}
