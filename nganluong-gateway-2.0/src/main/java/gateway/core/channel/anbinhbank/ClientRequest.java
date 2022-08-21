package gateway.core.channel.anbinhbank;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.channel.anbinhbank.dto.ABBankConstants;
import gateway.core.channel.anbinhbank.dto.req.RootRequest;
import gateway.core.channel.anbinhbank.dto.res.RootResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.net.ssl.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.Map.Entry;

public class ClientRequest {

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final Logger logger = LogManager.getLogger(ClientRequest.class);

	public static RootResponse sendRequest(String urlStr, RootRequest request, String mediaType) throws IOException, NoSuchAlgorithmException, KeyManagementException {
//		System.setProperty("javax.net.debug", "all"); 
		logger.info("URL ABB: " + urlStr);
		RootResponse apiResponse = new RootResponse();
		try {
//			System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
			URL url = new URL(urlStr);
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
			if (request.getHeaderReq() != null) {
				con.setRequestProperty(ABBankConstants.HEADER_TOKEN,
						"Bearer " + request.getHeaderReq().getAccessToken());
				con.setRequestProperty(ABBankConstants.HEADER_UUID, request.getHeaderReq().getVimoTransId());
				con.setRequestProperty(ABBankConstants.HEADER_SIGNATURE, request.getHeaderReq().getSignature());
			}

			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Accept-Charset", "UTF-8");
			con.setRequestProperty("Content-Type", mediaType);
			
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setConnectTimeout(60000);
			con.setReadTimeout(60000);

			if (request.getBodyReq() != null) {
				try (DataOutputStream wr = new DataOutputStream((con.getOutputStream()))) {
					if(mediaType.equals(MediaType.APPLICATION_FORM_URLENCODED)){
						JSONObject json = new JSONObject(mapper.writeValueAsString(request.getBodyReq()));
						Map<String, Object> map = json.toMap();
						String req = getParameterString(map);
						logger.info("GET TOKEN ABB: " + req);
						wr.write(req.getBytes("UTF-8"));
					} else{
						wr.write(mapper.writeValueAsString(request.getBodyReq()).getBytes("UTF-8"));
					}
					wr.flush();
					wr.close();
				}
			}

			// Parse Reponse
			StringBuilder response = new StringBuilder();
			InputStream is = null;
			String signatureHeaderRes = "";

			int responseCode = con.getResponseCode();
			logger.info("ABB HTTP STATUS: " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
				is = con.getInputStream();
				signatureHeaderRes = con.getHeaderField(ABBankConstants.HEADER_SIGNATURE);
			} else {
				is = con.getErrorStream();
			}

			try (BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
			}

			apiResponse.setHttpStatus(con.getResponseCode());
			apiResponse.setBodyRes(response.toString());
			apiResponse.setSignatureHeaderRes(signatureHeaderRes);

			return apiResponse;
		} catch (SocketTimeoutException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}

	private static TrustManager[] get_trust_mgr() {
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
	
	public static String getParameterString(Map<String, Object> map) {
		StringBuilder paramString = new StringBuilder();
		for (Entry<String, Object> entry : map.entrySet()) {
			paramString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		logger.info(paramString.toString().substring(0, paramString.length() - 1));
		return paramString.toString().substring(0, paramString.length() - 1);
	}
}
