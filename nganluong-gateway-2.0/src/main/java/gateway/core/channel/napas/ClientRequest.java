package gateway.core.channel.napas;

import gateway.core.channel.napas.dto.response.ApiResponse;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

public class ClientRequest {

	static String sendGetRequest(String urlStr, String author, String mediaType) throws IOException {
		try {
			URL url = new URL(urlStr);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			if (StringUtils.isNotBlank(author)) {
				con.setRequestProperty("Authorization", "Bearer " + author);
			}
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Accept-Charset", "UTF-8");
			con.setRequestProperty("Content-Type", mediaType);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setConnectTimeout(60000);
			con.setReadTimeout(60000);

			StringBuilder response = new StringBuilder();
			InputStream is = null;

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				is = con.getInputStream();
			} else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
				is = con.getErrorStream();
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

			return null;
		} catch (SocketTimeoutException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}

	public static ApiResponse sendRequest(String urlStr, String param, String author, String mediaType, String method)
			throws IOException, NoSuchAlgorithmException, KeyManagementException {
		ApiResponse apiResponse = new ApiResponse();
		try {
//			System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
			URL url = new URL(urlStr);
			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
			TrustManager[] trust_mgr = get_trust_mgr();
			ssl_ctx.init(null, trust_mgr, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier((string, ssls) -> true);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			if (StringUtils.isNotBlank(author)) {
				con.setRequestProperty("Authorization", "Bearer " + author);
			}
			con.setRequestMethod(method);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Accept-Charset", "UTF-8");

			con.setRequestProperty("Content-Type", mediaType);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setConnectTimeout(60000);
			con.setReadTimeout(60000);

			if (StringUtils.isNotBlank(param)) {
				try (DataOutputStream wr = new DataOutputStream((con.getOutputStream()))) {
					wr.write(param.getBytes(StandardCharsets.UTF_8));
					wr.flush();
					wr.close();
				}
			}

			StringBuilder response = new StringBuilder();
			InputStream is = null;

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				is = con.getInputStream();
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
			apiResponse.setResponse(response.toString());

			return apiResponse;
		} catch (SocketTimeoutException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}

	private static TrustManager[] get_trust_mgr () {
		TrustManager[] certs = new TrustManager[]{new X509TrustManager() {

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
				return new java.security.cert.X509Certificate[]{};
			}
		}};
		return certs;
	}
}
