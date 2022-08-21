package gateway.core.channel.ocb.dto.validate;

import gateway.core.channel.ocb.dto.request.PaymentStatusReq;
import gateway.core.channel.ocb.dto.request.PaymentStep1Req;
import gateway.core.channel.ocb.dto.request.PaymentStep2Req;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dungla@nganluong.vn
 */
public class PaymentRequestValidation {
    private static final String NOT_NULL = "must not be null";
    private static final String SPACE = " ";

    /**
     * Check validate PaymentStep1Req
     * @param body
     * @return PGResponse messages
     */
    public static String checkStep1DTO(PaymentStep1Req body){

        if(!regexCheck(body.getTrace().getClientTransId(),"^[a-zA-Z0-9-]{32,32}$"))
            return "clientTransId invalid";

        if(body.getData().getLinkId() == null){
            if(body.getData().getCardNumber() == null)
                return "cardNumber" + SPACE + NOT_NULL;
            if(body.getData().getIssueDate() ==null)
                return "issueDate"+ SPACE + NOT_NULL;
            if(body.getData().getFullName() == null)
                return "fullName" + SPACE + NOT_NULL;
        }

        if(body.getData().getLinkId() == null &&
                body.getData().getCardNumber() == null)
            return "linkId or cardNumber" + SPACE + NOT_NULL;


        if(body.getData().getCardNumber() != null){
            if(!regexCheck(body.getData().getCardNumber(),"^[0-9]{1,30}$|^[0-9]{0}$|^[0-9]{0}$"))
                return "cardNumber invalid";
            if(!regexCheck(body.getData().getIssueDate(),"^((0[1-9]|1[012])\\d\\d)$|^((0[1-9]|1[012])\\d\\d){0}$"))
                return "issueDate invalid";
            if(!regexCheck(body.getData().getFullName(),"^[a-zA-Z0-9-_ ]{1,249}$|^[0-9]{0}$"))
                return "fullName invalid";
            if(!regexCheck(body.getData().getAccountType(),"^[a-zA-Z]{1,7}$|^[0-9]{0}$"))
                return "accountType invalid";
            if(body.getData().getTransferAmount() < 0)
                return "transferAmount invalid";
            if(!regexCheck(body.getData().getTransferDescription(),"^[a-zA-Z0-9_(). ]{1,249}$"))
                return "transferDescription invalid";
            if(!regexCheck(body.getData().getPartnerCode(),"^[a-zA-Z0-9]{1,20}$"))
                return "partnerCode invalid";
            if(!regexCheck(body.getData().getMerchantName(),"^[a-zA-Z0-9 ]{1,200}$"))
                return "merchantName invalid";
        }
        return null;
    }

    /**
     * Check validate PaymentStep2Req
     * @param body
     * @return PGResponse messages
     */
    public static String checkStep2DTO(PaymentStep2Req body){
        if(!regexCheck(body.getTrace().getClientTransId(),"^[a-zA-Z0-9-]{32,32}$"))
            return "clientTransId invalid";

        if(!regexCheck(body.getData().getOtpCode(),"^[0-9]{6,8}$"))
            return "OTP code invalid";
        if(!regexCheck(body.getData().getBankRefNo(),"^[a-zA-Z0-9-]{36,36}$"))
            return "Bank reference invalid";
        if(!regexCheck(body.getData().getPartnerCode(),"^[a-zA-Z0-9]{1,20}$"))
            return "PartnerCode invalid";
        return null;
    }

    public static String checkTransactionStatusDTO(PaymentStatusReq body){
        if(!regexCheck(body.getTrace().getClientTransId(),"^[a-zA-Z0-9-]{32,32}$"))
            return "clientTransId invalid";

        if(!regexCheck(body.getData().getPartnerTransId(),"^[a-zA-Z0-9-]{32,32}$"))
            return "partnerTransId invalid";
        if(!regexCheck(body.getData().getPartnerCode(),"^[a-zA-Z0-9]{1,20}$"))
            return "partnerCode invalid";
        return null;
    }
    private static boolean regexCheck(String textValue, String patternValue){
        Pattern pattern = Pattern.compile(patternValue);
        Matcher matcher = pattern.matcher(textValue);
        return matcher.find();
    }
}
