package gateway.core.channel.ocb.dto;

import gateway.core.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trace {
    private String clientTransId;
    private String clientTimestamp;
    {
        clientTimestamp = TimeUtil.getCurrentTime("yyyyMMddHHmmssSSS");
    }
}
