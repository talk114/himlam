package gateway.core.channel.viettelpay;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.viettelpay.dto.req.BaseRequest;
import gateway.core.channel.viettelpay.dto.req.CheckOrderQrcodeReq;
import gateway.core.channel.viettelpay.dto.req.PayOrderQrcodeReq;
import gateway.core.channel.viettelpay.dto.req.QueryQrTransReq;
import gateway.core.channel.viettelpay.dto.res.BaseResponse;
import gateway.core.channel.viettelpay.dto.res.CheckOrderQrcodeRes;
import gateway.core.channel.viettelpay.dto.res.PayOrderQrcodeRes;
import gateway.core.dto.PGResponse;
import gateway.core.util.HttpUtil;
import org.json.JSONObject;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.entities.PaymentChannel;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

public class ViettelPay extends PaymentGate {

	static final String FUNC_QUERY = "query";
	static final String FUNC_PAYMENT = "payment";

//	{
//		"PRIORITY":"BankPlus"
//		"VERSION":"Version", -- version hiện tại là 1.0
//		"TYPE":" PAY_BILL ",
//		"BILLCODE":"TH3232323", -- Mã hóa đơn,
//		"ORDER_ID":"TH3232323", -- Mã giao dịch tại đối tác (option)
//		"AMOUNT":"20000",Số tiền cần thanh toán (option)
//		"MERCHANT_CODE":"VIMOQR"
//		}
	
	/*
	 * VTT call check order qr merchant
	 */
	protected String CheckOrderQrcode(PaymentAccount paymentAccount, String inputStr)
			throws JsonParseException, JsonMappingException, IOException, IllegalAccessException,
			InvocationTargetException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {

		CheckOrderQrcodeReq req = objectMapper.readValue(VTTPayUtil.convertFormData2Json(inputStr), CheckOrderQrcodeReq.class);
		WriteInfoLog("2. VTT CheckOrder Req", objectMapper.writeValueAsString(req));

		CheckOrderQrcodeRes res = null;
		if (!validateCheckSum(paymentAccount, req)) {
			WriteErrorLog("Invalid checksum");
			res = new CheckOrderQrcodeRes();
			pGBeanUtils.copyProperties(res, req);
			res.setErrorCode("02");
		} else {
			// call merchant
			req.setCheckSum(null);
			String merchantRes = callMerchant(paymentAccount, objectMapper.writeValueAsString(req), FUNC_QUERY);
			// parse response
			res = objectMapper.readValue(merchantRes, CheckOrderQrcodeRes.class);
		}

		res.setCheckSum(ViettelPaySecurity.hmacSha1(res.rawData(paymentAccount.getAuthKey()),
				paymentAccount.getEncryptKey()));
		return buildResponseToVTT(res);
	}

	/*
	 * VTT notify result qr trans to Merchant
	 */
	protected String PayOrderQrcode(PaymentAccount paymentAccount, String inputStr)
			throws NoSuchAlgorithmException, IllegalAccessException, InvocationTargetException, JsonParseException,
			JsonMappingException, IOException, InvalidKeyException, SignatureException {

		PayOrderQrcodeReq req = objectMapper.readValue(VTTPayUtil.convertFormData2Json(inputStr), PayOrderQrcodeReq.class);
		// TODO
		WriteInfoLog("2. VTT PayOrder Req", objectMapper.writeValueAsString(req));
		
		PayOrderQrcodeRes res = null;
		if (!validateCheckSum(paymentAccount, req)) {
			WriteErrorLog("Invalid checksum");
			res = new PayOrderQrcodeRes();
			pGBeanUtils.copyProperties(res, req);
			res.setErrorCode("02");
		} else {
			// call merchant
			req.setCheckSum(null);
			String merchantRes = callMerchant(paymentAccount, objectMapper.writeValueAsString(req), FUNC_PAYMENT);
			// parse response
			res = objectMapper.readValue(merchantRes, PayOrderQrcodeRes.class);
		}

		res.setCheckSum(ViettelPaySecurity.hmacSha1(res.rawData(paymentAccount.getAuthKey()),
				paymentAccount.getEncryptKey()));
		return buildResponseToVTT(res);
	}
	
	/*
	 * Merchant query qr trans
	 */
	protected String QrQueryTrans(PaymentChannel paymentChannel, String inputStr) throws JsonProcessingException, IOException{
		WriteInfoLog("2. Merchat Query QrVTT Trans", inputStr);
		QueryQrTransReq req = objectMapper.readValue(inputStr, QueryQrTransReq.class);
		
		String vttRes = callViettelPay(paymentChannel, objectMapper.writeValueAsString(req));
//		QueryQrTransRes res = objectMapper.readValue(vttRes, QueryQrTransRes.class);
		WriteInfoLog("3. VTT Response", vttRes);
		
		return buildResponseToMerchant(vttRes);
	}
	
	/*
	 * ######################################################################
	 * ######################################################################
	 */
	private boolean validateCheckSum(PaymentAccount paymentAccount, BaseRequest req)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		String checkSum = ViettelPaySecurity.hmacSha1(req.rawData(paymentAccount.getAuthKey()),
				paymentAccount.getEncryptKey());
		return checkSum.equalsIgnoreCase(req.getCheckSum());
	}

	private String callMerchant(PaymentAccount paymentAccount, String req, String func) throws IOException {
		System.out.println("Call Merchant: " + req);
		Map<String, Object> map = new HashMap<>();
		map.put("func", func);
		map.put("params", req);
		String result = HttpUtil.send(paymentAccount.getUrlApi(), map, null);
		// TODO
		WriteInfoLog("3. Merchant Response", result);
		return result;
	}

	private String buildResponseToVTT(BaseResponse res) throws JsonProcessingException {
		String resVTT = objectMapper.writeValueAsString(res);
		// TODO
		WriteInfoLog("3. Response To ViettelPay", resVTT);
		return resVTT;
	}
	
	private String callViettelPay(PaymentChannel paymentChannel, String req) throws IOException{
		// parse object to map
		JSONObject json = new JSONObject(req);
		Map<String, Object> map = json.toMap();

		// TODO
		String result = HttpUtil.send(paymentChannel.getUrl(), map, null);
		WriteInfoLog("2. ViettelPay Response", result);
		return result;
	}
	
	private String buildResponseToMerchant(String res) throws JsonProcessingException {
		PGResponse pgResponse = new PGResponse();
		pgResponse.setStatus(true);
		pgResponse.setData(res);
		return objectMapper.writeValueAsString(pgResponse);
	}

	

}
