/**
 * Payment_Service_Card_V3Locator.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gateway.core.channel.vcb_ecom_atm.ws_client;

public class Payment_Service_Card_V3Locator extends org.apache.axis.client.Service implements gateway.core.channel.vcb_ecom_atm.ws_client.Payment_Service_Card_V3 {

    // Use to get a proxy class for Payment_Service_Card_V3Soap
    private String Payment_Service_Card_V3Soap_address = "http://192.168.200.234:9091/EcommerceService_UAT/Payment_Service_Card_V3.asmx";
    // The WSDD service name defaults to the port name.
    private String Payment_Service_Card_V3SoapWSDDServiceName = "Payment_Service_Card_V3Soap";
    private java.util.HashSet ports = null;

    public Payment_Service_Card_V3Locator() {
    }

    public Payment_Service_Card_V3Locator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Payment_Service_Card_V3Locator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    public String getPayment_Service_Card_V3SoapAddress() {
        return Payment_Service_Card_V3Soap_address;
    }

    public String getPayment_Service_Card_V3SoapWSDDServiceName() {
        return Payment_Service_Card_V3SoapWSDDServiceName;
    }

    public void setPayment_Service_Card_V3SoapWSDDServiceName(String name) {
        Payment_Service_Card_V3SoapWSDDServiceName = name;
    }

    public gateway.core.channel.vcb_ecom_atm.ws_client.Payment_Service_Card_V3Soap getPayment_Service_Card_V3Soap() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Payment_Service_Card_V3Soap_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPayment_Service_Card_V3Soap(endpoint);
    }

    public gateway.core.channel.vcb_ecom_atm.ws_client.Payment_Service_Card_V3Soap getPayment_Service_Card_V3Soap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            gateway.core.channel.vcb_ecom_atm.ws_client.Payment_Service_Card_V3SoapStub _stub = new gateway.core.channel.vcb_ecom_atm.ws_client.Payment_Service_Card_V3SoapStub(portAddress, this);
            _stub.setPortName(getPayment_Service_Card_V3SoapWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPayment_Service_Card_V3SoapEndpointAddress(String address) {
        Payment_Service_Card_V3Soap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (gateway.core.channel.vcb_ecom_atm.ws_client.Payment_Service_Card_V3Soap.class.isAssignableFrom(serviceEndpointInterface)) {
                gateway.core.channel.vcb_ecom_atm.ws_client.Payment_Service_Card_V3SoapStub _stub = new gateway.core.channel.vcb_ecom_atm.ws_client.Payment_Service_Card_V3SoapStub(new java.net.URL(Payment_Service_Card_V3Soap_address), this);
                _stub.setPortName(getPayment_Service_Card_V3SoapWSDDServiceName());
                return _stub;
            }
        } catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("Payment_Service_Card_V3Soap".equals(inputPortName)) {
            return getPayment_Service_Card_V3Soap();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.vietcombank.com.vn/Services/C24OPServices/", "Payment_Service_Card_V3");
    }

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.vietcombank.com.vn/Services/C24OPServices/", "Payment_Service_Card_V3Soap"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {

        if ("Payment_Service_Card_V3Soap".equals(portName)) {
            setPayment_Service_Card_V3SoapEndpointAddress(address);
        } else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
