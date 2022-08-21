package clientTest;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;

public class HttpUtilTest {


    public static String send(String url, Map<String, Object> map, String userPass) throws IOException {
        String query = getParameterString(map);
        return send(url, query, userPass);
    }

    public static String send(String url, Map<String, Object> paramMap) throws IOException {
        return send(url, paramMap, null);
    }

    public static String sendRequest(String url, String param, String userPass) throws IOException {
        return send(url, param, userPass);
    }

    private static String send(String Url, String query, String userPass) throws IOException {
        try {
            URL url = new URL(Url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if (userPass != null) {
                String basicAuth = "Basic " + new String(new Base64().encode(userPass.getBytes()));
//            	String basicAuth = "Api-key " + new String(new Base64().encode(userPass.getBytes()));
                con.setRequestProperty("Authorization", basicAuth);
            }
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("User-Agent", "gateway/1.0.0");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setConnectTimeout(60000);
            con.setReadTimeout(60000);

            try (DataOutputStream wr = new DataOutputStream((con.getOutputStream()))) {
                wr.write(query.getBytes("UTF-8"));
                wr.flush();
                wr.close();
            }
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
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

    public static String getParameterString(Map<String, Object> map) {
        StringBuilder paramString = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            paramString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return paramString.toString();
    }

    public static String sendGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept-Charset", "UTF-8");
        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            System.out.println("Response : " + response.toString());
            return response.toString();
        }
        return null;
    }
}
