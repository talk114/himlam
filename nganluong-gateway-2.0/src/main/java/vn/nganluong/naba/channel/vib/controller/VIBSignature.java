package vn.nganluong.naba.channel.vib.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import vn.nganluong.naba.channel.vib.dto.RqAccountBalanceObject;
import vn.nganluong.naba.channel.vib.dto.RqERPLinkGetListTransObject;
import vn.nganluong.naba.channel.vib.dto.RqInputTransactionExtObject;
import vn.nganluong.naba.channel.vib.dto.RqTransactionHistoryInqObject;
import vn.nganluong.naba.channel.vib.dto.TransactionExtObj;

public class VIBSignature {

	static String PATH_PRIV_KEY_VIB_NL_1024 = "vib-key/private_vib_1024.pem";
	static String PATH_PRIV_KEY_VIB_NL_4096 = "vib-key/private_vib_4096.pem";
	@Autowired
    ResourceLoader resourceLoader;
	
	public static void main(String[] args) {

		/*
		try {
			// cif|cardno|fromacctno|reqid
			String data= "00016506|6666688888|002704060002045|970403|1234";
			System.out.println(signSHA512WithRSA(data));
			System.out.println("---Done signSHA512WithRSA---");
			System.out.println(signSHA1withRSA(data));
			System.out.println("---Done signSHA1withRSA---");
			
			
		} catch (IOException e) {
			
			logger.info(ExceptionUtils.getStackTrace(e));
		} catch (GeneralSecurityException e) {
			
			logger.info(ExceptionUtils.getStackTrace(e));
		} catch (URISyntaxException e) {
			
			logger.info(ExceptionUtils.getStackTrace(e));
		}
		
		*/
		
		
		// Kiem tra tai khoan
//		RqAccountBalanceObject rqObject = new RqAccountBalanceObject();
//		rqObject.CIF = "00016506";
//		rqObject.accountNumber = "002704060002045";
//		rqObject.username = "NLnhap01";
//		
//		
//		
//		
//		
//		String dataToSign = SHA1AccountBalance(rqObject);
//		String sig  = genSignature(dataToSign);
//		System.out.println(sig);
		
		
		// Khoi tao giao dich
//		RqInputTransactionExtObject rqObject = new RqInputTransactionExtObject();
//		rqObject.Amount = "1000000.00";
//		rqObject.Ccy = "VND";
//		rqObject.Chanel_type = "ERP";
//		rqObject.Client_no = "00016506";
//		rqObject.Narrative = "";
//		rqObject.ServiceID = "VIBA";
//		rqObject.Trans_type = "SINGLE";
//		rqObject.User_id = "NLnhap01";
//		List<TransactionExtObj> transactionExtObjs = new ArrayList<TransactionExtObj>();
//		TransactionExtObj tranObj = new  TransactionExtObj();
//		
//		
//		tranObj.amount = "1000000.00";
//		tranObj.ccy = "VND";
//		tranObj.ben_bankid = "";
//		tranObj.toacct = "001704060026697";
//		transactionExtObjs.add(tranObj);
//		rqObject.TransList = transactionExtObjs;
//
//		String dataToSign = SHA1AddTransaction(rqObject);
//		String sig  = genSignature(dataToSign);
//		System.out.println(sig);
		
		
		// Kiem tra trang thai giao dich
//		RqERPLinkGetListTransObject rqObject = new RqERPLinkGetListTransObject();
//		rqObject.i_client_no = "00016506";
//		rqObject.i_from_date = "03/11/2020";
//		rqObject.i_page_num = "1";
//		rqObject.i_page_size = "10";
//		rqObject.i_service_type = "VIBA";
//		rqObject.i_to_date = "04/11/2020";
//		rqObject.i_trans_id = "000001";
//		rqObject.i_trans_type = "ERPTRANSID";
//		rqObject.i_userid = "NLnhap01";
//		
		
//		RqERPLinkGetListTransObject rqObject = new RqERPLinkGetListTransObject();
//		rqObject.i_client_no = "00016506";
//		rqObject.i_from_date = "29/10/2020";
//		rqObject.i_page_num = "1";
//		rqObject.i_page_size = "10";
//		rqObject.i_service_type = "VIBA";
//		rqObject.i_to_date = "30/10/2020";
//		rqObject.i_trans_id = "";
//		rqObject.i_trans_type = "";
//		rqObject.i_userid = "NLnhap01";
//		String dataToSign = SHA1TransactionStatus(rqObject);
//		String sig  = genSignature(dataToSign);
//		System.out.println(sig);
		
		
		
		// Kiem tra lich su giao dich
		RqTransactionHistoryInqObject rqObject = new RqTransactionHistoryInqObject();
		
		rqObject.client_no = "00016506";
		rqObject.client_id = "NLnhap01";
		rqObject.i_account_no = "002704060002045";
		rqObject.i_cr_dr = "D"; // Dr, Cr
		rqObject.RequestId = "";
		
		rqObject.i_from_date = "15/11/2020";
		rqObject.i_page_num = "1";
		rqObject.i_page_size = "50";
		rqObject.i_to_date = "28/11/2020";
		
		String dataToSign = SHA1TransactionHistory(rqObject);
		String sig  = genSignature(dataToSign);
		System.out.println(sig);
		
		
	}
	
	public static String signSHA512WithRSA(String message) throws IOException, GeneralSecurityException, URISyntaxException {
        Signature sign = Signature.getInstance("SHA512WithRSA");
        sign.initSign(getPrivateKeyNLVIBForSHA512());
        sign.update(message.getBytes(StandardCharsets.UTF_8));

        return new String(Base64.encodeBase64(sign.sign()), StandardCharsets.UTF_8);
    }
	
	public static String signSHA1withRSA(String message) throws IOException, GeneralSecurityException, URISyntaxException {
        Signature sign = Signature.getInstance("SHA1withRSA");
        sign.initSign(getPrivateKeyNLVIBForSHA1());
        sign.update(message.getBytes(StandardCharsets.UTF_8));

        return new String(Base64.encodeBase64(sign.sign()), StandardCharsets.UTF_8);
    }
	
	
	private static RSAPrivateKey getPrivateKeyNLVIBForSHA512() throws IOException, GeneralSecurityException, URISyntaxException {
//        ClassLoader classLoader = GenKey.class.getClassLoader();
//        classLoader.getResourceAsStream(PATH_PRIV_KEY_NL);
//        System.out.println(classLoader.getResourceAsStream(PATH_PRIV_KEY_NL));
//        File file = new File(Objects.requireNonNull(classLoader.getResource(PATH_PRIV_KEY_NL)).toURI());
//		File file = new File(PATH_PRIV_KEY_NL);
//		Resource resource = resourceLoader.getResource("classpath:" + PATH_PRIV_KEY_NL);
		
		Resource resource = new ClassPathResource(PATH_PRIV_KEY_VIB_NL_4096);

//		InputStream input = resource.getInputStream();

		File file = resource.getFile();
		
		
        String privateKeyPEM = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            privateKeyPEM += line + "\n";
        }
        br.close();
        return getPrivateKeyFromVIB(privateKeyPEM);
    }
	
	private static RSAPrivateKey getPrivateKeyNLVIBForSHA1()
			throws IOException, GeneralSecurityException, URISyntaxException {

		Resource resource = new ClassPathResource(PATH_PRIV_KEY_VIB_NL_1024);

		File file = resource.getFile();

		String privateKeyPEM = "";
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			privateKeyPEM += line + "\n";
		}
		br.close();
		return getPrivateKeyFromVIB(privateKeyPEM);
	}
	
//	private static RSAPrivateKey getPrivateKeyFromString(String key) throws IOException, GeneralSecurityException {
//        String privateKeyPEM = key;
//        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----\n", "");
//        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
//        byte[] encoded = Base64.decodeBase64(privateKeyPEM);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
//        RSAPrivateKey privKey = (RSAPrivateKey) kf.generatePrivate(keySpec);
//        return privKey;
//    }
	
	private static RSAPrivateKey getPrivateKeyFromVIB(String key) throws IOException, GeneralSecurityException {
		
		 String privateKeyPEM = key;
	        privateKeyPEM = privateKeyPEM.replace("-----BEGIN RSA PRIVATE KEY-----\n", "");
	        privateKeyPEM = privateKeyPEM.replace("-----END RSA PRIVATE KEY-----\n", "");
	        byte[] encoded = Base64.decodeBase64(privateKeyPEM);
	        /* Add PKCS#8 formatting */
	        ASN1EncodableVector v = new ASN1EncodableVector();
	        v.add(new ASN1Integer(0));
	        ASN1EncodableVector v2 = new ASN1EncodableVector();
	        v2.add(new ASN1ObjectIdentifier(PKCSObjectIdentifiers.rsaEncryption.getId()));
	        v2.add(DERNull.INSTANCE);
	        v.add(new DERSequence(v2));
	        v.add(new DEROctetString(encoded));
	        ASN1Sequence seq = new DERSequence(v);
	        byte[] privKeyStr = seq.getEncoded("DER");


	        KeyFactory kf = KeyFactory.getInstance("RSA");
	        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privKeyStr);
	        RSAPrivateKey privKey = (RSAPrivateKey) kf.generatePrivate(keySpec);
	        return privKey;
    }
	
	/*
	 * Sign
	 */
	public static String genSignature(String data) {
		try {
			 Signature sign = Signature.getInstance("SHA512WithRSA");
		        sign.initSign(getPrivateKeyNLVIBForSHA512());
		        sign.update(data.getBytes(StandardCharsets.UTF_8));
		        
		        return new String(Base64.encodeBase64(sign.sign()));
		} catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
	}
	
	
	/*
	 * Sign
	 */
	@SuppressWarnings("deprecation")
	public static String SHA1AddTransaction(RqInputTransactionExtObject rqObject) {
		String SHA1String = "";
		String tranlistStr = "";
		if (rqObject.TransList.size() > 0) {
			for (TransactionExtObj tranObj : rqObject.TransList) {
				tranlistStr += StringUtils.trimToEmpty(tranObj.amount)
						+ StringUtils.trimToEmpty(tranObj.ccy)
						+ StringUtils.trimToEmpty(tranObj.ben_bankid)
						+ StringUtils.trimToEmpty(tranObj.toacct);
						
			}
		}
		
		String rqStr = StringUtils.trimToEmpty(rqObject.Amount)
				+  StringUtils.trimToEmpty(rqObject.Ccy)
				+  StringUtils.trimToEmpty(rqObject.Channel_type)
				+  StringUtils.trimToEmpty(rqObject.Client_no)
				+  StringUtils.trimToEmpty(rqObject.Narrative)
				+  StringUtils.trimToEmpty(rqObject.ServiceID) + tranlistStr
				+ StringUtils.trimToEmpty(rqObject.Trans_type)
				+ StringUtils.trimToEmpty(rqObject.User_id);
		SHA1String = DigestUtils.shaHex(rqStr);
				 
		return SHA1String;
					
	}
	
	/*
	 * DataToSignGetAccBalanceWS
	 */
	@SuppressWarnings("deprecation")
	public static String SHA1AccountBalance(RqAccountBalanceObject rqObject) {
		String SHA1String = "";
		
		
		String rqStr = StringUtils.trimToEmpty(rqObject.CIF)
				+  StringUtils.trimToEmpty(rqObject.accountNumber)
				+  StringUtils.trimToEmpty(rqObject.username);
		SHA1String = DigestUtils.shaHex(rqStr);
				 
		return SHA1String;
					
	}
	
	@SuppressWarnings("deprecation")
	public static String SHA1TransactionStatus(RqERPLinkGetListTransObject rqObject) {
		String SHA1String = "";
		
		
		String rqStr = StringUtils.trimToEmpty(rqObject.i_client_no)
				+  StringUtils.trimToEmpty(rqObject.i_from_date)
				+  StringUtils.trimToEmpty(rqObject.i_page_num)
				+  StringUtils.trimToEmpty(rqObject.i_page_size)
				+  StringUtils.trimToEmpty(rqObject.i_service_type)
				+  StringUtils.trimToEmpty(rqObject.i_to_date)
				+  StringUtils.trimToEmpty(rqObject.i_trans_id)
				+  StringUtils.trimToEmpty(rqObject.i_trans_type)
				+  StringUtils.trimToEmpty(rqObject.i_userid);
		SHA1String = DigestUtils.shaHex(rqStr);
				 
		return SHA1String;
					
	}
	
	@SuppressWarnings("deprecation")
	public static String SHA1TransactionHistory(RqTransactionHistoryInqObject rqObject) {
		String SHA1String = "";
		
		
		String rqStr = StringUtils.trimToEmpty(rqObject.client_id)
				+  StringUtils.trimToEmpty(rqObject.client_no)
				+  StringUtils.trimToEmpty(rqObject.i_account_no)
				+  StringUtils.trimToEmpty(rqObject.i_cr_dr)
				+  StringUtils.trimToEmpty(rqObject.i_from_date)
				+  StringUtils.trimToEmpty(rqObject.i_page_num)
				+  StringUtils.trimToEmpty(rqObject.i_page_size)
				+  StringUtils.trimToEmpty(rqObject.i_to_date)
				+  StringUtils.trimToEmpty(rqObject.RequestId);
		SHA1String = DigestUtils.shaHex(rqStr);
				 
		return SHA1String;
					
	}
}
