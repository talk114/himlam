package gateway.core.channel.stb_ecom;

import gateway.core.util.HttpUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

public class ClientRequest {
	private static String stbUrl = "https://partner.sacombank.com.vn/epaygw/stb/";
	private static String user = "74f48d97-afe6-4e84-a121-1f544e192ed2";
	private static String password = "aae8f0c2b6c8f32b";

	private static final Logger logger = LogManager.getLogger(ClientRequest.class);

	static public String callApi(String body, String bodySigned) throws Exception {
		// LOG.info("STB IBFT Param: " + body + "\t Sign: " + bodySigned);

		String userCredentials = user + ":" + password;
		String basicAuth = "Basic " + Base64.encodeBase64String(userCredentials.getBytes());
		URL url = new URL(stbUrl);

		SSLContext ssl_ctx = SSLContext.getInstance("TLS");
		TrustManager[] trust_mgr = get_trust_mgr();
		ssl_ctx.init(null, trust_mgr, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String string, SSLSession ssls) {
				return true;
			}
		});

		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", basicAuth);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Signature", bodySigned);
		con.setDoOutput(true);

		try (DataOutputStream wr = new DataOutputStream((con.getOutputStream()))) {
			wr.writeBytes(body);
			wr.flush();
		}

		StringBuilder response = new StringBuilder();

		InputStream is = null;
		int responseCode = con.getResponseCode();
		if ( responseCode == HttpURLConnection.HTTP_OK) {
			is = con.getInputStream();
		} else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
			logger.error("Giao dịch bị từ chối do nội dung request bị sai");
			is = con.getErrorStream();
			throw new Exception("Giao dịch bị từ chối do nội dung request bị sai");
		} else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED){
			logger.error("Không xác thực được ĐVCNTT");
			is = con.getErrorStream();
			throw new Exception("Không xác thực được ĐVCNTT");
		} else if (responseCode == HttpURLConnection.HTTP_CONFLICT){
			logger.error("Trùng giao dịch");
			is = con.getErrorStream();
			throw new Exception("Trùng giao dịch");
		} else if (responseCode == HttpURLConnection.HTTP_UNAVAILABLE){
			logger.error("Service unavailable");
			is = con.getErrorStream();
			throw new Exception("Dịch vụ đang tạm dừng");
		}else {
			logger.error("STB System error: " + responseCode);
			is = con.getErrorStream();
			throw new Exception("STB System error: " + responseCode);
		}

		try (BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		}
		return response.toString();
	}
	
	public static String notifyQrResult(String req, String urlMerchant)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException,
			IllegalBlockSizeException, BadPaddingException, IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("params", req);
		String result = HttpUtil.send(urlMerchant, map, null);
		return result;
	}

	public static TrustManager[] get_trust_mgr() {
		TrustManager[] certs = new TrustManager[] { new X509TrustManager() {

			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string)
					throws CertificateException {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string)
					throws CertificateException {

			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}
		} };
		return certs;
	}

	public static String getStbUrl() {
		return stbUrl;
	}

	public static void setStbUrl(String stbUrl) {
		ClientRequest.stbUrl = stbUrl;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		ClientRequest.user = user;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		ClientRequest.password = password;
	}

}
