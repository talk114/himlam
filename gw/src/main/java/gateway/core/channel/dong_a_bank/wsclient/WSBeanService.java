package gateway.core.channel.dong_a_bank.wsclient;

public interface WSBeanService extends javax.xml.rpc.Service {
    public java.lang.String getWSBeanAddress();

    public gateway.core.channel.dong_a_bank.wsclient.WSBean getWSBean() throws javax.xml.rpc.ServiceException;

    public gateway.core.channel.dong_a_bank.wsclient.WSBean getWSBean(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
