package gateway.core.channel.dong_a_bank;

import gateway.core.channel.cybersouce.service.impl.CybersourceServiceImpl;
import gateway.core.util.FilePathUtil;
import gateway.core.util.PGSecurity;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

public class DABSecurity {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    static String PRIVATE_KEY_PATH = "/var/lib/payment_gateway_test/key/dong_a_bank/nganluong_priv_key.pem";
    static String PUBLIC_KEY_PATH = "/var/lib/payment_gateway_test/key/dong_a_bank/nganluong_pub_key.pem";

    static String PARTNER_PUBLIC_KEY_PATH = "/var/lib/payment_gateway_test/key/dong_a_bank/dab_nl_pub.pem";
    static String PARTNER_PRIVATE_KEY_PATH = "/var/lib/payment_gateway_test/key/dong_a_bank/dab_nl_pri.pem";

    public static String calculateRFC2104HMAC(String data, String key) throws java.security.SignatureException {
        String result;
        try {
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());
            // base64-encode the hmac
            result = Base64.encodeBase64String(rawHmac);
        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

    public static String sign(String message, String privFilePath) throws IOException, GeneralSecurityException {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Signature sign = Signature.getInstance("SHA256withRSA", "BC");

//        PrivateKey privateKey = getPrivateKey(privFilePath);
        String url = FilePathUtil.getAbsolutePath(DABSecurity.class,privFilePath );
        File keyFile = new File(url);
        PrivateKey privateKey = PGSecurity.pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(keyFile);
        sign.initSign(privateKey);
        sign.update(message.getBytes("UTF-8"));
        return new String(Base64.encodeBase64(sign.sign()), "UTF-8");
    }

    private static RSAPrivateKey getPrivateKey(String privFilePath) throws IOException, GeneralSecurityException {
//		java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        String strKeyPEM = "";
      //  BufferedReader br = new BufferedReader(new FileReader(privFilePath));
        BufferedReader br = new BufferedReader(new FileReader(getAbsoluteFilePath(privFilePath)));
        String line;
        while ((line = br.readLine()) != null) {
            strKeyPEM += line;
        }
        br.close();
        // Remove the first and last lines
        strKeyPEM = strKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "");
        strKeyPEM = strKeyPEM.replace("-----END PRIVATE KEY-----", "");

        // Base64 decode data
        byte[] encoded = Base64.decodeBase64(strKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateKey privKey = (RSAPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(encoded));
        return privKey;
    }

    public static boolean verify(String message, String signature, String pubFilePath)
            throws IOException, GeneralSecurityException {
        RSAPublicKey publicKey = getPublicKey(getAbsoluteFilePath(pubFilePath));

        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initVerify(publicKey);
        sign.update(message.getBytes("UTF-8"));
        return sign.verify(Base64.decodeBase64(signature.getBytes("UTF-8")));
    }

    public static RSAPublicKey getPublicKey(String pubFilePath) throws IOException, GeneralSecurityException {
        String strKeyPEM = "";
        BufferedReader br = new BufferedReader(new FileReader(pubFilePath));
        String line;
        while ((line = br.readLine()) != null) {
            strKeyPEM += line;
        }
        br.close();

        // Remove the first and last lines
        strKeyPEM = strKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "");
        strKeyPEM = strKeyPEM.replace("-----END PUBLIC KEY-----", "");
        // Base64 decode data
        byte[] encoded = Base64.decodeBase64(strKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));

        return pubKey;
    }
    public static String getAbsoluteFilePath(String relativePath) {
        ClassLoader classLoader = CybersourceServiceImpl.class.getClassLoader();
        String absoluteFilePath = Objects.requireNonNull(classLoader.getResource(relativePath).getFile());
        return absoluteFilePath;
    }


    public static void main(String args[]) throws Exception {
        // String input= "sendOrderInformNewProcess10/09/2009 10:34:09";
        // String key = "HX9A:T22K1ccqB&e-~r[Hy5B:#9S5zR0e<=QSi/;";
        // System.out.println(calculateRFC2104HMAC(input, key));

        String pubLive = "D:\\public-key.pem";
        String privLive = "D:\\private-key.pem";

        String data = "NGUYEN TUAN VINH";
        String signed = sign(data, privLive);
//		System.out.println(signed);

//		System.out.println(verify(data, signed, pubLive));

//		System.out.println(verify("DAB|VM0030575306|1", "AaMPgkooE2sLFRyzpm0f2YBodtqnRXoKoxHYCAhcjo1ao+jfcBFyX4bYw5akJzjl6bZBFHkHOiQBUSdfgpL7m8m1z/lDPpKdxKGA6Msi0xxKBehTpIBm72fbB6d8mDRSRw8fU2uIRJeKrtJmN9qUlD2vJNlUQX5pNoiN+HI292ceIKjshKZsFzDGB2Re3+T/nLgp/zXawJf56ZA1gPV5PsQUeyT4QkkQX6vSWfh6pmhU0i4Bd/55VUpdqY0gNMK4w0ylStJduM829xZ2X7gU0Kwnjlgc4Q+pVz0bsx06BRsUV0udmHlMI1yLQF3jIMTSDzvAsBEaXZtNGM2tVQ==", PARTNER_PUBLIC_KEY_PATH));
    }
}
