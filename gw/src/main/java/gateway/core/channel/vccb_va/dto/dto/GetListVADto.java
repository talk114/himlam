package gateway.core.channel.vccb_va.dto.dto;

import gateway.core.channel.vccb_va.dto.request.GetListVA;
import lombok.Data;

@Data
public class GetListVADto extends GetListVA {
    private String requestId;
}
