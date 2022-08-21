package gateway.core.channel.ocb.dto.request;

import gateway.core.channel.ocb.dto.Trace;
import lombok.Data;

@Data
public class BaseRequest {
    private Trace trace;
}
