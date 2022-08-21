package gateway.core.channel.vccb.dto;

import com.google.common.collect.ImmutableMap;

public class VCCBConstants {
	// live 
	// public static final String URL_API_INQUIRY_IBFT = "https://sbi.vietcapitalbank.com.vn/payment/inquiry";
	public static final String URL_API_FUND_TRANSFER = "https://sbi.vietcapitalbank.com.vn/payment/fundTransfer";
	public static final String BASE_URL_API = "https://sbi.vietcapitalbank.com.vn/payment/inquiryEscrowAccount";
	public static final String URL_API_CHECK_BALANCE_TKDBTT_IBFT = "";
	public static final String URL_API_UPLOAD_RECONCILIATION = "https://sbi.vietcapitalbank.com.vn/payment/uploadReconciliation";

	//Key
	public static final String PATH_GATEWAY_PRIVATE_KEY = "vccb_key/private.pem";
	public static final String PATH_GATEWAY_PUBLIC_KEY = "vccb_key/public.pem";
	public static final String PATH_VCCB_PUBLIC_KEY = "vccb_key/VCCB_pub.pem";

	// test
//	public static final String URL_API_INQUIRY_IBFT = "http://203.205.28.29:9202/processor/payment/inquiry";
//	public static final String URL_API_FUND_TRANSFER = "http://203.205.28.29:9202/processor/payment/fundTransfer";
//	public static final String BASE_URL_API = "http://203.205.28.29:9202/processor/payment/inquiryEscrowAccount";
//	public static final String URL_API_CHECK_BALANCE_TKDBTT_IBFT = "";
//	public static final String URL_API_UPLOAD_RECONCILIATION = "http://203.205.28.29:9202/processor/payment/uploadReconciliation";
	public static final String API_RESPONSE_STATUS_CODE_SUCCESS = "1|1";
	public static final String API_RESPONSE_STATUS_CODE_PENDING = "0";

    	public static final ImmutableMap<String, String> ERROR_MESSAGE = ImmutableMap.<String, String>builder()
    			.put("0", "Hệ thông đang xử lý giao dịch")
    			.put("1", "Thành công")
    			.put("-1", "Lỗi không xác định")
    			.put("6", "Giao dịch cần thực hiện OTP")
    			.put("999", "Lỗi tại hệ thông Gateway")
    			.put("1000", "Lỗi tại hệ thông Gateway")
    			.put("1001", "Lỗi tại hệ thông Gateway")
    			.put("1002", "Lỗi tại hệ thông Gateway")
    			.put("1003", "Lỗi tại hệ thông Gateway")
    			.put("1004", "Lỗi tại hệ thông Gateway")
    			.put("1005", "Lỗi tại hệ thông Gateway")
    			.put("2001", "Request URL không hợp lệ")
    			.put("2002", "Thời gian giao dịch hết hạn")
    			.put("2003", "Tham số time trong request không hợp lệ")
    			.put("2004", "IP/Host TGTT không được chấp nhận")
    			.put("2006", "Trùng requestID")
    			.put("2008", "Chữ ký số không hợp lệ(Null/empty)")
    			.put("2009", "Sai chữ ký số")
    			.put("2010", "Giao dịch không hộ trợ")
    			.put("2012", "Mã giao dịch không hợp lệ (Null/empty)")
    			.put("2014", "Mã merchant không hợp lệ (Null/empty)")
    			.put("2016", "Request data không hợp lệ (Null/empty)")
    			.put("2017", "Request data sai format")
    			.put("2020", "Mã đối tác không tồn tại")
    			.put("2399", "Giao dịch gốc không thành công")
    			.put("78", "Thẻ liên kết tạm đóng/khóa/chưa kích hoạt")
    			.put("51", "Số dư không đủ để thực hiện thanh toán")
    			.put("14", "Thẻ liên kết không hợp lệ")
    			.put("26", "KH chưa đăng ký sử dụng dịch vụ NH Điện tử")
    			.put("29", "Số điện thoại KH sử dụng không trùng khớp với số điện thoại KH đăng ký sử dụng dịch vụ NH Điện tử")
    			.put("05", "Giao địch không thành công")
    			.put("2151","Số tài khoản không hợp lệ")
    			.put("2152", "Số tài khoản không hợp lệ (Null/Empty)")
    			.put("2153", "Mã ngân hàng không hợp lệ (Null/Empty)")
    			.put("2154", "Thông tin onus không hợp lệ (Null/Empty)")
        		.build();


	public static final String CHANNEL_CODE = "VCCB";
	public static final String CHANNEL_CODE_VA = "VCCB_VA";
	public static final String SERVICE_NAME_IBFT = "IBFT";

	public static final String FUNCTION_CODE_CHECK_BALANCE = "CheckBalance";
	public static final String FUNCTION_CODE_CHECK_CARD_VCCB = "CheckCardVCCB";
	public static final String FUNCTION_CODE_CHECK_BANK_ACC_VCCB = "CheckBankAccVCCB";
	public static final String FUNCTION_CODE_CHECK_BANK_ACC_IBFT = "CheckBankAccIBFT";
	public static final String FUNCTION_CODE_CHECK_CARD_IBFT = "CheckCardIBFT";

	public static final String FUNCTION_CODE_TRANSFER_CARD_VCCB = "TransferCardVCCB";

	public static final String FUNCTION_CODE_TRANSFER_CARD_IBFT = "TransferCardIBFT";
	public static final String FUNCTION_CODE_TRANSFER_BANK_ACC_IBFT = "TransferBankAccIBFT";
	public static final String FUNCTION_CODE_CHECK_BALANCE_BANK_ACC_IBFT = "CheckBalanceBankAccIBFT";
	public static final String FUNCTION_CODE_TRANSFER_BANK_ACC_VCCB = "TransferBankAccVCCB";

	public static final String FUNCTION_CODE_UPLOAD_RECONCILIATION_NGLA = "UploadReconciliationNGLA";
	public static final String FUNCTION_CODE_UPLOAD_RECONCILIATION_NGLB = "UploadReconciliationNGLB";




}
