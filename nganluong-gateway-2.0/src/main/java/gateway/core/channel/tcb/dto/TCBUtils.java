package gateway.core.channel.tcb.dto;

import gateway.core.channel.tcb.response.RspnInfResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.json.JSONObject;
import org.json.XML;
import java.io.StringReader;

public class TCBUtils {

    public static String formatJsonToXml(JSONObject object, String tagName) {
        return XML.toString(object, tagName);
    }

    public static JSONObject xmlToJson(String xml) {
        return XML.toJSONObject(xml);
    }

    public static RspnInfResponse parseXmlToObject(String xml) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(RspnInfResponse.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        RspnInfResponse res = (RspnInfResponse) jaxbUnmarshaller.unmarshal(new StringReader(xml));
        return res;
    }

//    public static Object parseListTransXmlToObject(String xml) throws JAXBException {
//        xml = xml.replace("<bank_ref>", "").replace("</bank_ref>", "").replace("<lst_trans>", "").replace("</lst_trans>", "");
//        JAXBContext jaxbContext = JAXBContext.newInstance(RootResponseListTrans.class);
//        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//        return jaxbUnmarshaller.unmarshal(new StringReader(xml));
//    }
}
