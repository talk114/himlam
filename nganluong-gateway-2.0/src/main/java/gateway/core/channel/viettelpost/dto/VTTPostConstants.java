package gateway.core.channel.viettelpost.dto;

import com.google.common.collect.ImmutableMap;

public class VTTPostConstants {

	public static String ENCRYPT_KEY = "ad7d6b1d136c66f1424c50365a9f62d1";//NganLuongViettelPost@2019 - md5
	public static String AUTH_KEY = "b6c8b33fb4053b6a6732449d2987b6ee";//NganLuong@2019 - md5
	public static String URL_API = "https://www.nganluong.vn/api/viettelPost";


	public static final String TRANS_SUCCESS = "00";
	public static final String ACCESS_DENIED = "01";
	public static final String CHECKSUM_INVALID = "02";
	public static final String DATA_CANNOT_DECRYPT = "03";
	public static final String ACCOUNT_NOT_EXIST = "04";
	public static final String TRANS_NOT_EXIST = "05";
	public static final String TRANS_EXCEED_LIMIT = "06";
	public static final String TRANS_FAIL = "07";
	public static final String AMOUNT_NOT_ENOUGH = "08";
	public static final String TRANS_HAS_EXPIRED = "09";
	public static final String TRANS_HAS_PAYED = "10";
	public static final String PAYMENT_ID_INVALID = "11";
	public static final String SYSTEM_ERROR = "99";
	
	static final ImmutableMap<String, String> ERROR_MESSAGE = ImmutableMap.<String, String>builder()
			.put(TRANS_SUCCESS, "Giao dich thanh cong")
			.put(ACCESS_DENIED, "Truy cap bi tu choi")
			.put(CHECKSUM_INVALID, "Checksum sai")
			.put(DATA_CANNOT_DECRYPT, "Khong giai ma duoc du lieu")
			.put(ACCOUNT_NOT_EXIST, "Tai khoan khong ton tai")
			.put(TRANS_NOT_EXIST, "Giao dich khong ton tai")
			.put(TRANS_EXCEED_LIMIT, "Qua han muc giao dich")
			.put(TRANS_FAIL, "Giao dich loi")
			.put(AMOUNT_NOT_ENOUGH, "So du vi khong du")
			.put(TRANS_HAS_EXPIRED, "Qua thoi han xu ly")
			.put(TRANS_HAS_PAYED, "Giao dich da duoc thanh toan")
			.put(PAYMENT_ID_INVALID, "PaymentId sai định dạng. Prefix PS1 - Vimo, PS2 - Ngân lượng")
			.put(SYSTEM_ERROR, "Loi he thong")
			.build();
	
	public static String getErrorMessge(String errorCode){
		if(ERROR_MESSAGE.containsKey(errorCode))
			return ERROR_MESSAGE.get(errorCode);
		else
			return "Loi khong xac dinh";
	}
}
