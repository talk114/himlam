package gateway.core.channel.migs.config;

import com.google.common.collect.ImmutableMap;
import gateway.core.channel.migs.entity.input.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MIGSConfig {
    public static final String CHANNEL_CODE = "MIGS";
    public static final String SERVICE_NAME = "DIRECT PAYMENT";
    public static final String FUNTION_INITIATE_AUTHENTICATION = "MIGS_INITIATE_AUTHENTICATION";
    public static final String FUNTION_AUTHENTICATE_PAYER = "MIGS_AUTHENTICATE_PAYER";
    public static final String FUNTION_PAY_3DS2 = "MIGS_PAY_3DS2";
    public static final String FUNTION_PAY_3DS1 = "MIGS_PAY_3DS1";
    public static final String FUNTION_CHECK_3DS_ENROLLMENT = "MIGS_CHECK_3DS_ENROLLMENT";
    public static final String FUNTION_PROCESS_ACS_RESULT = "MIGS_PROCESS_ACS_RESULT";
    public static final String FUNTION_RESULT_CHECK_3DS = "MIGS_RESULT_CHECK_3DS";
    public static final String FUNTION_REFUND_TRANSACTION = "MIGS_REFUND_TRANSACTION";
    public static final String FUNTION_QUERY_TRANSACTION = "MIGS_QUERY_TRANSACTION";

    @Bean
    public Config buildConfigMic() {
        Config config = new Config();
        //sandbox
//        config.setMerchantId("165-M-123456");
//        config.setApiUsername("merchant.165-M-123456");
//        config.setApiPassword("81194733f1c790ac6f1ed3c9631d2e0a");
//        config.setGatewayHost("https://test-gateway.mastercard.com/api/rest");
        //live
        config.setMerchantId("165-M-131690");
        config.setApiUsername("merchant.165-M-131690");
        config.setApiPassword("cd267f01e138b1610f5ea792b965a41a");
        config.setGatewayHost("https://ap-gateway.mastercard.com/api/rest");

        config.setAuthenticationType(Config.AuthenticationType.PASSWORD);
        return config;
    }

    static final ImmutableMap<String, String> ERROR_MESSAGE = ImmutableMap.<String, String>builder()
            .put("ABORTED", "Transaction aborted by payer")
            .put("ACQUIRER_SYSTEM_ERROR", "Acquirer system error occurred processing the transaction")
            .put("APPROVED", "Transaction Approved")
            .put("APPROVED_AUTO", "The transaction was automatically approved by the gateway. it was not submitted to the acquirer.")
            .put("APPROVED_PENDING_SETTLEMENT", "Transaction Approved - pending batch settlement")
            .put("AUTHENTICATION_FAILED", "Payer authentication failed")
            .put("AUTHENTICATION_IN_PROGRESS", "The operation determined that payer authentication is possible for the given card, but this has not been completed, and requires further action by the merchant to proceed.")
            .put("BALANCE_AVAILABLE", "A balance amount is available for the card, and the payer can redeem points.")
            .put("BALANCE_UNKNOWN", "A balance amount might be available for the card. Points redemption should be offered to the payer.")
            .put("BLOCKED", "Transaction blocked due to Risk or 3D Secure blocking rules")
            .put("CANCELLED", "Transaction cancelled by payer")
            .put("DECLINED", "The requested operation was not successful. For example, a payment was declined by issuer or payer authentication was not able to be successfully completed.")
            .put("DECLINED_AVS", "Transaction declined due to address verification")
            .put("DECLINED_AVS_CSC", "Transaction declined due to address verification and card security code")
            .put("DECLINED_CSC", "Transaction declined due to card security code")
            .put("DECLINED_DO_NOT_CONTACT", "Transaction declined - do not contact issuer")
            .put("DECLINED_INVALID_PIN", "Transaction declined due to invalid PIN")
            .put("DECLINED_PAYMENT_PLAN", "Transaction declined due to payment plan")
            .put("DECLINED_PIN_REQUIRED", "Transaction declined due to PIN required")
            .put("DEFERRED_TRANSACTION_RECEIVED", "Deferred transaction received and awaiting processing")
            .put("DUPLICATE_BATCH", "Transaction declined due to duplicate batch")
            .put("EXCEEDED_RETRY_LIMIT", "Transaction retry limit exceeded")
            .put("EXPIRED_CARD", "Transaction declined due to expired card")
            .put("INSUFFICIENT_FUNDS", "Transaction declined due to insufficient funds")
            .put("INVALID_CSC", "Invalid card security code")
            .put("LOCK_FAILURE", "Order locked - another transaction is in progress for this order")
            .put("NOT_ENROLLED_3D_SECURE", "Card holder is not enrolled in 3D Secure")
            .put("NOT_SUPPORTED", "Transaction type not supported")
            .put("NO_BALANCE", "A balance amount is not available for the card. The payer cannot redeem points.")
            .put("PARTIALLY_APPROVED", "The transaction was approved for a lesser amount than requested. The approved amount is returned in order.totalAuthorizedAmount.")
            .put("PENDING", "Transaction is pending")
            .put("REFERRED", "Transaction declined - refer to issuer")
            .put("SUBMITTED", "The transaction has successfully been created in the gateway. It is either awaiting submission to the acquirer or has been submitted to the acquirer but the gateway has not yet received a response about the success or otherwise of the payment.")
            .put("SYSTEM_ERROR", "Internal system error occurred processing the transaction")
            .put("TIMED_OUT", "The gateway has timed out the request to the acquirer because it did not receive a response. Points redemption should not be offered to the payer.")
            .put("UNKNOWN", "The transaction has been submitted to the acquirer but the gateway was not able to find out about the success or otherwise of the payment. If the gateway subsequently finds out about the success of the payment it will update the response code.")
            .put("UNSPECIFIED_FAILURE", "Transaction could not be processed")
            .put("PROCEED", "Proceed using this card.")
            .put("DO_NOT_PROCEED", "Do not proceed using this card.")


            .build();

    public static String getErrorMessage(String errorCode) {
        if (ERROR_MESSAGE.containsKey(errorCode)) {
            return ERROR_MESSAGE.get(errorCode);
        } else {
            return "Unknown error";
        }
    }

    //DOI SOAT
    public static final String SFTPWORKINGDIR_DOWNLOAD = "./in";
    public static final String SFTPWORKINGDIR_UPLOAD = "./out";
    public static final String SFTP_HOST = "10.0.14.10";
    public static final int SFTP_PORT = 4122;
    public static final String SFTP_USERNAME = "sftp_vpbank";
    public static final String LOCAL_PATH = "/data/tomcat/uat/apache-tomcat-8.0.36/MIGS_REC";
}
