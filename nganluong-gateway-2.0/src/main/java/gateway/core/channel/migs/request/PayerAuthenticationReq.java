package gateway.core.channel.migs.request;

import gateway.core.channel.migs.entity.Authentication;
import gateway.core.channel.migs.entity.Device;
import gateway.core.channel.migs.entity.Order;
import gateway.core.channel.migs.entity.SourceOfFunds;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayerAuthenticationReq {
    private Authentication authentication;
    private String transactionId;
    private Device device;
    private Order order;
    private SourceOfFunds sourceOfFunds;
    private String apiOperation;
    private String correlationId;

}
