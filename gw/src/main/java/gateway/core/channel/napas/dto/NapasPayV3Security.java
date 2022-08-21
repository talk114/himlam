package gateway.core.channel.napas.dto;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class NapasPayV3Security {
    private static final Logger logger = LogManager.getLogger(NapasPayV3Security.class);

    private static final String PATH_PUB_KEY_NAPAS = "napasV3_doiSoatFile/public01.cer";

    public static String checksumSha256(String data, String key) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest((data + key).getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
    }

    static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String decrypt(String data) {
        try {
            Cipher rsa;
            rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.DECRYPT_MODE, getPrivateKey());
//            byte[] utf8 = rsa.doFinal(data.getBytes());
            byte[] cypher_bytes = Base64.getDecoder().decode(data);
//            byte[] utf8 = rsa.doFinal(cypher_bytes);
            return new String(cypher_bytes, "UTF-8");
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    private static PublicKey getPrivateKey() throws FileNotFoundException, CertificateException, URISyntaxException {

        ClassLoader classLoader = new NapasPayV3Security().getClass().getClassLoader();
        File file = new File(classLoader.getResource(PATH_PUB_KEY_NAPAS).toURI());

        FileInputStream fin = new FileInputStream(file);
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) f.generateCertificate(fin);
        PublicKey pubKey = certificate.getPublicKey();
        return pubKey;
    }


    public static void main(String args[]) throws NoSuchAlgorithmException {
//        String data = "eyJwYXltZW50UmVzdWx0Ijp7ImFwaU9wZXJhdGlvbiI6IlBVUkNIQVNFIiwibWVyY2hhbnRJZCI6IlZJTU8iLCJvcmRlciI6eyJhbW91bnQiOiI1MDAwMCIsImNyZWF0aW9uVGltZSI6IjIwMTgtMDktMjhUMTA6MTM6MDcuMzYxWiIsImN1cnJlbmN5IjoiVk5EIiwiaWQiOiJPUkRfMTExMTExMjEifSwicmVzcG9uc2UiOnsiYWNxdWlyZXJDb2RlIjoiMCIsImdhdGV3YXlDb2RlIjoiU1VDQ0VTUyIsIm1lc3NhZ2UiOiJUcmFuc2FjdGlvbiBpcyBzdWNjZXNzZnVsLiJ9LCJyZXN1bHQiOiJTVUNDRVNTIiwic291cmNlT2ZGdW5kcyI6eyJwcm92aWRlZCI6eyJjYXJkIjp7ImJyYW5kIjoiU01MIiwibmFtZU9uQ2FyZCI6Ik5HVVlFTiBWQU4gQSIsImlzc3VlRGF0ZSI6IjAzMDciLCJudW1iZXIiOiI5NzA0MDB4eHh4eHgwMDE4Iiwic2NoZW1lIjoiT1RIRVJTIn19LCJ0eXBlIjoiQ0FSRCJ9LCJ0cmFuc2FjdGlvbiI6eyJhY3F1aXJlciI6eyJpZCI6IjgyODkwMDAxMiIsInRyYW5zYWN0aW9uSWQiOiI4Mjg5MDAwMTIifSwiYW1vdW50IjoiNTAwMDAiLCJjdXJyZW5jeSI6IlZORCIsImlkIjoiNDM3MzgzIiwidHlwZSI6IlBBWU1FTlQifSwidmVyc2lvbiI6IjEiLCJjaGFubmVsIjoiNzM5OSJ9fQ==";
//        String key = "97E38A7BC43EADB40794764061F598CB";
//        System.out.println(checksumSha256(data, key));
        String data = "eyJ0b2tlblJlc3VsdCI6eyJyZXN1bHQiOiJTVUNDRVNTIiwicmVzcG9uc2UiOnsiZ2F0ZXdheUNvZGUiOiJTVUNDRVNTIiwibWVzc2FnZSI6IlRyYW5zYWN0aW9uIGlzIHN1Y2Nlc3NmdWwuIn0sInRva2VuIjoiOTcwNDAwNTQxMjUyMDAxOCIsImNhcmQiOnsiYnJhbmQiOiJTTUwiLCJuYW1lT25DYXJkIjoiTkdVWUVOIFZBTiBBIiwiaXNzdWVEYXRlIjoiMDMwNyIsIm51bWJlciI6Ijk3MDQwMHh4eHh4eDAwMTgiLCJzY2hlbWUiOiJTTUwifSwiZGV2aWNlSWQiOiIzOTc2MyJ9LCJwYXltZW50UmVzdWx0Ijp7ImFwaU9wZXJhdGlvbiI6IlBVUkNIQVNFIiwibWVyY2hhbnRJZCI6Ik5HQU5MVU9OR0NFIiwib3JkZXIiOnsiYW1vdW50IjoiMjAwMDAiLCJjcmVhdGlvblRpbWUiOiIyMDIwLTA3LTIyVDEzOjAxOjA0LjY2NFoiLCJjdXJyZW5jeSI6IlZORCIsImlkIjoiNTA2MDMxOCJ9LCJyZXNwb25zZSI6eyJhY3F1aXJlckNvZGUiOiIwIiwiZ2F0ZXdheUNvZGUiOiJTVUNDRVNTIiwibWVzc2FnZSI6IlRyYW5zYWN0aW9uIGlzIHN1Y2Nlc3NmdWwuIn0sInJlc3VsdCI6IlNVQ0NFU1MiLCJzb3VyY2VPZkZ1bmRzIjp7InByb3ZpZGVkIjp7ImNhcmQiOnsiYnJhbmQiOiJTTUwiLCJuYW1lT25DYXJkIjoiTkdVWUVOIFZBTiBBIiwiaXNzdWVEYXRlIjoiMDMwNyIsIm51bWJlciI6Ijk3MDQwMHh4eHh4eDAwMTgiLCJzY2hlbWUiOiJPVEhFUlMifX0sInR5cGUiOiJDQVJEIn0sInRyYW5zYWN0aW9uIjp7ImFjcXVpcmVyIjp7ImRhdGUiOiIyMDIwLzA3LzIyIDEzOjAyOjE2IiwiaWQiOiI4MzU2NzI4MDQiLCJ0cmFuc2FjdGlvbklkIjoiODM1NjcyODA0In0sImFtb3VudCI6IjIwMDAwIiwiY3VycmVuY3kiOiJWTkQiLCJpZCI6IjE1ODkzMzUwIiwidHlwZSI6IlBBWU1FTlQifX19";
        String result = decrypt(data);
        System.out.println(result);
    }
}
