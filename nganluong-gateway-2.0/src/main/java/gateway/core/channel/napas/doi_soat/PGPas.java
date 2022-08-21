package gateway.core.channel.napas.doi_soat;

import gateway.core.channel.PaymentGate;
import gateway.web.operator.PaymentHistoryController;
import org.apache.logging.log4j.LogManager;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PGPas extends PaymentGate {

    protected static org.apache.logging.log4j.Logger LOG = LogManager.getLogger(PGPas.class);
    private static java.util.Base64.Decoder base64de = java.util.Base64.getDecoder();
    // CÃ¡c kÃ½ tá»± xuá»‘ng dÃ²ng \r \n
    private static char eol1 = (char) 13;
    private static char eol2 = (char) 10;

    public static String SYM_ALGORITHM = "AES";
    public static int SESSION_KEY_LENGTH = 128;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String oriFile = "D:\\032620_ACQ_VIMOCE_971001_1_SL_ECOM.dat";
        String encFile = "/home/thanh/Downloads/041420_ACQ_NGANLUONGCE_971019_1_TC_ECOM.dat.pgp";
        String outFile = "/home/thanh/gateway_doc/Key/napas_v3/999999_ACQ_NGANLUONG_999999_1_SL_TC_ECOM.dat.pgp";
        String privateKeyPath = "/home/thanh/Alepay/nganluong_gateway/src/main/resources/napasV3_doiSoatFile/private.pem";
//        PGPas.PGPdecrypt(encFile, outFile);
    }

    public static void PGPencrypt(String originalFile, String encryptedFile, String publicKeyPath) throws Exception {
        PublicKey publicKey = PKICrypt.getPublickey(publicKeyPath);
//        WriteInfoLog("############### PUBLIC KEY" + publicKey.toString());
//        System.out.println(publicKey.toString());
//        LOG.info("############### PUBLIC KEY" + publicKey.toString());
//		System.out.println(new Date().toString() + ":  ----Begin encrypt----");
        // Sinh khóa phiên ngẫu nhiên sử dụng thuật toán AES-128-EBC
        SecretKey sessionKey = PGPas.generateSessionkey();

        Path path = Paths.get(originalFile);
        byte[] data = Files.readAllBytes(path);

//		System.out.println("data input: " + data.toString());
//		System.out.println(new Date().toString() + ": Read file successfully");
        // Mã hóa dữ liệu sử dụng khóa phiên đối xứng AES
        byte[] encData = symmetricEncrypt(data, sessionKey);

//		System.out.println(new Date().toString() + ": Encrypt data successfully");
        // Mã hóa khóa phiên sử dụng thuật toán mã hóa bất đối xứng RSA với public key.
        byte[] sessionKeyByte = sessionKey.getEncoded();
        byte[] encSessionKey = PKICrypt.encrypt(sessionKeyByte, publicKey);
//        WriteInfoLog("#################### sessionKeyByte: " + new String(sessionKeyByte));
//        LOG.info("#################### sessionKeyByte: " + new String(sessionKeyByte));
//        WriteInfoLog("#################### encSessionKey: " + new String(encSessionKey));
//        LOG.info("#################### encSessionKey: " + new String(encSessionKey));

        // Encode base64 khóa phiên và dữ liệu sau khi được mã hóa
        String base64EncData = new String(Base64.encode(encData));
        String base64EncSessionKey = new String(Base64.encode(encSessionKey)).replaceAll("(?:\\r\\n|\\n\\r|\\n|\\r)",
                "");

        String output = base64EncSessionKey + eol1 + eol2 + base64EncData;

//		System.out.println("output: " + output);
//		System.out.print("base64EncSessionKey:" + base64EncSessionKey);
//		System.out.println("base64EncData: " + base64EncData);
//		System.out.println(new Date().toString() + ": Encode base64 successfully");
        // Ghi ra file, khóa phiên và dữ liệu sau khi được mã hóa nằm trên 2 dòng
        BufferedWriter bw = new BufferedWriter(new FileWriter(encryptedFile));
        bw.write(base64EncSessionKey);
        // chèn thêm ký tự xuống dòng
        bw.write(eol1);
        bw.write(eol2);
        bw.write(base64EncData);
        bw.flush();
        bw.close();
//		System.out.println(new Date().toString() + ": Write encrypted file successfully");
    }

    public static void PGPdecrypt(String encryptedFile, String decryptedFile, String privateKeypath) throws Exception {
        ClassLoader classLoader = new PGPas().getClass().getClassLoader();
        File file = new File(classLoader.getResource(privateKeypath).toURI());
        PrivateKey privateKey = PKICrypt.pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(file);

//		System.out.println(new Date().toString() + ":  ----Begin decrypt----");
//		System.out.println("File Encrypted: " + encryptedFile);
        Path path = Paths.get(encryptedFile);
        byte[] allContent = Files.readAllBytes(path);
//		System.out.println(new Date().toString() + ": Read file successfully");

        // Loại bỏ các ký tự xuống dòng vô nghĩa ở đầu file
        int i = 0, s = 0;
        while (((char) allContent[i] == eol1) || ((char) allContent[i] == eol2)) {
            i++;
        }
        s = i;
        // Tìm đến ký tự xuống dòng để cắt chuỗi
        while ((eol1 != (char) allContent[i]) && (eol2 != (char) allContent[i])) {
            i++;
        }
        // Cắt lấy phần khóa phiên được mã hóa và encode
        byte[] base64EncSessionKey = Arrays.copyOfRange(allContent, s, i);
        // Loại bỏ các ký tự xuống dòng vô nghĩa ở giữa file
        while (((char) allContent[i] == eol1) || ((char) allContent[i] == eol2)) {
            i++;
        }
        int len = allContent.length;
        // Loại bỏ các ký tự xuống dòng vô nghĩa ở cuối file
        while (((char) allContent[len - 1] == eol1) || ((char) allContent[len - 1] == eol2)) {
            len--;
        }

        // Cắt lấy phần dữ liệu đã mã hóa và encode
        byte[] base64EncData = Arrays.copyOfRange(allContent, i, len);

        // Decode base64 khóa và dữ liệu
        byte[] encSessionKey = base64de.decode(base64EncSessionKey);
        byte[] decData = base64de.decode(base64EncData);
//		System.out.println(new Date().toString() + ": Decode base64 successfully");

        //Giải mã khóa phiên sử dụng private key
        byte[] sessionKeyByte = PKICrypt.decrypt(encSessionKey, privateKey);
        SecretKey sessionKey = new SecretKeySpec(sessionKeyByte, PGPas.SYM_ALGORITHM);
//		System.out.println(new Date().toString() + ": Decrypt session key successfully");

        //Giải mã dữ liệu sử dụng khóa phiên lấy được trong bước trước
        byte[] data = symmetricDecrypt(decData, sessionKey);
//		System.out.println(new Date().toString() + ": Decrypt date successfully");
        // Ghi file
        path = Paths.get(decryptedFile);
        Files.write(path, data);
//		System.out.println(new Date().toString() + ": Write data file successfully");

    }

    public static void PGPencrypt1(String originalFile, String encryptedFile, PublicKey publicKey) throws Exception {
        System.out.println(new Date().toString() + ":  ----Begin encrypt----");
        //Sinh khóa phiên ngẫu nhiên sử dụng thuật toán AES-128-EBC
        SecretKey sessionKey = PGPas.generateSessionkey();

        Path path = Paths.get(originalFile);
        byte[] data = Files.readAllBytes(path);

        System.out.println(new Date().toString() + ": Read file successfully");
        // Mã hóa dữ liệu sử dụng khóa phiên đối xứng AES
        byte[] encData = symmetricEncrypt(data, sessionKey);

        System.out.println(new Date().toString() + ": Encrypt data successfully");
        // Mã hóa khóa phiên sử dụng thuật toán mã hóa bất đối xứng RSA với public key.
        byte[] sessionKeyByte = sessionKey.getEncoded();
        byte[] encSessionKey = PKICrypt.encrypt(sessionKeyByte, publicKey);
        System.out.println(new Date().toString() + ": Encrypt session key successfully");

        ///Encode base64 khóa phiên và dữ liệu sau khi được mã hóa
        String base64EncData = new String(Base64.encode(encData));
        String base64EncSessionKey = new String(Base64.encode(encSessionKey)).replaceAll("(?:\\r\\n|\\n\\r|\\n|\\r)",
                "");

        System.out.println("base64EncData: " + base64EncData);
        System.out.println("base64EncSessionKey:" + base64EncSessionKey);

        System.out.println(new Date().toString() + ": Encode base64 successfully");
        //Ghi ra file, khóa phiên và dữ liệu sau khi được mã hóa nằm trên 2 dòng
        BufferedWriter bw = new BufferedWriter(new FileWriter(encryptedFile));
        bw.write(base64EncSessionKey);
        // chèn thêm ký tự xuống dòng
        bw.write(eol1);
        bw.write(eol2);
        bw.write(base64EncData);
        bw.flush();
        bw.close();
        System.out.println(new Date().toString() + ": Write encrypted file successfully");
    }

    private static SecretKey generateSessionkey() {
        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance(SYM_ALGORITHM);
            keyGen.init(SESSION_KEY_LENGTH);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PGPas.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static byte[] symmetricEncrypt(byte[] messageB, SecretKey key) throws Exception {
        // SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        Cipher cipher = Cipher.getInstance(SYM_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] buf = cipher.doFinal(messageB);
        return buf;
    }

    private static byte[] symmetricDecrypt(byte[] encryptedTextB, SecretKey key) throws Exception {

        Cipher decipher = Cipher.getInstance(SYM_ALGORITHM);
        decipher.init(Cipher.DECRYPT_MODE, key);

        byte[] plainText = decipher.doFinal(encryptedTextB);
        return plainText;
    }

}
