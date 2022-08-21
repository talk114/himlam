package gateway.core.channel.tcb.dto;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Iterator;
import java.util.Objects;
import org.apache.commons.codec.binary.Base64;

public class JavaSignSHA256_V2_1 {
    private static final Logger logger = LogManager.getLogger(JavaSignSHA256_V2_1.class);

    public static void main(String[] arg) {
        try {

            String publicPath = "D:\\TaiLieuTichHop\\TCB\\public_key01.pem";
            String privatePath = "D:\\TaiLieuTichHop\\TCB\\private_key01.pem";
//            String pathData = "D:\\TCB\\java_workspace\\JavaSecurity\\encrypt-by-partner.xml";

            //Sign
            //String sSource = getDataAcctInfo(pathData);
            String sSource = "FundTransferNL12238237829829129837291VU DUY CHINH2021-03-18500000";
            String signStr = signData_SHA256(sSource, privatePath);
            System.out.println(">>>>>>>>>>Sign: " + signStr);

            //Verify
            //String sSource = getDataInquiryOversea(pathData);
            String sSource1 = "FundTransferNL12238237829829129837291VU DUY CHINH2021-03-18500000";
            //String signStr = signData_SHA256(sSource);
            String signStr1 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANIr1jD2KeG6l6y8tLi4v2PD8ic/u1ZMpe1Fw5W1PA5dqD+zEA7xZU3+lfNPzIhkM4voLkkIU2RYNxUDn44vW1qD2Chh/3AXf9rSZNrL/aZYBCnR0ooEPSiP5i0zAoZoUB3UXyGXi9z+6t82ewIxKwwTRApmkP3Ys3PBFJXxnyUrAgMBAAECgYEAmILRAmSPTcs+Z03kgqslzzqQjSS1R9GFEqHuZMPIGdCi196Th5RqY+ebXp2ImWzE7wCEHeEZypYtGmWvsdjxm3NyJyVSDB11cyqaAJxB+V2TzsXdhxkFWUi4UsrBsKoXGcGqbM+5/mYLsPvcTsspcY56runkd1GT6S72QUKbldkCQQDwyTcrTQsgGQ8UpohFqnaBtpwPFgpQwYCFT/LFdFuQuoTgE37oT47731ooDLabiSqWQqQZz5Bjt3Djsfw4UXYPAkEA33NrHAXh8k5hamW3gNYlmH0mAp7NCpKJt+1opWVpCIRpC/VM1dwX7FQEnVvrZJS/bi3qPsNZIWawCtvGvMKbJQJAQHBpLlJxDscGDS3APHyxOGepfjQU0KXogkTyILvSIXp5QwqUpRFdn7SXiS5V1GwyFIKDLlEpQDaApL8mU+AhKwJAXQha8gHo/s9vc7MB4dxHLuP8LV8ck2hLeo6X4TUFtAwdCGOLKAdI1pZsjW+149yijycCvjrrIyLz7LFwXyrPGQJAGKFH7WofQUUOOHtd7aJU2AoS54NyRbXJESuY0nzHLHhIt1hrKmohVNs/fAl/EyTeII4+sKCXgPD7kmGDtol50Q==";
            boolean result = verifySign_SHA256(sSource1, publicPath, signStr);
            System.out.println(">>>>>>>>>>result: " + result);

        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
    }

    public static String signData_SHA256(Object sSource, String privatePath) {
        byte[] bSource = (byte[]) null;
        byte[] bSourceSha256 = (byte[]) null;
        byte[] bSign = (byte[]) null;
        String bSignBase64 = null;
        try {
            String privateKeyPath = "d:\\TCB\\other\\Singnature\\keystore\\KeyTestGrap\\mobivi.pfx";
            String keyStorePass = "1";
            String keyStoreType = "PKCS12";
            String alias = "le-7f0cf49a-2207-4799-94b5-35f5649631cc";
            String keyPass = "1";
            PrivateKey privateKey = loadKey(privatePath);
            bSource = sSource.toString().getBytes("UTF-8");
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            bSourceSha256 = digest.digest(bSource);
            Signature sha256_rsa = Signature.getInstance("SHA256withRSA");
            sha256_rsa.initSign(privateKey);
            sha256_rsa.update(bSourceSha256);
            bSign = sha256_rsa.sign();
            bSignBase64 = DatatypeConverter.printBase64Binary(bSign);

        } catch (Exception var8) {
        }
        return bSignBase64;
    }

    public static boolean verifySign_SHA256(Object sSource, String _path, Object sSign) {
        byte[] bSign = (byte[]) null;
        byte[] bSource = (byte[]) null;
        byte[] bSourceSha256 = (byte[]) null;
        boolean bVerify = false;

        try {
            PublicKey publicKey = getPulicKey(_path);
            bSource = sSource.toString().getBytes("UTF-8");
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            bSourceSha256 = digest.digest(bSource);
            bSign = DatatypeConverter.parseBase64Binary(sSign.toString());
            Signature sha256_rsa = Signature.getInstance("SHA256withRSA");
            sha256_rsa.initVerify(publicKey);
            sha256_rsa.update(bSourceSha256);
            bVerify = sha256_rsa.verify(bSign);
        } catch (Exception var9) {
        }

        return bVerify;
    }

    public static String encryptWithPublicKeyTCB(String _content) {
        String _pathPublic = "d:\\TCB\\other\\Singnature\\keystore\\KeyTestGrap/mobivi.cer";
        return encrypt_AES256(_content, _pathPublic);
    }

    public static String decryptFtWithTCB(String content) {
        String keyStorePath = "d:\\TCB\\other\\Singnature\\keystore\\KeyTestGrap\\mobivi.pfx";
        String keyStorePass = "1";
        String keyStoreType = "PKCS12";
        String alias = "le-7f0cf49a-2207-4799-94b5-35f5649631cc";
        String keyPass = "1";
        String _pathPublic = "d:\\TCB\\other\\Singnature\\keystore\\KeyTestGrap/mobivi.cer";
        return decrypt_AES256(content, keyStorePath, keyStorePass, keyStoreType, alias, keyPass, _pathPublic);
    }

    public static String verifySHA256(String msgInput, String inputType, String sign, String publicPath, String algorithm) {
        try {
            PublicKey publicKey = getPulicKey(publicPath);
            if (msgInput == null) {
                return "Error Message Input cannot null";
            } else if (inputType == null) {
                return "Error Input type cannot null";
            } else {
                String var6;
                switch ((var6 = inputType.toUpperCase()).hashCode()) {
                    case 87031:
                        if (var6.equals("XML")) {
                            if (verifyXML(msgInput, publicKey)) {
                                return "true";
                            }

                            return "false";
                        }
                        break;
                    case 2571565:
                        if (var6.equals("TEXT")) {
                            if (sign == null) {
                                return "Error Signature value cannot null";
                            }

                            if (verifyMsg(msgInput, publicKey, sign, algorithm)) {
                                return "true";
                            }

                            return "false";
                        }
                }

                return "Error Input type doesn't support";
            }
        } catch (Exception var7) {
            return "Error" + var7.getMessage();
        }
    }

    private static boolean verifing(byte[] bSource, PublicKey publicKey, byte[] bSign, String signAlgorithm) {
        Boolean bverify = false;

        try {
            Signature signature = Signature.getInstance(signAlgorithm);
            signature.initVerify(publicKey);
            signature.update(bSource);
            bverify = signature.verify(bSign);
        } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException var7) {
            return false;
        }

        return bverify;
    }

    public static boolean verifyXML(String xmlInput, PublicKey publicKey) {
        boolean result = false;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream inputArr = new ByteArrayInputStream(xmlInput.getBytes("UTF-8"));
            Document doc = builder.parse(inputArr);
            NodeList nl = doc.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature");
            System.out.println("Number of signature tag : " + nl.getLength());
            if (nl.getLength() == 0) {
                return false;
            } else {
                int length = nl.getLength();

                for (int i = 0; i < length; ++i) {
//                    DOMValidateContext valContext = new DOMValidateContext(new X509KeySelector(), nl.item(i));
//                    XMLSignatureFactory xmlFactory = XMLSignatureFactory.getInstance("DOM");
//                    XMLSignature signature = xmlFactory.unmarshalXMLSignature(valContext);
//                    KeyInfo k = signature.getKeyInfo();
//                    k.getContent();
//                    result = signature.validate(valContext);
//                    System.out.println("Number " + i + " " + result);
                }

                return result;
            }
        } catch (Exception var15) {
            return false;
        }
    }

    public static boolean verifyMsg(String sSource, PublicKey publicKey, String sSign, String signAlgorithm) {
        boolean bVerify = false;

        try {
            byte[] bSign = new byte[sSign.length()];
            byte[] bSource = new byte[sSource.length()];
            bSource = sSource.getBytes("UTF-8");
            bSign = DatatypeConverter.parseBase64Binary(sSign);
            bVerify = verifing(bSource, publicKey, bSign, signAlgorithm);
            return bVerify;
        } catch (Exception var8) {
            return false;
        }
    }

    private static final String PKCS_1_PEM_HEADER = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String PKCS_1_PEM_FOOTER = "-----END RSA PRIVATE KEY-----";
    private static final String PKCS_8_PEM_HEADER = "-----BEGIN PRIVATE KEY-----";
    private static final String PKCS_8_PEM_FOOTER = "-----END PRIVATE KEY-----";

    public static PrivateKey loadKey(String keyFilePath) throws Exception {
        ClassLoader classLoader = JavaSecutityEncrypt_V2_1.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(keyFilePath)).getFile());
        
        byte[] keyDataBytes = Files.readAllBytes(file.toPath());
        String keyDataString = new String(keyDataBytes, StandardCharsets.UTF_8);

        if (keyDataString.contains(PKCS_1_PEM_HEADER)) {
            // OpenSSL / PKCS#1 Base64 PEM encoded file
            keyDataString = keyDataString.replace(PKCS_1_PEM_HEADER, "");
            keyDataString = keyDataString.replace(PKCS_1_PEM_FOOTER, "");
            return readPkcs1PrivateKey(Base64.decodeBase64(keyDataString));
        }

        if (keyDataString.contains(PKCS_8_PEM_HEADER)) {
            // PKCS#8 Base64 PEM encoded file
            keyDataString = keyDataString.replace(PKCS_8_PEM_HEADER, "");
            keyDataString = keyDataString.replace(PKCS_8_PEM_FOOTER, "");
            return readPkcs8PrivateKey(Base64.decodeBase64(keyDataString));
        }

        // We assume it's a PKCS#8 DER encoded binary file
        return readPkcs8PrivateKey(Files.readAllBytes(Paths.get(keyFilePath)));
    }

    private static PrivateKey readPkcs8PrivateKey(byte[] pkcs8Bytes) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "SunRsaSign");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8Bytes);
        try {
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Unexpected key format!", e);
        }
    }

    private static PrivateKey readPkcs1PrivateKey(byte[] pkcs1Bytes) throws GeneralSecurityException {
        // We can't use Java internal APIs to parse ASN.1 structures, so we build a PKCS#8 key Java can understand
        int pkcs1Length = pkcs1Bytes.length;
        int totalLength = pkcs1Length + 22;
        byte[] pkcs8Header = new byte[]{
            0x30, (byte) 0x82, (byte) ((totalLength >> 8) & 0xff), (byte) (totalLength & 0xff), // Sequence + total length
            0x2, 0x1, 0x0, // Integer (0)
            0x30, 0xD, 0x6, 0x9, 0x2A, (byte) 0x86, 0x48, (byte) 0x86, (byte) 0xF7, 0xD, 0x1, 0x1, 0x1, 0x5, 0x0, // Sequence: 1.2.840.113549.1.1.1, NULL
            0x4, (byte) 0x82, (byte) ((pkcs1Length >> 8) & 0xff), (byte) (pkcs1Length & 0xff) // Octet string + length
        };
        byte[] pkcs8bytes = join(pkcs8Header, pkcs1Bytes);
        return readPkcs8PrivateKey(pkcs8bytes);
    }

    private static byte[] join(byte[] byteArray1, byte[] byteArray2) {
        byte[] bytes = new byte[byteArray1.length + byteArray2.length];
        System.arraycopy(byteArray1, 0, bytes, 0, byteArray1.length);
        System.arraycopy(byteArray2, 0, bytes, byteArray1.length, byteArray2.length);
        return bytes;
    }

    public static PrivateKey getPrivateKey(String keyStorePath, String keyStorePass, String keyStoreType, String alias, String keyPass) {
        try {
            FileInputStream is = new FileInputStream(keyStorePath);
            KeyStore keystore = KeyStore.getInstance(keyStoreType);
            keystore.load(is, keyStorePass.toCharArray());
            if (keystore.isKeyEntry(alias)) {
                PrivateKey privateKey = (PrivateKey) keystore.getKey(alias, keyPass.toCharArray());
                is.close();
                return privateKey;
            } else {
                is.close();
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("TCB path PrivateKey is Null!!!!!!!");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                return null;
            }
        } catch (Exception var8) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Exception TCB Get PrivateKey :" + var8.toString());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            return null;
        }
    }

    public static PublicKey getPulicKey(String certPath) {
        try {
            ClassLoader classLoader = JavaSecutityEncrypt_V2_1.class.getClassLoader();
            FileInputStream fis = new FileInputStream(Objects.requireNonNull(classLoader.getResource(certPath)).getFile());
            if (fis == null) {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("TCB path PublicKey is Null!!!!!!!");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                return null;
            } else {
                CertificateFactory fact = CertificateFactory.getInstance("X.509");
                X509Certificate cer = (X509Certificate) fact.generateCertificate(fis);
                PublicKey publicKey = cer.getPublicKey();
                fis.close();
                return publicKey;
            }
        } catch (Exception var5) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Exception TCB Get Publickey :" + var5.toString());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            return null;
        }
    }

    public static String getDataFt(String pathData) throws Exception {
        String _sSource = "FundTransfer";
        Document doc = convertXmlFileToDocument(pathData);
        String value = getValueByPath(doc, "/XferReq/ReqGnlInf/Id");
        _sSource += value;
        value = getValueByPath(doc, "/XferReq/ReqInf/ToAcct/AcctId");
        _sSource += value;
        value = getValueByPath(doc, "/XferReq/ReqInf/ToAcct/AcctTitl");
        _sSource += value;
        value = getValueByPath(doc, "/XferReq/ReqInf/TxDt");
        _sSource += value;
        value = getValueByPath(doc, "/XferReq/ReqInf/TxAmt");
        _sSource += value;
        return _sSource;
    }

    public static String getDataAcctInfo(String pathData) throws Exception {
        String _sSource = "AcctInq";
        Document doc = convertXmlFileToDocument(pathData);
        String value = getValueByPath(doc, "/AcctInqRq/RqUID");
        _sSource += value;
        value = getValueByPath(doc, "/AcctInqRq/MsgRqHdr/SvcIdent/SvcName");
        _sSource += value;
        value = getValueByPath(doc, "/AcctInqRq/MsgRqHdr/ContextRqHdr/ClientApp/Name");
        _sSource += value;
        value = getValueByPath(doc, "/AcctInqRq/MsgRqHdr/CredentialsRqHdr/SubjectRole");
        _sSource += value;
        value = getValueByPath(doc, "/AcctInqRq/MsgRqHdr/CredentialsRqHdr/OvrdExceptionCode");
        _sSource += value;
        value = getValueByPath(doc, "/AcctInqRq/AcctSel/AcctKeys/AcctId");
        _sSource += value;
        return _sSource;
    }

    public static String getDataInquiryOversea(String pathData) throws Exception {
        String _sSource = "InqTransactionStatus";
        Document doc = convertXmlFileToDocument(pathData);
        String value = getValueByPath(doc, "/InqTxStsReq/ReqGnlInf/Id");
        _sSource += value;
        value = getValueByPath(doc, "/InqTxStsReq/ReqInf/ReqTxId");
        _sSource += value;
        return _sSource;
    }

    public static Document convertXmlFileToDocument(String fileUrl) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc = null;
        try {
            File xmlFile = new File(fileUrl);
            builder = factory.newDocumentBuilder();
            doc = builder.parse(xmlFile);

        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            throw e;
        }
        return doc;
    }

    public static String getValueByPath(Document doc, String path, String[]... nameSpace) {
        String result = "";
        try {
            Node node = getNodeByPath(doc, path, nameSpace);
            if (node != null) {
                result = node.getTextContent();
            }
            return result;
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public static String encrypt_AES256(String _content, String _path) {
        String _result = null;
        try {
            SecureRandom rand = new SecureRandom();
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(256, rand);
            SecretKey secretKey = generator.generateKey();
            PublicKey publicKey = getPulicKey(_path);
            if (publicKey == null) {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("TCB Get PublicKey is Null!!!!!!!");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                throw new Exception();
            }

            _result = encrypt_AES_RSA(_content, secretKey.getEncoded(), publicKey);
        } catch (Exception var7) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Exception TCB Security Encrypt AES256:" + var7.toString());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }

        return _result;
    }

    public static String encrypt_AES_RSA(Object objSource, byte[] keyGen, PublicKey publicKey) {
        byte[] bSource = (byte[]) null;
        String base64Return = null;

        try {
            bSource = objSource.toString().getBytes();
            int ivSize = 16;
            byte[] iv = new byte[ivSize];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyGen, 0, keyGen.length, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(1, secretKeySpec, ivParameterSpec);
            byte[] enSource = cipher.doFinal(bSource);
            byte[] comIVandEnSource = new byte[ivSize + enSource.length];
            System.arraycopy(iv, 0, comIVandEnSource, 0, ivSize);
            System.arraycopy(enSource, 0, comIVandEnSource, ivSize, enSource.length);
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(1, publicKey);
            byte[] enSecretKey = rsaCipher.doFinal(secretKeySpec.getEncoded());
            byte[] comReturn = new byte[enSecretKey.length + comIVandEnSource.length];
            System.arraycopy(enSecretKey, 0, comReturn, 0, enSecretKey.length);
            System.arraycopy(comIVandEnSource, 0, comReturn, enSecretKey.length, comIVandEnSource.length);
            base64Return = DatatypeConverter.printBase64Binary(comReturn);
        } catch (Exception var16) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Exception Encrypt AES256 + RSA256(KEYGEN):" + var16.toString());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }

        return base64Return;
    }

    public static Node getNodeByPath(Document doc, String path, String[]... nameSpace) {
        Node node = null;
        try {
            if (path == null || path.length() == 0) {
                return null;
            }
            XPath xPath = XPathFactory.newInstance().newXPath();
            if (nameSpace != null && nameSpace.length > 0) {
                NamespaceContext nsContext = new NamespaceContext() {
                    @Override
                    public String getNamespaceURI(String prefix) {
                        for (String[] _ns : nameSpace) {
                            if (prefix.equals(_ns[0])) {
                                return _ns[1];
                            }
                        }
                        return null;
                    }

                    @Override
                    public String getPrefix(String namespaceURI) {
                        for (String[] _ns : nameSpace) {
                            if (namespaceURI.equals(_ns[1])) {
                                return _ns[0];
                            }
                        }
                        return null;
                    }

                    @Override
                    public Iterator getPrefixes(String namespaceURI) {
                        return null;
                    }
                };
                xPath.setNamespaceContext(nsContext);

            }
            NodeList nodeList = (NodeList) xPath.compile(path).evaluate(
                    doc, XPathConstants.NODESET);
            if (nodeList != null && nodeList.getLength() > 0) {
                node = nodeList.item(0);
            }
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            return null;
        }
        return node;
    }

    public static String decrypt_AES256(String _content, String keyStorePath, String keyStorePass, String keyStoreType, String alias, String keyPass, String _pathPublic) {
        String _result = null;

        try {
            PublicKey publicKey = getPulicKey(_pathPublic);
            PrivateKey privatekey = getPrivateKey(keyStorePath, keyStorePass, keyStoreType, alias, keyPass);
            if (privatekey == null) {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("TCB Get PrivateKey is Null!!!!!!!");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                throw new Exception();
            }

            _result = decrypt_AES_RSA(_content, privatekey, publicKey);
        } catch (Exception var10) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Exception TCB Security Decrypt AES256:" + var10.toString());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }

        return _result;
    }

    public static String decrypt_AES_RSA(Object objSource, PrivateKey privateKey, PublicKey publicKey) {
        int ivSize = 16;
        byte[] bSource = (byte[]) null;
        String deReturn = null;

        try {
            int keyLength = getKeyLength(publicKey) / 8;
            bSource = DatatypeConverter.parseBase64Binary(objSource.toString());
            byte[] enSecretKey = new byte[keyLength];
            System.arraycopy(bSource, 0, enSecretKey, 0, keyLength);
            byte[] iv = new byte[ivSize];
            System.arraycopy(bSource, keyLength, iv, 0, iv.length);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            int encryptedSize = bSource.length - ivSize - keyLength;
            byte[] encryptedBytes = new byte[encryptedSize];
            System.arraycopy(bSource, keyLength + ivSize, encryptedBytes, 0, encryptedSize);
            byte[] decryptedKey = decrypt(enSecretKey, privateKey);
            SecretKeySpec originalKey = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, "AES");
            Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipherDecrypt.init(2, originalKey, ivParameterSpec);
            byte[] decrypted = cipherDecrypt.doFinal(encryptedBytes);
            deReturn = new String(decrypted, "UTF-8");
        } catch (Exception var16) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Exception Decrypt AES256 + RSA256(KEYGEN):" + var16.toString());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }

        return deReturn;
    }

    public static byte[] decrypt(byte[] enSecretKey, PrivateKey privateKey) {
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(2, privateKey);
            byte[] decodedKeyBytes = rsaCipher.doFinal(enSecretKey);
            return decodedKeyBytes;
        } catch (Exception var4) {
            return null;
        }
    }

    public static int getKeyLength(PublicKey publicKey) {
        try {
            RSAPublicKey rsaPk = (RSAPublicKey) publicKey;
            int result = rsaPk.getModulus().bitLength();
            return result;
        } catch (Exception var3) {
            return 0;
        }
    }

    public static String readFileFrPath(String path) {

        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {

            // read line by line
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        // System.out.println(sb);
        return sb.toString();
    }
}
