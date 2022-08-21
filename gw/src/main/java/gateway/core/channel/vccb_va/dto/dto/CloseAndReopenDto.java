package gateway.core.channel.vccb_va.dto.dto;

import gateway.core.channel.vccb_va.dto.request.CloseAndReopen;
import lombok.Data;

@Data
public class CloseAndReopenDto extends CloseAndReopen {
    private String requestId;
}
