package gateway.core.channel.ocb.dto.request;

import gateway.core.channel.ocb.dto.DataStep2;
import gateway.core.channel.ocb.dto.Trace;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentStep2Req extends BaseRequest{
    private Trace trace;
    private DataStep2 data;

}
