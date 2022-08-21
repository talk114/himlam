package gateway.core.channel.ocb.dto.error;

import lombok.Data;

@Data
public class TraceError {
    private String clientTransId;
    private String bankRefNo;
    private String clientTimestamp;
}
