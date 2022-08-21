package gateway.core.channel.vccb_va.dto;

import lombok.Data;

@Data
public class CallbackHolder {
    private String to;
    private String traceId;
    private String eventName;
    private long bankTime;
    private Object data;
}
