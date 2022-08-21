package gateway.core.channel.vnpost;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

public class VnPostSecurity {

	public static String ENCRYPT_KEY_DEFAULT = "NEXTTECH11Hlx36rcZGIQE20042018";

	static String orignalText = "NGUYEN TUAN VINH 243243";
	static String textEncrypt = "ROfrLbpwkZuom2ApEjMYOY9M5ICGZYWdLvm1kKZUocBMvui3PLa5gmFfMaNRun+SG9rA5Ef478o/+aVAMIFWUHzcOeVsd1guVFVaspxvS5fCmVz7V+DCgzY4eNbgBHwK3ZS4HSRKt1j4cA5a41mmk/6G8opZS3hqbdX8gCnktrNCVJUSMapgWiB8mXm1Ca/tlwthkWNoZpIXcZg/Ql8Ji81UdoR1iJ2JKbDg+u4FsgUDyaP6rTqDAmVvpUIczJls";

	public static void main(String[] args) throws Exception {
		// openssl_encrypt();
		// ENCRYPT_KEY_DEFAULT = "NL@!#@$#%#^^#@^";
		// textEncrypt =
		// "AvNV98XBHI5/5PxCOAA2X0f40ls7wWbcBOSMqbtePkd7zOfPR35IaQRQgdYBBlZ9kAvLRfV8QFrgxvGJEdQx3xIrTZ2mRvvCnLcasFZN4PZYBlA8mM53kvUuz7gqrspIoP1TKQOdK6gSy5pbaox6p99p3fijryUyELaLfbnmavijH7U4EPagKcFsnbJS9i4TVMa9ivPogyhlIROfcrQVjmd1SassE8sQZIV08Tv3IipkPFGOuW1iNiEFByJUVQQCJPIKthi1dJ8X2EE0U6r92I9aRuOFO+pzku6LrRO5pUU=";

		// textEncrypt =
		// "vWBjC7x7JOg4W4r068UdkkTCdHtZdJL3iH7ROo4kJKS4R2ZnLgKl+iez6+33t6hsB6H0FGjA7xUQ8JhWNxQkLp5IiWp793GvF92ZVF6CJxS2fZYuvuVYrjbBbFRZRHWuYxhR3OIIkbCJGjVRzh9JTX8nKFIYfinMaqzb2yuiF/WSb9KLaajS7V0egMOQg4w95zozzEHhWjKGfm8tbaCDz9SAVP1vZLA1wBrxoc80n1lBriZz8ia96yWnmRUgIfNKihfRVv8ApPap82QXKZVt4ymBApDBt9XBI7u8BJJeYIiQJ5xZD4wd+oEdDC/PclzT";

		textEncrypt = openssl_encrypt(orignalText, ENCRYPT_KEY_DEFAULT);

		String descryptText = openssl_decrypt(textEncrypt, ENCRYPT_KEY_DEFAULT);
//		System.out.println(descryptText);
	}

	public static String openssl_encrypt(String orignalText, String encryptKey) throws Exception {
		final MessageDigest md = MessageDigest.getInstance("SHA-256");
		final byte[] digestOfPassword = md
				.digest(StringUtils.defaultIfBlank(encryptKey, ENCRYPT_KEY_DEFAULT).getBytes("utf-8"));
		final SecretKey key = new SecretKeySpec(digestOfPassword, "AES");
		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		final IvParameterSpec iv = new IvParameterSpec(new byte[16]);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		final byte[] plainTextBytes = orignalText.getBytes("utf-8");
		final byte[] encodeTextBytes = cipher.doFinal(plainTextBytes);

		textEncrypt = new Base64().encodeToString(encodeTextBytes);
		return textEncrypt;
	}

	public static String openssl_decrypt(String request, String encryptKey) throws Exception {
		final MessageDigest md = MessageDigest.getInstance("SHA-256");
		final byte[] digestOfPassword = md
				.digest(StringUtils.defaultIfBlank(encryptKey, ENCRYPT_KEY_DEFAULT).getBytes("utf-8"));
		final SecretKey key = new SecretKeySpec(digestOfPassword, "AES");
		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		final IvParameterSpec iv = new IvParameterSpec(new byte[16]);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);

		String encodedText = new String(cipher.doFinal(Base64.decodeBase64(request.getBytes("UTF-8"))));
		return encodedText;
	}
}
