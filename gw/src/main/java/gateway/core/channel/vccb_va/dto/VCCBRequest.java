package gateway.core.channel.vccb_va.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"from","data","requestId"})
@ToString
public class VCCBRequest {
    private String from;
    private String requestId;
    private Object data;

    public static VCCBRequest getInstance(String RequestId, Object data){
        return new VCCBRequest(VCCBVAConfig.PARTNER_CODE,RequestId,data);
    }
}
