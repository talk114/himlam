package gateway.core.channel.msb_va.dto.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author sonln
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResAccountVA {
    private String accountNumber;
    private String serviceCode;
    private String referenceNumber;
    private String name;
    private String beneficaryName;
    private String detail1;
    private String detail2;
    private String detail3;
    private String email;
    private String phone;
    private String expiryDate;
    private String status;
    private String responseCode;
    private String responseDesc;
    private String moreInfo;
}
