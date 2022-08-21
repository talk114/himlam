package gateway.core.channel.tcb_qrcode.dto.req;

import lombok.Data;

@Data
public class DataResponse {
    private long merchant_id;
    private String payment_id;
    private String transaction_id;
    private String payment_token;
    private String merchant_request_id;
    private int time_performed;
    private long amount;
    private int fee;
    private int status;

}
