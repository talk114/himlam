package gateway.core.channel.vccb;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.channel.vccb.dto.VCCBConstants;
import gateway.core.channel.vccb.dto.req.RootRequest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.Map.Entry;

public class VCCBClientRequest {

	private static final Logger logger = LogManager.getLogger(VCCBClientRequest.class);

    public static String callApi(Map<String, Object> mapReq , String api) throws IOException{
    	ObjectMapper objectMapper = new ObjectMapper();
    	URL url = new URL(api);
		String body = getParameterString(mapReq);
		System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");

//            SSLContext ssl_ctx = SSLContext.getInstance("TLS");
		TrustManager[] trust_mgr = get_trust_mgr();
		SSLSocketFactory sockFact = null;

//		//Certificate
//		try {
//			ClassLoader classLoader = new VCCBClientRequest().getClass().getClassLoader();
//			File pKeyFile = new File(classLoader.getResource("vccb_key/intermediate.crt").getFile());
////                File pKeyFile = new File(classLoader.getResource("citibank-key-Certificate/PC20190926089219.CER").getFile());
////			String pKeyPassword = "BdVUwRiV4Vb72f2i";
//			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
//			KeyStore keyStore = KeyStore.getInstance("PKCS12");
//			InputStream keyInput = new FileInputStream(pKeyFile);
//			keyStore.load(keyInput, null);
//			keyInput.close();
//			keyManagerFactory.init(keyStore, null);
//			SSLContext context = SSLContext.getInstance("TLS");
//			context.init(keyManagerFactory.getKeyManagers(), trust_mgr, new SecureRandom());
//			sockFact = context.getSocketFactory();
//		} catch (Exception e) {
//			logger.info(ExceptionUtils.getStackTrace(e));
//		}



		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String string, SSLSession ssls) {
				return true;
			}
		});

		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

//		con.setSSLSocketFactory( sockFact );

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);
		//System.out.println("Call To Bank ");
		try (DataOutputStream wr = new DataOutputStream((con.getOutputStream()))) {
			wr.writeBytes(body);
			wr.flush();
		}
		
		StringBuilder response = new StringBuilder();
		InputStream is = null;
	
		int responseCode = con.getResponseCode();
		logger.info("HTTP STATUS RES:  " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) {
			is = con.getInputStream();
		}else {
			is = con.getErrorStream();
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
    
    

    public static void callApiToUploadFile(String sourceFile, RootRequest dataReq) throws ClientProtocolException, IOException {
    	ObjectMapper objectMapper = new ObjectMapper();
    	
    	CloseableHttpClient httpClient = HttpClients.createDefault();
    	HttpPost httpPost = new HttpPost(VCCBConstants.URL_API_UPLOAD_RECONCILIATION);
    	
    	File file = new File(sourceFile);
    	
    	FileEntity entity = new FileEntity(file);
    	
    	httpPost.setEntity(entity);
    	HttpResponse response = httpClient.execute(httpPost);
    	HttpEntity resEntity = response.getEntity();

//        System.out.println(response.getStatusLine());
        if (resEntity != null) {;
//          System.out.println(EntityUtils.toString(resEntity));
        }

        httpClient.close();
    	
//    	File file = new File(sourceFile);
//    	FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
//		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//		builder.addPart("file", fileBody);
//		builder.addTextBody("data", objectMapper.writeValueAsString(dataReq));
//		HttpEntity entity = builder.build();
//
//		HttpPost request = new HttpPost(VCCBConstants.URL_API_UPLOAD_RECONCILIATION);
//		request.setEntity(entity);
//		HttpClient client = HttpClientBuilder.create().build();
//		try {
//			HttpResponse response = client.execute(request);
//			int statusCode = response.getStatusLine().getStatusCode();
//			System.out.println(statusCode);
//		} catch (IOException e) {
//			logger.info(ExceptionUtils.getStackTrace(e));
//		}
	}
    
    
    public static String getParameterString(Map<String, Object> map) {
        StringBuilder paramString = new StringBuilder();
        for (Entry<String, Object> entry : map.entrySet()) {
            paramString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        if(paramString.toString().length() > 1)
        	return paramString.toString().substring(0, paramString.toString().length() - 1);
        return paramString.toString();
    }

    public static void main(String[] args) {

//		TrustManager[] trust_mgr = get_trust_mgr();
//		SSLSocketFactory sockFact = null;

		//Certificate
//		try {
//			ClassLoader classLoader = new VCCBClientRequest().getClass().getClassLoader();
//			File pKeyFile = new File(classLoader.getResource("vccb_key/intermediate.crt").getFile());
////                File pKeyFile = new File(classLoader.getResource("citibank-key-Certificate/PC20190926089219.CER").getFile());
//			String pKeyPassword = "";
//			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
//			KeyStore keyStore = KeyStore.getInstance("JKS");
//			InputStream keyInput = new FileInputStream(pKeyFile);
//			keyStore.load(keyInput, pKeyPassword.toCharArray());
//			keyInput.close();
//			keyManagerFactory.init(keyStore, null);
//			SSLContext context = SSLContext.getInstance("TLS");
//			context.init(keyManagerFactory.getKeyManagers(), trust_mgr, new SecureRandom());
//			sockFact = context.getSocketFactory();
//			System.out.println(sockFact);
//		} catch (Exception e) {
//			logger.info(ExceptionUtils.getStackTrace(e));
//			System.out.println(e.getMessage());
//		}
//
//            LocalDateTime now = LocalDateTime.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMdHHmmss");
//            String formatDateTime = now.format(formatter);
//        System.out.println(formatDateTime);

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
