package gateway.core.channel.ocb.dto.error;

import lombok.Data;

@Data
public class ErrorResponse {
    private TraceError trace;
    private Error error;
}
