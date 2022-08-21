package gateway.core.channel.anbinhbank.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetAccessTokenRes {

	@JsonProperty("token_type")
	private String tokenType;
	
	@JsonProperty("access_token")
	private String accessToken;
	
	@JsonProperty("expires_in")
	private String expiresIn;
	
	@JsonProperty("consented_on")
	private String consentedOn;
	
	@JsonProperty("scope")
	private String scope;

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getConsentedOn() {
		return consentedOn;
	}

	public void setConsentedOn(String consentedOn) {
		this.consentedOn = consentedOn;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
	
}
