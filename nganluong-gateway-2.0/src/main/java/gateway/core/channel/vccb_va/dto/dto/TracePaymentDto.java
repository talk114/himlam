package gateway.core.channel.vccb_va.dto.dto;

import gateway.core.channel.vccb_va.dto.request.TracePayment;
import lombok.Data;

@Data
public class TracePaymentDto extends TracePayment {
    private String requestId;
}
