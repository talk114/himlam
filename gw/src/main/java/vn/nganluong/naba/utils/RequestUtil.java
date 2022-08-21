package vn.nganluong.naba.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.net.ssl.*;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestUtil {
	private static final Logger logger = LogManager.getLogger(RequestUtil.class);

	public static String createUrlChannelFunction(String host, String port, String url) {

		if (StringUtils.isNotEmpty(port)) {
			port = ":" + port + "/";
		}
		return new StringBuilder().append(StringUtils.trimToEmpty(host)).append(StringUtils.trimToEmpty(port))
				.append(StringUtils.trimToEmpty(url)).toString();
	}
	
	public static String overlayAccountNo(String accountNo) {
		return StringUtils.overlay(accountNo, StringUtils.repeat("X", 6), 6, accountNo.length()-4);
	}

	public static Map<String, String> splitQuery(String urlStr) throws URISyntaxException, UnsupportedEncodingException {
		if (StringUtils.contains(urlStr, "%3")) {
			urlStr = URLDecoder.decode(urlStr, Charset.forName("UTF-8").toString());
		}
		URI url = new URI(urlStr);

		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String query = url.getQuery();
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
		}
		return query_pairs;
	}

	public static HttpComponentsClientHttpRequestFactory createRequestFactory()
			throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sslContext = SSLContext.getInstance("TLS");
		TrustManager[] trust_mgr = get_trust_mgr();
		sslContext.init(null, trust_mgr, new SecureRandom());

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

		requestFactory.setHttpClient(httpClient);
		return requestFactory;
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

	public static void disableSslVerification() {
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			logger.info(ExceptionUtils.getStackTrace(e));
		} catch (KeyManagementException e) {
			logger.info(ExceptionUtils.getStackTrace(e));
		}
	}
}
