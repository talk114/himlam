package gateway.core.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gateway.core.channel.msb_va.dto.request.MSBNotifyReq;
import gateway.core.channel.vccb_va.dto.dto.CallbackVCCB;
import gateway.core.channel.vib.dto.VIBNotifyRequest;
import lombok.Getter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JsonPropertyOrder({"bankTransactionId", "transactionAmount", "cashinId", "bankCode", "transactionDate"})
@Getter
public class NLVARequest {
    private static final String VCCB_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss:SSS";
    private static final String VIB_TIME_PATTERN = "yyyy/MM/dd HH:mm:ss";
    private static final String MSB_TIME_PATTERN = "yyMMddHHmmss";

    /* LIVE */
    public static final String APP_NOTIFY_URL = "https://www.nganluong.vn/api/vaNotify/index";
    /* UAT */
    public static final String APP_NOTIFY_URL_UAT = "https://uat.nganluong.vn/api/vaNotify/index";
    /* SANDBOX */
//    public static final String APP_NOTIFY_URL = "https://sandbox.nganluong.vn:8088/nl35/api/vaNotify/index";

    @JsonProperty(value = "bank_transaction_id")
    private String bankTransactionId;

    @JsonProperty("bank_account")
    private String bankAccount;

    @JsonProperty(value = "transaction_amount")
    private String transactionAmount;

    @JsonProperty(value = "cashin_id")
    private String cashinId;

    @JsonProperty(value = "bank_code")
    private String bankCode;

    @JsonProperty(value = "transaction_date")
    private String transactionDate;

    private String currency;

    private String fromAccountName;

    private String fromAccountNumber;

    private String description;

    private NLVARequest() {
    }

    private NLVARequest(String bankTransactionId,
                        String bankAccount,
                        String transactionAmount,
                        String description,
                        String bankCode,
                        String transactionDate,
                        String currency,
                        String fromAccountName,
                        String fromAccountNumber) {
        this.bankTransactionId = bankTransactionId;
        this.bankAccount = bankAccount;
        this.transactionAmount = transactionAmount;
        this.bankCode = bankCode;
        this.transactionDate = this.timeFormat(bankCode,transactionDate);
        this.cashinId = this.getCashinId(description);
        this.currency = currency;
        this.fromAccountName = fromAccountName != null ? fromAccountName : "";
        this.fromAccountNumber = fromAccountNumber != null ? fromAccountNumber : "";
        this.description = description;
    }

    public static NLVARequest parse(VIBNotifyRequest request, String bankCode) {
        return new NLVARequest(String.valueOf(request.getSequenceNumber()),
                request.getVirtualAccount(),
                String.valueOf(request.getTransactionAmount()),
                request.getTransactionDescription(),
                bankCode,
                request.getTransactionDate(),
                request.getCurrency(),
                null,
                null);
    }

    public static NLVARequest parse(CallbackVCCB request, String bankCode) {
        return new NLVARequest(request.getTransactionReferenceNumber(),
                request.getRelatedAccount(),
                request.getLcyAmount(),
                request.getNarrative(),
                bankCode,
                request.getValueDate(),
                request.getCurrencyCode(),
                null,
                null);
    }

    public static NLVARequest parse(MSBNotifyReq request, String bankCode) {
        return new NLVARequest(request.getTranSeq(),
                request.getFromAccountNumber(),
                request.getTranAmount(),
                request.getTranRemark(),
                bankCode,
                request.getTranDate(),
                "VND",
                request.getFromAccountName(),
                request.getFromAccountNumber());
    }

    private String timeFormat(String time) throws ParseException {
        String time1 = "02-11-2021 15:35:05:771";
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").parse(time1);
        return null;
    }
    private String getCashinId(String narrative){
        Pattern pattern = Pattern.compile("(NL.........)");
        Matcher matcher = pattern.matcher(narrative);
        String cashinId2 = matcher.find() ? matcher.group(1).substring(2) : "";
        StringBuilder builder = new StringBuilder("");

        for (int i = 0; i < cashinId2.length(); i++) {
            if ((int) cashinId2.charAt(i) < 48 || (int) cashinId2.charAt(i) > 57) {
                break;
            }
            builder.append(cashinId2.charAt(i));
        }
        return builder.toString();
    }

    private static String timeFormat(String bankCode, String time){
        if (bankCode == null || bankCode.length() == 0){
            return time;
        }
        String pattern = "";
        switch (bankCode){
            case "VIB":
                pattern = VIB_TIME_PATTERN;
            case "VCCB":
                pattern = VCCB_TIME_PATTERN;
                break;
            case "MSB":
                pattern = MSB_TIME_PATTERN;
                break;
        }
        try {
            Date date = new SimpleDateFormat(pattern).parse(time);
            String dateTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
            return dateTime;
        }catch (ParseException e){
            return time;
        }
    }

}
