package gateway.core.channel.ocb.dto.response;

import lombok.Data;

@Data
public class Trace {
    private String clientTransId;
    private String bankRefNo;
    private String clientTimestamp;
}
