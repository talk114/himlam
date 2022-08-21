package gateway.core.channel.cybersouce.soap;

import com.google.common.base.Strings;
import gateway.core.channel.cybersouce.dto.CybersourceConfig;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SOAPUtil {
    private static final Logger logger = LogManager.getLogger(SOAPUtil.class);
//    public static String merchantName_STB = "nganluong_it";
    public static Map<String, String> getCyberKeyInfo(String merchantId) {
        String mid = null;
        String transactionKey = null;
        if (!Strings.isNullOrEmpty(merchantId)) {
            mid = merchantId;
        }
        switch (Objects.requireNonNull(mid)) {
            case CybersourceConfig.MERCHANT_ID_VTB:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_VTB;
                break;
            case CybersourceConfig.MERCHANT_ID_STB:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_STB;
                break;
            case CybersourceConfig.MERCHANT_ID_STB_TIKI:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_STB_TIKI;
                break;
            case CybersourceConfig.MERCHANT_ID_VCB:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_VCB;
                break;
            case CybersourceConfig.MERCHANT_ID_VCB_SGD:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_VCB_SGD;
                break;
            case CybersourceConfig.MERCHANT_ID_STB_TRAVEL:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_STB_TRAVEL;
                break;
            case CybersourceConfig.MERCHANT_ID_STB_CBS:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_STB_CBS;
                break;
            case CybersourceConfig.MERCHANT_ID_STB_BH:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_STB_BH;
                break;
            case CybersourceConfig.MERCHANT_ID_STB_DVC1:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_STB_DVC1;
                break;
            case CybersourceConfig.MERCHANT_ID_STB_DVC2:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_STB_DVC2;
                break;
            case CybersourceConfig.MERCHANT_ID_STB_VISA:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_STB_VISA;
                break;
            case CybersourceConfig.MERCHANT_ID_VCB_ST:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_VCB_ST;
                break;
            case CybersourceConfig.MERCHANT_ID_VCB_OTHERS:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_VCB_OTHERS;
                break;
            case CybersourceConfig.MERCHANT_ID_VCB_TH:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_VCB_TH;
                break;
            case CybersourceConfig.MERCHANT_ID_SCB_ALOTHER:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_SCB_ALOTHER;
                break;
            case CybersourceConfig.MERCHANT_ID_SCB_VANTAICONG:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_SCB_VANTAICONG;
                break;
            case CybersourceConfig.MERCHANT_ID_STB_BAOHIEM:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_STB_BAOHIEM;
                break;
            case CybersourceConfig.MERCHANT_ID_VCB_BAOHIEM:
                transactionKey = CybersourceConfig.TRANSACTION_KEY_VCB_BAOHIEM;
        }
        Map<String, String> map = new HashMap<>();
        map.put("MID", mid);
        map.put("KEY", transactionKey);
        return map;
    }

    private static SOAPMessage createSOAPRequest(Map<String, String> body) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.DEFAULT_SOAP_PROTOCOL);
        Map<String, String> keyInfo = getCyberKeyInfo(body.get("merchantID"));
        String s = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"urn:schemas-cybersource-com:transaction-data-1.169\"><SOAP-ENV:Header xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"><wsse:Security SOAP-ENV:mustUnderstand=\"1\"><wsse:UsernameToken><wsse:Username>"
                + keyInfo.get("MID")
                + "</wsse:Username><wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wssusername-token-profile-1.0#PasswordText\">"
                + keyInfo.get("KEY")
                + "</wsse:Password></wsse:UsernameToken></wsse:Security></SOAP-ENV:Header><SOAP-ENV:Body></SOAP-ENV:Body></SOAP-ENV:Envelope>";
        SOAPMessage soapMessage = messageFactory.createMessage(new MimeHeaders(),
                new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        SOAPBody soapBody = envelope.getBody();
        if (body == null) {
            body = new HashMap<String, String>();
        }
        SOAPElement requestMessage = soapBody.addChildElement("requestMessage", "ns1");
        map(body, requestMessage);
        soapMessage.saveChanges();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapMessage.writeTo(out);
        String strMsg = new String(out.toByteArray());
//        System.out.println("SOAP REQUEST: " + checkLogCardMask(strMsg));
        return soapMessage;
    }

    private static String printSOAPResponse(SOAPMessage soapResponse) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(sourceContent, result);
        return writer.toString();
    }

    public static JSONObject runProcess(Map<String, String> body) {
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(body), CybersourceConfig.WSDL_URL);

            String s = printSOAPResponse(soapResponse);
            soapConnection.close();
            JSONObject jsonObj = XML.toJSONObject(s);
            return jsonObj;
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    public static Node runProcessGetReplyMessage(Map<String, String> body) {
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(body), CybersourceConfig.WSDL_URL);
            SOAPBody soapBody = soapResponse.getSOAPBody();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapResponse.writeTo(out);
            String strMsg = new String(out.toByteArray());
//            System.out.println("SOAP RESPONSE: " + strMsg);
            SOAPMessage soapRequest = createSOAPRequest(body);
            ByteArrayOutputStream in = new ByteArrayOutputStream();
            soapRequest.writeTo(in);
            String strRquest = new String(in.toByteArray());
            logger.info("#####################################################################################");
            logger.info("SOAP REQUEST: " + cardInfoCover(strRquest));
            logger.info("SOAP RESPONSE: " + cardInfoCover(strMsg));
            logger.info("#####################################################################################");
            return soapBody.getFirstChild();
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    private static void map(Map<String, String> map, SOAPElement element) throws SOAPException {
        Map<String, SOAPElement> mapE = new LinkedHashMap<>();
        Map<String, String> chirendE = new LinkedHashMap<>();
        Map<String, String> attribute = new LinkedHashMap<>();
        // Duyet lan 1 tao ele cap 1
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String s = entry.getKey();
            if (s.contains("_")) {
                attribute.put(s, String.valueOf(entry.getValue()));
            } else if (s.contains(".")) {
                chirendE.put(s, entry.getValue());
            } else {
                SOAPElement element2 = element.addChildElement(entry.getKey(), "ns1");
                // element2.setPrefix("ns1");
                if (entry.getValue() != null) {
                    element2.addTextNode(entry.getValue());
                }
                mapE.put(entry.getKey(), element2);

            }
        }
        // Duyet lan 2 tao ele cap 2 (Lam the nay la fix cung no no 2 cap thoi)
        for (Map.Entry<String, String> entry : chirendE.entrySet()) {
            String key = entry.getKey();
            String parent = entry.getKey().substring(0, key.indexOf("."));
            String chirend = entry.getKey().substring(key.indexOf(".") + 1, key.length());
            SOAPElement element3 = mapE.get(parent).addChildElement(chirend, "ns1");
            // element3.setPrefix("ns1");
            if (entry.getValue() != null) {
                element3.addTextNode(entry.getValue());
            }
            mapE.put(key, element3);
        }

        // Duyet lan 3 tao attribute
        for (Map.Entry<String, String> entry : attribute.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String elementX = entry.getKey().substring(0, key.indexOf("_"));
            String attributeX = entry.getKey().substring(key.indexOf("_") + 1, key.length());
            mapE.get(elementX).setAttribute(attributeX, value);
        }
        if (mapE.get("merchantDefinedData.mddField2") != null) {
            mapE.get("merchantDefinedData.mddField2").setElementQName(new QName("ns1:mddField"));
        }

    }

    /**
     * vi du node {a:{b:{c:5}}} de lay gia tri 5 thi path = a.b.c
     *
     * @param node
     * @param path
     * @return gia tri cua bien
     */
    public static String getValue(Node node, String path) {
        String l1 = "";
        String l2 = "";
        // phan tach element
        if (path.contains(".")) {
            l1 = path.substring(0, path.indexOf("."));
            l2 = path.substring(path.indexOf(".") + 1);
        } else {
            l1 = path;
        }
        try {
            Node k;
            NodeList lnode = node.getChildNodes();
            for (int i = 0; i < lnode.getLength(); i++) {
                k = lnode.item(i);
                if (k.getLocalName().equals(l1)) // ket thuc de quy khi khong ton tai l2
                {
                    if (org.springframework.util.StringUtils.isEmpty(l2)) {
                        return k.getTextContent();
                    } // de quy khi con ton tai l2
                    else {
                        return getValue(k, l2);
                    }
                }
            }

        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
    private static String cardInfoCover(String input){
        return input.replaceAll("(accountNumber>................)", "accountNumber>xxxxxxxxxxxxxxxx")
                .replaceAll("(expirationMonth>..)","expirationMonth>MM")
                .replaceAll("(expirationYear>....)","expirationYear>yyyy")
                .replaceAll("(cvNumber>...)","cvNumber>CCV");
    }
}
