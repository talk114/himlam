package gateway.core.channel.viettelpay;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import vn.nganluong.naba.entities.PaymentAccount;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class ViettelPayVM extends ViettelPay {

	public String CheckOrderQrcode(PaymentAccount paymentAccount, String inputStr) throws InvalidKeyException, IllegalAccessException, InvocationTargetException, NoSuchAlgorithmException, SignatureException, IOException{
		return super.CheckOrderQrcode(paymentAccount, inputStr);
	}
	
	public String PayOrderQrcode(PaymentAccount paymentAccount, String inputStr) throws InvalidKeyException, NoSuchAlgorithmException, IllegalAccessException, InvocationTargetException, SignatureException, IOException{
		return super.PayOrderQrcode(paymentAccount, inputStr);
	}
}
