/**
 * PgVcbEcomSoapService_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package clientTest.vcb_ecom;

public class PgVcbEcomSoapService_ServiceLocator extends org.apache.axis.client.Service implements PgVcbEcomSoapService_Service {

    public PgVcbEcomSoapService_ServiceLocator() {
    }


    public PgVcbEcomSoapService_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PgVcbEcomSoapService_ServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PgVcbEcomSoapServicePort
    private String PgVcbEcomSoapServicePort_address = "http://10.0.0.70:8081/PaymentGateway/PgVcbEcomSoapService";

    public String getPgVcbEcomSoapServicePortAddress() {
        return PgVcbEcomSoapServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private String PgVcbEcomSoapServicePortWSDDServiceName = "PgVcbEcomSoapServicePort";

    public String getPgVcbEcomSoapServicePortWSDDServiceName() {
        return PgVcbEcomSoapServicePortWSDDServiceName;
    }

    public void setPgVcbEcomSoapServicePortWSDDServiceName(String name) {
        PgVcbEcomSoapServicePortWSDDServiceName = name;
    }

    public PgVcbEcomSoapService_PortType getPgVcbEcomSoapServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PgVcbEcomSoapServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPgVcbEcomSoapServicePort(endpoint);
    }

    public PgVcbEcomSoapService_PortType getPgVcbEcomSoapServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            clientTest.vcb_ecom.PgVcbEcomSoapServicePortBindingStub _stub = new clientTest.vcb_ecom.PgVcbEcomSoapServicePortBindingStub(portAddress, this);
            _stub.setPortName(getPgVcbEcomSoapServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPgVcbEcomSoapServicePortEndpointAddress(String address) {
        PgVcbEcomSoapServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (PgVcbEcomSoapService_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                clientTest.vcb_ecom.PgVcbEcomSoapServicePortBindingStub _stub = new clientTest.vcb_ecom.PgVcbEcomSoapServicePortBindingStub(new java.net.URL(PgVcbEcomSoapServicePort_address), this);
                _stub.setPortName(getPgVcbEcomSoapServicePortWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
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
        if ("PgVcbEcomSoapServicePort".equals(inputPortName)) {
            return getPgVcbEcomSoapServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://vcb_ecom.channel.pg/", "PgVcbEcomSoapService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://vcb_ecom.channel.pg/", "PgVcbEcomSoapServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {

if ("PgVcbEcomSoapServicePort".equals(portName)) {
            setPgVcbEcomSoapServicePortEndpointAddress(address);
        }
        else
{ // Unknown Port Name
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
