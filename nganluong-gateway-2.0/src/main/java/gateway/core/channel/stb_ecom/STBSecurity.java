package gateway.core.channel.stb_ecom;

import gateway.core.channel.dong_a_bank.DABSecurity;
import gateway.core.channel.stb_ecom.dto.req.LinkCardReq;
import gateway.core.util.FilePathUtil;
import gateway.core.util.PGSecurity;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
import vn.nganluong.naba.entities.PaymentAccount;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.Arrays;

public class STBSecurity {
	private static final String PRIV_KEY_NL = "STB_ECOM/privatekey.pem";
	private static final String SecretKey = "8ca5c7d0cc98de34";

	private static final String ALGORITHM = "DESede";
	private static final String UNICODE_FORMAT = "UTF8";
	private static final String TRIPLE_DES_TRANSFORMATION = "DESede";
	private static final String TRIPLE_DES = "AES/ECB/PKCS5Padding";

	private static String PASSWORD_ENCRYPT = "a8e698210483fb64"; // 90a1b2e2132137d1 a8e698210483fb64

	// static final String privateKeySTB =
	// "MIIEowIBAAKCAQEAhYUwl/KgehLrfQBPbLwjfg4znBhtfj5fd7I4fGk7w/1VIIr8ynLBESQFh5+jOlSUNR2g7wYBFua+wzx0ti7lmNQ6Fzamuky1vdE2CuyhjyLkkp9K6ncGFZIUgxtMm8EglMv5gbHwMW6CvKcdY6M6n8fDCwFJdS+L3toRQ/6sDFQiBFqvCZZz0lC7bLGpSavJ/dXkaBzE0Kc0lyKQ6CuqfJZPNr1GxyCZzW6aGOR2nP7noUdGLdVk6TAmkdzYklxsH+A0iYIVVRjsvLDcOJtCJo3PD6DJcddh7dnvaYy9rnrL2mlAFE5OL97OQdF0Fk5msqWMP1tJ+y0LifoLeEBbcwIDAQABAoIBAAS6Ak5euG+Z29xA9o7S2i7MMeeEtduSOa7zECzXTNZTUYAla21/QnZo4Ak9DXozJEv9EIVloQQ+huJCcZ9Bab9PQ/8uYBiYPpbJoTAh0FZQ1eaa3PwP2pdI5JD/lmatH4ok7mA4/FfDob0r7U5pqNmfKLhrAz6ywDDTZ2kQyuZkxkp4NE6g3zv2JY9rGgS17L+BC8Vu2UUeDqjE30NO2ptM7PKK+aBZ/QMVv2+KXCbs1R9rKnVJwm9y7sNgfsdVRsnOZeeqYqfR07g9812WK6MhOhU7FL62Gw1xadfkN65kh+yLKliaBhLw/fAQz+ZJp3TBGEnPUg0176yJirNrPAECgYEA/tfDyAO1EmT2aQe88+MKqnbsgvBK2DZwLE3daaTsgTbdkVbVnQTF3XB/ODiiW9bJCaYVgBaC1fY85OZNrKvcQUm0j4Cg8qOeEwLl815vAoIYefBfjSFUP49TN3UWPgcuhbRh1+MSy40JNcYUYcm/bymEjJRxL8zvmW0/+xiKOtMCgYEAhiBlmloNwR1Pj5LJS32WXnyP3FbebQwqQcuHW1NYG8sR33plcANHmyQiTd8tZMjAA3iDqcrwmkqiPXFhB7nqTj+He8I6CbuUD1iBPhbp6BsYEVHij/qR8k8GzB1IpHD8IPRA6KjOMcHVf6Y/uT1hT/y1oORWQWee5JkPxo65uOECgYA7MvQP6oyNkVaMX3fNT9iScL3QTPGV+E9me1AL0nv+KDigIoPMzecI4bEr5jwRCXv5+dLUxmXuPh03LyisC+BGalC/ZSn+M3PtBqdUOYOgGYiGCwinSC7B9RFnuJNKJXDxLk3sUJt1HXYeP71MWglWUVaxMWp5qCmggnhyY+j4cQKBgH1Q550/Znp4GE3cp5aQ0F2XpBIKV+cq5pByyRTa2gedxCU5Bm2RouWg7Sw0yQTRTBXyE5IguoxuDwe5Vx8Q5YoWcU+AORm+2Kc92gNZBVKHfgUwrK45PwNWMtOiyKxbpDZl//9hOMXkhMWvXvqEDtfdFRYuQSj618pd+A9casoBAoGBAJdBEn1ozxZpCHojEuWGWdx4x94xzA8OZAocXNaewo9y15cPqtjRb3HRwGVMpaDyjhxVtIce2ZbfRbiyyHlgunsLtvPs5bnYbXMEAnAdr7NOflL5e2RbOUdbIHQyMhnbUhLULRCPwolIW9moi5AHCGdx6+5olKZPVA3vmX3iPP1j";

	private static String PRIV_KEY_STB = "/var/lib/payment_gateway_live/key/stb/private_key_STB.pem";
	private static String PUB_KEY_STB = "STB_ECOM/publickeySTB.pem";
	private static String PrivateKeySTB = "STB_ECOM/privatekeyNL.pem";
	private static String PUB_KEY_NL = "STB_ECOM/publickeyNL.pem";




	public static void initParam(PaymentAccount acc) {
		PASSWORD_ENCRYPT = acc.getEncryptKey();
		PRIV_KEY_STB = acc.getPartnerKeyPath();
		PUB_KEY_STB = acc.getPublicKeyPath();
	}
	public static String encryptCardNumber(String cardnumber) {
		if (cardnumber == null) {
			cardnumber = "";
		}
//		return "XXXX..." + cardnumber.substring(cardnumber.length() -4, cardnumber.length());
		return "XXXXX" +cardnumber.substring(cardnumber.length() -10, cardnumber.length()-4) +"XXXXX";
	}

	public static void main(String[] args) throws Exception {
		String test2 = "9704060129837294";
		String a = encryptCardNumber(test2);
		System.out.println(a);
//		String de = "Wls/RPavMIJ0zBRfRFJcBzSt7qpYDlagPNoCHQsCM9Wxe5HJvqFiN7/vUx3W5l1mJEiI0NpWHC6LVwTdq7wl6HHxmRorM3TkUEoX3gW+uIjv7h57e1Gn9NuqWxB3QedrQpOYLvGQO8mOCcNxUIyDmrst2tlfI+gOXiVCeewEqaPkBYqQyvLRBzkQLo/0hyuXYhZki2KutD05eWRc1wjJ0VHRMfzIvD8WbjXXZhMTZFIsFgI+piSBnUW8mNRh5OQO";
//		String test = "9Gbe/ZD2Iviru/wGKWfR4BY/orcSt3JjnfmHTPao037bKn1vt0QkMs/hXLgLi0ymLMnfcs2MDg7X2kfnkP8S/M+akEKfXLgvGOTT6OIQyyEu/D5vwNCz+njSV8SSN1le";
//	    String a = STBSecurity.Decrypt3DES(test,"8ca5c7d0cc98de34");
		//String a = STBSecurity.md5();
		//System.out.println(a);
		//	System.out.println(STBSecurity.md5("{\"Data\":\"9Gbe/ZD2Iviru/wGKWfR4BY/orcSt3JjnfmHTPao037bKn1vt0QkMs/hXLgLi0ymLMnfcs2MDg7X2kfnkP8S/M+akEKfXLgvGOTT6OIQyyEu/D5vwNCz+njSV8SSN1le\",\"FunctionName\":\"ERequestOTP\",\"RequestDateTime\":\"2022-01-07T03:49:22Z\",\"RequestID\":\"bda0adea-9c0a-4f6f-a82b-a651228919e7\"}"));
		//System.out.println(STBSecurity.sign("A95F65C420007E3D2B9A7B6224FD97A5"));
////		System.out.println(Decrypt3DES("Wls/RPavMIK66f+lqcd4dqHbhM59Z+rRU4f3mScOdflnuWbIaeJiJYcE3zDmF3U9K9m2u2Kfv3tfxJg4/04SVqk0bN4hiL+ppxnW6l9dRPynz61iBOHP9f8cZW2FhxR8Q2YLtQwsDMQLTVqASYluqKd4gSJY58bOMDKobogb0g1o2TJyjbDzHiTdCiqU7wUGdBgJ8H3ofi8upProULVgZyxQVLf089eNMPaBmtP0I+TkkOcct0Qftw==", PASSWORD_ENCRYPT));
		String test[] = {"ProfileID", "AccessKey", "TransactionID", "TransactionDateTime", "Language", "IsTokenRequest", "Description", "TotalAmount", "DomesticFee", "InternationalFee", "SSN", "Currency", "FirstName", "LastName", "Gender", "Address", "District", "City", "PostalCode", "Country", "Email", "Mobile", "ReturnUrl", "CancelUrl", "Signature", "SubscribeWithMin"};
		//	String test[] = {"Description","TransactionID","ResponseCode","Token","CardNumber","AccountNo","ExpiryDate","SubscriptionSource","Signature","SubscriptionType","CardType"};
		//	String test[] = {"ProfileID","AccessKey","RequestID","Data"};
		String temp;
		for (int i = 0; i < test.length; i++) {
			for (int j = i + 1; j < test.length; j++) {
				if (test[i].compareTo(test[j]) > 0) {
					temp = test[i];
					test[i] = test[j];
					test[j] = temp;
				}
			}
		}
		System.out.print("Các chuỗi sau khi sắp xếp là:");
		for (int i = 0; i <= test.length - 1; i++) {
			System.out.print(test[i] + ", ");
//		}
//		String data ="qhajhtixgkhghozycgxmmseuX7md/5Oic8d/jE/whai5KEZOTLKsl6o4Y1N9X3EWt/G9tiWF+Si5o9QM9n7UooEKraoDcZgblv5gWRNwDo97PiY5C4rofFzEp6WjmZJrqFodgbc+r7Qy0XsYF6ksIeH/NganLuongnsihvd405be8d-3763-4873-9bee-6a5bc007b4e6";
//		String sign = signatureRSAWithSecretKey(data);
//		Boolean test1= verifySign(sign,data);
//		String data = "{\"TransactionID\":\"NL123490011\",\"TransactionDateTime\":\"2022-01-18T09:18:28Z\",\"IsQuery\":\"True\"}";
//		String test = Encrypt3DESS(data,"ohfqmaepsppjfghayvwszlpw");


//		String data = "qhajhtixgkhghozycgxmmseuHai LonghttpNam DinhVNVNDNl1232678989Hai Hauthannv@gmail.comNguyenFfalseVIThan0364301779NganLuongnsihvhttp91910282020false50002022-01-11T09:16:54ZNl12342678989";
//		String sign = signatureByMD5withRSA(data);
//		System.out.println(sign);
//		Boolean test = verifySign(sign,data);
//		System.out.println(verifySign(sign, data));
//		String test = "co-prof-tranx=eyJhbGciOiJSU0EtT0FFUC0yNTYiLCJlbmMiOiJBMjU2R0NNIn0.khRST0jrhhD8Jn96jjjnuwyy4nsRIpCIqNHLKWPvnzNCthhFo-CUiCSPkFUG7ivV2peUJldAYmOF39wBGNbgnYzs-KLo4RkQS5oJ8gvUDbOLjF2L1xwb0mI8JNyxlX0oZW-3WlgkR92dKo8gMqzg2Uw2zjvQCbj88-yc4MX5LFKapJHW9fSLEPvgQVBRuF5PVNn7zpcqH19QpWs7ddF2xMaYQafUgWkRTZoLDPwcunGgRUvaL9hhAvBTWf8sz6czqq5qZeH9pzTnVOEyvrYimvPM-fIFk1zY1TojwU2bf-QSBBLHf5QBntvirW9cSlyPWvji7HCfTNklhtQBtzRIQYkyu2P3r8DQeMwC5ijUAejfxlUTTqdtbcTouOghxedjKRW577Az6DFq4rHz-L2Nax-pv6zFUbqK4HHzvpXDBIpmG-C3m-MEQk8pDnoSdlE4HaPW-M6gGOK-yInWUiwFUwx3gIcDRzQ2vgTG8kF32mGdT9Tw5MTGpoeFRRDiYVdhjfXwvXFwn5zAu-zG1eh-DFF4gbfaiXIU9nFz37zdPGL-wyzkbmMw7K0EOFY3r4bed4ewkALQclcogLOzZOLg2U7WzqCfWqDOLStYLAPoiT6ez-TWNfmLXYhahjvCAinI2WQYxMifXxis8TU0C98gSwUrs7umd9Zt6vNJ8AZnLiY.7ASdIe74Zq4KdZeU.4aGxg6NWW7G45mPGvJZS7m4iKcdizG6wqZnPElAv3LQ4xiLFUlgPOm6HsDj6l0uqsK9tBrBK5h3gkWq9lwFWlx7-.OLyyYFvVJW1R5lM-Y2t_yw; expires=Sun, 16-Jan-2022 16:33:07 GMT; path=/; HttpOnly";
//		String[] result = test.split(";");
//		for (int i=0;i<result.length;i++){
//			System.out.println(result[i]);
//		}
//		String test1 = result[0];
//		String[] result1 = test1.split("=");
//		for (int i=0;i<result1.length;i++){
//			System.out.println(result1[i]);
//		}
		}
	}


	public static String md5(String input) throws NoSuchAlgorithmException {
		MessageDigest instance = MessageDigest.getInstance("MD5");
		byte[] byteData = instance.digest(input.getBytes());
		String hexString = DatatypeConverter.printHexBinary(byteData);
		return hexString;
	}

	public static String md5(String... input) throws NoSuchAlgorithmException {
		StringBuilder sb = new StringBuilder();
		for (String str : input) {
			sb.append(str);
		}
		MessageDigest instance = MessageDigest.getInstance("MD5");
		byte[] byteData = instance.digest(sb.toString().getBytes());
		String hexString = DatatypeConverter.printHexBinary(byteData);
		return hexString;
	}

	private static SecretKey getTripleDESKey(String keyString) throws InvalidKeySpecException, NoSuchAlgorithmException,
			InvalidKeyException, UnsupportedEncodingException {
		KeySpec keySpec;
		SecretKey key = null;
		// try {
		final byte[] digestOfPassword = keyString.getBytes(UNICODE_FORMAT);
		final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
		for (int j = 0, k = 16; j < 8;) {
			keyBytes[k++] = keyBytes[j++];
		}

		keySpec = new DESedeKeySpec(keyBytes);
		key = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		return key;
	}

	public static String Encrypt3DES(String value, String keyString)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		// try {
		SecretKey secretKey = getTripleDESKey(keyString);
		Cipher ecipher = Cipher.getInstance(TRIPLE_DES_TRANSFORMATION);
		ecipher.init(Cipher.ENCRYPT_MODE, secretKey);
		if (value == null)
			return null;
		// Encode the string into bytes using utf-8
		byte[] utf8 = value.getBytes(UNICODE_FORMAT);
		// // Encrypt
		byte[] enc = ecipher.doFinal(utf8);
		// // Encode bytes to base64 to get a string
		// return new String(Base64.encode(enc, Base64.DEFAULT));
		return Base64.encodeBase64String(enc);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return null;
	}
	public static String Encrypt3DESS(String value, String keyString)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		// try {
		SecretKey secretKey = getTripleDESKey(keyString);
		Cipher ecipher = Cipher.getInstance(TRIPLE_DES);

		ecipher.init(Cipher.ENCRYPT_MODE, secretKey);
		if (value == null)
			return null;
		// Encode the string into bytes using utf-8
		byte[] utf8 = value.getBytes(UNICODE_FORMAT);
		// // Encrypt
		byte[] enc = ecipher.doFinal(utf8);
		// // Encode bytes to base64 to get a string
		// return new String(Base64.encode(enc, Base64.DEFAULT));
		return Base64.encodeBase64String(enc);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return null;
	}

	public static String Decrypt3DES(String value, String keyString) {
		try {
			SecretKey secretKey = getTripleDESKey(keyString);
			Cipher dcipher = Cipher.getInstance(TRIPLE_DES_TRANSFORMATION);
			dcipher.init(Cipher.DECRYPT_MODE, secretKey);
			if (StringUtils.isEmpty(value))
				return null;
			// Decode base64 to get bytes
			byte[] dec = Base64.decodeBase64(value.getBytes("UTF-8"));// Base64.decode(value.getBytes(),
																// Base64.DEFAULT);
			// Decrypt
			byte[] utf8 = dcipher.doFinal(dec);
			// Decode using utf-8
			return new String(utf8, UNICODE_FORMAT);
		} catch (Exception e) {
//			System.out.println("Value: " + value + "\t Key: " + keyString);
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean verifySign(String signature, String data) throws InvalidKeyException, Exception {
		byte[] sign = Base64.decodeBase64(signature.getBytes(StandardCharsets.UTF_8));
//		MessageDigest instance = MessageDigest.getInstance("MD5");
//		byte[] byteData = instance.digest(data.getBytes());

		Signature rsaVerify = Signature.getInstance(PGSecurity.MD5RSA);
		rsaVerify.initVerify(getPublicSTB());
		rsaVerify.update(data.getBytes(StandardCharsets.UTF_8));
		return rsaVerify.verify(sign);
	}
	
	public static RSAPublicKey getPublicSTB() throws Exception {
		String url = FilePathUtil.getAbsolutePath(STBSecurity.class,PUB_KEY_NL);
		File file = new File(url);

		String publicKeyPem = "";
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			publicKeyPem += line;
		}
		br.close();

		publicKeyPem = publicKeyPem.replace("-----BEGIN PUBLIC KEY-----", "");
		publicKeyPem = publicKeyPem.replace("-----END PUBLIC KEY-----", "");
		byte[] publicKeyBytes = Base64.decodeBase64(publicKeyPem);

		X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return (RSAPublicKey) kf.generatePublic(spec);
	}

	public static String sign(String input) throws Exception {
		Signature signature = initSign();
		signature.update(input.getBytes(), 0, input.length());
		String result = Base64.encodeBase64String(signature.sign());
		return result;
	}

	private static Signature initSign() throws Exception {
		Signature signature = Signature.getInstance(PGSecurity.SHA1RSA);
		// URL url =
		// STBSecurity.class.getResource("/var/lib/payment_gateway_live/key/stb/private_key_STB.pem");
		//File file = new File(PRIV_KEY_STB);
		String url = FilePathUtil.getAbsolutePath(STBSecurity.class,PRIV_KEY_STB);
		File file = new File(url);
		PrivateKey privateKey = PGSecurity.pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(file);
	//	PrivateKey privKey = pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(file);
		// PrivateKey privKey = getPrivateKeySTB();
		signature.initSign(privateKey);
		return signature;
	}
	public static String signNl(String input) throws  Exception{
		Signature signature = Signature.getInstance(PGSecurity.SHA1RSA);
		String url = FilePathUtil.getAbsolutePath(STBSecurity.class,PRIV_KEY_NL);
		File file = new File(url);
		PrivateKey privateKey = PGSecurity.pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(file);
		signature.initSign(privateKey);
		signature.update(input.getBytes(), 0, input.length());
		String result = Base64.encodeBase64String(signature.sign());
		return result;

	}

	public static String signatureByMD5withRSA(String input) throws  Exception{
		Signature signature = Signature.getInstance(PGSecurity.MD5RSA);
		String url = FilePathUtil.getAbsolutePath(STBSecurity.class,PRIV_KEY_NL);
		File file = new File(url);
		PrivateKey privateKey = PGSecurity.pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(file);
		signature.initSign(privateKey);
		signature.update(input.getBytes(), 0, input.length());
		String result = Base64.encodeBase64String(signature.sign());
		return result;

	}
	public static String signatureRSAWithSecretKey(String input) throws Exception{
		Signature signature = Signature.getInstance(PGSecurity.MD5RSA);
		String path = FilePathUtil.getAbsolutePath(STBSecurity.class,PRIV_KEY_NL);
		PrivateKey privateKey = PGSecurity.pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(new File(path));
		signature.initSign(privateKey);
		signature.update(input.getBytes(), 0, input.length());
		String result = Base64.encodeBase64String(signature.sign());
		return result;



	}

	@SuppressWarnings({ "restriction" })
	private static PrivateKey pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(File pemFileName)
			throws GeneralSecurityException, IOException {
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
