package gateway.core.channel.cybersouce.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author TaiND
 */
@Data
public class CheckEnrollment implements Serializable {

    private String transactionReferenceId;
    private String orderCurrency;
    private String referenceID;
    private String returnURL;
    private long grandTotalAmount;
    private BillAddress billAddress;
    private Card card;
    private int gatewayMID;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDescriptor;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDefinedDataField5;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDefinedDataField6;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private InvoiceHeader invoiceHeader;
}
