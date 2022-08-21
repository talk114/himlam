package gateway.core.channel.mb_qrcode.service;

import gateway.core.dto.PGResponse;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public interface MBBankQRCodeOffusService {
    PGResponse createQRCode(String request) throws IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException, KeyStoreException, UnrecoverableKeyException;
    PGResponse checkOrder(String request) throws IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException, KeyStoreException, UnrecoverableKeyException;
    String mbBankCallBack(String request) throws IOException, NoSuchAlgorithmException;
    PGResponse createFileDS(String dateDS) throws Exception;
    PGResponse buildFileDS(String data) throws Exception;
}
