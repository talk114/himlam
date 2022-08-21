package gateway.core.channel.tcb_qrcode.dto.req;

import lombok.Data;

@Data
public class GetQrBankCodeReq {
    private String service_type_code;
    private String service_code;
    private String method_code;
}
