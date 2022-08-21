package gateway.core.channel.migs.request;

import gateway.core.channel.migs.entity.Authentication;
import gateway.core.channel.migs.entity.Order;
import gateway.core.channel.migs.entity.SourceOfFunds;
import com.fasterxml.jackson.annotation.JsonInclude;
import gateway.core.channel.migs.entity.Transaction;
import gateway.core.channel.migs.response.TransactionResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment3DS2Req {
    private String apiOperation;
    private Authentication authentication;
    private Order order;
    private SourceOfFunds sourceOfFunds;
    private String transactionId;
    private Transaction transaction;
}
