package gateway.core.channel.napas.doi_soat;

import java.io.File;

public class DoiSoatConstants {
    // TODO: cashin
    public static final String CREATER_FILE = "NGANLUONG";
    public static final String RECEIVER_BANK_CODE = "971001";
    public static final String WHITE_SPACE = "WHITE_SPACE";
    public static final String ZERO = "ZERO";
    public static final String VND = "704";

    // SFTP SERVER
    public static final String SFTP_HOST = "pubsftp.napas.com.vn";
    public static final int SFTP_PORT = 2443;
    public static final String SFTP_USERNAME = "nganluong";
    public static final String SFTP_PASSWORD = "wt@G8179";

    public static final String LOCAL_PATH_DOWNLOAD = "/data/doisoat/napas";
    public static final String LOCAL_PATH_DECODE = LOCAL_PATH_DOWNLOAD + File.separator + "decode";
    public static final String LOCAL_PATH_ENCODE = LOCAL_PATH_DOWNLOAD + File.separator + "encode";
    public static final String LOCAL_PATH_CSV = LOCAL_PATH_DOWNLOAD + File.separator + "csv";
    public static final String REMOTE_PATH = "ECOM/Inbox/";
    public static final String REMOTE_PATH_UPLOAD = "ECOM/Outbox/";

    public static final String PUBLIC_KEY_ENCYPT_PATH = "napasV3_doiSoatFile/public01.cer";
    public static final String PRIVATE_KEY_DECRYPT_PATH = "napasV3_doiSoatFile/private.pem";

    public static final String SECRET_KEY = "971019";
    public static final String NGL_ACQUIRER_IDENTIFY = "971019";
    public static final String NGL_MID = "NGANLUONGCE";
    public static final String SERVICE_CODE = "EC_CASHIN";
    public static final String NGL_CHANNEL_CODE_TRANSACTION = "04";
    public static final String CONDITIONAL_CODE_AT_SERVICE_ACCESS_POINT = "08";
    public static final String AGENCY_CODE_TYPE = "7399";
    public static final String AGENCY_CODE_IDENTIFY = "00005540";

    // TODO: whitelable
    public static final String SFTP_HOST_WL = "103.9.4.63";
    public static final int SFTP_PORT_WL = 2443;
    public static final String SFTP_USERNAME_WL = "nganluong";
    public static final String SFTP_PASSWORD_WL = "3q%zqJdY";

    public static final String LOCAL_PATH_DOWNLOAD_WL = "/data/doisoat/napasWL";
    public static final String LOCAL_PATH_DECODE_WL = LOCAL_PATH_DOWNLOAD_WL + File.separator + "decode";
    public static final String LOCAL_PATH_ENCODE_WL = LOCAL_PATH_DOWNLOAD_WL + File.separator + "encode";
    public static final String LOCAL_PATH_CSV_WL = LOCAL_PATH_DOWNLOAD_WL + File.separator + "csv";
    public static final String REMOTE_PATH_WL = "ECOM/Inbox/";
    public static final String REMOTE_PATH_UPLOAD_WL = "ECOM/Outbox/";

    public static final String PUBLIC_KEY_ENCYPT_PATH_WL = "napasV3_doiSoatFile/public01.cer";
    public static final String PRIVATE_KEY_DECRYPT_PATH_WL = "napasV3_doiSoatFile/private.pem";

    public static final String SECRET_KEY_WL = "971019";
    public static final String NGL_ACQUIRER_IDENTIFY_WL = "971019";
    public static final String NGL_MID_WL = "NGANLUONGWL";
    public static final String SERVICE_CODE_WL = "WHITELABEL";
    public static final String NGL_CHANNEL_CODE_TRANSACTION_WL = "04";
    public static final String CONDITIONAL_CODE_AT_SERVICE_ACCESS_POINT_WL = "08";
    public static final String AGENCY_CODE_TYPE_WL = "7399";
    public static final String AGENCY_CODE_IDENTIFY_WL = "00005688";

}
