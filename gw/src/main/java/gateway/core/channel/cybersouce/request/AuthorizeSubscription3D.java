package gateway.core.channel.cybersouce.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class AuthorizeSubscription3D {
    private String orderNumber;
    private String orderCurrency;
    private String amount;
    private String subscriptionId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String referenceID;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String turnOn3ds;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String authenticationTransactionID;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Card card;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDescriptor;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDefinedDataField5;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDefinedDataField6;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private InvoiceHeader invoiceHeader;
}
