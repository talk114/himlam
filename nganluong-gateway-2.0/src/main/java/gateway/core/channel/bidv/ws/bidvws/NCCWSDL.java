
package gateway.core.channel.bidv.ws.bidvws;


import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01
 * Generated source version: 2.2
 *
 */
//@WebServiceClient(name = "NCC_WSDL", targetNamespace = "BIDVWS", wsdlLocation = "file:/D:/Work/NEXTTECH/Project/Payment_GW/payment-gateway-documents/PaymentGateWay/BIDV/DEV/cttt/cttt.wsdl")
@WebServiceClient(name = "NCC_WSDL", targetNamespace = "BIDVWS", wsdlLocation = "classpath:bidv-key/BIDV.wsdl")

public class NCCWSDL
        extends Service
{

    private final static URL NCCWSDL_WSDL_LOCATION;
    private final static WebServiceException NCCWSDL_EXCEPTION;
    private final static QName NCCWSDL_QNAME = new QName("BIDVWS", "NCC_WSDL");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("classpath:bidv-key/BIDV.wsdl");
//        	url = new URL("http://119.17.209.164:9588/BIDVGateway/WS");

//        	url = new URL("file:/home/vinhnt/BIDV/BIDV.wsdl");
//        	url = NCCWSDL.class.getResource("/bidv-key/BIDV.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        NCCWSDL_WSDL_LOCATION = url;
        NCCWSDL_EXCEPTION = e;
    }

    public NCCWSDL() {
        super(__getWsdlLocation(), NCCWSDL_QNAME);
    }

    public NCCWSDL(WebServiceFeature... features) {
        super(__getWsdlLocation(), NCCWSDL_QNAME, features);
    }

    public NCCWSDL(URL wsdlLocation) {
        super(wsdlLocation, NCCWSDL_QNAME);
    }

    public NCCWSDL(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, NCCWSDL_QNAME, features);
    }

    public NCCWSDL(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public NCCWSDL(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns NCCPortType
     */
    @WebEndpoint(name = "NCC_PortTypeEndpoint0")
    public NCCPortType getNCCPortTypeEndpoint0() {
        return super.getPort(new QName("BIDVWS", "NCC_PortTypeEndpoint0"), NCCPortType.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns NCCPortType
     */
    @WebEndpoint(name = "NCC_PortTypeEndpoint0")
    public NCCPortType getNCCPortTypeEndpoint0(WebServiceFeature... features) {
        return super.getPort(new QName("BIDVWS", "NCC_PortTypeEndpoint0"), NCCPortType.class, features);
    }

    private static URL __getWsdlLocation() {
        if (NCCWSDL_EXCEPTION!= null) {
            throw NCCWSDL_EXCEPTION;
        }
        return NCCWSDL_WSDL_LOCATION;
    }

}