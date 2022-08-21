package gateway.core.channel.cybersouce.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayerAuthValidateService implements Serializable {
    private String authenticationTransactionID;
    private boolean run;
}
