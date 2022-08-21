package gateway.core.channel.msb_offus;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.channel.msb_offus.dto.MSBOFFUSConstant;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MSBOFFUSClientRequest {
    private static final Logger logger = LogManager.getLogger(MSBOFFUSClientRequest.class);

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static String sendRequest(String urlStr, String query, String method) throws Exception {
        try {
            URL url = new URL(urlStr);
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
                wr.writeBytes(query);
                wr.flush();
                wr.close();
            }
            StringBuilder response = new StringBuilder();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
            }
            return response.toString();
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    public static String sendRequestRefund(String Url, String query) throws Exception {
        try {
            URL url = new URL(Url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            
            String basicAuth = "Bearer " + MSBOFFUSConstant.ACCESS_TOKEN;
            con.setRequestProperty("AccessToken", basicAuth);
            
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setConnectTimeout(60000);
            con.setReadTimeout(60000);
            try (DataOutputStream wr = new DataOutputStream((con.getOutputStream()))) {
                wr.writeBytes(query);
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
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }
}
