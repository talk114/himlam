package gateway.core.channel.cybersouce.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class Authentication3ds implements Serializable {
    private Card card;
    private BillAddress billAddress;
    private String transactionReferenceId;
    private String orderCurrency;
    private String keepSubscript;
    private long grandTotalAmount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String referenceID;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private InvoiceHeader invoiceHeader;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String turnOn3ds;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String authenticationTransactionID;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDescriptor;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDefinedDataField5;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDefinedDataField6;
}
