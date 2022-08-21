package vn.nganluong.naba.channel.vib.dto;

public class VIBConst {

	// TODO Do remove hardcode
	public static final String PG_USER_CODE = "NL";

	public static final String CHANNEL_CODE = "VIB";

	public static final String SERVICE_NAME_IBFT = "IBFT";

	public static final String CHANNEL_FUNCTION_ACCESS_TOKEN_CODE = "ACCESS_TOKEN";
	public static final String CHANNEL_FUNCTION_INVALID_ACCOUNT_VIB_CODE = "INVALID_ACCOUNT_VIB";
	public static final String CHANNEL_FUNCTION_INVALID_ACCOUNT_NAPAS_CODE = "INVALID_ACCOUNT_NAPAS";
	public static final String CHANNEL_FUNCTION_BALANCE_ACCOUNT_CODE = "BALANCE_ACCOUNT";
	public static final String CHANNEL_FUNCTION_ADD_TRANSACTION = "ADD_TRANSACTION";
	public static final String CHANNEL_FUNCTION_STATUS_TRANSACTION = "STATUS_TRANSACTION";
	public static final String CHANNEL_FUNCTION_HISTORY_TRANSACTION = "HISTORY_TRANSACTION";
	
	// Create new Virtual Account
	public static final String CHANNEL_FUNCTION_CREATE_VA = "VIB_VA_CREATE_VA";
	public static final String CHANNEL_FUNCTION_DELETE_VA = "VIB_VA_DELETE_VA";
	public static final String CHANNEL_FUNCTION_ENABLE_VA = "VIB_VA_ENABLE_VA";
	public static final String CHANNEL_FUNCTION_VA_HISTORY_TRANSACTION_VA = "VIB_VA_HISTORY_TRANSACTION_VA";
	public static final String CHANNEL_FUNCTION_VA_ERROR_HISTORY_TRANSACTION_VA = "VIB_VA_ERROR_HISTORY_TRANSACTION_VA";
	public static final String CHANNEL_FUNCTION_VA_HISTORY_TRANSACTION_OF_REAL_ACCOUNT = "VIB_VA_HISTORY_TRANSACTION_OF_REAL_ACCOUNT";
	public static final String CHANNEL_FUNCTION_VA_CALL_BACK = "VIB_VA_CALL_BACK";
	public static final String CHANNEL_FUNCTION_VA_LIST_VA = "VIB_VA_LIST_VA";
	public static final String CHANNEL_FUNCTION_VA_DETAIL_VA = "VIB_VA_DETAIL_VA";

	public static final String CHANNEL_FUNCTION_ADD_TRANSACTION_RESPONSE_STATUS_CODE_SUCCESS = "MT0000";
	public static final String CHANNEL_FUNCTION_STATUS_TRANSACTION_RESPONSE_STATUS_CODE_SUCCESS = "ITS000";
	public static final String CHANNEL_FUNCTION_HISTORY_TRANSACTION_RESPONSE_STATUS_CODE_SUCCESS = "ITH000";
	
	public static final String CCY = "VND";
	public static final String FUNCTION_CODE_ACCESS_TOKEN = "VIB_IBFT_ACCESS_TOKEN";
	public static final String FUNCTION_CODE_VALID_ACCOUNT = "VIB_IBFT_VALID_ACCOUNT";
	public static final String FUNCTION_CODE_BALANCE_ACCOUNT = "VIB_IBFT_BALANCE_ACCOUNT";
	public static final String FUNCTION_CODE_ADD_TRANSACTION = "VIB_IBFT_ADD_TRANSACTION";
	public static final String FUNCTION_CODE_STATUS_TRANSACTION = "VIB_IBFT_STATUS_TRANSACTION";
	public static final String FUNCTION_CODE_HISTORY_TRANSACTION = "VIB_IBFT_HISTORY_TRANSACTION";
	
	public static final String FUNCTION_CODE_IBFT_RECONCILIATION_DAY = "VIB_IBFT_RECONCILIATION_DAY";
	public static final String FUNCTION_CODE_IBFT_RECONCILIATION_MONTH = "VIB_IBFT_RECONCILIATION_MONTH";

	public static final String FUNCTION_CODE_VA_RECONCILIATION_DAY = "VIB_VA_RECONCILIATION_DAY";
	public static final String FUNCTION_CODE_VA_RECONCILIATION_MONTH = "VIB_VA_RECONCILIATION_MONTH";
	
	// Create new Virtual Account
	public static final String VA_SERVICE_NAME = "VA";
	public static final String FUNCTION_CODE_CREATE_VA = "VIB_VA_CREATE_VA";
	public static final String FUNCTION_CODE_DELETE_VA = "VIB_VA_DELETE_VA";
	public static final String FUNCTION_CODE_ENABLE_VA = "VIB_VA_ENABLE_VA";
	public static final String FUNCTION_CODE_VA_HISTORY_TRANSACTION_VA = "VIB_VA_HISTORY_TRANSACTION_VA";
	public static final String FUNCTION_CODE_VA_ERROR_HISTORY_TRANSACTION_VA = "VIB_VA_ERROR_HISTORY_TRANSACTION_VA";
	public static final String FUNCTION_CODE_VA_HISTORY_TRANSACTION_OF_REAL_ACCOUNT = "VIB_VA_HISTORY_TRANSACTION_VA_OF_REAL_ACCOUNT";
	public static final String FUNCTION_CODE_VA_CALL_BACK = "VIB_VA_CALL_BACK";
	public static final String FUNCTION_CODE_VA_LIST_VA = "VIB_VA_LIST_VA";
	public static final String FUNCTION_CODE_VA_DETAIL_VA = "VIB_VA_DETAIL_VA";
	
	public static final String CHANNEL_NOT_CONFIG_MSG = "Channel has not config";
	
	public static final String VIB_IBFT_PARAMS_FORMAT_DATE = "yyyyMMdd";
	public static final String VIB_IBFT_RECONCILIATION_FILE_NAME_FORMAT_DATE = "yyyyMMdd";
	public static final String VIB_IBFT_RECONCILIATION_CHARACTER_AFTER_DATE_IN_FILE_NAME = "_";
	public static final String VIB_IBFT_RECONCILIATION_TRAN_TIME_FORMAT_DATE = "dd-MMM-yyHH:mm:ss";
	
	public static final String VIB_IBFT_RECONCILIATION_TRAN_TIME_CSV_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";
	public static final String VIB_IBFT_RECONCILIATION_DATE_FORMAT_DATE = "dd-MM-yyyy";
	
	public static final String VIB_IBFT_PARAMS_YEAR_MONTH_FORMAT_DATE = "yyyyMM";
	public static final String VIB_IBFT_RECONCILIATION_FILE_NAME_YEAR_MONTH_FORMAT_DATE = "yyyyMM";
	public static final String ERP_RECONCILIATION_SEPERATOR_CHAR_IN_FILE = "\\|";
	
	public static final String ERP_RECONCILIATION_FILE_STRUCT_RECORD_TYPE_HEADER = "0001";
	public static final String ERP_RECONCILIATION_FILE_STRUCT_RECORD_TYPE_BODY = "0002";
	public static final String ERP_RECONCILIATION_FILE_STRUCT_RECORD_TYPE_FOOTER = "0009";
	public static final String ERP_RECONCILIATION_FILE_STRUCT_RECON_CODE_SUCCESS = "00";
	public static final String ERP_RECONCILIATION_FILE_STRUCT_RECON_RESULT_EQUAL = "00";
	public static final String ERP_RECONCILIATION_FILE_STRUCT_RECON_RESULT_DIFF_NL = "01";
	public static final String ERP_RECONCILIATION_FILE_STRUCT_RECON_RESULT_DIFF_VIB = "02";
	public static final String ERP_RECONCILIATION_FILE_STRUCT_RECON_RESULT_DIFF_VALUE = "03";
	
	public static final String ERP_RECONCILIATION_FILE_STRUCT_FORMAT_DATE = "ddMMyyyy";
	public static final String ERP_RECONCILIATION_FILE_STRUCT_FORMAT_TIME = "HHmmss";
	
	//public static final String ERP_RECONCILIATION_MONTH_FILE_STRUCT_FORMAT_DATE = "dd/MM/yyyy";
	public static final String ERP_RECONCILIATION_MONTH_FILE_STRUCT_FORMAT_DATE = "ddMMyyyy";
	
	public static final String ERP_RECONCILIATION_FILE_STRUCT_RECON_CREATOR = "NGANLUONG";

	public static final String API_RESPONSE_STATUS_CODE_SUCCESS = "000000";
	public static final String CHANNEL_TYPE = "ERP";
	public static final String TRANSACTION_TYPE = "SINGLE";



	public enum EnumTransactionType {
		VIBA("VBA"), SMLACCT("SML");

		private String typeName;

		EnumTransactionType(String typeName) {
			this.typeName = typeName;
		}

		public String typeName() {
			return typeName;
		}

	}
	
}
