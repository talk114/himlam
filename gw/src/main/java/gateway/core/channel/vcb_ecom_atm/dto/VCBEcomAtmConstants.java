package gateway.core.channel.vcb_ecom_atm.dto;

import com.google.common.collect.ImmutableMap;

public class VCBEcomAtmConstants {

	public static final String VCB_ECOM_PARTNER_ID = "NLVN_CARD";
	public static final String VCB_ECOM_PASSWORD = "NLVN#be4S9w1R6ep1belbyfIA8M0i&5zQHONGyS3";
	public static final String VCB_ECOM_MERCHANT_ID = "NLVN_CARD";
	public static final String VCB_ECOM_MID = "NLVN_CARD";

	// PATH KEY TEST:
	public static final String GW_PATH_PRIVATE_KEY = "vcb_ecom_key_live/private_key.xml";
	public static final String GW_PATH_PUBLIC_KEY = "vcb_ecom_key_live/public_key.xml";
	public static final String VCB_ECOM_PATH_PUBLIC_KEY = "vcb_ecom_key_live//public_key_VCB.xml";

	// URL call VCB TEST
//	public static final String URL_API_CALL_VCB = "http://192.168.200.87:9091/EcomV3/Payment_Service_Card_V3.asmx";
	//Live
	public static final String URL_API_CALL_VCB = "http://192.168.200.115/EcommerceV1/Payment_Service_Card_V3.asmx";


	private static final String TRANS_SUCCESS =  "1";
	private static final String MISSING_PARAM = "2";
	private static final String WRONG_TRANS_ID = "3";
	private static final String REFUND_AMOUNT_INVALID = "5";
	private static final String REFUND_CURR_INVALID = "6";
	private static final String ORIG_TRANS_FAIL_OR_REFUNED = "7";
	private static final String REFUND_ERROR = "8";
	private static final String EXCEPTION = "09";
	private static final String WRONG_PARTNER_INFO = "11";

	private static final String WRONG_AMOUNT = "12";
	private static final String AMOUNT_NOT_IN_RANGE_VALID = "13";
	private static final String EMPRY_SIGN_PUBKEY = "14";
	private static final String CANNOT_FIND_TRANSID = "15";
	private static final String TRANS_FAIL = "16";
	private static final String NOT_AVAILABLE_BALANCE = "17";
	private static final String ACC_STATUS_INVALID = "18";
	private static final String ACC_CANNOT_DETERMINE = "19";
	private static final String REQUEST_MESSAGE_ERROR = "20";
	private static final String INIT_TRANS_ERROR = "21";
	private static final String BRANCH_CANNOT_DETERMINE = "22";
	private static final String VERIFY_OTP_FAIL = "23";
	private static final String TRANS_DATE_NOT_DETERMINE = "24";
	private static final String TRANS_PAID_BEFORE = "25";
	private static final String MOBILE_SMS_CANNOT_DETERMINE = "26";
	private static final String CURRENCY_ACC_CANNOT_PAY = "27";
	private static final String TRANS_NOT_PAY = "28";
	private static final String PAY_FAIL = "31";
	private static final String EXCEPTION_2 = "32";
	private static final String WRONG_TRANS_ID_2 = "33";
	private static final String TRANS_VALID = "40";
	private static final String SYSTEM_MAINTAIN = "50";
	private static final String CARD_LOCKED_OR_INVALID = "61";
	private static final String CARD_INFO_WRONG = "62";
	private static final String ACCEPT_VCB_CONNECT_24_ONLY = "63";
	private static final String DONT_HAVE_IB_ACC = "64";
	private static final String WRONG_MERCHANT_ID = "65";
	private static final String WRONG_VALID_AMOUNT_PAYMENT = "66";
	private static final String WRONG_CURRENCY = "67";
	private static final String DECRYPT_ERROR = "68";
	private static final String VERIFY_SIGNATURE_ERROR = "69";
	private static final String WRONG_SIGNATURE = "70";
	private static final String VERIFY_SIGNATURE_EXCEPTION = "98";
	private static final String UNKNOWN_ERROR = "99";
	private static final String INVALID_OTP = "73";
	private static final String SIGNUP_SMS = "71";
	private static final String CANNOT_SEND_OTP = "72";

	public static final String CHANNEL_CODE = "VCB_ECOM";
	public static final String FUNCTION_CODE_VERIFY_CARD = "VerifyCard";
	public static final String FUNCTION_CODE_VERIFY_OTP = "VerifyOtp";
	public static final String FUNCTION_CODE_QUERY = "Query";
	public static final String FUNCTION_CODE_REFUND = "Refund";
	public static final String FUNCTION_CODE_VERIFY_CARD_2 = "VerifyCard2";
	public static final String FUNCTION_CODE_VERIFY_OTP_2 = "VerifyOtp2";
	
	static final ImmutableMap<String, String> MAP_ERROR = ImmutableMap.<String, String>builder()
			.put(TRANS_SUCCESS, "Giao d???ch th??nh c??ng.")
			.put(MISSING_PARAM, "Tham s??? kh??ng h???p l???.")
			.put(WRONG_TRANS_ID, "Sai m?? giao d???ch.")
			.put(REFUND_AMOUNT_INVALID, "S??? ti???n ho??n l???n h??n s??? ti???n thanh to??n.")
			.put(REFUND_CURR_INVALID, "????n v??? ti???n ho??n kh??c ????n v??? ti???n thanh to??n.")
			.put(ORIG_TRANS_FAIL_OR_REFUNED, "Giao d???ch g???c th???t b???i ho???c ???? ???????c ho??n.")
			.put(REFUND_ERROR, "Ho??n ti???n th???t b???i.")
			
			.put(EXCEPTION, "L???i h??? th???ng.")
			.put(WRONG_PARTNER_INFO, "Sai th??ng tin ?????i t??c.")
			.put(WRONG_AMOUNT, "S??? ti???n kh??ng h???p l???.")
			.put(AMOUNT_NOT_IN_RANGE_VALID, "S??? ti???n giao d???ch kh??ng n???m trong kho???ng quy ?????nh.")
			.put(EMPRY_SIGN_PUBKEY, "L???i h??? th???ng.")
			.put(CANNOT_FIND_TRANSID, "Kh??ng t??m th???y giao d???ch.")
			
			.put(TRANS_FAIL, "Giao d???ch th???t b???i.")
			.put(NOT_AVAILABLE_BALANCE, "S??? d?? t??i kho???n kh??ng ?????.")
			.put(ACC_STATUS_INVALID, "Tr???ng th??i t??i kho???n kh??ng h???p l???.")
			.put(ACC_CANNOT_DETERMINE, "Kh??ng x??c ?????nh ???????c s??? t??i kho???n.")
			.put(REQUEST_MESSAGE_ERROR, "Giao d???ch th???t b???i.")
			.put(INIT_TRANS_ERROR, "Giao d???ch th???t b???i.")
			
			.put(BRANCH_CANNOT_DETERMINE, "Kh??ng x??c ?????nh ???????c chi nh??nh ???ng v???i TK.")
			.put(VERIFY_OTP_FAIL, "X??c th???c OTP th???t b???i.")
			.put(TRANS_DATE_NOT_DETERMINE, "Ng??y giao d???ch kh??ng x??c ?????nh.")
			.put(TRANS_PAID_BEFORE, "Giao d???ch ???????c ???????c thanh to??n tr?????c ????.")

			.put(MOBILE_SMS_CANNOT_DETERMINE, "Kh??ng x??c ?????nh ???????c s??? ??i???n tho???i m???c ?????nh nh???n OTP.")
			.put(CURRENCY_ACC_CANNOT_PAY, "Lo???i ngo???i t??? ???ng v???i t??i kho???n kh??ng ???????c ch???p nh???n thanh to??n.")
			.put(TRANS_NOT_PAY, "Giao d???ch ch??a ???????c thanh to??n.")
			.put(PAY_FAIL, "Thanh to??n kh??ng th??nh c??ng.")
			.put(EXCEPTION_2, "L???i h??? th???ng.")
			.put(WRONG_TRANS_ID_2, "M?? giao d???ch kh??ng ch??nh x??c.")
			.put(TRANS_VALID, "Giao d???ch h???p l???.")
			
			.put(SYSTEM_MAINTAIN, "H??? th???ng VCB ??ang b???o tr?? d???ch v???.")
			
			.put(CARD_LOCKED_OR_INVALID, "Th??? ??ang b??? kh??a ho???c tr???ng th??i th??? kh??ng h???p l???.")
			.put(CARD_INFO_WRONG, "Th??ng tin th??? kh??ng ????ng.")
			.put(ACCEPT_VCB_CONNECT_24_ONLY, "Ch??? ch???p nh???n th??? Vietcombank Connect24.")
			.put(DONT_HAVE_IB_ACC, "T??i kho???n ch??a ????ng k?? Internet Banking,")
			.put(WRONG_MERCHANT_ID, "Sai m?? merchant.")

			.put(WRONG_VALID_AMOUNT_PAYMENT, "S??? ti???n thanh to??n kh??ng h???p l???.")
			
			.put(WRONG_CURRENCY, "????n v??? ti???n t??? kh??ng ch??nh x??c.")
			.put(DECRYPT_ERROR, "L???i gi???i m??.")
			.put(VERIFY_SIGNATURE_ERROR, "L???i x??c th???c ch??? k?? s???.")
			.put(WRONG_SIGNATURE, "Ch??? k?? s??? sai.")
			.put(VERIFY_SIGNATURE_EXCEPTION, "L???i x??c th???c ch??? k?? s???.")
			.put(UNKNOWN_ERROR, "L???i kh??ng x??c ?????nh.")
			.put(INVALID_OTP, "OTP kh??ng ch??nh x??c.")
			.put(SIGNUP_SMS, "Ch??a ????ng k?? SMS")
			.put(CANNOT_SEND_OTP, "Kh??ng th??? l???y, g???i OTP.")
			.build();
	
	public static String getResMsg(String errorCode){
		if(MAP_ERROR.containsKey(errorCode))
			return errorCode + ": " + MAP_ERROR.get(errorCode);
		else
			return "Giao d???ch kh??ng th??nh c??ng. Vui l??ng li??n h??? ng??n h??ng ????? bi???t th??m chi ti???t.";
	}
}
