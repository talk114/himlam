package gateway.core.channel.msb_ecom;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.channel.msb_ecom.dto.req.BaseRequest;
import gateway.core.channel.msb_ecom.dto.req.RootRequest;
import gateway.core.channel.msb_ecom.dto.res.RootResponse;
import gateway.core.util.PGUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class MSBEcomClientRequest {
    private static final Logger logger = LogManager.getLogger(MSBEcomClientRequest.class);
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static RootResponse sendRequest(String urlStr, RootRequest rootReq, BaseRequest baseReq, String mediaType, String method) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        try {
//            System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
            RootResponse apiResponse = new RootResponse();
            URL url = new URL(urlStr);
            // PGUtil.WriteInfoLog("2.1 MSB ECOM URL - " + apiName + " API: ", rootReq.getTransId() + PGUtil.CHARACTER + urlStr);
//			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
//			TrustManager[] trust_mgr = get_trust_mgr();
//			ssl_ctx.init(null, trust_mgr, new SecureRandom());
//			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
//			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//				@Override
//				public boolean verify(String string, SSLSession ssls) {
//					return true;
//				}
//			});
//
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setConnectTimeout(60000);
            con.setReadTimeout(60000);

            try (DataOutputStream wr = new DataOutputStream((con.getOutputStream()))) {
                wr.writeBytes(objectMapper.writeValueAsString(baseReq));
                wr.flush();
                wr.close();
            }

            // PGUtil.WriteInfoLog("2.2 MSB ECOM HTTP STATUS - " + apiName + " API: ", rootReq.getTransId() + PGUtil.CHARACTER + con.getResponseCode());
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
                // PGUtil.WriteInfoLog("2.3 MSB ECOM RESPONSE - " + apiName + " API: ", rootReq.getTransId() + PGUtil.CHARACTER + response.toString());
                apiResponse.setHttpCode(con.getResponseCode());
                apiResponse.setBodyRes(response.toString());
                return apiResponse;
            }

            return null;
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    private static TrustManager[] get_trust_mgr() {
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