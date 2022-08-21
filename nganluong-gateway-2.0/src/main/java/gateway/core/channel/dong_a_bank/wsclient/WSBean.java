package gateway.core.channel.dong_a_bank.wsclient;

public interface WSBean extends java.rmi.Remote {

    public java.lang.String getString(java.lang.String str) throws java.rmi.RemoteException;
    public java.lang.Object[] checkMethod(java.lang.String accesskey, java.lang.String methodname, int servicetype, java.lang.Object[] arrObjParas) throws java.rmi.RemoteException;
    public boolean checkPartner(java.lang.String accesskey) throws java.rmi.RemoteException;
    public java.lang.Object[] callExecution(java.lang.String methodname, int servicetype, java.lang.Object[] arrObjParas) throws java.rmi.RemoteException;
}
