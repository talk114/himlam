package gateway.core.channel.napas.dto.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sonln
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResultPORes {
        private String acquirerCode;
        private String acquirerMessage;
        private PaymentResultPO.CardSecurityCode cardSecurityCode;
        private String gatewayCode;
        private String message;
}
