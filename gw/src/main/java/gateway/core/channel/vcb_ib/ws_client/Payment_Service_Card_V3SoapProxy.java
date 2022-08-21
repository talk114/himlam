package gateway.core.channel.vcb_ib.ws_client;

public class Payment_Service_Card_V3SoapProxy implements Payment_Service_Card_V3Soap {

    private String _endpoint = null;
    private Payment_Service_Card_V3Soap payment_Service_Card_V3Soap = null;

    public Payment_Service_Card_V3SoapProxy() {
        _initPayment_Service_Card_V3SoapProxy();
    }

    public Payment_Service_Card_V3SoapProxy(String endpoint) {
        _endpoint = endpoint;
        _initPayment_Service_Card_V3SoapProxy();
    }

    private void _initPayment_Service_Card_V3SoapProxy() {
        try {
            payment_Service_Card_V3Soap = (new Payment_Service_Card_V3Locator()).getPayment_Service_Card_V3Soap();
            if (payment_Service_Card_V3Soap != null) {
                if (_endpoint != null) {
                    ((javax.xml.rpc.Stub) payment_Service_Card_V3Soap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
                } else {
                    _endpoint = (String) ((javax.xml.rpc.Stub) payment_Service_Card_V3Soap)._getProperty("javax.xml.rpc.service.endpoint.address");
                }
            }

        } catch (javax.xml.rpc.ServiceException serviceException) {
        }
    }

    public String getEndpoint() {
        return _endpoint;
    }

    public void setEndpoint(String endpoint) {
        _endpoint = endpoint;
        if (payment_Service_Card_V3Soap != null) {
            ((javax.xml.rpc.Stub) payment_Service_Card_V3Soap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        }

    }

    public Payment_Service_Card_V3Soap getPayment_Service_Card_V3Soap() {
        if (payment_Service_Card_V3Soap == null) {
            _initPayment_Service_Card_V3SoapProxy();
        }
        return payment_Service_Card_V3Soap;
    }

    public String verifyPayment(String requestString) throws java.rmi.RemoteException {
        if (payment_Service_Card_V3Soap == null) {
            _initPayment_Service_Card_V3SoapProxy();
        }
        return payment_Service_Card_V3Soap.verifyPayment(requestString);
    }

    public String query(String requestString) throws java.rmi.RemoteException {
        if (payment_Service_Card_V3Soap == null) {
            _initPayment_Service_Card_V3SoapProxy();
        }
        return payment_Service_Card_V3Soap.query(requestString);
    }

    public String refund(String requestString) throws java.rmi.RemoteException {
        if (payment_Service_Card_V3Soap == null) {
            _initPayment_Service_Card_V3SoapProxy();
        }
        return payment_Service_Card_V3Soap.refund(requestString);
    }

}
