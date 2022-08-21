package gateway.core.channel;

public class ChannelCommonConfig {
    //############################################# VTB - QR Code ######################################################
    //EndPoint Live:
    public static final String URL_API_VTB = "http://192.168.6.167:9085/mobile/qr/generateQrPaymentToken";
    public static final String VTB_URL_API_CALL_NGL_MC = "https://www.nganluong.vn/qrcode_result.php";
    //Key Live:
    public static final String GW_PRIVATE_KEY_VTB_QRCODE = "vtb_qrcode/ngl_vtb_qr/priv_key.pem";
    public static final String GW_PUBLIC_KEY_VTB_QRCODE = "vtb_qrcode/ngl_vtb_qr/pub_key.cer";
    public static final String PUBLIC_KEY_VTB_QRCODE = "vtb_qrcode/ngl_vtb_qr/publicVTB.cer";
    //Live:
    public static final String VTB_MERCHANT_ID = "0106001236";
    public static final String VTB_MERCHANT_NAME = "NGANLUONG";
    //########################################## MSB - QR CODE ########################################################
    //Live
    public static final String MSB_QRCode_PROVIDER_ID = "MaritimeBank";
    public static final String MSB_QRCode_MERCHANT_ID = "011000000869650";
    public static final String MSB_QRCode_MERCHANT_NAME = "CTY CP NGAN LUONG";
    public static final String MSB_QRCode_ENCRYP_KEY = "vnpay@MaritimeBank";
    public static final String MSB_QRCode_URL_API_CALL_NGL = "https://www.nganluong.vn/api/msbQrcode";
    public static final String MSB_QRCode_PEIVATE_KEY_PART = "nganluongmsb@2018";
    public static final String MSB_QRCode_URL_API_CALL_MSB = "https://externalgw.msb.com.vn/msbgateway/services";

    //Test
//    public static final String MSB_QRCode_PROVIDER_ID = "MaritimeBank";
//    public static final String MSB_QRCode_MERCHANT_ID = "011000000000002";
//    public static final String MSB_QRCode_MERCHANT_NAME = "NGAN LUONG";//NGANLUONG
//    public static final String MSB_QRCode_ENCRYP_KEY = "msb1991@test";
//    public static final String MSB_QRCode_URL_API_CALL_NGL = "https://sandbox.nganluong.vn:8088/nl35/api/msbQrcode/index";
//    public static final String MSB_QRCode_PEIVATE_KEY_PART = "nganluongmsb@2018";
//    // public static final String MSB_QRCode_URL_API_CALL_MSB = "http://103.89.122.10:8080/v1/api/msb/acq-hub/qr-process";
//    public static final String MSB_QRCode_URL_API_CALL_MSB = "http://103.89.122.10:7080/msbgateway/services";


    //######################################## DONG A BANK ############################################################
    //TEST:
    public static final String DAB_MERCHANT_ID = "NLTT";
    public static final String DAB_AUTH_KEY = "NLTThDYPCH923NYv6wFx";
    public static final String DAB_ENCRYP_KEY = "SXgPh0zDxGJ3iYZwgLb6y62xUZ{9eXtkrTt)IVee";
    public static final String DAB_URL_API_CALL_NGL = "https://sandbox.nganluong.vn:8088/nl35/api/dab";
    public static final String DAB_URL_API_CALL_DAB = "http://sandbox.dongabank.com.vn/egate-new-dl-khcn/WSBean";
    public static final String DAB_HOST = "http://uat-apps.dongabank.com.vn/ebankinternet-khcn/partner?";

    public static final String DAB_PRIVATE_KEY_GW = "Dong_A_Bank_Key/uat/nganluong_priv_key.pem";
    public static final String DAB_PUBLIC_KEY_GW = "Dong_A_Bank_Key/uat/nganluong_pub_key.pem";
    public static final String DAB_PUBLIC_KEY = "Dong_A_Bank_Key/uat/dab_nl_pub.pem";

    //####################################### VIETTEL PAY ##############################################################
    //TEST:
//    public static final String VIETTEL_PAY_AUTH_KEY = "d41d8cd98f00b204e9800998ecf8427e04795ece99a1e7632753837d41c89486";
//    public static final String VIETTEL_PAY_ENCRRYP_KEY= "d41d8cd98f00b204e9800998ecf8427e8f2525793d95370596fe530477c789c7";
//    public static final String VIETTEL_PAY_MERCHANT_ID= "NGANLUONGTEST";
//    public static final String VIETTEL_PAY_URL_API_CALL_MERCHANT= "https://sandbox.nganluong.vn:8088/nl35_dev/api/viettelPay";
//    public static final String VIETTEL_PAY_URL_API_MERCHANT_CALL_VITETTEL_PAY= " http://125.235.40.34:8085/ViettelSDKPayment/webresources/postData?";

    // LIVE:
    public static final String VIETTEL_PAY_AUTH_KEY = "d41d8cd98f00b204e9800998ecf8427edce86fc31b84f73d8383d3a782739205";
    public static final String VIETTEL_PAY_ENCRRYP_KEY= "d41d8cd98f00b204e9800998ecf8427eb01cd98952cca23a2feefa4353a31da9";
    public static final String VIETTEL_PAY_MERCHANT_ID= "NGANLUONG";
    public static final String VIETTEL_PAY_URL_API_CALL_MERCHANT= "https://www.nganluong.vn/api/viettelpay";
    public static final String VIETTEL_PAY_URL_API_MERCHANT_CALL_VITETTEL_PAY= "http://125.235.40.34:8085/ViettelSDKPayment/webresources/postData";

    //######################################## VIETTEL POST ############################################################
    //TEST:
    public static String VTT_ENCRYPT_KEY = "PeaceSoftViettelPost@2018";
    public static String VTT_AUTH_KEY = "PeaceSoft@2018";
    public static String VTT_URL_API = "https://sandbox.nganluong.vn:8088/nl35/api/viettelPost";

    //############################################# VNPOST ######################################################
    //TEST
    public static String VNPOST_ENCRYPT_KEY_CALLBACK = "NL@!#@$#%#^^#@^";
    public static String VNPOST_ENCRYPT_KEY = "NL@!#@$#%#^^#@^";
    public static String VNPOST_URL_API = "https://sandbox.nganluong.vn:8088/nl35/vnpost.api.post.php";
    public static String VNPOST_USER_NAME = "PAYMENT_NGANLUONG2017";
    public static String VNPOST_PASSWORD = "kKtzm@hx@Wx3nxPdVC";
    //LIVE
//    public static String VNPOST_ENCRYPT_KEY = "NL@!#@$#%#^^#@^";
//    public static String VNPOST_URL_API = "https://www.nganluong.vn/vnpost.api.post.php";
//    public static String VNPOST_USER_NAME = "nganluong";
//    public static String VNPOST_PASSWORD = "Ng@n2018";
    
    //############################################# AnBinhBank ######################################################
    //TEST
//    public static String PUBLIC_KEY_ABB_PATH = "abb-key/test/public_key_abb.p12";
//    public static String PRIVATE_KEY_PATH = "abb-key/test/private_key_vimo.p12";
//    public static String PRIVATE_KEY_PASSWORD = "123456";
//    public static String AUTH_KEY_ABB = "289b0b2f70ec99b1a1c65223b224d928:9d569b8f8d51241847afd8d5a8493cdd";
//    public static String ABB_SESSION_FILE_TOKEN_PATH = "abb-key/NGANLUONG.txt";
//    public static String ABB_URL_API = "http://10.2.9.50:9443/api/sandbox";

    //LIVE
    public static String PUBLIC_KEY_ABB_PATH = "abb-key/live/public_ABB_VIMO.crt";
    public static String PRIVATE_KEY_PATH = "abb-key/live/server_VIMO.p12";
    public static String PRIVATE_KEY_PASSWORD = "123456";
    public static String AUTH_KEY_ABB = "99291dbc79c363fe28b69b48273396bf:535f95eaf71ae61f965f0cba0dc48313";
    public static String ABB_SESSION_FILE_TOKEN_PATH = "abb-key/NGANLUONG.txt";
    public static String ABB_URL_API = "https://apigw-apiprod.abbank.vn:9443/api/prod";

    //############################################ VAY MUON QR CODE ####################################################
    //test:
//    public static final String VAYMUON_QRCODE_API = "https://captindungbeta.vaymuon.vn/";
//    public static final String VAYMUON_QRCODE_SECRET_KEY = "mOxoSXW0yXa3TlgtgPHC";
    //live:
    public static final String VAYMUON_QRCODE_API = "https://captindung.vaymuon.vn/";
    public static final String VAYMUON_QRCODE_SECRET_KEY = "mOKoSXW0yXa3TlgtgNLV";
}
