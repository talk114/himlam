package gateway.core.channel.mb_qrcode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;

import static gateway.core.channel.mb_qrcode.dto.MBBankVNPayOffUsConstant.SECRET_KEY;
import static gateway.core.channel.mb_qrcode.dto.MBBankVNPayOffUsConstant.USER_NAME;

public class MBBankVNPayOffusClientRequest {
    private static final Logger logger = LogManager.getLogger(MBBankVNPayOffusClientRequest.class);

    public static String callApi(String body, String api, String clientMessageId) throws IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException, KeyStoreException, UnrecoverableKeyException {
        ObjectMapper objectMapper = new ObjectMapper();

        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
        URL url = new URL(api);

        TrustManager[] trust_mgr = get_trust_mgr();
        SSLSocketFactory sockFact = null;
        //Certificate
        try {
            ClassLoader classLoader = MBBankVNPayOffusClientRequest.class.getClassLoader();
            File pKeyFile = new File(Objects.requireNonNull(classLoader.getResource("MB" + File.separator + "certificate.pem")).getFile());

            FileInputStream fis = new FileInputStream(pKeyFile);
            X509Certificate ca = (X509Certificate) CertificateFactory.getInstance(
                    "X.509").generateCertificate(new BufferedInputStream(fis));

//            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry(Integer.toString(1), ca);
            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            SSLContext context = SSLContext.getInstance("TLS");
            tmf.init(keyStore);
            context.init(null, tmf.getTrustManagers(), null);
            sockFact = context.getSocketFactory();
        } catch (CertificateException | KeyStoreException e) {
            System.out.println("CALL MB EXCEPTION: " + e.getMessage() + " |     " + body);
            logger.info(ExceptionUtils.getStackTrace(e));
            throw e;
        }
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String string, SSLSession ssls) {
                return true;
            }
        });
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setSSLSocketFactory(sockFact);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("clientMessageId", clientMessageId);
        con.setRequestProperty("secretKey", SECRET_KEY);
        con.setRequestProperty("username", USER_NAME);
        con.setDoOutput(true);
        System.out.println("CALL TO BANK MB " + api + " |     " + body);
        try (DataOutputStream wr = new DataOutputStream((con.getOutputStream()))) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            wr.write(input, 0, input.length);
            wr.flush();
        }

        StringBuilder response = new StringBuilder();
        InputStream is = null;

        int responseCode = con.getResponseCode();
        System.out.println("MB RESPONSE CODE: " + responseCode + " |     " + body);
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

        return response.toString();
    }

    private static TrustManager[] get_trust_mgr() {
        TrustManager[] certs = new TrustManager[]{new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] xcs, String string)
                    throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] xcs, String string)
                    throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }};
        return certs;
    }

    public static String sendGET(String api, String clientMessageId) throws IOException {
        URL url = new URL(api);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("clientMessageId", clientMessageId);
        con.setRequestProperty("secretKey", SECRET_KEY);
        con.setRequestProperty("username", USER_NAME);
        con.setDoOutput(true);
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            return response.toString();
        } else {
            return "GET request not worked";
        }
    }

    public static String getParameterString(Map<String, Object> map) {
        StringBuilder paramString = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            paramString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        if (paramString.toString().length() > 1) {
            return paramString.toString().substring(0, paramString.toString().length() - 1);
        }
        return paramString.toString();
    }

}
