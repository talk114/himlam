package gateway.core.channel.anbinhbank.dto.req;

public class HeaderRequest {

	private String vimoTransId;
	
	private String signature;
	
	private String accessToken;

	public String getVimoTransId() {
		return vimoTransId;
	}

	public void setVimoTransId(String vimoTransId) {
		this.vimoTransId = vimoTransId;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	
}
