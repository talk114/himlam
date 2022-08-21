package vn.nganluong.naba.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

import org.apache.tomcat.util.codec.binary.Base64;

public class RSASignature {
	
	static String PATH_PUB_KEY_NGL = "D:/naba01_key/public_key.pem";
	static String PATH_PRV_KEY_NGL = "D:/naba01_key/private_key.pem";
	
	public void run() throws Exception {
        String message = "abc-test-baonq";
        String signed = signSHA512WithRSA(message);
//      message ="false";
        boolean a = verify(message, signed);
        System.out.println(a);
    }
	
    private static PublicKey getPublicKey() throws FileNotFoundException, CertificateException, URISyntaxException {
//		URL url = BIDVSecurity.class.getResource(PATH_PUB_KEY_VIMO);
//		File file = new File(url.toURI());
        ClassLoader classLoader = RSASignature.class.getClassLoader();
//        File file = new File(Objects.requireNonNull(classLoader.getResource(PATH_PUB_KEY_NGL)).getFile());
        File file = new File(PATH_PUB_KEY_NGL);
        FileInputStream fin = new FileInputStream(file);
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) f.generateCertificate(fin);
        PublicKey pubKey = certificate.getPublicKey();
        return pubKey;
    }
    
    private static RSAPrivateKey getPrivateKeyNL() throws IOException, GeneralSecurityException, URISyntaxException {
//        ClassLoader classLoader = BIDVTransfer247Security.class.getClassLoader();
//        System.out.println(classLoader.getResourceAsStream(PATH_PRIV_KEY_NGL));
        File file = new File(PATH_PRV_KEY_NGL);

        String privateKeyPEM = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            privateKeyPEM += line + "\n";
        }
        br.close();
        //LOG.info("privateKeyPEM="+privateKeyPEM);
        return getPrivateKeyFromString(privateKeyPEM);
    }
    
    private static RSAPrivateKey getPrivateKeyFromString(String key) throws IOException, GeneralSecurityException {
        String privateKeyPEM = key;
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----\n", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
        byte[] encoded = Base64.decodeBase64(privateKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        RSAPrivateKey privKey = (RSAPrivateKey) kf.generatePrivate(keySpec);
        return privKey;
    }

    public static String signSHA512WithRSA(String message) throws IOException, GeneralSecurityException, URISyntaxException {
        Signature sign = Signature.getInstance("SHA512withRSA");
        sign.initSign(getPrivateKeyNL());
        sign.update(message.getBytes("UTF-8"));
        return new String(Base64.encodeBase64(sign.sign()), "UTF-8");
    }
    
    public static String signSHA1withRSA(String message) throws IOException, GeneralSecurityException, URISyntaxException {
        Signature sign = Signature.getInstance("SHA1withRSA");
        sign.initSign(getPrivateKeyNL());
        sign.update(message.getBytes(StandardCharsets.UTF_8));

        return new String(Base64.encodeBase64(sign.sign()), StandardCharsets.UTF_8);
    }
    
    public static boolean verify(String message, String signature)
            throws SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException,
            FileNotFoundException, CertificateException, URISyntaxException {
        Signature sign = Signature.getInstance("SHA512withRSA");
        sign.initVerify(getPublicKey());
        sign.update(message.getBytes(StandardCharsets.UTF_8));
        return sign.verify(Base64.decodeBase64(signature.getBytes(StandardCharsets.UTF_8)));
    }
}
