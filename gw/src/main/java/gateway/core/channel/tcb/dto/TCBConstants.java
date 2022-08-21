package gateway.core.channel.tcb.dto;

import com.google.common.collect.ImmutableMap;

/**
 * @author taind
 */
public class TCBConstants {

    public static final String CHANNEL_CODE = "TCB";
    public static final String SERVICE_NAME = "TCB TRANSFER";
    public static final String METHOD = "POST";

    public static final String PARTNER_ID = "NGLUONG";
    public static final String PARTNER_RQ_STR = "PARTNERID";
    public static final String CARD_TYPE = "DOMESTIC";
//    public static final String ACCOUNT_NUMBER_TO = "10210100296031";
//    public static final String ACCOUNT_NAME_TO = "KHACH HANG 10100296 SN1";

    public static final String USER_NAME = "srv_esb_ngluong";
    public static final String PASSWORD = "Tie#66!88nontt";

    public static String URL_WSDL_CA = "https://api-test.techcombank.com.vn:448/Partner2TCB/Payment/v3?wsdl";
    public static String URL_WSDL = "https://103.4.128.214:443/Partner2TCB/NGLUONG_payment_v3?wsdl";

    public static final String FUNCTION_LIST_BANK_INFO = "InqListBankInfo";
    public static final String FUNCTION_GET_TRANSACTION_STATUS = "InqTransactionStatus";
    public static final String FUNCTION_FUN_TRANSFER = "FundTransfer";
    public static final String UPDATE_TRANSACTION_STATUS = "UPDATE_TRANSACTION_STATUS";

    //    public static final String CERTIFICATE_KEY_01 = "tcb_key/transfer/certificate01.cer";
//    public static final String CERTIFICATE_KEY_02 = "tcb_key/transfer/certificate02.cer";
//    public static final String PUBLIC_KEY_01_PEM = "tcb_key/transfer/public_key01.pem";
//    public static final String PUBLIC_KEY_02_PEM = "tcb_key/transfer/public_key02.pem";
    public static final String PRIVATE_KEY_01_PEM = "tcb_key/transfer/quanda.pem";
    public static final String PRIVATE_KEY_02_PEM = "tcb_key/transfer/huongntt.pem";
    public static final String PUBLIC_KEY_MAHOA_PEM = "tcb_key/transfer/tcb_mahoa.cer";
    public static final String PUBLIC_KEY_DECRYPT_UPDATESTS = "tcb_key/transfer/tcb_pubic.cer";

//    public static final String CERTIFICATE_KEY_CCA = "tcb_key/transfer/certificate_cca.cer";
//    public static final String PRIVATE_KEY_CCA = "tcb_key/transfer/private_cca.pem";
//    public static final String PUBLIC_KEY_CCA = "tcb_key/transfer/public_cca.pem";

    //CCA
    public static final String CLIENT_KEYSTORE_TYPE = "PKCS12";
    public static final String CLIENT_KEYSTORE_PATH = "tcb_key/transfer/NL-CCA-Key.p12";
    public static final String CLIENT_KEYSTORE_PASS = "0U4cc897QLn5";
    //SFTP
    public static final String SFTP_HOST = "103.4.128.152";
    public static final Integer SFTP_POST = 1024;
    public static final String SFTP_ENCRYPT_FILE_PATH = "/data/doisoat/tcb/encrypt/";
    public static final String SFTP_LOCAL_PATH = "/data/doisoat/tcb/";
    public static final String SFTP_USER = "ngluong";

    static final ImmutableMap<String, String> ERROR_MESSAGE = ImmutableMap.<String, String>builder()
            .put("000", "Success")
            .put("001", "Other error")
            .put("SOA-001", "Configuration file is not correct")
            .put("SOA-002", "Validate action fail")
            .put("SOA-003", "Connection to backend timeout")
            .put("SOA-004", "Cannot connect to backend")
            .put("SOA-007", "Can not find the signature configuration file")
            .put("SOA-008", "Verify signature fail")
            .put("SOA-009", "Signing fail")
            .put("SOA-010", "Can not encrypt the message")
            .put("SOA-011", "Can not decrypt the message")
            .put("PA-004", "Beneficiary bank is inactive")
            .put("PA-005", "Beneficiary bank not found")
            .put("PA-008", "Invalid character in beneficiary name")
            .put("PA-009", "Invalid character in payment description")
            .put("PA-013", "Invalid character in sender name")
            .put("PA-017", "Duplicate transaction")
            .build();

    public static String getErrorMessage(String errorCode) {
        if (ERROR_MESSAGE.containsKey(errorCode)) {
            return ERROR_MESSAGE.get(errorCode);
        } else {
            return "Lỗi không xác định";
        }
    }
}
