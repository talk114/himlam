package gateway.core.channel.ocb.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import gateway.core.channel.ocb.dto.DataStep1;
import gateway.core.channel.ocb.dto.Trace;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentStep1Req extends BaseRequest{
    private Trace trace;
    private DataStep1 data;
}
