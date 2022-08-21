package gateway.core.channel.viettelpay.service;

import vn.nganluong.naba.entities.PaymentAccount;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public interface ViettelPayNglService {
    String CheckOrderQrcode(PaymentAccount paymentAccount, String inputStr) throws InvalidKeyException, IllegalAccessException, InvocationTargetException, NoSuchAlgorithmException, SignatureException, IOException;
    String PayOrderQrcode(PaymentAccount paymentAccount, String inputStr) throws InvalidKeyException, NoSuchAlgorithmException, IllegalAccessException, InvocationTargetException, SignatureException, IOException;
}
