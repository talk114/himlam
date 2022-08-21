/**
 * Payment_Service_Card_V3Soap.java
 * <p>
 * This file was auto-generated from WSDL by the Apache Axis 1.4 Apr 22, 2006
 * (06:55:48 PDT) WSDL2Java emitter.
 */
package gateway.core.channel.vcb_ib.ws_client;

public interface Payment_Service_Card_V3Soap extends java.rmi.Remote {

    String verifyPayment(String requestString) throws java.rmi.RemoteException;

    String query(String requestString) throws java.rmi.RemoteException;

    String refund(String requestString) throws java.rmi.RemoteException;
}
