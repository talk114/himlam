package gateway.core.channel.migs.request;

import gateway.core.channel.migs.entity.Authentication;
import gateway.core.channel.migs.entity.Order;
import gateway.core.channel.migs.entity.SourceOfFunds;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitiateAuthenticationReq implements Serializable {
    private String apiOperation;
    private Authentication authentication;
    private String transactionId;
    private Order order;
    private SourceOfFunds sourceOfFunds;
    private String correlationId;
}
