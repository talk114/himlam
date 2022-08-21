package gateway.core.channel.tcb_qrcode.dto.req;

import lombok.Data;

@Data
public class CheckPaymentReq {
    private String merchant_request_id;
}
