package gateway.core.channel.vcb_ecom_atm;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.channel.vcb_ecom_atm.dto.VCBEcomAtmConstants;
import gateway.core.util.PGSecurity;
import gateway.core.util.PasswordDeriveBytes;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;
import vn.nganluong.naba.entities.PaymentAccount;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class VCBEcomAtmSecurity {
	private static final Logger logger = LogManager.getLogger(VCBEcomAtmSecurity.class);

	static String salt = "vietcombank@)!^";
	static String passPhase = "MBVCB@)!^";

	static String PATH_PUB_KEY_GW = "/var/lib/payment_gateway_test/key/vcb_ecom/public_key.xml";
	static String PATH_PRIV_KEY_GW = "/var/lib/payment_gateway_test/key/vcb_ecom/private_key.xml";
	static String PATH_PUB_KEY_VCB = "/var/lib/payment_gateway_test/key/vcb_ecom/public_key_VCB.xml";

	// Private, Public Key Vimo
	// static String privateKey =
	// "<RSAKeyValue><Modulus>kkQVNo45BE1cb9b58MJtRxQefsEPk2cW9R9rPBpdiZeDPcC45Q/F6bjRcofbg6onilHbNw80LuYi&#13;40X698tGvIv9eU2GxTLCx/msgX0h/YRnaXeP2BeLwgKLmfcSF6TiBbNu5ChzJGTzVVjcT22tcSsQ&#13;udJJZ4erSh18U/Se9Ks=</Modulus><Exponent>ZmN8ig0zQpy5CZ1up6gjAS/HTSnuslvnCT6uPYujUO8cMz1W8RqCi+MR01/hPaD+PuigUUG2YOI/&#13;J+PUFGRbJg46apfRPJCD0THL1/qPLozKyPVcFBWnTv04uiCCaM85+m4jfi7EOiRn2TQPsvMT8afD&#13;ns+8fRVvI7ZSdhmyzQE=</Exponent></RSAKeyValue>";
	// static String publicKey =
	// "<RSAKeyValue><Modulus>kkQVNo45BE1cb9b58MJtRxQefsEPk2cW9R9rPBpdiZeDPcC45Q/F6bjRcofbg6onilHbNw80LuYi&#13;40X698tGvIv9eU2GxTLCx/msgX0h/YRnaXeP2BeLwgKLmfcSF6TiBbNu5ChzJGTzVVjcT22tcSsQ&#13;udJJZ4erSh18U/Se9Ks=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
	// static String publicKeyVCB =
	// "<RSAKeyValue><Modulus>5TXaYqXrybyGWUcLm4Ifaf5E+79wJqijpV2NVZsktJJzmBZlJ/twtPi/LKvbtn7b/U54/wJCzaU7asXlaKKG5oBVzZA13yKZyxMrdd/0iVmuiJMIZgv4HZOh/g2hS8EI/TzLMII0bgN3sJNAckvZZdIhucfLhwFBXF6jKRZgDI7u3YXMoRBLUQs+87XISMjL1Azk1xXZhXx+gwKYGvH5SMQhuqwDmnoBe8xQ+I6orHiTgcoYb6Qe+iUu4P/Km+OR4USi/bUvGOcOSDOEiFr1OQ6AdoPdv0JscfuK2QKmgsf82X3bh5s/Cf09dyskf9SkS0DiA87bi3uZWX10rihBUw==</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";

	public static void initParam(PaymentAccount acc) {
		passPhase = acc.getEncryptKey();
		PATH_PRIV_KEY_GW = acc.getPrivateKeyPath();
		PATH_PUB_KEY_GW = acc.getPublicKeyPath();
		PATH_PUB_KEY_VCB = acc.getPartnerKeyPath();
	}

	public static String encryptAES(String plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {
		final PasswordDeriveBytes pass = new PasswordDeriveBytes(passPhase, salt.getBytes("UTF-8"));
		byte[] key = pass.getBytes(256 / Byte.SIZE);
		SecretKeySpec specKey = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, specKey, new IvParameterSpec(new byte[16]));
		byte[] ciphertext = cipher.doFinal(plaintext.getBytes());
		return Base64.encodeBase64String(ciphertext);
	}

	/**
	 * public static String decryptAES(String encrypted) throws
	 * NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
	 * InvalidAlgorithmParameterException, UnsupportedEncodingException,
	 * IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
	 * final PasswordDeriveBytes pass = new PasswordDeriveBytes(passPhase,
	 * salt.getBytes("UTF-8")); byte[] key = pass.getBytes(256 / Byte.SIZE);
	 * IvParameterSpec iv = new IvParameterSpec(pass.getBytes(128 / Byte.SIZE));
	 * SecretKeySpec secretKey = new SecretKeySpec(key, "AES"); Cipher cipher =
	 * Cipher.getInstance("AES/CBC/NoPadding"); // PKCS5Padding
	 * cipher.init(Cipher.DECRYPT_MODE, secretKey, iv); return new
	 * String(cipher.doFinal(Base64.decodeBase64(encrypted.getBytes("UTF-8"))),
	 * "UTF-8"); }
	 */

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

	/**
	 * Sign Data Encrypted
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static String sign(String input) throws Exception {
		Signature signature = initSign();
		signature.update(input.getBytes());
		String result = Base64.encodeBase64String(signature.sign());
		return result;
	}

	/**
	 * Verify Data Encrypted With Signature
	 * 
	 * @param data
	 * @param sig
	 * @return
	 * @throws Exception
	 */
	public static boolean verifyVCB(String data, String sig) throws Exception {
		Signature signature = initVerifyVCB();
		signature.update(data.getBytes());
		return signature.verify(Base64.decodeBase64(sig));
	}

	public static boolean verifyGateway(String data, String sig) throws Exception {
		Signature signature = initVerifyGateway();
		signature.update(data.getBytes());
		return signature.verify(Base64.decodeBase64(sig));
	}

	/**
	 * <b>GET PRIVATE KEY VIMO</b>
	 */
	private static RSAPrivateKeySpec getPrivKeyVimo() throws Exception {
		//File file = new File(PATH_PRIV_KEY_GW);

		ClassLoader classLoader = new VCBEcomAtmSecurity().getClass().getClassLoader();
		File file = new File(classLoader.getResource(VCBEcomAtmConstants.GW_PATH_PRIVATE_KEY).getFile());
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
	 * <b>GET PUBLIC KEY VIMO</b>
	 */
	private static RSAPublicKeySpec getPubKeyVimo() throws Exception {
		// URL url = Security.class.getResource(PATH_PUB_KEY_VIMO);
		// File file = new File(url.toURI());

		//File file = new File(PATH_PUB_KEY_GW);
		ClassLoader classLoader = new VCBEcomAtmSecurity().getClass().getClassLoader();
		File file = new File(classLoader.getResource(VCBEcomAtmConstants.GW_PATH_PUBLIC_KEY).getFile());

		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			logger.error("============================ PUBLIC KEY FILE NOT FOUND ============================");
		}

		SAXBuilder sb = new SAXBuilder();
		// Document doc = sb.build(new InputSource(new
		// StringReader(getPublicKey())));
		// Document doc = sb.build(new InputSource(fileReader == null ? new
		// StringReader(publicKey) : fileReader));
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

	/**
	 * <b>GET PUBLIC KEY VCB</b>
	 */
	private static RSAPublicKeySpec getPubKeyVCB() throws Exception {
		// URL url = Security.class.getResource(PATH_PUB_KEY_VCB);
		// File file = new File(url.toURI());

		//File file = new File(PATH_PUB_KEY_VCB);
		ClassLoader classLoader = new VCBEcomAtmSecurity().getClass().getClassLoader();
		File file = new File(classLoader.getResource(VCBEcomAtmConstants.VCB_ECOM_PATH_PUBLIC_KEY).getFile());

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

	private static Signature initSign() throws Exception {
		KeyFactory factory = KeyFactory.getInstance("RSA");
		Signature signature = Signature.getInstance(PGSecurity.SHA1RSA);
		signature.initSign(factory.generatePrivate(getPrivKeyVimo()));
		return signature;
	}

	private static Signature initVerifyVCB() throws Exception {
		KeyFactory factory = KeyFactory.getInstance("RSA");
		Signature signature = Signature.getInstance(PGSecurity.SHA1RSA);
		signature.initVerify(factory.generatePublic(getPubKeyVCB()));
		return signature;
	}

	private static Signature initVerifyGateway() throws Exception {
		KeyFactory factory = KeyFactory.getInstance("RSA");
		Signature signature = Signature.getInstance(PGSecurity.SHA1RSA);
		signature.initVerify(factory.generatePublic(getPubKeyVimo()));
		return signature;
	}

	public static void main(String args[]) throws Exception {
		// String data =
		// "NLVNVM9432681950WZYMcbyu+8mygMBnYB5fWKFGpijtIv/Arq/LtuJRJyG1j85Omnlpv3XdSlo3sLOR7URmMmzZ4Ao100kEMW3cdr4y4os3oVzoneToxoqTc3VEspYxNSMn/SQ/PXomey0g+dbjSKUvu7IxJOyQLJywYfUIKVtXwGjVnGHhEl7b5SYEiG50/VFEue4M96wPsO1EcVf0fUj31iMZwLF6+1rNworNY929DqAwNx095OBYOKLVBqBZ8BsPPeOu6Rk0Y3m2pCPdhRpY+p2jHI8Si3xE6A3JWnpA6wDSD4uWUH10Eti42zSEAFh4NuThPTWVl55o1WWOsHQeIPmfk2woSPKOr57sfF929d+UxzGxQuQe4gakG9hlZMZ0Ko6B2kLTbFwM68mZMFFTerK64a93pUwTyWFS6mBbel3TLgp6mNtj6cQ=";
		// String signature = sign(data);
		// System.out.println(signature);
		// System.out.println(verifyVimo(data, signature));
		//
//		 System.out.println(decryptAES2("O/qZmNPkFue9VK4eAkAe99QYbovEdgfo+kFyyu/7LZ9iAmMSlN2VUbG/4nWMp9LE08LaBCKZK4vz8efN3s5yEQ=="));

		// SecretKeyFactory factory =
		// SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		// PBEKeySpec pbeKeySpec = new PBEKeySpec(
		// "TmAEdiQ1A68f34OtbMtjUSL7JDS/6b0r9s63fThV7TA=".toCharArray(),
		// salt.getBytes(), 1000,
		// 256 + 128);
		// Key secretKey = factory.generateSecret(pbeKeySpec);
		// byte[] key = new byte[256/8];
		// byte[] iv = new byte[128/8];
		// System.arraycopy(secretKey.getEncoded(), 0, key, 0, 256/8);
		// System.arraycopy(secretKey.getEncoded(), 256/8, iv, 0, 128/8);
		//
		// System.out.println("Key: " + new String(key));
		// System.out.println("IV: " + new String(iv));

		ObjectMapper objectMapper = new ObjectMapper();
		String vcbRes = "xHinWQNMCftcoEQQY4wSFqB+kwc2+dt0aSBW12JoCZfkBCTKTx8vOhD0B8++tNsxG9ksYNMSmWPRMdpkQe9Tk3eO8dREILMkzTw/4u5M4DwGwmVbI0SBEljvZ7yuFDMVf2tIEC7iGpETb4FpSsIcyg==";
		String vfff = VCBEcomAtmSecurity.decryptAES2(vcbRes);
//		System.out.println(vfff);
//		VerifyPaymentRes res = objectMapper.readValue(vfff, VerifyPaymentRes.class);
//		System.out.println(objectMapper.writeValueAsString(res));
	}

}
