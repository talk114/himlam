package gateway.core.channel.viettelpay;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class ViettelPaySecurity {

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	public static void main(String args[]) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
//		System.out.println(hmacSha1("NGUYEN TUAN VINH", "aaa"));
	}

	public static String hmacSha1(String data, String key)
			throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		byte[] rawHmac = mac.doFinal(data.getBytes());
		// base64-encode the hmac
		String res =  Base64.encodeBase64String(rawHmac);
		return res;
	}
}
