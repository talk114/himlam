package gateway.core.channel.vccb;

import gateway.core.channel.vccb.dto.VCCBConstants;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
import vn.nganluong.naba.entities.PaymentAccount;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class VCCBSecurity {
//    private static  String PRIVATE_KEY_GW = "/var/lib/payment_gateway_test/key/vccb_ngl/private.pem";
//    private static  String PUBLIC_KEY_VCCB = "/var/lib/payment_gateway_test/key/vccb_ngl/public.pem";

    private static  String PRIVATE_KEY_GW = "";
    private static  String PUBLIC_KEY_VCCB = "";

    public static void initParam(PaymentAccount acc) {
    	PRIVATE_KEY_GW = acc.getPrivateKeyPath();
    	PUBLIC_KEY_VCCB = acc.getPartnerKeyPath();
    }
    
    public static String CheckSum(String input) throws NoSuchAlgorithmException {
    	MessageDigest md = MessageDigest.getInstance("SHA-256");
    	byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
    	return Hex.encodeHexString(hash);
    }
    
    public static String md5(String dataFileRequest) throws NoSuchAlgorithmException {
    	MessageDigest md = MessageDigest.getInstance("MD5");
    	byte[] messageDigest = md.digest(dataFileRequest.getBytes());
    	String hexString = DatatypeConverter.printHexBinary(messageDigest);
		return hexString;
    }
    
    public static String sign(String input) throws IOException, GeneralSecurityException {
        Signature signature = initSign();
        signature.update(input.getBytes());
        String result = Hex.encodeHexString(signature.sign());
        return result;
    }

    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public static boolean verifySign(String signature, String data) throws InvalidKeyException, Exception {
        byte[] sign = hexStringToByteArray(signature);
        Signature rsaVerify = Signature.getInstance("SHA256withRSA");
        rsaVerify.initVerify(getPublicVCCB());
        rsaVerify.update(data.getBytes());
        return rsaVerify.verify(sign);
    }

    public static PublicKey getPublicVCCB() throws Exception {

        ClassLoader classLoader = new VCCBSecurity().getClass().getClassLoader();
        File file = new File(classLoader.getResource(VCCBConstants.PATH_VCCB_PUBLIC_KEY).getFile());

        String publicKeyPem = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            publicKeyPem += line + "\n";
        }
        br.close();

        publicKeyPem = publicKeyPem.replace("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyPem = publicKeyPem.replace("-----END PUBLIC KEY-----", "");
        publicKeyPem = publicKeyPem.replace("\n", "");
        byte[] publicKeyBytes = Base64.decodeBase64(publicKeyPem);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }


    private static Signature initSign() throws IOException, GeneralSecurityException {

        ClassLoader classLoader = new VCCBSecurity().getClass().getClassLoader();
        File file = new File(classLoader.getResource(VCCBConstants.PATH_GATEWAY_PRIVATE_KEY).getFile());
        Signature signature = Signature.getInstance("SHA256withRSA");
        //File file = new File(PRIVATE_KEY_GW);
        PrivateKey privateKey = pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(file);
        signature.initSign(privateKey);
        return signature;
    }

    private static PrivateKey pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(File pemFileName) throws IOException, GeneralSecurityException {
        // PKCS#8 format
        final String PEM_PRIVATE_START = "-----BEGIN PRIVATE KEY-----";
        final String PEM_PRIVATE_END = "-----END PRIVATE KEY-----";

        // PKCS#1 format
        final String PEM_RSA_PRIVATE_START = "-----BEGIN RSA PRIVATE KEY-----";
        final String PEM_RSA_PRIVATE_END = "-----END RSA PRIVATE KEY-----";

        Path path = Paths.get(pemFileName.getAbsolutePath());

        String privateKeyPem = new String(Files.readAllBytes(path));

        if (privateKeyPem.indexOf(PEM_PRIVATE_START) != -1) { // PKCS#8 format
            privateKeyPem = privateKeyPem.replace(PEM_PRIVATE_START, "").replace(PEM_PRIVATE_END, "");
            privateKeyPem = privateKeyPem.replaceAll("\\s", "");

            byte[] pkcs8EncodedKey = Base64.decodeBase64(privateKeyPem);

            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePrivate(new PKCS8EncodedKeySpec(pkcs8EncodedKey));

        } else if (privateKeyPem.indexOf(PEM_RSA_PRIVATE_START) != -1) { // PKCS#1
            // format
            privateKeyPem = privateKeyPem.replace(PEM_RSA_PRIVATE_START, "").replace(PEM_RSA_PRIVATE_END, "");
            privateKeyPem = privateKeyPem.replaceAll("\\s", "");

            DerInputStream derReader = new DerInputStream(Base64.decodeBase64(privateKeyPem));

            DerValue[] seq = derReader.getSequence(0);

            if (seq.length < 9) {
                throw new GeneralSecurityException("Could not parse a PKCS1 private key.");
            }

            // skip version seq[0];
            BigInteger modulus = seq[1].getBigInteger();
            BigInteger publicExp = seq[2].getBigInteger();
            BigInteger privateExp = seq[3].getBigInteger();
            BigInteger prime1 = seq[4].getBigInteger();
            BigInteger prime2 = seq[5].getBigInteger();
            BigInteger exp1 = seq[6].getBigInteger();
            BigInteger exp2 = seq[7].getBigInteger();
            BigInteger crtCoef = seq[8].getBigInteger();

            RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2,
                    exp1, exp2, crtCoef);

            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePrivate(keySpec);
        }

        throw new GeneralSecurityException("Not supported format of a private key");
    }
}
