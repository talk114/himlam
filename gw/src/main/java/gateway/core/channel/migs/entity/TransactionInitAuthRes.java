package gateway.core.channel.migs.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionInitAuthRes {
    private String amount;
    private String authenticationStatus;
    private String currency;
    private String id;
    private String type;
    private Acquirer acquirer;
}

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
class Acquirer {
    private String merchantId;
}
