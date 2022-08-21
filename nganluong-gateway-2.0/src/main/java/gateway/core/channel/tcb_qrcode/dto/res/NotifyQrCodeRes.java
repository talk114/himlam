package gateway.core.channel.tcb_qrcode.dto.res;


import gateway.core.channel.tcb_qrcode.dto.req.DataResponse;
import lombok.Data;

@Data
public class NotifyQrCodeRes {
    private DataResponse data;
    private String checksum;

}
