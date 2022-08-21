package gateway.core.channel.msb_qr;

import com.fasterxml.jackson.core.JsonProcessingException;
import gateway.core.util.PGUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
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

public class ApiClient {
    private static final Logger logger = LogManager.getLogger(ApiClient.class);

    public static String sendRequest(String Url, String query, String userPass, String indexCheckLog, String typeAPI) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        try {
            //			System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
            URL url = new URL(Url);
            PGUtil.WriteInfoLog("2.3. URL MSB-QRCODE :  ", indexCheckLog + PGUtil.CHARACTER + Url);
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
            // TODO Live
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            // TODO Test
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            if (userPass != null) {
                String basicAuth = "Basic " + new String(new Base64().encode(userPass.getBytes(StandardCharsets.UTF_8)));
                con.setRequestProperty("Authorization", basicAuth);
            }
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("partnerId", "NGANLUONG");
            con.setRequestProperty("username", "NganLuong");
            con.setRequestProperty("password", "$2a$05$eNFmKnqBiqZmqaZRhHHpteI2XuoBTAcdiEJf6aspLl4j97SxkVuzO");
            if (typeAPI.equals("CheckQrOrder")) {
                con.setRequestProperty("srvCode", "RC243");
            } else {
                con.setRequestProperty("srvCode", "RC242");
            }
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setConnectTimeout(60000);
            con.setReadTimeout(60000);

            try (DataOutputStream wr = new DataOutputStream((con.getOutputStream()))) {
                wr.writeBytes(query);
                wr.flush();
                wr.close();
            }

            // TODO Live
            PGUtil.WriteInfoLog("2.5. MSB-QRCODE HTTP STATUS :  ", indexCheckLog + PGUtil.CHARACTER + con.getResponseCode());
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
                PGUtil.WriteInfoLog("2.5. MSB-QRCODE RESPONSE TO GATEWAY :  ", indexCheckLog + PGUtil.CHARACTER + response.toString());
                return response.toString();
            }

            return null;

            // TODO Test
//            return "{\n" +
//                    "\t\"code\": \"00\",\n" +
//                    "\t\"message\": \"Success\",\n" +
//                    "\t\"data\": \"00020101021226240006970426011009102020105204343453037045406100000550105802VN5910NGAN LUONG6005HANOI62540118R000004.03NL7830480315CONG THANH TOAN07091100123456304AE49\",\n" +
//                    "\t\"url\": \" \",\n" +
//                    "\t\"checksum\": \"DAC316DB8F94C704BA276B280DE88184\",\n" +
//                    "\t\"isDelete\": \"TRUE\",\n" +
//                    "\t\"idQrcode\": \"9216\"\n" +
//                    "}";
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

//    public static boolean terminalOfVimo(String terminalId) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            String url = "https://gwmerchant.vimo.vn/getTerminal.php";
//
//            CheckTerminalVimoReq req = new CheckTerminalVimoReq("MSB_QRCODE", terminalId);
//            System.out.println("Req: " + mapper.writeValueAsString(req));
//            String vimoRes = HttpUtil.sendRequest(url, mapper.writeValueAsString(req), null);
//            System.out.println("Res: " + vimoRes);
//            CheckTerminalVimoRes res = mapper.readValue(vimoRes, CheckTerminalVimoRes.class);
//            if (res.getErrorCode().equalsIgnoreCase("SUCCESS")) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            logger.info(ExceptionUtils.getStackTrace(e));
//            return false;
//        }
//    }

    public static void main(String args[]) throws JsonProcessingException, IOException {
//        System.out.println(terminalOfVimo("11000003205"));
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
