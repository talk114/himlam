/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.core.util;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.utils.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import java.util.Map;


/**
 * @author nnes
 */
public final class PGSecurity {

    public static final String MD2RSA = "MD2withRSA";
    public static final String MD5RSA = "MD5withRSA";
    public static final String SHA1RSA = "SHA1withRSA";
    public static final String SHA256RSA = "SHA256withRSA";
    public static final String SHA384RSA = "SHA384withRSA";
    public static final String SHA512RSA = "SHA512withRSA";

    public static final String WITH_NGANLUONG_CALLBACK = "5b6ebb63d8941bad5210522163e511b9";

    private static final Logger logger = LogManager.getLogger(PGSecurity.class);

    private PGSecurity(){}

    public static String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance("MD5");
        byte[] byteData = null;
        try {
            byteData = instance.digest(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        // convert the byte to hex format
        String hexString = DatatypeConverter.printHexBinary(byteData);
        return hexString.toLowerCase();
    }

    public static String md5(String... input) throws NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        for (String str : input) {
            sb.append(str);
        }
        MessageDigest instance = MessageDigest.getInstance("MD5");
        byte[] byteData = null;
        try {
            byteData = instance.digest(sb.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        // convert the byte to hex format
        String hexString = DatatypeConverter.printHexBinary(byteData);
        return hexString.toLowerCase();
    }

    public static String encryptAES(String key, String value) {
        try {
            //IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, generateRandomIv());

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encode(encrypted);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            logger.info(ex.getMessage());
        }

        return null;
    }

    public static String decryptAES(String key, String encrypted) {
        try {
            //IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, generateRandomIv());
            byte[] original = cipher.doFinal(Base64.decode(encrypted.getBytes("UTF-8")));
            return new String(original);
        } catch (Base64DecodingException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            logger.info(ex.getMessage());
        }

        return null;
    }

    public static String encrypt3DES(String input, String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
        byte[] arrayBytes = getValidKey(key);
        KeySpec ks = new DESedeKeySpec(arrayBytes);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DESede");
        Cipher cipher = Cipher.getInstance("DESede");
        SecretKey seckey = skf.generateSecret(ks);
        cipher.init(Cipher.ENCRYPT_MODE, seckey);
        byte[] plainByte = input.getBytes();
        byte[] encryptedByte = cipher.doFinal(plainByte);
        return Base64.encode(encryptedByte);

    }

    public static String decrypt3DES(String encrypted, String key) throws Exception {
        byte[] arrayBytes = getValidKey(key);
        KeySpec ks = new DESedeKeySpec(arrayBytes);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DESede");
        Cipher cipher = Cipher.getInstance("DESede");
        SecretKey seckey = skf.generateSecret(ks);
        cipher.init(Cipher.DECRYPT_MODE, seckey);
        byte[] encryptByte = Base64.decode(encrypted);
        byte[] plainByte = cipher.doFinal(encryptByte);
        return new String(plainByte);
    }

    private static byte[] getValidKey(String key) {
        try {
            String sTemp = md5(key).substring(0, 24).toLowerCase();
            return sTemp.getBytes();
        } catch (Exception e) {
        }
        return null;
    }

    private static IvParameterSpec generateRandomIv() {
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }
    public static void removeCryptographyRestrictions() {
        if (!isRestrictedCryptography()) {
            logger.info("Cryptography restrictions removal not needed");
            return;
        }
        try {
            /*
             * Do the following, but with reflection to bypass access checks:
             *
             * JceSecurity.isRestricted = false;
             * JceSecurity.defaultPolicy.perms.clear();
             * JceSecurity.defaultPolicy.add(CryptoAllPermission.INSTANCE);
             */
            final Class<?> jceSecurity = Class.forName("javax.crypto.JceSecurity");
            final Class<?> cryptoPermissions = Class.forName("javax.crypto.CryptoPermissions");
            final Class<?> cryptoAllPermission = Class.forName("javax.crypto.CryptoAllPermission");

            final Field isRestrictedField = jceSecurity.getDeclaredField("isRestricted");
            isRestrictedField.setAccessible(true);
            final Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(isRestrictedField, isRestrictedField.getModifiers() & ~Modifier.FINAL);
            isRestrictedField.set(null, false);

            final Field defaultPolicyField = jceSecurity.getDeclaredField("defaultPolicy");
            defaultPolicyField.setAccessible(true);
            final PermissionCollection defaultPolicy = (PermissionCollection) defaultPolicyField.get(null);

            final Field perms = cryptoPermissions.getDeclaredField("perms");
            perms.setAccessible(true);
            ((Map<?, ?>) perms.get(defaultPolicy)).clear();

            final Field instance = cryptoAllPermission.getDeclaredField("INSTANCE");
            instance.setAccessible(true);
            defaultPolicy.add((Permission) instance.get(null));

        } catch (final Exception e) {
            logger.warn("Failed to remove cryptography restrictions", e);
        }
    }

    private static boolean isRestrictedCryptography() {
        // This matches Oracle Java 7 and 8, but not Java 9 or OpenJDK.
        final String name = System.getProperty("java.runtime.name");
        final String ver = System.getProperty("java.version");
        return name != null && name.equals("Java(TM) SE Runtime Environment")
                && ver != null && (ver.startsWith("1.7") || ver.startsWith("1.8"));
    }

    public static String sha256(String input) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(input.getBytes());
        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static void main(String args[]) throws Exception {
        String rawData = "{\"from\":\"NGLG\",\"data\":{\"partnerCode\":\"NGLG\",\"accNameSuffix\":\"NGUYEN VAN A\",\"accSuffix\":\"\",\"accType\":\"O\"},\"requestId\":\"ALE00000001\"}";
        File file = new File(FilePathUtil.getAbsolutePath(PGSecurity.class,"vccb_key/private_NL_live.pem"));
        String signature = signatureRSASHA256(rawData,pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(file));
        System.out.println(signature);
        boolean isTrue = verifySHA256withRSA(signature,rawData,getPublicKey("D:\\NganLuong\\gateway\\nganluong-gateway-2.0\\src\\main\\resources\\vccb_key\\bvb_live_pub.pem"));
        System.out.println(isTrue);

//        String rawData = "{\"from\":\"NGLG\",\"data\":{\"partnerCode\":\"NGLG\",\"accNameSuffix\":\"NGUYEN VAN A\",\"accSuffix\":\"\",\"accType\":\"O\"},\"requestId\":\"ALE00000001\"}";
//        File file = new File(FilePathUtil.getAbsolutePath(PGSecurity.class,"vccb_key/private-NL-sb.pem"));
//        String signature = "vByk0432KQ6SE73sQY/X0M9Ff1KaBglNnGjFxkRcK0snJ1ttU4ucwM7C4Y6SezOHOlnmFGS4k84HvDcOCy7ESPkveGwsWpzozV8NMXZuYl+cIPU0y40Cu7vuC+jNOLaCQZUw6t0SUixvus/knA8i+xDoc92BzhFpWUW8cFyye+zR6puIYsCSmv9dImgrq2sSdgogqYywE5Go15UXkuX7pJT+qUriaVd0wk9oIqzR995uUBNlnsZruBey5dsgi0BGlkO69s0ZgGV/AQH9JIu/wos6RaXu9lKGH+Vwfymw58SUxXC2XE+PYh3FNSlSm/dPSHd+RaMEb6T+i+dPZjbCNA==";
//        System.out.println(signature);
//        boolean isTrue = verifySHA256withRSA(signature,rawData,getPublicKey("vccb_key/BVB_VIMO_SIT_2048_pub.pem"));
//        System.out.println(isTrue);
//        System.out.println(decrypt3DES("A16LQ0VKWSy4ewtx46+TT+f7+AKtLtgpfG29ywrpHE2OQ0rSC+gLTk/Hop69My8rHgupHs64jai7XKGDpChK04qjxD2Rx83QJNlij9s0/Ye9UVTPGo5oa1GSRnleLXo2wG+9YWZlL8ZFM2Ie6Hh3rmfuGtKZlZfp4FoKNuJtc1AvT6FyoDtEPDWlLwg1lGPp", "2345678901"));
    }


    /**
     * @param rawData
     * @param privateKey
     * @return String signature encode by Base64
     */
    public static String signatureRSASHA256(String rawData, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(rawData.getBytes(StandardCharsets.UTF_8));
            String result = java.util.Base64.getEncoder().encodeToString(signature.sign());
            return result;
        } catch (NoSuchAlgorithmException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        } catch (InvalidKeyException e) {

        } catch (SignatureException e) {

        }
        return null;
    }

    /**
     * @param signatureBase64
     * @param rawData
     * @param key
     * @return true if match
     */
    public static boolean verifySHA256withRSA(String signatureBase64, String rawData, PublicKey key) {
        byte[] signature = java.util.Base64.getDecoder().decode(signatureBase64);
        try {
            Signature signatureCell = Signature.getInstance("SHA256withRSA");

        signatureCell.initVerify(key);
        signatureCell.update(rawData.getBytes(StandardCharsets.UTF_8));

        return signatureCell.verify(signature);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("PGSecurity.verifySHA256withRSA: "+"NoSuchAlgorithmException");
        } catch (InvalidKeyException e){
            System.out.println("PGSecurity.verifySHA256withRSA: "+"InvalidKeyException");
        } catch (SignatureException e){
            logger.info(ExceptionUtils.getStackTrace(e));
            System.out.println("PGSecurity.verifySHA256withRSA: "+"SignatureException");
        }
        return false;
    }
    public static PrivateKey getPrivateKey(File file){
        try (FileReader keyReader = new FileReader(file);
             PemReader  pemReader = new PemReader (keyReader)){
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PemObject pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(content);
            return (PrivateKey) factory.generatePrivate(privateKeySpec);
        } catch (NoSuchAlgorithmException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        } catch (FileNotFoundException e){
            System.out.println("PGSecurity.getPrivateKey: "+"FileNotFoundException");
        } catch (IOException e) {
            System.out.println("PGSecurity.getPrivateKey: "+"IOException");
        } catch (InvalidKeySpecException e){
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    public static PublicKey getPublicKey(String keyPath){
        try (FileReader keyReader  = new FileReader(keyPath);
             PemReader pemReader = new PemReader(keyReader)){
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PemObject pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(content);

            return (PublicKey) keyFactory.generatePublic(publicKeySpec);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("PGSecurity.getPublicKey: "+"NoSuchAlgorithmException");
        } catch (FileNotFoundException e){
            System.out.println("PGSecurity.getPublicKey: "+"FileNotFoundException");
        } catch (IOException e) {
            System.out.println("PGSecurity.getPublicKey: "+"IOException");
        } catch (InvalidKeySpecException e){
            System.out.println("PGSecurity.getPublicKey: "+"InvalidKeySpecException");
        }
        return null;
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

    public static String encryptTripleDESECB(String key, String data) throws InvalidKeyException,
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            NoSuchProviderException,
            IllegalBlockSizeException,
            BadPaddingException {
        PGSecurity.addBCProvider();
        if(key.length() != 24){
            throw new InvalidKeyException("Wrong key length");
        }
        SecretKey keyLoad = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8),"DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding","BC");
        cipher.init(Cipher.ENCRYPT_MODE, keyLoad);
        byte[] dataByte = data.getBytes(StandardCharsets.UTF_8);
        byte[] cipherText = cipher.doFinal(dataByte);
        return Base64.encode(cipherText);
    }

    public static String decryptTripleDESECB(String key, String data) throws InvalidKeyException,
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            NoSuchProviderException,
            IllegalBlockSizeException,
            BadPaddingException, Base64DecodingException, UnsupportedEncodingException {
        PGSecurity.addBCProvider();
        if(key.length() != 24){
            throw new InvalidKeyException("Wrong key length");
        }
        SecretKey keyLoad = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8),"DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding","BC");
        cipher.init(Cipher.DECRYPT_MODE, keyLoad);
        byte[] dataByte = Base64.decode(data);
        byte[] cipherText = cipher.doFinal(dataByte);
        return new String(cipherText,"UTF8");
    }

    private static void addBCProvider(){
        Security.addProvider(new BouncyCastleProvider());
    }
}
