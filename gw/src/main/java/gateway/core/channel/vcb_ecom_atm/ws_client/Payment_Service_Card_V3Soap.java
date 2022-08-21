/**
 * Payment_Service_Card_V3Soap.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gateway.core.channel.vcb_ecom_atm.ws_client;

public interface Payment_Service_Card_V3Soap extends java.rmi.Remote {
    String verifyCardAndSendOTP(String requestString) throws java.rmi.RemoteException;

    String verifyOTPAndPayment(String requestString) throws java.rmi.RemoteException;

    String query(String requestString) throws java.rmi.RemoteException;

    String refundOffline(String requestString) throws java.rmi.RemoteException;
}
