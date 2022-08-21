/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.core.channel.tcb.service.impl;

import org.apache.http.conn.ssl.TrustStrategy;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
/**
 *
 * @author taind
 */
public class TrustAllStrategy implements TrustStrategy {
    
    @Override
    public boolean isTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        return true;
    }
}
