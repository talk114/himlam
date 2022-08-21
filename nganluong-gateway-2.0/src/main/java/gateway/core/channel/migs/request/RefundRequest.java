package gateway.core.channel.migs.request;

import lombok.Data;

/**
 * @author sonln
 */
@Data
public class RefundRequest {
    private String apiOperation;
    private String amount;
    private String currency;
    private String orderId;
    private String transId;
}
