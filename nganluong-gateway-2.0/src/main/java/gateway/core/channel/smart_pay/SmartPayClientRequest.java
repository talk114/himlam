package gateway.core.channel.smart_pay;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class SmartPayClientRequest {

    public static String callApi(String body, String api, String requestId) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        URL url = new URL(api);
//        String body = getParameterString(mapReq);
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String string, SSLSession ssls) {
                return true;
            }
        });

        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("X-Request-ID", requestId);
        con.setDoOutput(true);
        //System.out.println("Call To Bank ");
        try (DataOutputStream wr = new DataOutputStream((con.getOutputStream()))) {
            byte[] input = body.getBytes("utf-8");
            wr.write(input, 0, input.length);
            wr.flush();
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

        return response.toString();
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
