package gateway.core.channel.cybersouce.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
@Data
public class ValidateAuthentication3ds implements Serializable {
    private Card card;
    private BillAddress billAddress;
    private String authenticationTransactionID;
    private String orderCurrency;
    private long grandTotalAmount;
    private String transactionReferenceId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDescriptor;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDefinedDataField5;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDefinedDataField6;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private InvoiceHeader invoiceHeader;
}
