package gateway.core.channel.bidv.bidv_transfer_247.dto;

import com.google.common.collect.ImmutableMap;

public class BIDVTransferConstants {
    public static final String PRIVATE_KEY = "bidv-nganluong-abc123";
    public static final String SERVICE_ID = "019004";
    public static final String MERCHANT_ID = "019004";
    public static final String URL_API_CALL_NL_DS = "https://sandbox.nganluong.vn:8088/nl35/api/bidv247/index?date=09/16/2020";

    public static final String CHANNEL_CODE = "BIDV_TRANSFER_247";
    public static final String FUNCTION_CODE_GET_FILE_BIDV_DAILY = "getFileBIDVDaily";
    public static final String FUNCTION_CODE_GET_LIST_BANK_247 = "getListBank247";
    public static final String FUNCTION_CODE_GET_NAME_247 = "getName247";
    public static final String FUNCTION_CODE_TRANSFER2_BANK_247 = "tranfer2Bank247";
    public static final String FUNCTION_CODE_GET_NAME_BIDV = "getNameBidv";
    public static final String FUNCTION_CODE_INQUERY = "inquery";
    public static final String FUNCTION_CODE_CHECK_PROVIDER_BALANCE = "checkProviderBalance";

    public static final ImmutableMap<String, String> CHANNEL_ID_MAP = ImmutableMap.<String, String>builder()
            .put("mobile", "211701")
            .put("web", "211601")
            .put("sms", "211901")
            .build();
}
