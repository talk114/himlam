package gateway.core.channel.tcb_qrcode.dto.res;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.json.JSONArray;

@Data
public class GetQrBankCodeRes {
    private String error_code;
    private String error_message;
    private String app_id;
    private String checksum;
    @JsonProperty("data")
    private Object[] data;
}
