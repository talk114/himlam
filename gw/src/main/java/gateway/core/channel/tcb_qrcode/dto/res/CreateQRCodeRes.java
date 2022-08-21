package gateway.core.channel.tcb_qrcode.dto.res;


import lombok.Data;

@Data
public class CreateQRCodeRes {
    private String error_code;
    private String error_message;
    private String bank_code;
    private String amount_payment;
    private String payment_id;
    private String qrcode_image;
    private String qrcode_pay;
    private String time_expired;
    private String payment_token;

}
