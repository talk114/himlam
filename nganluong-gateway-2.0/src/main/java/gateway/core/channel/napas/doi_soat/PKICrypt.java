package gateway.core.channel.napas.doi_soat;

import gateway.core.channel.PaymentGate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class PKICrypt extends PaymentGate {

    protected static Logger LOG = LogManager.getLogger(PKICrypt.class);
    /**
     * String to hold name of the encryption algorithm.
     */
    public static String ALGORITHM = "RSA";
    public static int KEY_PAIR_LENGTH = 2048;
    public static String PRIVATE_KEY_FILE = "/home/chinhvd/Applications/PGPas/private.key";// "C:/keys/private.key";
    public static String PUBLIC_KEY_FILE = "/home/chinhvd/Applications/PGPas/public.key";// "C:/keys/public.key";

    /**
     * Generate key which contains a pair of private and public key using 1024
     * bytes. Store the set of keys in Prvate.key and Public.key files.
     *
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void generateKey() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM); // RSA
            keyGen.initialize(KEY_PAIR_LENGTH); // 2048
            KeyPair key = keyGen.generateKeyPair();
            PublicKey publicKey = key.getPublic();
            PrivateKey privateKey = key.getPrivate();

            File privateKeyFile = new File(PRIVATE_KEY_FILE);
            File publicKeyFile = new File(PUBLIC_KEY_FILE);

            // Create files to store public and private key
            if (privateKeyFile.getParentFile() != null) {
                privateKeyFile.getParentFile().mkdirs();
            }

            privateKeyFile.createNewFile();

            if (publicKeyFile.getParentFile() != null) {
                publicKeyFile.getParentFile().mkdirs();
            }

            publicKeyFile.createNewFile();

            // Saving the Public key in a file
            ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
            publicKeyOS.writeObject(key.getPublic());
            publicKeyOS.close();

            // Saving the Private key in a file
            ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
            privateKeyOS.writeObject(key.getPrivate());
            privateKeyOS.close();
        } catch (IOException | NoSuchAlgorithmException e) {
            LOG.error(e);
        }

    }

    /**
     * The method checks if the pair of public and private key has been
     * generated.
     *
     * @return flag indicating if the pair of keys were generated.
     */
    public static boolean areKeysPresent() {

        File privateKey = new File(PRIVATE_KEY_FILE);
        File publicKey = new File(PUBLIC_KEY_FILE);

        return privateKey.exists() && publicKey.exists();
    }

    /**
     * Encrypt the plain text using public key.
     *
     * @param data
     * @param publicKey
     * @param: text : original plain text
     * @param: key :The public key
     * @return Encrypted text
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, publicKey); // ENCRYPT_MODE=1
            cipherText = cipher.doFinal(data);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            LOG.error(e);
        }
        return cipherText;
    }

    /**
     * Decrypt text using private key.
     *
     * @param encData
     * @param privateKey
     * @param: text :encrypted text
     * @param: key :The private key
     * @return plain text
     */
    public static byte[] decrypt(byte[] encData, PrivateKey privateKey) {
        byte[] dectyptedTextB = null;
        try {
            // get an RSA cipher object and print the provider
            Cipher cipher = Cipher.getInstance(ALGORITHM);// RSA
            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, privateKey); // 2
            dectyptedTextB = cipher.doFinal(encData);

        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            LOG.error(ex);
        }
        return dectyptedTextB;
    }

    public static PublicKey getPublickey(String sPublicKeyName) {
        PublicKey pk = null;
        try {
            String sExtFile = getExtension(sPublicKeyName);
            switch (sExtFile) {
                case "pem": {
                    //                File f = new File(sPublicKeyName);
                    ClassLoader classLoader = new PKICrypt().getClass().getClassLoader();
                    File f = new File(classLoader.getResource(sPublicKeyName).toURI());
                    FileInputStream fis = new FileInputStream(f);
                    DataInputStream dis = new DataInputStream(fis);
                    byte[] keyBytes = new byte[(int) f.length()];
                    dis.readFully(keyBytes);
                    dis.close();
                    String temp = new String(keyBytes);
                    String publicKeyPEM = temp.replace("-----BEGIN PUBLIC KEY-----\n", "");
                    publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
                    byte[] decoded = Base64.decode(publicKeyPEM);
                    X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
                    KeyFactory kf = KeyFactory.getInstance("RSA");
                    pk = kf.generatePublic(spec);
                    break;
                }
                case "cer": {
                    //                FileInputStream fin = new FileInputStream(sPublicKeyName);
                    ClassLoader classLoader = new PKICrypt().getClass().getClassLoader();
                    File file = new File(classLoader.getResource(sPublicKeyName).toURI());
                    FileInputStream fin = new FileInputStream(file);
                    CertificateFactory f = CertificateFactory.getInstance("X.509");
                    X509Certificate certificate = (X509Certificate) f.generateCertificate(fin);
                    pk = certificate.getPublicKey();
                    break;
                }
                case "key":
                    ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PKICrypt.PUBLIC_KEY_FILE));
                    pk = (PublicKey) inputStream.readObject();
                    break;
                default:
                    break;
            }
            return pk;
        } catch (IOException | ClassNotFoundException | URISyntaxException | NoSuchAlgorithmException | CertificateException | InvalidKeySpecException ex) {
            LOG.error(ex);
            return null;
        }
    }

    public static PrivateKey getPrivateKey(String sPrivateKeyName) {
        PrivateKey pk = null;
        try {
            File file = new File(sPrivateKeyName);

            final String PEM_PRIVATE_START = "-----BEGIN PRIVATE KEY-----";
            final String PEM_PRIVATE_END = "-----END PRIVATE KEY-----";

            Path path = Paths.get(file.getAbsolutePath());

            String privateKeyPem = new String(Files.readAllBytes(path));

            privateKeyPem = privateKeyPem.replace(PEM_PRIVATE_START, "").replace(PEM_PRIVATE_END, "");
            privateKeyPem = privateKeyPem.replaceAll("\\s", "");

            byte[] pkcs8EncodedKey = Base64.decode(privateKeyPem);

            KeyFactory factory = KeyFactory.getInstance("RSA");
            pk = factory.generatePrivate(new PKCS8EncodedKeySpec(pkcs8EncodedKey));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return pk;
    }

    public static PrivateKey pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(File pemFileName) throws IOException, GeneralSecurityException {
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

            byte[] pkcs8EncodedKey = org.apache.commons.codec.binary.Base64.decodeBase64(privateKeyPem);

            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePrivate(new PKCS8EncodedKeySpec(pkcs8EncodedKey));

        } else if (privateKeyPem.indexOf(PEM_RSA_PRIVATE_START) != -1) { // PKCS#1
            // format
            privateKeyPem = privateKeyPem.replace(PEM_RSA_PRIVATE_START, "").replace(PEM_RSA_PRIVATE_END, "");
            privateKeyPem = privateKeyPem.replaceAll("\\s", "");

            DerInputStream derReader = new DerInputStream(org.apache.commons.codec.binary.Base64.decodeBase64(privateKeyPem));

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

    private static String getExtension(String sFile) {
        String extension = "";
        int i = sFile.lastIndexOf('.');
        int p = Math.max(sFile.lastIndexOf('/'), sFile.lastIndexOf('\\'));

        if (i > p) {
            extension = sFile.substring(i + 1);
        }
        return extension;

    }

    public static String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance("MD5");
        byte[] byteData = null;
        try {
            byteData = instance.digest(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            LOG.error(e);
        }
        // convert the byte to hex format
        String hexString = DatatypeConverter.printHexBinary(byteData);
        return hexString.toLowerCase();
    }

    static String checkSum(String content, String key) throws NoSuchAlgorithmException {
        String str = "";
        String strMa = md5(content);

        String strKQ = strMa;
        key = "5" + key + "5";
        char[] chars = key.toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            int idx1 = Integer.valueOf(key.substring(i, i + 1));
            int idx2 = idx1 + (20 - Integer.valueOf(key.substring(i + 1, i + 2)));
            str = str + strMa.substring(idx1, idx2);
        }
        strKQ = md5(str);
        return strKQ;
    }

    public static void main(String args[]) throws NoSuchAlgorithmException {
        String content = "TR[NOT]000000014[CRE]               NAPAS[TIME]085105[DATE]19022020[CSF]";
        String key = "971001";
        System.out.println(checkSum(content, key));
    }

}
