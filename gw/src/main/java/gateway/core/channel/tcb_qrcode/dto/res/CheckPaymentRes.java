package gateway.core.channel.tcb_qrcode.dto.res;


import lombok.Data;

@Data
public class CheckPaymentRes {
    private String merchant_id;
    private String merchant_request_id;
    private String service_code;
    private String method_code;
    private String type_card_payment;
    private String bank_code;
    private String card_number;
    private String card_fullname;
    private String payment_id;
    private String payment_token;
    private String time_performed;
    private String amount;
    private String fee;
    private String status;


}
