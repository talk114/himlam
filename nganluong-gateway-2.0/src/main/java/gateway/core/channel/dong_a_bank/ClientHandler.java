package gateway.core.channel.dong_a_bank;

import gateway.core.channel.dong_a_bank.dto.DABConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler extends GenericHandler {

    private static final Logger logger = LogManager.getLogger(ClientHandler.class);

    private Map<String, Object> handlerConfig = new HashMap<>();

    public ClientHandler() {
    }

    public boolean handleResponse(MessageContext context) {
//		System.out.println("response");
        return true;
    }

    public boolean handleRequest(MessageContext context) {
//		System.out.println("handleRequest");
        try {
            SOAPMessageContext smc = (SOAPMessageContext) context;
            SOAPMessage msg = smc.getMessage();
            SOAPPart sp = msg.getSOAPPart();
            SOAPEnvelope se = sp.getEnvelope();
            SOAPHeader shd = se.getHeader();

            SOAPElement security = shd.addChildElement("securityHeader", "ns1", "https://webservice.dongabank.com.vn/securities/WSBean");

            SOAPElement accessKey = security.addChildElement(DABConstants.ACCESS_KEY_HEADER, "ns1");
            accessKey.addTextNode(handlerConfig.get(DABConstants.ACCESS_KEY_HEADER).toString());

            SOAPElement timestamp = security.addChildElement(DABConstants.TIME_STAMP_HEADER, "ns1");
            timestamp.addTextNode(handlerConfig.get(DABConstants.TIME_STAMP_HEADER).toString());

            SOAPElement signature = security.addChildElement(DABConstants.SIGNATURE_HEADER, "ns1");
            signature.addTextNode(handlerConfig.get(DABConstants.SIGNATURE_HEADER).toString());

            msg.saveChanges();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            msg.writeTo(out);
            String strMsg = new String(out.toByteArray());

            logger.info("SOAP message: \n" + strMsg);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean handleFault(javax.xml.rpc.handler.MessageContext context) {
//		System.out.println("ClientHandler: In handleFault");
        return true;
    }

    public void init(javax.xml.rpc.handler.HandlerInfo config) {
        handlerConfig = config.getHandlerConfig();
    }

    public void destroy() {
    }

    @Override
    public QName[] getHeaders() {
        // TODO Auto-generated method stub
        return null;
    }
}
