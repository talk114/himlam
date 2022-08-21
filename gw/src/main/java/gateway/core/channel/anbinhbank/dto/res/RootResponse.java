package gateway.core.channel.anbinhbank.dto.res;


public class RootResponse {

	private String signatureHeaderRes;
	
	private String bodyRes;
	
	private int httpStatus;

	public String getSignatureHeaderRes() {
		return signatureHeaderRes;
	}

	public void setSignatureHeaderRes(String signatureHeaderRes) {
		this.signatureHeaderRes = signatureHeaderRes;
	}

	public String getBodyRes() {
		return bodyRes;
	}

	public void setBodyRes(String bodyRes) {
		this.bodyRes = bodyRes;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	
	
}
