package gateway.core.channel.tcb_qrcode.dto.req;

import lombok.Data;

@Data
public class BaseTCBQrCodeReq {
    private String fnc ;
    private String app_id ;
    private String data;
    private String checksum;
}
