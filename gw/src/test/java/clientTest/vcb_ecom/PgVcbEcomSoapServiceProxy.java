package clientTest.vcb_ecom;

public class PgVcbEcomSoapServiceProxy implements PgVcbEcomSoapService_PortType {
  private String _endpoint = null;
  private PgVcbEcomSoapService_PortType pgVcbEcomSoapService_PortType = null;

  public PgVcbEcomSoapServiceProxy() {
    _initPgVcbEcomSoapServiceProxy();
  }

  public PgVcbEcomSoapServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initPgVcbEcomSoapServiceProxy();
  }

  private void _initPgVcbEcomSoapServiceProxy() {
    try {
      pgVcbEcomSoapService_PortType = (new PgVcbEcomSoapService_ServiceLocator()).getPgVcbEcomSoapServicePort();
      if (pgVcbEcomSoapService_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)pgVcbEcomSoapService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)pgVcbEcomSoapService_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }

    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }

  public String getEndpoint() {
    return _endpoint;
  }

  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (pgVcbEcomSoapService_PortType != null)
      ((javax.xml.rpc.Stub)pgVcbEcomSoapService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);

  }

  public PgVcbEcomSoapService_PortType getPgVcbEcomSoapService_PortType() {
    if (pgVcbEcomSoapService_PortType == null)
      _initPgVcbEcomSoapServiceProxy();
    return pgVcbEcomSoapService_PortType;
  }

  public VcbNotifyRes notifyEcomVimo(String arg0) throws java.rmi.RemoteException{
    if (pgVcbEcomSoapService_PortType == null)
      _initPgVcbEcomSoapServiceProxy();
    return pgVcbEcomSoapService_PortType.notifyEcomVimo(arg0);
  }

  public VcbNotifyRes notifyEcomNganluong(String arg0) throws java.rmi.RemoteException{
    if (pgVcbEcomSoapService_PortType == null)
      _initPgVcbEcomSoapServiceProxy();
    return pgVcbEcomSoapService_PortType.notifyEcomNganluong(arg0);
  }
  
  
}