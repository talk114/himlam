package vn.nganluong.naba.channel.vib.controller;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VIBSOAPUtil {

	private static String printSOAPResponse(SOAPMessage soapResponse) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(sourceContent, result);
        return writer.toString();
    }
//
//    public static Node runProcessGetReplyMessage(Map<String, String> body) {
//        try {
//            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
//            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
//
//            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(body), URL);
//            LOG.info("CYBER RESPONSE: " + printSOAPResponse(soapResponse));
//            SOAPBody soapBody = soapResponse.getSOAPBody();
//            return soapBody.getFirstChild();
//        } catch (Exception e) {
//            LOG.error(e);
//        }
//        return null;
//    }

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
                    element2.addTextNode(String.valueOf(entry.getValue()));
                }
                mapE.put(entry.getKey(), element2);

            }
        }
        // Duyet lan 2 tao ele cap 2 (Lam the nay la fix cung no no 2 cap thoi)
        for (Map.Entry<String, String> entry : chirendE.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
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
            l2 = path.substring(path.indexOf(".") + 1, path.length());
        } else {
            l1 = path;
        }
        try {
            Node k;
            NodeList lnode = node.getChildNodes();
            for (int i = 0; i < lnode.getLength(); i++) {
                k = lnode.item(i);
                if (k.getLocalName().equals(l1)) // ket thuc de quy khi khong
                // ton tai l2
                {
                    if (org.springframework.util.StringUtils.isEmpty(l2)) {
                        return k.getTextContent();
                    } // de quy khi con ton tai l2
                    else {
                        return getValue(k, l2);
                    }
                }
            }

        } catch (DOMException e) {
            return null;
        }
        return null;
    }
}
