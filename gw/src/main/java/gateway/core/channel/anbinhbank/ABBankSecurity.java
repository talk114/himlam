package gateway.core.channel.anbinhbank;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.*;
import java.security.cert.CertificateFactory;

public class ABBankSecurity {

	private static final Logger logger = LogManager.getLogger(ABBankSecurity.class);


	static String GW_PRIVATE_KEY = "/var/lib/payment_gateway_test/key/an_binh_bank/private_key_vimo.p12";
	static String ABB_PUBLIC_KEY = "/var/lib/payment_gateway_test/key/an_binh_bank/public_key_abb.p12";

	public static String signP12(String privateKeyFilePath, String password, String data) {
		logger.info("Signature Data: " + data + "\t Password: " + password);
		String result = "";
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			ClassLoader classLoader = new ABBankSecurity().getClass().getClassLoader();
			File file = new File(classLoader.getResource(privateKeyFilePath).getFile());

			ks.load(new FileInputStream(file), password.toCharArray());
			logger.info("alias: " + ks.aliases().nextElement());

			PrivateKey pK = (PrivateKey) ks.getKey(ks.aliases().nextElement(), password.toCharArray());

			Signature sign = Signature.getInstance("SHA256withRSA");
			result = ks.aliases().nextElement();
			sign.initSign(pK);
			sign.update(data.getBytes());
			byte[] signatureBytes = sign.sign();
			result = Base64.encodeBase64String(signatureBytes);

		} catch (FileNotFoundException e) {
			logger.error(e.toString());
			result = "null -> " + e.toString();
		} catch (NoSuchAlgorithmException nsa) {
			logger.error((new StringBuilder()).append(nsa.getMessage()).toString());
			result = "No Algorithm";
		} catch (Exception e) {
			logger.error(e.toString());
			return result;
		}
		return result;
	}

	public static boolean verifyP12(String signature, String data, String pubicKeyFilePath) {
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			ClassLoader classLoader = new ABBankSecurity().getClass().getClassLoader();
//			File pKeyFile = new File(classLoader.getResource("abb-key/public_key_abb.p12").getFile());
			FileInputStream fis = new FileInputStream(classLoader.getResource(pubicKeyFilePath).getFile());
//			FileInputStream fis = new FileInputStream(pubicKeyFilePath);
			java.security.cert.Certificate cert = cf.generateCertificate(fis);
			PublicKey publicKey = cert.getPublicKey();
			byte signatureBytes[] = Base64.decodeBase64(signature);

			Signature sign = Signature.getInstance("SHA256withRSA");
			sign.initVerify(publicKey);
			sign.update(data.getBytes());
			return sign.verify(signatureBytes);

		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		}
	}
	
	public static void main(String args[]){
		String rawData = "|20190504100927904|20190504100927||";
		String signature = "LzeboOXZM4LjYJ2GAD+Ua7WClRuYnpLA+u21bHQHt23jO5V5Kwj7njkh3QUg1a48YCjwfHnEwG4Ur3qFbemnAnLSFfYyQTDJwFcsD7si9RZDAeMIr8tSdNxeOmJkMud+SGYa8vWMKYBNctgbmHf22wXxF/N6usrAX8cdSDGOwHm26aenvgDWDFrGFRJwpaXEcdesQHiesczMwcCZxLhJKuWoQiCh+Qb0aoS71FzrmjGx/0ro+GK4D+AZ+75V+G9xbyE+J5+7vB7ggHEDMKZWJNtqF5cecmVeUgTtCAvc7BXOq4HCXt6Dq27JAcF0f5+SmvAg7aJUkcjMbkFn/7tMaQ==";
//		System.out.println(verifyP12(signature, rawData, ABB_PUBLIC_KEY));
	}
}
