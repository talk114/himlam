package gateway.core.channel.tcb_qrcode.dto.req;

import lombok.Data;

@Data
public class NotifyTCBQrCodeReq {
    private String merchant_id;
    private String merchant_request_id;
    private String payment_id;
    private String payment_token;
    private String time_performed;
    private String amount;
    private String fee;
    private String status;

}
