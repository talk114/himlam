package gateway.core.channel.anbinhbank.report;

import gateway.core.channel.anbinhbank.dto.ABBankConstants;
import org.apache.commons.codec.binary.Base64;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

public class HttpUtil {
	public static String sendPost(String Url, String query, String userPass) throws IOException {
		try {
			URL url = new URL(Url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			if (userPass != null) {
				String basicAuth = "Basic " + new String(new Base64().encode(userPass.getBytes()));
				con.setRequestProperty("Authorization", basicAuth);
			}
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Accept-Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setConnectTimeout(20000);
			con.setReadTimeout(200000);

			try (DataOutputStream wr = new DataOutputStream((con.getOutputStream()))) {
				wr.write(query.getBytes(StandardCharsets.UTF_8));
				wr.flush();
				wr.close();
			}
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				StringBuilder response = new StringBuilder();
				try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
				}
				return response.toString();
			}

			return null;
		} catch (SocketTimeoutException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}

	public static String sendGet(String urlStr, String key) throws IOException, KeyManagementException, NoSuchAlgorithmException {
		ABBankConstants.WriteInfoLog("ABB DOI SOAT URL:  ", key, urlStr);
		ABBankConstants.NGL_DOISOAT_URL = urlStr;
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

			con.setRequestMethod("GET");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Accept-Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("User-Agent", "gateway/1.0.0");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setConnectTimeout(60000* ABBankConstants.COUNT_INDEX);
			con.setReadTimeout(60000* ABBankConstants.COUNT_INDEX);

			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				ABBankConstants.NGL_DOISOAT_URL = "";
				ABBankConstants.COUNT_INDEX = 1;
				StringBuilder response = new StringBuilder();
				try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
				}
				ABBankConstants.WriteInfoLog("GET FILE DOI SOAT NGL THANH CONG  ", key, response.toString());
				return response.toString();
			} else {
				ABBankConstants.WriteInfoLog("GET FILE DOI SOAT NGL THAT BAI  ", key, con.getResponseCode());
			}

			return null;
		} catch (SocketTimeoutException e) {
			ABBankConstants.WriteInfoLog("TIME OUT EXCEPTION COUNT  ", key, ABBankConstants.COUNT_INDEX);
			ABBankConstants.WriteInfoLog("TIME OUT  ", key, 60000* ABBankConstants.COUNT_INDEX);
			ABBankConstants.COUNT_INDEX++;
			if (ABBankConstants.COUNT_INDEX == 3) {
				BaseReport.sendMailReportError(CreateReportNglAbb.subjectMail + CreateReportNglAbb.timeReport,"LAY FILE DOI SOAT NGL THAT BAI - TIME OUT EXCEPTION - COUNT_INDEX_" + ABBankConstants.COUNT_INDEX,BaseReport.MAIL_CC_NGL);
				throw e;
			}
			return sendGet(ABBankConstants.NGL_DOISOAT_URL, "DSABB*" + CreateReportNglAbb.timeReport2);
		} catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
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
}
