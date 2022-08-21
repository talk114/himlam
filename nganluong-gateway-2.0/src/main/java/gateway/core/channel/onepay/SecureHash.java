/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.core.channel.onepay;

import gateway.core.channel.onepay.dto.OnePayConstants;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 *
 * @author nnes
 */
public class SecureHash {

    public static String SECURE_SECRET;
    static final char[] HEX_TABLE = new char[]{
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final byte[] decodeHexArray = new byte[103];

    static {
        int i = 0;
        for (byte b : new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'}) {
            decodeHexArray[b] = (byte) i++;
        }
        decodeHexArray['a'] = decodeHexArray['A'];
        decodeHexArray['b'] = decodeHexArray['B'];
        decodeHexArray['c'] = decodeHexArray['C'];
        decodeHexArray['d'] = decodeHexArray['D'];
        decodeHexArray['e'] = decodeHexArray['E'];
        decodeHexArray['f'] = decodeHexArray['F'];
        
//        SECURE_SECRET = "A3EFDFABA8653DF2342E8DAC29B51AF0";        

    }

    public static String hashAllFields(Map fields) throws Exception {
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder buf = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if (fieldName.equals("vpc_CustomerUserAgent")) {
                fieldValue = fieldValue.replace("+", "");
            }
            if ((fieldValue != null) && (fieldValue.length() > 0) && fieldName.indexOf("vpc_") == 0) {
                buf.append(fieldName).append("=").append(fieldValue);
                if (itr.hasNext()) {
                    buf.append('&');
                }
            }
        }
       //logger.info(buf.toString());
        byte[] mac = null;
        try {
            byte[] b = decodeHexa(SECURE_SECRET.getBytes());
            SecretKey key = new SecretKeySpec(b, "HMACSHA256");
            Mac m = Mac.getInstance("HMACSHA256");
            m.init(key);
            m.update(buf.toString().getBytes("UTF-8"));
            mac = m.doFinal();
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
        String hashValue = hex(mac);
        return hashValue;
    }

    public static String hashAllFieldsRefund(Map fields) throws Exception {
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder buf = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0) && fieldName.indexOf("vpc_") == 0) {
                if (fieldName.indexOf("vpc_Message") == 0) {
                    fieldValue = ((String) fields.get(fieldName)).replace("+", " ");
                }
                buf.append(fieldName).append("=").append(fieldValue);
                if (itr.hasNext()) {
                    buf.append('&');
                }
            }
        }
        //logger.info(buf.toString());
        byte[] mac = null;
        try {
            byte[] b = decodeHexa(SECURE_SECRET.getBytes());
            SecretKey key = new SecretKeySpec(b, "HMACSHA256");
            Mac m = Mac.getInstance("HMACSHA256");
            m.init(key);
            m.update(buf.toString().getBytes("UTF-8"));
            mac = m.doFinal();
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
        String hashValue = hex(mac);
        return hashValue;
    }

    public static byte[] decodeHexa(byte[] data) throws Exception {
        if (data == null) {
            return null;
        }
        if (data.length % 2 != 0) {
            throw new Exception("Invalid data length:" + data.length);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte b1, b2;
        int i = 0;
        while (i < data.length) {
            b1 = decodeHexArray[data[i++]];
            b2 = decodeHexArray[data[i++]];
            out.write((b1 << 4) | b2);
        }
        out.flush();
        out.close();
        return out.toByteArray();
    }

    static String hex(byte[] input) {
        StringBuilder sb = new StringBuilder(input.length * 2);
        for (int i = 0; i < input.length; i++) {
            sb.append(HEX_TABLE[(input[i] >> 4) & 0xf]);
            sb.append(HEX_TABLE[input[i] & 0xf]);
        }
        return sb.toString();
    }
    /*
    public static void main(String[] args) throws Exception {
        String data = "vpc_AccessCode=VVX3BUGZ&vpc_Amount=10000000&vpc_BankId=2&vpc_CardMonth=01&vpc_CardName=Default&vpc_CardNo=1234567891234567&vpc_CardYear=18&vpc_Command=pay&vpc_Currency=VND&vpc_CustomerUserAgent=Mozilla/5.0 (Linux; Android 8.1.0; Joy 1  Build/OPM1.171019.026; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/80.0.3987.149 Mobile Safari/537.36&vpc_Locale=vn&vpc_MerchTxnRef=NL22947110&vpc_Merchant=NGANLUONG&vpc_OrderInfo=NL22947110&vpc_ReturnURL=https://www.nganluong.vn/onepay.return.php&vpc_TicketNo=1.55.206.238&vpc_Version=2";
        byte[] mac = null;
        try {
            byte[] b = decodeHexa(OnePayConstants.SECURE_SECRET.getBytes());
            SecretKey key = new SecretKeySpec(b, "HMACSHA256");
            Mac m = Mac.getInstance("HMACSHA256");
            m.init(key);
            m.update(data.toString().getBytes("UTF-8"));
            mac = m.doFinal();
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
        String hashValue = hex(mac);
    }
    */
}
