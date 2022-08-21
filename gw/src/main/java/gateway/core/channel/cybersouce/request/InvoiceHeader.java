package gateway.core.channel.cybersouce.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class InvoiceHeader {

    private String merchantDescriptor;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDescriptorContact;
    private String merchantDescriptorAlternate;

    private String merchantDescriptorStreet;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDescriptorCity;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDescriptorState;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDescriptorPostalCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantDescriptorCountry;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String subMidId;
}
