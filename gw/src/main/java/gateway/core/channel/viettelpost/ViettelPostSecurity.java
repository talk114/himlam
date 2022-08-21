package gateway.core.channel.viettelpost;

import gateway.core.channel.viettelpost.dto.VTTPostConstants;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ViettelPostSecurity {

	public static String ENCRYPT_KEY_DEFAULT = "PeaceSoftViettelPost@a8747f49d1c72a79";

	static String orignalText = "{\"func\":\"inquery\",\"access_code\":\"VIMO\",\"service_code\":\"VMPAY\",\"payment_id\":\"abcd1234\",\"request_id\":\"1111\",\"date_time\":\"10:45:05 10/08/2018\",\"checksum\":\"eadc48d61b15580db2b2c9e59384ce15\"}";
	static String textEncrypt = "3Pj4nGFjfoKIjpV48fwmXgP7XQaKra8plkBkSglhz+u68s20ah7Y9S+eF25L3CX14wifJ7Q/eXRzBbHfVeXc84LH9AwMh3QAsl1ZciK7G7ywgPr+KhxeLbUxIJNACfEr8A+sFI+MdMmUnjV6FkKxl1KN5kSPZZ3Xlz5qA4z1q8puQeCe1o+ocoX5EYN4Oy7XjAwzCucf1MjemkxhI+0P+lUKxwRyGqnCLqyH3O7h3c6X/lv06en4G70LGOZocrNfQKyPU8pfrbZHUbd08CGcxc3yL0V17gondYR+L79AM/4=";

	public static void main(String[] args) throws Exception {
		// openssl_encrypt();
		// textEncrypt = encryptAES(orignalText, ENCRYPT_KEY_DEFAULT);
		// System.out.println(textEncrypt);
		String descryptText = decryptAES(
				"0eDrr9+KE7ul1ntGmYN2eowmZtJVZubD/ycc6z5MJFah9xJ8UxJKv+rqNaifw1qMNLR8tRvWrYDkcP2UuUwC1gQd1LdpLPGu68uocxFV4+eb4vF6e92GiABkL3ASfhrOeD8ZXsxxYgZnyHw3aC9Uggcp1CLzu/qx8ln3YmbXB/WDy1zAHIhHjqReRP61DOfNBmTxUzQxA2VvpPfErufGVtfbqBeUZvJF/3zYxXEV7WaTk3cJGZO0GoOFc/G0ALZgfhHIuBlXvdg/B6rLUrh47g==", VTTPostConstants.ENCRYPT_KEY);
		// {"func":"inquery","access_code":"PS","service_code":"PSPAY","payment_id":"PS2-5016963","request_id":"VTT-TRANS-ID","date_time":"16:24:00 02/10/2019","checksum":"75c8945ca5cf334e281af40b4d30c55d"}

//		System.out.println(descryptText);
	}

	public static String encryptAES(String orignalText, String encryptKey) throws Exception {
		final MessageDigest md = MessageDigest.getInstance("SHA-256");
		final byte[] digestOfPassword = md
				.digest(StringUtils.defaultIfBlank(encryptKey, VTTPostConstants.ENCRYPT_KEY).getBytes("utf-8"));

		final SecretKey key = new SecretKeySpec(digestOfPassword, "AES");
		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		final IvParameterSpec iv = new IvParameterSpec(new byte[16]);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		final byte[] plainTextBytes = orignalText.getBytes("utf-8");
		final byte[] encodeTextBytes = cipher.doFinal(plainTextBytes);

		textEncrypt = new Base64().encodeToString(encodeTextBytes);
		return textEncrypt;
	}

	public static String decryptAES(String request, String encryptKey)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		final MessageDigest md = MessageDigest.getInstance("SHA-256");
		final byte[] digestOfPassword = md
				.digest(StringUtils.defaultIfBlank(encryptKey, VTTPostConstants.ENCRYPT_KEY).getBytes("utf-8"));
		final SecretKey key = new SecretKeySpec(digestOfPassword, "AES");
		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		final IvParameterSpec iv = new IvParameterSpec(new byte[16]);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);

		String encodedText = new String(cipher.doFinal(Base64.decodeBase64(request.getBytes("UTF-8"))));
		return encodedText;
	}
}
