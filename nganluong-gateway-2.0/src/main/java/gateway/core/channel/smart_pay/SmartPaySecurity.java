package gateway.core.channel.smart_pay;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class SmartPaySecurity {

    public static String genSign(String hashKey, Map<String, Object> map) {
        StringBuilder stringA = new StringBuilder();
        map.entrySet().forEach((entry) -> {
            String value = String.valueOf(entry.getValue());
            if (value != null && !value.isEmpty()) {
                stringA.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
        });

        if (stringA.length() > 0) {
            stringA.deleteCharAt(stringA.length() - 1);
        }

        String stringSignTemp = stringA.toString() + "&key=" + hashKey;
//        System.out.println("SIGNATURE TO SHA256 = " + stringSignTemp);
        return sha256(stringSignTemp).toUpperCase();
    }

    private static String sha256(String plaintext) {
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(plaintext.getBytes(StandardCharsets.UTF_8));
            for (int i = 0; i < hash.length; i++) {
                String hex = String.format("%02x", hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return hexString.toString();
    }
}
