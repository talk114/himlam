package gateway.core.channel.viettelpay.service.impl;

import gateway.core.channel.viettelpay.ViettelPay;
import gateway.core.channel.viettelpay.service.ViettelPayNglService;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.entities.PaymentAccount;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

@Service
public class ViettelPayNglServiceImpl extends ViettelPay implements ViettelPayNglService {
    public String CheckOrderQrcode(PaymentAccount paymentAccount, String inputStr) throws InvalidKeyException, IllegalAccessException, InvocationTargetException, NoSuchAlgorithmException, SignatureException, IOException {
        return super.CheckOrderQrcode(paymentAccount, inputStr);
    }

    public String PayOrderQrcode(PaymentAccount paymentAccount, String inputStr) throws InvalidKeyException, NoSuchAlgorithmException, IllegalAccessException, InvocationTargetException, SignatureException, IOException {
        return super.PayOrderQrcode(paymentAccount, inputStr);
    }
}
