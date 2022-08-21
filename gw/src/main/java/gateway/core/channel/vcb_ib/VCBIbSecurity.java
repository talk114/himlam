package gateway.core.channel.vcb_ib;

import gateway.core.channel.vcb_ib.dto.VCBIbConstants;
import gateway.core.util.PGSecurity;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class VCBIbSecurity {

    private static final Logger logger = LogManager.getLogger(VCBIbSecurity.class);


    public static String encryptAES2(String text) throws Exception {
        byte[] key = Base64.decodeBase64("r6AV8M+aI9URybrV0bAYdjEpjKUUzPclC7c/7vD0MO4=");
        byte[] iv = Base64.decodeBase64("pUCBDzvX79NBoFu3InvWyA==");

        byte[] data = text.getBytes("UTF-8");

        SecretKey secretKey = new SecretKeySpec(key, "AES");
        final IvParameterSpec ivParam = new IvParameterSpec(iv);
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParam);
        return Base64.encodeBase64String(cipher.doFinal(data));
    }

    public static String sign(String input) throws Exception {
        Signature signature = initSign();
        signature.update(input.getBytes());
        return Base64.encodeBase64String(signature.sign());
    }

    private static Signature initSign() throws Exception {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        Signature signature = Signature.getInstance(PGSecurity.SHA1RSA);
        signature.initSign(factory.generatePrivate(getPrivKeyNL()));
        return signature;
    }

    public static boolean verifyVCB(String data, String sig) throws Exception {
        Signature signature = initVerifyVCB();
        signature.update(data.getBytes());
        return signature.verify(Base64.decodeBase64(sig));
    }

    private static Signature initVerifyVCB() throws Exception {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        Signature signature = Signature.getInstance(PGSecurity.SHA1RSA);
        signature.initVerify(factory.generatePublic(getPubKeyVCB()));
        return signature;
    }

    private static RSAPrivateKeySpec getPrivKeyNL() throws Exception {
        //File file = new File(PATH_PRIV_KEY_GW);

        ClassLoader classLoader = new VCBIbSecurity().getClass().getClassLoader();
        File file = new File(classLoader.getResource(VCBIbConstants.GW_PATH_PRIVATE_KEY).getFile());
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            logger.error("============================ PRIVATE KEY FILE NOT FOUND ============================");
        }

        SAXBuilder sb = new SAXBuilder();
        // Document doc = sb.build(new InputSource(new
        // StringReader(getPrivateKey())));

        // Document doc = sb.build(new InputSource(fileReader == null ? new
        // StringReader(privateKey) : fileReader));
        Document doc = sb.build(new InputSource(fileReader));

        Element root = doc.getRootElement();
        Element modulusElem = root.getChild("Modulus");
        Element exponentElem = root.getChild("Exponent");
        byte[] modBytes = Base64.decodeBase64(modulusElem.getText().trim().getBytes());
        byte[] exponentBytes = Base64.decodeBase64(exponentElem.getText().trim().getBytes());

        BigInteger modules = new BigInteger(1, modBytes);
        BigInteger publicExponent = new BigInteger(1, exponentBytes);

        // RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(modules, d);
        Element pElement = root.getChild("P");
        Element qElement = root.getChild("Q");
        Element dpElement = root.getChild("DP");
        Element dqElement = root.getChild("DQ");
        Element inverseQElement = root.getChild("InverseQ");
        Element dElement = root.getChild("D");

        byte[] dBytes = Base64.decodeBase64(dElement.getText().trim().getBytes());
        BigInteger dBig = new BigInteger(1, dBytes);

        byte[] pBytes = Base64.decodeBase64(pElement.getText().trim().getBytes());
        BigInteger pBig = new BigInteger(1, pBytes);

        byte[] qBytes = Base64.decodeBase64(qElement.getText().trim().getBytes());
        BigInteger qBig = new BigInteger(1, qBytes);

        byte[] dpBytes = Base64.decodeBase64(dpElement.getText().trim().getBytes());
        BigInteger dpBig = new BigInteger(1, dpBytes);

        byte[] dqBytes = Base64.decodeBase64(dqElement.getText().trim().getBytes());
        BigInteger dqBig = new BigInteger(1, dqBytes);

        byte[] inverseQBytes = Base64.decodeBase64(inverseQElement.getText().trim().getBytes());
        BigInteger inverseQBig = new BigInteger(1, inverseQBytes);

        RSAPrivateKeySpec privSpec = new RSAPrivateCrtKeySpec(modules, publicExponent, dBig, pBig, qBig, dpBig, dqBig,
                inverseQBig);
        return privSpec;
    }

    /**
     * <b>GET PUBLIC KEY VCB</b>
     */
    private static RSAPublicKeySpec getPubKeyVCB() throws Exception {
        // URL url = Security.class.getResource(PATH_PUB_KEY_VCB);
        // File file = new File(url.toURI());

        //File file = new File(PATH_PUB_KEY_VCB);
        ClassLoader classLoader = new VCBIbSecurity().getClass().getClassLoader();
        File file = new File(classLoader.getResource(VCBIbConstants.VCB_ECOM_PATH_PUBLIC_KEY).getFile());

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            logger.error("============================ PUBLIC KEY VCB FILE NOT FOUND ============================");
        }

        SAXBuilder sb = new SAXBuilder();
        // Document doc = sb.build(new InputSource(new
        // StringReader(getPublicKeyVCB())));
        // Document doc = sb.build(new InputSource(fileReader == null ? new
        // StringReader(publicKeyVCB) : fileReader));
        Document doc = sb.build(new InputSource(fileReader));

        Element root = doc.getRootElement();
        Element modulusElem = root.getChild("Modulus");
        Element dElem = root.getChild("Exponent");
        byte[] modBytes = Base64.decodeBase64(modulusElem.getText().trim().getBytes());
        byte[] dBytes = Base64.decodeBase64(dElem.getText().trim().getBytes());

        BigInteger modules = new BigInteger(1, modBytes);
        BigInteger d = new BigInteger(1, dBytes);

        RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(modules, d);
        return pubSpec;
    }

    public static String decryptAES2(String text) throws Exception {
        byte[] key = Base64.decodeBase64("r6AV8M+aI9URybrV0bAYdjEpjKUUzPclC7c/7vD0MO4=");
        byte[] iv = Base64.decodeBase64("pUCBDzvX79NBoFu3InvWyA==");

        SecretKey secretKey = new SecretKeySpec(key, "AES");
        final IvParameterSpec ivParam = new IvParameterSpec(iv);
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);
        byte[] data = Base64.decodeBase64(text);
        return new String(cipher.doFinal(data), "UTF-8");
    }

}
