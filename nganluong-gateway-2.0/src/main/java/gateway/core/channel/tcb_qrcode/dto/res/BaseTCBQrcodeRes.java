package gateway.core.channel.tcb_qrcode.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import gateway.core.channel.tcb_qrcode.dto.req.DataResponse;
import lombok.Data;
import org.json.JSONObject;

@Data
public class BaseTCBQrcodeRes {
    private String error_code;
    private String error_message;
    private String app_id;
    private String checksum;
    @JsonProperty("data")
    private Object data;

}
