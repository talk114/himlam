package gateway.core.channel.cybersouce.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SSPCreateTransactionReq {
    private String callback;
    private String serviceId;
    private String orderNumber;
    private String recurring;
    private String protocolType;
    private String protocolVersion;
    private String amountOption;
    private String amountCurrency;
    private String amountTotal;
    private String merchantName;
    private String merchantUrl;
    private String merchantReference;
    private String[] allowedBrands;

}
