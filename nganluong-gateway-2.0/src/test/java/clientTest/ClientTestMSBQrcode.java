
package clientTest;

import gateway.core.channel.msb_qr.dto.MsbConstants;
import gateway.core.dto.request.DataRequest;
import gateway.core.util.RandomUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientTestMSBQrcode extends BaseTest {

	static {
		CHANNEL_NAME = "MSB_QRCODE";
		USER_CODE = "NL";
		// USER_CODE = "NGANLUONG_QRMSB";
		MD5 = "04392bb24a98c047dce89046f08ad188";
		USER_AUTH = "NL@2017";
		PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";

		// CHANNEL_NAME = "MSB_QR_NGL";
		// USER_CODE = "NGANLUONG_QRMSB";
		// MD5 = "f1f71ac0923752297c8c83834e0081d2";
		// USER_AUTH = "NglMSBQr@2019";
		// PASS_AUTH = "f69020c7a580d3c469a8b2b666f73b4b";

//		 CHANNEL_NAME = "MSB_QR_MPOS";
//		 USER_CODE = "MPOS_MSBQR";
//		 MD5 = "a3c6d5e51ed5a5644c7a1b81d188615c";
//		 USER_AUTH = "MposMsbQr@2019";
//		 PASS_AUTH = "bde4d9f0933d1a9bb6128799828b8c11";
	}

	public static void main(String[] args) throws Exception {
		String func = "";
		DataRequest param = null;

		func = "QrCodePayment";
		// TEST QR DYNAMIC
//		param = paramCreateQrCode();

		// TEST QR STATIC
//		param = paramCreateQrCodeStatic();

//		 func = "CheckQrOrder";
//		 param = paramQueryOrder();
//
//		callApi(mapper.writeValueAsString(param), func, 0);

//		 resetChannel("MSB_QR_NGL", 1);

		String partnerFnc = "";
		String req = "";
		// partnerFnc = "msbCompleteVMQrPayment";
		partnerFnc = "msbCompleteNLQrPayment";
		req = "{\"code\":\"00\",\"message\":\"System Ok\",\"msgType\":\"1\",\"txnId\":\"03190706243659\",\"qrTrace\":\"000442069\",\"bankCode\":\"VNPAY\",\"mobile\":\"0357606998\",\"accountNo\":\"\",\"amount\":\"669300\",\"payDate\":\"20190706100212\",\"masterMerCode\":\"970426\",\"merchantCode\":\"0106393463\",\"terminalId\":\"00026\",\"addData\":[{\"merchantType\":\"8358\",\"serviceCode\":\"06\",\"masterMerCode\":\"970426\",\"merchantCode\":\"0106393463\",\"terminalId\":\"00026\",\"productId\":\"\",\"amount\":\"669300\",\"tipAndFee\":\"\",\"ccy\":\"704\",\"qty\":\"1\",\"note\":\"\"}],\"ccy\":\"704\"}";
		callApiPartner(req, partnerFnc, 0);

	}

	private static DataRequest paramCreateQrCode() {
		DataRequest param = new DataRequest();
		param.setTerminalId("PS011013");//NL2019 // vimo2018 NL2019 // live Ngl:
		param.setPayMethod(MsbConstants.QR_TYPE_ORDER_OFFLINE);
		param.setFeeAmount(0d);
//		param.setTransId(RandomUtil.randomDigitString(12, "NL"));
		param.setAmount(100000d);
		param.setDescription("Mpos");
		param.setBillNo(RandomUtil.randomDigitString(6, "NL"));
//		param.setProductId(RandomUtil.randomDigitString(6, "PRD-NL"));
		param.setExpireDate(getExDate());
//		param.setExpireDate("2009272125");
		return param;
	}

	private static DataRequest paramCreateQrCodeStatic() {
		DataRequest param = new DataRequest();
		param.setTerminalId("PS011013");//NL2019 // vimo2018 NL2019 // live Ngl:
		param.setPayMethod(MsbConstants.QR_TYPE_PRODUCT);
		param.setFeeAmount(0d);
//		param.setTransId(RandomUtil.randomDigitString(12, "NL"));
		param.setAmount(100000d);
		param.setDescription("Mpos");
		param.setProductId(RandomUtil.randomDigitString(6, "PRD-NL"));
		param.setExpireDate(getExDate());
		return param;
	}

	private static DataRequest paramQueryOrder() {
		DataRequest param = new DataRequest();
		param.setTerminalId("PS011013");
		param.setPayMethod(MsbConstants.QR_TYPE_ORDER_OFFLINE);
		//param.setPayMethod(MsbConstants.QR_TYPE_BILLING);
//		param.setPayMethod(MsbConstants.QR_TYPE_PRODUCT);
		param.setBillNo("NL5066999");
		return param;
	}

	private static String getExDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmm");
		return df.format(new Date());
	}
}
