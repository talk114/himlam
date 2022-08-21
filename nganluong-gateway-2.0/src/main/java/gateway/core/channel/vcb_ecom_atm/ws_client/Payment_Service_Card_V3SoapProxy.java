package gateway.core.channel.vcb_ecom_atm.ws_client;

public class Payment_Service_Card_V3SoapProxy implements gateway.core.channel.vcb_ecom_atm.ws_client.Payment_Service_Card_V3Soap {
    private String _endpoint = null;
    private gateway.core.channel.vcb_ecom_atm.ws_client.Payment_Service_Card_V3Soap payment_Service_Card_V3Soap = null;

    public Payment_Service_Card_V3SoapProxy() {
        _initPayment_Service_Card_V3SoapProxy();
    }

    public Payment_Service_Card_V3SoapProxy(String endpoint) {
        _endpoint = endpoint;
        _initPayment_Service_Card_V3SoapProxy();
    }

    private void _initPayment_Service_Card_V3SoapProxy() {
        try {
            payment_Service_Card_V3Soap = (new gateway.core.channel.vcb_ecom_atm.ws_client.Payment_Service_Card_V3Locator()).getPayment_Service_Card_V3Soap();
            if (payment_Service_Card_V3Soap != null) {
                if (_endpoint != null)
                    ((javax.xml.rpc.Stub) payment_Service_Card_V3Soap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
                else
                    _endpoint = (String) ((javax.xml.rpc.Stub) payment_Service_Card_V3Soap)._getProperty("javax.xml.rpc.service.endpoint.address");
            }

        } catch (javax.xml.rpc.ServiceException serviceException) {
        }
    }

    public String getEndpoint() {
        return _endpoint;
    }

    public void setEndpoint(String endpoint) {
        _endpoint = endpoint;
        if (payment_Service_Card_V3Soap != null)
            ((javax.xml.rpc.Stub) payment_Service_Card_V3Soap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);

    }

    public gateway.core.channel.vcb_ecom_atm.ws_client.Payment_Service_Card_V3Soap getPayment_Service_Card_V3Soap() {
        if (payment_Service_Card_V3Soap == null)
            _initPayment_Service_Card_V3SoapProxy();
        return payment_Service_Card_V3Soap;
    }

    public String verifyCardAndSendOTP(String requestString) throws java.rmi.RemoteException {
        if (payment_Service_Card_V3Soap == null)
            _initPayment_Service_Card_V3SoapProxy();
        return payment_Service_Card_V3Soap.verifyCardAndSendOTP(requestString);
    }

    public String verifyOTPAndPayment(String requestString) throws java.rmi.RemoteException {
        if (payment_Service_Card_V3Soap == null)
            _initPayment_Service_Card_V3SoapProxy();
        return payment_Service_Card_V3Soap.verifyOTPAndPayment(requestString);
    }

    public String query(String requestString) throws java.rmi.RemoteException {
        if (payment_Service_Card_V3Soap == null)
            _initPayment_Service_Card_V3SoapProxy();
        return payment_Service_Card_V3Soap.query(requestString);
    }

    public String refundOffline(String requestString) throws java.rmi.RemoteException {
        if (payment_Service_Card_V3Soap == null)
            _initPayment_Service_Card_V3SoapProxy();
        return payment_Service_Card_V3Soap.refundOffline(requestString);
    }


}