package gateway.core.channel.bidv.bidv_transfer_247;

import gateway.core.channel.bidv.BIDVSecurity;
import gateway.core.channel.dong_a_bank.ClientHandler;
import gateway.core.util.PGSecurity;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Objects;

import org.apache.commons.codec.digest.DigestUtils;
import vn.nganluong.naba.entities.PaymentAccount;

public class BIDVTransfer247Security {

    private static final Logger logger = LogManager.getLogger(BIDVSecurity.class);
    static String PATH_PUB_KEY_NGL = "bidv-key/ngan_luong/uat/nl_publickey.cer";
    //static String PATH_PUB_KEY_NGL = "bidv-key/ngan_luong/uat/public_key.cer";
    static String PATH_PRIV_KEY_NGL = "bidv-key/ngan_luong/uat/private_key.pem";
    static String PATH_PUB_KEY_BIDV = "bidv-key/ngan_luong/uat/nl_publickey.cer";
//    static String PATH_PUB_KEY_BIDV = "bidv-key/ngan_luong/uat/public_key_BIDV.cer";

    public static void initParam(PaymentAccount acc) {
        PATH_PUB_KEY_NGL = acc.getPublicKeyPath();
        PATH_PRIV_KEY_NGL = acc.getPrivateKeyPath();
        PATH_PUB_KEY_BIDV = acc.getPartnerKeyPath();
    }

    public static String md5(String[] param, String keyEncrypt) throws NoSuchAlgorithmException {
        String params = keyEncrypt + "|" + StringUtils.join(param, "|");
        return PGSecurity.md5(params);
    }

    public static String md5(String param, String keyEncrypt) throws NoSuchAlgorithmException {
        return PGSecurity.md5(keyEncrypt + "|" + param);
    }

    public static String sign(String[] param, String keyEncrypt)
            throws IOException, GeneralSecurityException, URISyntaxException {
        String params = keyEncrypt + "|" + StringUtils.join(param, "|");
        return sign(params);
    }

    public static String sign(String param, String keyEncrypt) throws IOException, GeneralSecurityException, URISyntaxException {
        String input = keyEncrypt + "|" + param;
        return sign(input);
    }

    public static boolean verifySign(String[] param, String signature, String keyEncrypt)
            throws IOException, GeneralSecurityException, URISyntaxException {
        String params = keyEncrypt + "|" + StringUtils.join(param, "|");
        return verify(params, signature);
    }

    /**
     * Sign request PW
     *
     */
    public static String sign(String message) throws IOException, GeneralSecurityException, URISyntaxException {
        Signature sign = Signature.getInstance(PGSecurity.SHA1RSA);
        sign.initSign(getPrivateKeyVimo());
        sign.update(message.getBytes(StandardCharsets.UTF_8));

        return new String(Base64.encodeBase64(sign.sign()), StandardCharsets.UTF_8);
    }

    /**
     * Verify response BIDV
     *
     */
    public static boolean verify(String message, String signature)
            throws SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException,
            FileNotFoundException, CertificateException, URISyntaxException {
        Signature sign = Signature.getInstance(PGSecurity.SHA1RSA);
        sign.initVerify(getPublicKeyBIDV());
        sign.update(message.getBytes(StandardCharsets.UTF_8));
        return sign.verify(Base64.decodeBase64(signature.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Verify response VIMO
     *
     */
    public static boolean verifyVimo(String message, String signature)
            throws SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException,
            FileNotFoundException, CertificateException, URISyntaxException {
        Signature sign = Signature.getInstance(PGSecurity.SHA1RSA);
        sign.initVerify(getPublicKeyVimo());
        sign.update(message.getBytes(StandardCharsets.UTF_8));
        return sign.verify(Base64.decodeBase64(signature.getBytes(StandardCharsets.UTF_8)));
    }

    private static PublicKey getPublicKeyBIDV() throws FileNotFoundException, CertificateException, URISyntaxException {
//		URL url = BIDVSecurity.class.getResource(PATH_PUB_KEY_BIDV);
//		File file = new File(url.toURI());
        ClassLoader classLoader = BIDVTransfer247Security.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(PATH_PUB_KEY_BIDV)).getFile());
        FileInputStream fin = new FileInputStream(file);
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) f.generateCertificate(fin);
        PublicKey pubKey = certificate.getPublicKey();
        return pubKey;
    }

    private static PublicKey getPublicKeyVimo() throws FileNotFoundException, CertificateException, URISyntaxException {
//		URL url = BIDVSecurity.class.getResource(PATH_PUB_KEY_VIMO);
//		File file = new File(url.toURI());
        ClassLoader classLoader = BIDVTransfer247Security.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(PATH_PUB_KEY_NGL)).getFile());
        FileInputStream fin = new FileInputStream(file);
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) f.generateCertificate(fin);
        PublicKey pubKey = certificate.getPublicKey();
        return pubKey;
    }

    private static RSAPrivateKey getPrivateKeyVimo() throws IOException, GeneralSecurityException, URISyntaxException {
        ClassLoader classLoader = BIDVTransfer247Security.class.getClassLoader();
        System.out.println(classLoader.getResourceAsStream(PATH_PRIV_KEY_NGL));
        File file = new File(Objects.requireNonNull(classLoader.getResource(PATH_PRIV_KEY_NGL)).toURI());

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

    public static <S> String getMD5(S ob) {
        return DigestUtils.md5Hex(ob.toString());
    }

    public static String getMD5data(String data) {
        return getMD5(data.trim());
    }

    public static void main(String args[]) throws URISyntaxException, IOException, GeneralSecurityException {

        String message1 = "bidv-nganluong-abc123|019004|019004|||NL1588833899155|NGANLUONG_Chuyen tien thanh toan hang hoa|100000.0|VND||||||||212901||||||9704181234123412341234|BIDV";
        String signature = sign(message1);
        System.out.println("signature=" + signature);
        System.out.println("verify = " + verify(message1, signature));
        System.out.println("verifySign = " + verifyVimo(message1, signature));
//        System.out.println("Verify Vimo: \n" + verifyVimo("0bidv-nganluong-abc123|019004|019004|||NL1588833899155|NGANLUONG_Chuyen tien thanh toan hang hoa|100000.0|VND||||||||212901||||||9704181234123412341234|BIDV", "t5Wh9Ax+t4+HMreEi3n4tTXugM7TkbZKE1322rxFiA9mbEHaDJ/GCiYQdIeNUZtmE2vvj4MN7ZFlhzLzXXYr8F1cbRh60Trwnc+7Gn5aEgqOSgeyVpQdjXyCzcYuNKH1soGOepuruZZifAJt8Pe5jK2/YhA3RJPXELT+fQb2Ii8="));
//		System.out.println("Verify Vimo: \n" + verifyVimo("0987654321|083002|000001500000|0922091147|000023||211301|808|970418|083002|000|||Han Van Loi|null|VND||||[8912||15000| Topup vimo, khach hang: 0987654321, So tien nap: 15000]", "Es0iyreqNvP3qfz//lYA/6ds/VvZl55LSRiYdjr/QfvcSR5pvA/x2770xNexGxki3pHgu+7SeZ9h9RxH4w3sNtLMbLUDFZage0inC7QgeYzicOX6L/bBPmDTuk1AmTzDDOzsjcajdd+Uh3AbES46okXE/Z1x/fDuF4LmvnhLIGc="));
//		System.out.println("Verify BIDV: \n" + verify("0987654321|083002|000015789000|0922145116|000025||211301|808|970418|083002|000|||Han Van Loi|null|VND||||[8921||157890| Topup vimo, khach hang: 0987654321, So tien nap: 157890]", "k7SFkhuFeFmU27fTu+pLhfIWGsRpEluxhoPLUx/5SqUdVmZRrJGFIA6ExHv82MFZFuQdwbcHECXNgXJvgRs45EXUGe6eRGRDNhqQ+yW79xcEXcIH9yxmMMaQ1S3ZWrJq6MH3syEeHrLzPxuFF+6Jzq9jPtErONyLDQDCk00dx0M=") );
//        System.out.println(sign("<PROVIDER_ID>083</PROVIDER_ID><TRAN_DATE>20171020</TRAN_DATE><FILE_TYPE>1</FILE_TYPE>"));
//        System.out.println(verifyVimo("<PROVIDER_ID>083</PROVIDER_ID><TRAN_DATE>20171016</TRAN_DATE><FILE_TYPE>1</FILE_TYPE>", "mS6ioCSSPUxgffSUEydnfvjAaDm+mi5FSpjI2kF7fXI+9XPMPr3+0eaIw49daS+Wni3shhP46592ZG/aD2YW3L+ixdPx2AlRGOEbh2zqz5f7ixNXbsD1XbkhGBhOWahYzxLdNcY0TIRN6HamsKPY4wt9HHC7tepLuNdMnaYj0C4="));
    }
}
