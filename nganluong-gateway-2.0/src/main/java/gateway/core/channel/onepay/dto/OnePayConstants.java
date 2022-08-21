package gateway.core.channel.onepay.dto;

import java.util.HashMap;
import java.util.Map;

public class OnePayConstants {

	public static final String CHANNEL_CODE = "ONEPAY";

	public static final String SERVICE_NAME = "OnePay";

	public static final String FUNCTION_CODE_VERIFY_CARD = "VerifyCard";
	public static final String FUNCTION_CODE_VERIFY_AUTHEN = "VerifyAuthen";
	public static final String FUNCTION_CODE_QUERY_ORDER = "Query";
	public static final String FUNCTION_CODE_REFUND = "refund";

	public static final String API_RESPONSE_STATUS_CODE_SUCCESS = "0";
	public static final String[] API_RESPONSE_STATUS_CODE_PENDING = {"300", "100"};

	public static final String VERIFY_CARD_SUFFIX = "https://secure.onepay.vn/onecomm-pay/verifycardapi.op";
	public static final String VERIFY_AUTHEN_SUFFIX = "https://secure.onepay.vn/onecomm-pay/verifyauthapi.op";
	public static final String QUERY_ORDER_SUFFIX = "https://onepay.vn/onecomm-pay/Vpcdps.op";
	public static final String REFUND_SUFFIX = "https://onepay.vn/onecomm-pay/refund.op";


//	public static final String URL_QUERY = "https://onepay.vn/onecomm-pay/Vpcdps.op";
//	public static final String URL_VERIFY_CARD = "https://onepay.vn/onecomm-pay/Vpcdps.op";
//	public static final String URL_VERIFY_AUTHEN = "https://onepay.vn/onecomm-pay/Vpcdps.op";
//	public static final String URL_REFUND = "https://onepay.vn/onecomm-pay/Vpcdps.op";
//	//TEST:
//	public static final String BASE_URL = "https://mtf.onepay.vn/onecomm-pay";
//	public static final String MERCHANT_ID = "ONEPAY";
//	public static final String ACCESS_CODE = "D67342C2";
//	public static final String SECURE_SECRET = "A3EFDFABA8653DF2342E8DAC29B51AF0";
//	public static final String USER = "op01";
//	public static final String PASSWORD = "op123456";
	//LIVE
// 	public static final String BASE_URL = "https://secure.onepay.vn/onecomm-pay";
	public static final String MERCHANT_ID = "NGANLUONG";
	public static final String ACCESS_CODE = "VVX3BUGZ";
	// public static final String SECURE_SECRET = "A4FA64B826425E5408A6C12C969EC5AD";
	// public static final String USER = "op01";
	// public static final String PASSWORD = "op123456";

	
	public static final String TPBank = "3";
	public static final String SHB = "12";
	public static final String OCEANBank = "18";

	public static Map<String, BankInfoObject> map = null;
	
	static{
		map = new HashMap<String, BankInfoObject>();
		BankInfoObject tpb = new BankInfoObject();
		tpb.setAuthMethod("IB");
		map.put(TPBank, tpb);
		
		BankInfoObject shb = new BankInfoObject();
		shb.setCardName("002");
		map.put(SHB, shb);
		
		BankInfoObject ocean = new BankInfoObject();
		ocean.setCardType("ATM");
		ocean.setAuthMethod("SMS");
		map.put(OCEANBank, ocean);
		
	}
	
}
