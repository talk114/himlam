package gateway.core.channel.bidv;

import gateway.core.util.PGSecurity;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import vn.nganluong.naba.entities.PaymentAccount;

import java.io.*;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;


public class BIDVSecurity {


    static String PATH_PUB_KEY_VIMO = "/var/lib/payment_gateway_live/key/bidv_vimo/public_key.cer";
    static String PATH_PRIV_KEY_VIMO = "/var/lib/payment_gateway_live/key/bidv_vimo/private_key.pem";
    static String PATH_PUB_KEY_BIDV = "/var/lib/payment_gateway_live/key/bidv_vimo/public_key_BIDV.cer";

    public static void initParam(PaymentAccount acc) {
        PATH_PUB_KEY_VIMO = acc.getPublicKeyPath();
        PATH_PRIV_KEY_VIMO = acc.getPrivateKeyPath();
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
     */
    public static String sign(String message) throws IOException, GeneralSecurityException, URISyntaxException {
        Signature sign = Signature.getInstance(PGSecurity.SHA1RSA);
        sign.initSign(getPrivateKeyVimo());
        sign.update(message.getBytes("UTF-8"));
        return new String(Base64.encodeBase64(sign.sign()), "UTF-8");
    }

    /**
     * Verify response BIDV
     */
    public static boolean verify(String message, String signature)
            throws SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException,
            FileNotFoundException, CertificateException, URISyntaxException {
        Signature sign = Signature.getInstance(PGSecurity.SHA1RSA);
        sign.initVerify(getPublicKeyBIDV());
        sign.update(message.getBytes("UTF-8"));
        return sign.verify(Base64.decodeBase64(signature.getBytes("UTF-8")));
    }

    /**
     * Verify response VIMO
     */
    public static boolean verifyVimo(String message, String signature)
            throws SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException,
            FileNotFoundException, CertificateException, URISyntaxException {
        Signature sign = Signature.getInstance(PGSecurity.SHA1RSA);
        sign.initVerify(getPublicKeyVimo());
        sign.update(message.getBytes("UTF-8"));
        return sign.verify(Base64.decodeBase64(signature.getBytes("UTF-8")));
    }

    private static PublicKey getPublicKeyBIDV() throws FileNotFoundException, CertificateException, URISyntaxException {
//		URL url = BIDVSecurity.class.getResource(PATH_PUB_KEY_BIDV);
//		File file = new File(url.toURI());

        File file = new File(PATH_PUB_KEY_BIDV);
        FileInputStream fin = new FileInputStream(file);
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) f.generateCertificate(fin);
        PublicKey pubKey = certificate.getPublicKey();
        return pubKey;
    }

    private static PublicKey getPublicKeyVimo() throws FileNotFoundException, CertificateException, URISyntaxException {
//		URL url = BIDVSecurity.class.getResource(PATH_PUB_KEY_VIMO);
//		File file = new File(url.toURI());

        File file = new File(PATH_PUB_KEY_VIMO);
        FileInputStream fin = new FileInputStream(file);
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) f.generateCertificate(fin);
        PublicKey pubKey = certificate.getPublicKey();
        return pubKey;
    }

    private static RSAPrivateKey getPrivateKeyVimo() throws IOException, GeneralSecurityException, URISyntaxException {
//		URL url = BIDVSecurity.class.getResource(PATH_PRIV_KEY_VIMO);
//		File file = new File(url.toURI());

        File file = new File(PATH_PRIV_KEY_VIMO);
        String privateKeyPEM = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            privateKeyPEM += line + "\n";
        }
        br.close();
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

    public static void main(String args[]) throws URISyntaxException, IOException, GeneralSecurityException {
        // ec7ebacda3b36ca24a2eb748d479fae1
//		System.out.println(md5("|970418|BIDV||083001|083001|VIMO47860725|000|210757", "bidvvidmo123"));

//		String message1 = "0987654321|083002|000001500000|0922091147|000023||211301|808|970418|083002|000|||Han Van Loi|null|VND||||[8912||15000| Topup vimo, khach hang: 0987654321, So tien nap: 15000]";
//		String signature = sign(message1);
//		System.out.println(signature);

//		String message2 = "0987654321|083002|000001500000|0922143838|000023||211301|808|970418|083002|000|||Han Van Loi|null|VND||||[8919||15000| Topup vimo, khach hang: 0987654321, So tien nap: 15000]";
//		String signature2 = sign(message2);
//		System.out.println(verifyVimo(message1, signature));

//		System.out.println("Verify Vimo: \n" + verifyVimo("0987654321|083002|000001500000|0922143838|000023||211301|808|970418|083002|000|||Han Van Loi|null|VND||||[8919||15000| Topup vimo, khach hang: 0987654321, So tien nap: 15000]", "t5Wh9Ax+t4+HMreEi3n4tTXugM7TkbZKE1322rxFiA9mbEHaDJ/GCiYQdIeNUZtmE2vvj4MN7ZFlhzLzXXYr8F1cbRh60Trwnc+7Gn5aEgqOSgeyVpQdjXyCzcYuNKH1soGOepuruZZifAJt8Pe5jK2/YhA3RJPXELT+fQb2Ii8="));
//		System.out.println("Verify Vimo: \n" + verifyVimo("0987654321|083002|000001500000|0922091147|000023||211301|808|970418|083002|000|||Han Van Loi|null|VND||||[8912||15000| Topup vimo, khach hang: 0987654321, So tien nap: 15000]", "Es0iyreqNvP3qfz//lYA/6ds/VvZl55LSRiYdjr/QfvcSR5pvA/x2770xNexGxki3pHgu+7SeZ9h9RxH4w3sNtLMbLUDFZage0inC7QgeYzicOX6L/bBPmDTuk1AmTzDDOzsjcajdd+Uh3AbES46okXE/Z1x/fDuF4LmvnhLIGc="));

//		System.out.println("Verify BIDV: \n" + verify("0987654321|083002|000015789000|0922145116|000025||211301|808|970418|083002|000|||Han Van Loi|null|VND||||[8921||157890| Topup vimo, khach hang: 0987654321, So tien nap: 157890]", "k7SFkhuFeFmU27fTu+pLhfIWGsRpEluxhoPLUx/5SqUdVmZRrJGFIA6ExHv82MFZFuQdwbcHECXNgXJvgRs45EXUGe6eRGRDNhqQ+yW79xcEXcIH9yxmMMaQ1S3ZWrJq6MH3syEeHrLzPxuFF+6Jzq9jPtErONyLDQDCk00dx0M=") );

//		System.out.println(sign("<PROVIDER_ID>083</PROVIDER_ID><TRAN_DATE>20171020</TRAN_DATE><FILE_TYPE>1</FILE_TYPE>"));
//		System.out.println(verifyVimo("<PROVIDER_ID>083</PROVIDER_ID><TRAN_DATE>20171016</TRAN_DATE><FILE_TYPE>1</FILE_TYPE>", "mS6ioCSSPUxgffSUEydnfvjAaDm+mi5FSpjI2kF7fXI+9XPMPr3+0eaIw49daS+Wni3shhP46592ZG/aD2YW3L+ixdPx2AlRGOEbh2zqz5f7ixNXbsD1XbkhGBhOWahYzxLdNcY0TIRN6HamsKPY4wt9HHC7tepLuNdMnaYj0C4="));
    }


}