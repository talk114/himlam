package gateway.core.channel.tcb_qrcode;

import com.github.mervick.aes_everywhere.legacy.Aes256;
import gateway.core.channel.tcb_qrcode.dto.TCB_QrcodeConstants;
import org.apache.xml.security.utils.Base64;
import org.bouncycastle.util.encoders.Hex;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TCB_QrCodeSecurity {
    public static String MD5(String md5) throws NoSuchAlgorithmException {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] array = md.digest(md5.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public static String encryptAES256(String data) throws Exception{
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        byte [] keyByte = DatatypeConverter.parseHexBinary(TCB_QrcodeConstants.Secret_key);
        SecretKeySpec secretKey = new SecretKeySpec(keyByte, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE,secretKey,ivspec);

        return Hex.toHexString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decrypt(String strToDecrypt) {
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            byte[] key= DatatypeConverter.parseHexBinary(TCB_QrcodeConstants.Secret_key);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE,secretKey,ivspec);
            return new String(cipher.doFinal(Hex.decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Aes256.encrypt("32323223",TCB_QrcodeConstants.Secret_key));
    }


}
