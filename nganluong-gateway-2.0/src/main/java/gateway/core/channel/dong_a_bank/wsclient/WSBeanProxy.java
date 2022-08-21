package gateway.core.channel.dong_a_bank.wsclient;

public class WSBeanProxy implements WSBean {

    private String _endpoint = null;
    private gateway.core.channel.dong_a_bank.wsclient.WSBean wSBean = null;

    public WSBeanProxy() {
        _initWSBeanProxy();
    }

    public WSBeanProxy(String endpoint) {
        _endpoint = endpoint;
        _initWSBeanProxy();
    }

    private void _initWSBeanProxy() {
        try {
            wSBean = (new gateway.core.channel.dong_a_bank.wsclient.WSBeanServiceLocator()).getWSBean();
            if (wSBean != null) {
                if (_endpoint != null)
                    ((javax.xml.rpc.Stub) wSBean)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
                else
                    _endpoint = (String) ((javax.xml.rpc.Stub) wSBean)._getProperty("javax.xml.rpc.service.endpoint.address");
            }

        } catch (javax.xml.rpc.ServiceException serviceException) {
        }
    }

    public String getEndpoint() {
        return _endpoint;
    }

    public void setEndpoint(String endpoint) {
        _endpoint = endpoint;
        if (wSBean != null)
            ((javax.xml.rpc.Stub) wSBean)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);

    }

    public gateway.core.channel.dong_a_bank.wsclient.WSBean getWSBean() {
        if (wSBean == null)
            _initWSBeanProxy();
        return wSBean;
    }

    public java.lang.String getString(java.lang.String str) throws java.rmi.RemoteException {
        if (wSBean == null)
            _initWSBeanProxy();
        return wSBean.getString(str);
    }

    public java.lang.Object[] checkMethod(java.lang.String accesskey, java.lang.String methodname, int servicetype, java.lang.Object[] arrObjParas) throws java.rmi.RemoteException {
        if (wSBean == null)
            _initWSBeanProxy();
        return wSBean.checkMethod(accesskey, methodname, servicetype, arrObjParas);
    }

    public boolean checkPartner(java.lang.String accesskey) throws java.rmi.RemoteException {
        if (wSBean == null)
            _initWSBeanProxy();
        return wSBean.checkPartner(accesskey);
    }

    public java.lang.Object[] callExecution(java.lang.String methodname, int servicetype, java.lang.Object[] arrObjParas) throws java.rmi.RemoteException {
        if (wSBean == null)
            _initWSBeanProxy();
        return wSBean.callExecution(methodname, servicetype, arrObjParas);
    }
}
