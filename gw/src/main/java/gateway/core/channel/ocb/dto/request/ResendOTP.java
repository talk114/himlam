package gateway.core.channel.ocb.dto.request;

import gateway.core.channel.ocb.dto.DataResend;
import gateway.core.channel.ocb.dto.Trace;
import lombok.Data;

@Data
public class ResendOTP extends BaseRequest{
    private Trace trace;
    private DataResend data;
}
