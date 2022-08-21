package gateway.core.channel.ocb.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import gateway.core.channel.ocb.dto.DataStep3;
import gateway.core.channel.ocb.dto.Trace;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentStatusReq extends BaseRequest{
    private Trace trace;
    private DataStep3 data;
}
