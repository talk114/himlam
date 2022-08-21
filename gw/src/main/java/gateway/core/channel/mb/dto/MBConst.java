package gateway.core.channel.mb.dto;

public class MBConst {

    public static final String CHANNEL_CODE = "MB";
    public static final int CHANNEL_ID = 2;
    public static final String SERVICE_NAME = "ECOM";

//	public static final String CHANNEL_FUNCTION_ACCESS_TOKEN_CODE = "MB_ECOM_ACCESS_TOKEN";
//	public static final String CHANNEL_FUNCTION_ADD_TRANSACTION = "ADD_TRANSACTION";
//	public static final String CHANNEL_FUNCTION_CONFIRM_TRANSACTION = "CONFIRM_TRANSACTION";
//	public static final String CHANNEL_FUNCTION_REVERT_TRANSACTION = "REVERT_TRANSACTION";
//	public static final String CHANNEL_FUNCTION_STATUS_TRANSACTION = "STATUS_TRANSACTION";
    public static final String CCY = "VND";
    public static final String FUNCTION_CODE_ACCESS_TOKEN = "MB_ECOM_ACCESS_TOKEN";
    public static final String FUNCTION_CODE_ADD_TRANSACTION = "MB_ECOM_ADD_TRANSACTION";
    public static final String FUNCTION_CODE_CONFIRM_TRANSACTION = "MB_ECOM_CONFIRM_TRANSACTION";
    public static final String FUNCTION_CODE_REVERT_TRANSACTION = "MB_ECOM_REVERT_TRANSACTION";
    public static final String FUNCTION_CODE_STATUS_TRANSACTION = "MB_ECOM_STATUS_TRANSACTION";
    public static final String FUNCTION_CODE_REFUND_TRANSACTION = "MB_ECOM_REFUND_TRANSACTION";


    public static final String FUNCTION_CODE_RECONCILIATION_DAY_STEP_1 = "MB_ECOM_RECONCILIATION_DAY";
    public static final String FUNCTION_CODE_RECONCILIATION_DAY_STEP_2 = "MB_ECOM_RECONCILIATION_DAY_STEP_2";
    public static final String FUNCTION_CODE_RECONCILIATION_MONTH = "MB_ECOM_RECONCILIATION_MONTH";

    public static final String CHANNEL_NOT_CONFIG_MSG = "Channel has not config";

    public static final String MB_RESPONSE_ERROR_CODE_SUCCESS = "000";

    public static final String RECONCILIATION_PARAMS_FORMAT_DATE = "yyyyMMdd";
    public static final String RECONCILIATION_PARAMS_FORMAT_MONTH = "yyyyMM";

    // dd/MM/yyyy hh24:mm:ss
    public static final String RECONCILIATION_FORMAT_DATE_REPORT = "dd-MM-yyyy";
    public static final String RECONCILIATION_FORMAT_MONTH_REPORT = "MM-yyyy";
    public static final String RECONCILIATION_FILE_STRUCT_FORMAT_TIME = "HH:mm:ss";
    public static final String RECONCILIATION_FILE_STRUCT_FORMAT_DATE_TIME = "dd/MM/yyyy HH:mm:ss";
    public static final String RECONCILIATION_FILE_NAME_FORMAT_DATE = "yyyyMMdd";
    public static final char RECONCILIATION_FILE_STRUCT_SEPARATOR_CHAR = '|';
    public static final String RECONCILIATION_FILE_STRUCT_SEPARATOR_CHAR_IN_FILE = "\\|";
    public static final String RECONCILIATION_FILE_STRUCT_RECORD_TYPE_BODY = "0002";
    public static final String RECONCILIATION_FILE_STRUCT_RECORD_TYPE_FOOTER = "0009";

    public static final String RECONCILIATION_FILE_STRUCT_RECON_RESULT_EQUAL = "00";
    public static final String RECONCILIATION_FILE_STRUCT_RECON_RESULT_DIFF_NL = "01";
    public static final String RECONCILIATION_FILE_STRUCT_RECON_RESULT_DIFF_MB = "02";
    public static final String RECONCILIATION_FILE_STRUCT_RECON_RESULT_DIFF_VALUE = "03";

    public static final String RECONCILIATION_FILE_STRUCT_RECON_CODE_SUCCESS = "00";

    public static final String RECONCILIATION_FILE_STRUCT_RECON_MSG_TYPE_PAYG_CARD = "1004";
    public static final String RECONCILIATION_FILE_STRUCT_RECON_CHANNEL_ID_MB = "P3";
    public static final String RECONCILIATION_FILE_STRUCT_RECON_TRANS_TYPE_MB = "PAYG";
    public static final String RECONCILIATION_FILE_STRUCT_RECON_CREATOR = "NGANLUONG";

    public static final String RECONCILIATION_FILE_NAME_FORMAT_MONTH = "yyyyMM";

    public static final String RECONCILIATION_FILE_NAME_CHARACTER_AFTER_DATE = "_";

    /**
     *  TransactionId = ChannelID + Transaction Type + MerchantTransactionID
     *  ChannelID (length = 2), Fixed value MB sẽ cung cấp khi tích hợp
     *  TransactionType (length = 2): CI - Cashin, CO - Cashout, TT - Payment Gateway
     *  MerchantTransactionID (length = 8): Uniquely generated
     */
    public static final String TRANSACTION_ID = "P3";
    public static final String TRANSACTION_TYPE = "CI";
}
