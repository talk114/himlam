package gateway.core.channel.napas.dto.obj;

public class InputParam {
	private String clientIP;
	private String deviceId;
	private String environment;
	private String cardScheme;
	private String enable3DSecure;

	public InputParam() {
	}

	public InputParam(String clientIP, String deviceId, String environment, String cardScheme, String enable3DSecure) {
		this.clientIP = clientIP;
		this.deviceId = deviceId;
		this.environment = environment;
		this.cardScheme = cardScheme;
		this.enable3DSecure = enable3DSecure;
	}

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getCardScheme() {
		return cardScheme;
	}

	public void setCardScheme(String cardScheme) {
		this.cardScheme = cardScheme;
	}

	public String getEnable3DSecure() {
		return enable3DSecure;
	}

	public void setEnable3DSecure(String enable3dSecure) {
		enable3DSecure = enable3dSecure;
	}

}